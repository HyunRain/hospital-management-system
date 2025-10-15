<script setup lang="ts">
import MonthCalendar from './MonthCalendar.vue';
import { months } from '@/util/types/constants';
import { useToggleStore } from '@/stores/toggleStore';
import Selection from '../misc/Selection.vue';
import { calendarRanges } from '@/util/types/constants';
import { useAppointmentStore } from '@/stores/appointmentStore';
import WeekCalendar from './WeekCalendar.vue';
import DayCalendar from './DayCalendar.vue';
import { useCalendar } from '@/composables/useCalendar';

const toggleStore = useToggleStore();
const appointmentStore = useAppointmentStore();

const {
  currentYear,
  currentMonth,
  currentDay,
  daysInCurrentMonth,
  nextMonth,
  previousMonth,
  applyTodaysDate,
  firstWeekDayOfMonth,
  daysOfPreviousMonth
} = useCalendar();
</script>

<template>
  <header class="flex items-center pb-5 justify-between">

    <section class="flex items-center gap-2">
      <button @click="applyTodaysDate" type="button" class="button">Today</button>
      <div @click="previousMonth" class="p-1.5 hover:bg-rose-100 dark:hover:bg-[#1d1d1dcf] cursor-pointer rounded-lg">
        <img class="size-6 cursor-pointer" :src="`/assets/icons/${toggleStore.darkModeState}/leftarrow.svg`" alt="Left Arrow Icon" />
      </div>
      <div @click="nextMonth" class="p-1.5 hover:bg-rose-100 dark:hover:bg-[#1d1d1dcf] cursor-pointer rounded-lg">
        <img class="size-6" :src="`/assets/icons/${toggleStore.darkModeState}/rightarrow.svg`" alt="Right Arrow Icon" />
      </div>
      <p class="text-[16px]">{{ months[currentMonth] }} {{ currentYear }} {{ firstWeekDayOfMonth }} {{ daysOfPreviousMonth}}</p>
    </section>

    <section class="flex gap-3 items-center">
      <Selection :data="calendarRanges" storeName="appointment" stateName="selectedCalendarRange" toggle-state-name="showCalendarRangeSelection">
      </Selection>
      <button class="button" type="button">
        + New Appointment
      </button>
    </section>
  </header>

  <MonthCalendar v-show="appointmentStore.selectedCalendarRange === 'Month'" :days-of-previous-month="daysOfPreviousMonth" :days-in-month="daysInCurrentMonth"></MonthCalendar>
  <WeekCalendar v-show="appointmentStore.selectedCalendarRange === 'Week'"></WeekCalendar>
  <DayCalendar v-show="appointmentStore.selectedCalendarRange === 'Day'"></DayCalendar>

</template>
