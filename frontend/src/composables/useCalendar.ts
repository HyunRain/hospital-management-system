import { ref, computed, onMounted, onUnmounted, watchEffect } from 'vue';
import { days, months, shortMonths, slotTime } from '@/util/types/constants';
import { departmentMap, type CalendarState } from '@/util/types/types';
import { useAppointmentStore } from '@/stores/appointmentStore';

export function useCalendar(): CalendarState {
  const todaysDate = ref(new Date());

  // Updates todaysDate starting exactly at every minute for the time marker in day and week calendar
  onMounted(() => {
    const now = new Date();
    const msUntilNextMinute = (60 - now.getSeconds()) * 1000 - now.getMilliseconds();

    let minuteTimer: number | undefined;

    const syncTimeout = setTimeout(() => {
      todaysDate.value = new Date();

      minuteTimer = window.setInterval(() => {
        todaysDate.value = new Date();
      }, 60_000);
    }, msUntilNextMinute);

    onUnmounted(() => {
      clearTimeout(syncTimeout);
      if (minuteTimer) clearInterval(minuteTimer);
    });
  });

  // Visual date that changes depending on where you are in the calendar
  const date = ref(new Date());
  const currentYear = computed(() => date.value.getFullYear());
  const currentMonth = computed(() => date.value.getMonth());
  const currentDay = computed(() => date.value.getDate());

  // eg Wed
  const currentWeekDay = computed(() => days[date.value.getDay()]);

  // 1 hour = 100px, 1 min = 1.666667 px. -6 is magic
  const currentTimeTopPixelValue = computed(() => todaysDate.value.getHours() * 100 + todaysDate.value.getMinutes() * 1.666667 - 6);
  const isLeapYear = computed(
    () => (currentYear.value % 4 === 0 && currentYear.value % 100 !== 0) || currentYear.value % 400 === 0,
  );

  function daysInMonth(year: number, month: number) {
    return new Date(year, month + 1, 0).getDate();
  }

  const daysInCurrentMonth = computed(() => daysInMonth(currentYear.value, currentMonth.value));

  const daysInCurrentMonthArray = computed(() => {
    const daysArray: number[] = [];
    for (let i: number = 1; i <= daysInCurrentMonth.value; i++) {
      daysArray.push(i);
    }
    return daysArray;
  });

  function applyTodaysDate() {
    date.value = new Date();
  }

  function toLocalDateString(dateObj: Date): string {
    return (
      dateObj.getFullYear() +
      '-' +
      String(dateObj.getMonth() + 1).padStart(2, '0') +
      '-' +
      String(dateObj.getDate()).padStart(2, '0')
    );
  }

  function changeDate(direction: 'prev' | 'next', isWeek: boolean, selectedCalendarRange: string) {
    const newDate = new Date(date.value);
    const incrementValue = isWeek ? (direction === 'prev' ? -7 : 7) : direction === 'prev' ? -1 : 1;

    switch (selectedCalendarRange) {
      case 'Month':
        // Covers edge case when current day is 31 and prev or next month has <31 days.
        // eg date is oct 31. click back a month -> sep 31 (sep has 30 days, so it auto corrects forwards to oct 1)
        if (date.value.getDate() > daysInMonth(newDate.getFullYear(), newDate.getMonth() + incrementValue)) {
          newDate.setDate(daysInMonth(newDate.getFullYear(), newDate.getMonth() + incrementValue));
        }
        newDate.setMonth(newDate.getMonth() + incrementValue);
        break;
      case 'Week':
        newDate.setDate(newDate.getDate() + incrementValue);
        break;
      case 'Day':
        newDate.setDate(newDate.getDate() + incrementValue);
        break;
    }

    date.value = newDate;
    fetchAppointmentsForMonthRange();
  }

  // Returns the number of the first week day in the month
  // [0,1,2,3,4,5,6] Starting from sunday: 0 -> saturday: 6
  function computeFirstWeekdayOfMonth(year: number, month: number): number {
    return new Date(year, month, 1).getDay();
  }

  // Returns a string like 'Mon' of the first week day in the month.
  const firstWeekDayOfMonth = computed(() => {
    const dayIndex = computeFirstWeekdayOfMonth(currentYear.value, currentMonth.value);
    return days[dayIndex];
  });

  // Returns a number array with the days of the previous month till sunday
  // Example: current month: october 2025, first day of october 2025: wednesday => [28,29,30] of september 2025
  const daysOfPreviousMonth = computed(() => {
    let daysTotal = days.indexOf(firstWeekDayOfMonth.value);
    const daysOfPreviousMonth = daysInMonth(currentYear.value, currentMonth.value - 1);
    const daysArray: number[] = [];
    while (daysTotal) {
      daysArray.push(daysOfPreviousMonth - (daysTotal - 1));
      daysTotal--;
    }
    return daysArray;
  });

  // Returns the number of the last week day in the month
  // [0,1,2,3,4,5,6] Starting from sunday: 0 -> saturday: 6
  function computeLastWeekDayOfMonth(year: number, month: number) {
    return new Date(year, month, daysInCurrentMonth.value).getDay();
  }

  // Returns a string like 'Mon' of the last week day in the month.
  const lastWeekDayOfMonth = computed(() => {
    const dayIndex = computeLastWeekDayOfMonth(currentYear.value, currentMonth.value);
    return days[dayIndex];
  });

  // Returns a number array with the days of the next month till saturday
  // Example: current month: october 2025, last day of october 2025: friday => [1] of november 2025
  const daysOfNextMonth = computed(() => {
    let daysTotal = days.length - 1 - days.indexOf(lastWeekDayOfMonth.value);
    let dayValue = 1;
    const daysArray: number[] = [];
    while (daysTotal) {
      daysArray.push(dayValue);
      dayValue++;
      daysTotal--;
    }
    return daysArray;
  });

  // Array that includes day values of prev current and next month
  const totalDaysForCurrentMonth = computed(() => {
    const previousDays = daysOfPreviousMonth.value.map((day) => ({ value: day, type: 'prev' }));
    const currentDays = daysInCurrentMonthArray.value.map((day) => ({ value: day, type: 'curr' }));
    const nextDays = daysOfNextMonth.value.map((day) => ({ value: day, type: 'next' }));

    return [...previousDays, ...currentDays, ...nextDays];
  });

  // only relevant for week calendar
  const containsPrevMonthDays = ref(false);
  const containsNextMonthDays = ref(false);

  const weekMonthOverLapString = computed(() => {
    const tempDate = new Date();

    if (containsPrevMonthDays.value) {
      const targetMonth = date.value.getMonth() - 1;
      const daysInTargetMonth = daysInMonth(date.value.getFullYear(), targetMonth);

      if (tempDate.getDate() > daysInMonth(date.value.getFullYear(), targetMonth)) {
        tempDate.setDate(daysInTargetMonth);
      }

      tempDate.setMonth(date.value.getMonth() - 1);
      const prevMonth = tempDate.getMonth();
      return `${shortMonths[prevMonth]} - ${shortMonths[currentMonth.value]}`;
    }
    if (containsNextMonthDays.value) {
      const targetMonth = date.value.getMonth() + 1;
      const daysInTargetMonth = daysInMonth(date.value.getFullYear(), targetMonth);

      if (tempDate.getDate() > daysInMonth(date.value.getFullYear(), targetMonth)) {
        tempDate.setDate(daysInTargetMonth);
      }

      tempDate.setMonth(targetMonth);
      const nextMonth = tempDate.getMonth();
      return `${shortMonths[currentMonth.value]} - ${shortMonths[nextMonth]}`;
    }
    return months[currentMonth.value];
  });

  const currentWeekDays = computed(() => {
    const weekDayNumber = date.value.getDay();
    const weekDaysArray: { value: number, type: string}[] = [];
    let newMonthDayIndex = 0;

    for (let i: number = 0; i <= weekDayNumber; i++) {
      if (currentDay.value - weekDayNumber + i <= 0) {
        weekDaysArray.push({ value: daysOfPreviousMonth.value[i], type: 'prev'});
      } else {
        weekDaysArray.push({ value: currentDay.value - weekDayNumber + i, type: 'curr' });
      }
    }

    for (let i: number = 1; i <= 7 - (weekDayNumber + 1); i++) {
      if (currentDay.value + i > daysInMonth(currentYear.value, currentMonth.value)) {
        weekDaysArray.push({ value: daysOfNextMonth.value[newMonthDayIndex], type: 'next'});
        newMonthDayIndex++;
      } else {
        weekDaysArray.push({ value: currentDay.value + i, type: 'curr' });
      }
    }
    return weekDaysArray;
  });

  watchEffect(() => {
    const firstDay = currentWeekDays.value[0].value;
    const lastDay = currentWeekDays.value[currentWeekDays.value.length - 1].value;

    // Compare with first/last day of current month
    containsPrevMonthDays.value = firstDay > currentDay.value;
    containsNextMonthDays.value = lastDay < currentDay.value;
  });

  function isEndBeforeStart(startDate: string, startTime: string, endDate: string, endTime: string) {
    return new Date(`${endDate}T${endTime}`) < new Date(`${startDate}T${startTime}`);
  }

  async function fetchAppointmentsForMonthRange() {
    const appointmentStore = useAppointmentStore();

    const prevDate = new Date(date.value);
    prevDate.setMonth(date.value.getMonth() - 1);
    const nextDate = new Date(date.value);
    nextDate.setMonth(date.value.getMonth() + 1);

    // +1 because months are 0 indexed in JS Date but 1 indexed in javas local date, lol
    // TODO: Ideally only make 1 api call for all 3 months, while still caching the results month by month in the store.
    await Promise.all([
      appointmentStore.getAppointmentsByMonthRange(prevDate.getFullYear(), prevDate.getMonth() + 1),
      appointmentStore.getAppointmentsByMonthRange(date.value.getFullYear(), date.value.getMonth() + 1),
      appointmentStore.getAppointmentsByMonthRange(nextDate.getFullYear(), nextDate.getMonth() + 1),
    ]);
  }


function getAppointmentsForDay(day: { value: number, type: string; }, month: number, year: number) {
  const appointmentStore = useAppointmentStore();

  const temp = new Date(year, month, day.value);

  if (day.type === 'next') temp.setMonth(month + 1);
  if (day.type === 'prev') temp.setMonth(month - 1);

  // format date as yyyy-mm-dd to return the appointments of the clicked day
  const key = toLocalDateString(temp);

  const appointments = appointmentStore.cachedDays[key] || [];

  // only return the appointments of currently selected department
  const filteredAppointments = appointments.filter(appointment => appointment.departmentId === departmentMap[appointmentStore.selectedDepartment]);

  // sort them by appointment time
  return {
    appointments: filteredAppointments.slice().sort((a, b) => a.appointmentTime.localeCompare(b.appointmentTime)),
    length: filteredAppointments.length
  };
}

function isPast(day: number, hour: number, slot: number) {
  const date = new Date(currentYear.value, currentMonth.value, day);
  date.setHours(hour - 1);
  date.setMinutes(slotTime[slot]);

  return date < new Date();
}

  return {
    date,
    toLocalDateString,
    todaysDate,
    currentYear,
    currentMonth,
    currentDay,
    currentWeekDay,
    currentWeekDays,
    containsNextMonthDays,
    containsPrevMonthDays,
    weekMonthOverLapString,
    currentTimeTopPixelValue,
    isLeapYear,
    daysInCurrentMonth,
    applyTodaysDate,
    daysInMonth,
    changeDate,
    firstWeekDayOfMonth,
    lastWeekDayOfMonth,
    daysOfPreviousMonth,
    daysOfNextMonth,
    totalDaysForCurrentMonth,
    fetchAppointmentsForMonthRange,
    isEndBeforeStart,
    getAppointmentsForDay,
    isPast
  };
}
