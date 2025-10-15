import { ref, computed, watch } from 'vue';
import { days } from '@/util/types/constants';
import type { CalendarState } from '@/util/types/types';

export function useCalendar(): CalendarState {
  const date = ref(new Date());

  const currentYear = computed(() => date.value.getFullYear());
  const currentMonth = computed(() => date.value.getMonth());
  const currentDay = computed(() => date.value.getDate());
  const isLeapYear = computed(
    () =>
      (currentYear.value % 4 === 0 && currentYear.value % 100 !== 0) ||
      currentYear.value % 400 === 0,
  );

  function daysInMonth(year: number, month: number) {
    return new Date(year, month + 1, 0).getDate();
  }

  const daysInCurrentMonth = computed(() => daysInMonth(currentYear.value, currentMonth.value));

  function applyTodaysDate() {
    date.value = new Date();
  }

  // Sets new date by going foward 1 month
  function nextMonth() {
    const newDate = new Date(date.value);
    newDate.setMonth(newDate.getMonth() + 1);
    date.value = newDate;
  }

  // Sets new date by going back 1 month
  function previousMonth() {
    const newDate = new Date(date.value);
    newDate.setMonth(newDate.getMonth() - 1);
    date.value = newDate;
  }

  // Returns the number of the first week day in the month
  // [0,1,2,3,4,5,6] Starting from Sunday: 0 -> Saturday: 6
  function computeFirstWeekdayOfMonth(year: number, month: number): number {
    return new Date(year, month, 1).getDay();
  }

  // Returns a string like 'Mon' of the first week day in the month.
  const firstWeekDayOfMonth = computed(() => {
    const dayIndex = computeFirstWeekdayOfMonth(currentYear.value, currentMonth.value);
    return days[dayIndex];
  });

  // Returns a number array with the days of the previous month till sunday
  // Example: Current Month: October 2025, First Day Of October 2025: Wednesday => [28,29,30] of September 2025
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

  function computeLastWeekDayOfMonth(year: number, month: number) {
    
  }

  const lastWeekDayOfMonth = computed(() => {

  });

  // Returns a number array with the days of the next month till saturday
  // Example: Current Month: October 2025, Last Day Of October 2025: Friday => [1] of November 2025
  const daysOfNextMonth = computed(() => {

  });

  return {
    date,
    currentYear,
    currentMonth,
    currentDay,
    isLeapYear,
    daysInCurrentMonth,
    applyTodaysDate,
    nextMonth,
    previousMonth,
    firstWeekDayOfMonth,
    daysOfPreviousMonth,
  };
}
