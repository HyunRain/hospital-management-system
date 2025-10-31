import { ref, computed, onMounted, onUnmounted, watchEffect } from 'vue';
import { days, months, shortMonths } from '@/util/types/constants';
import type { CalendarState } from '@/util/types/types';
import { useAppointmentStore } from '@/stores/appointmentStore';

export function useCalendar(): CalendarState {
  const appointmentStore = useAppointmentStore();

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

  const containsPrevMonthDays = ref(false);
  const containsNextMonthDays = ref(false);

  const currentWeekDays = computed(() => {
    const weekDayNumber = date.value.getDay();
    const weekDaysArray: number[] = [];

    for (let i: number = 0; i <= weekDayNumber; i++) {
      if (currentDay.value - weekDayNumber + i <= 0) {
        weekDaysArray.push(daysOfPreviousMonth.value[i]);
      } else {
        weekDaysArray.push(currentDay.value - weekDayNumber + i);
      }
    }

    for (let i: number = 1; i <= 7 - (weekDayNumber + 1); i++) {
      if (currentDay.value + i > daysInMonth(currentYear.value, currentMonth.value)) {
        weekDaysArray.push(daysOfNextMonth.value[i - 1]);
      } else {
        weekDaysArray.push(currentDay.value + i);
      }
    }
    return weekDaysArray;
  });

  watchEffect(() => {
    const firstDay = currentWeekDays.value[0];
    const lastDay = currentWeekDays.value[currentWeekDays.value.length-1];

    // Compare with first/last day of current month
    containsPrevMonthDays.value = firstDay > currentDay.value;
    containsNextMonthDays.value = lastDay < currentDay.value;
  });

  const weekMonthOverLapString = computed(() => {
    const tempDate = new Date();

    if(containsPrevMonthDays) {
      tempDate.setMonth(date.value.getMonth()-1);
      const prevMonth = tempDate.getMonth();
      return `${shortMonths[prevMonth]} - ${shortMonths[currentMonth.value]}`;
    }
    if(containsNextMonthDays) {
      tempDate.setMonth(date.value.getMonth() + 1);
      const nextMonth = tempDate.getMonth();
      return `${shortMonths[currentMonth.value]} - ${shortMonths[nextMonth]}`;
    }
    return months[currentMonth.value];
  })

  // hh--mm
  const currentTimeTopPixelValue = computed(
    () => todaysDate.value.getHours() * 84 + todaysDate.value.getMinutes() * 1.4 + 4.5,
  );

  const isLeapYear = computed(
    () =>
      (currentYear.value % 4 === 0 && currentYear.value % 100 !== 0) ||
      currentYear.value % 400 === 0,
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

  function changeDate(direction: 'prev' | 'next', isWeek: boolean) {
    const newDate = new Date(date.value);
    const incrementValue = isWeek ? (direction === 'prev' ? -7 : 7) : direction === 'prev' ? -1 : 1;

    switch (appointmentStore.selectedCalendarRange) {
      case 'Month':
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

  return {
    date,
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
    changeDate,
    firstWeekDayOfMonth,
    lastWeekDayOfMonth,
    daysOfPreviousMonth,
    daysOfNextMonth,
    totalDaysForCurrentMonth,
  };
}
