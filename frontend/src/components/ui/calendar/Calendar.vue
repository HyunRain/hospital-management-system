<script setup lang="ts">
import MonthCalendar from './MonthCalendar.vue';
import { months, shortMonths } from '@/util/types/constants';
import { useToggleStore } from '@/stores/toggleStore';
import Selection from '../misc/Selection.vue';
import { calendarRanges } from '@/util/types/constants';
import { useAppointmentStore } from '@/stores/appointmentStore';
import WeekCalendar from './WeekCalendar.vue';
import DayCalendar from './DayCalendar.vue';
import { useCalendar } from '@/composables/useCalendar';
import { computed } from 'vue';

const toggleStore = useToggleStore();
const appointmentStore = useAppointmentStore();

const {
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
  changeDate,
  applyTodaysDate,
  totalDaysForCurrentMonth
} = useCalendar();

const isWeek = computed(() => appointmentStore.selectedCalendarRange === 'Week');
</script>

<template>
  <main class="flex flex-col h-full">
    <header class="flex items-center justify-between" :class="[appointmentStore.selectedCalendarRange !== 'Day' ? 'pb-5' : 'pb-5']">

      <section class="flex items-center gap-2">
        <button @click="applyTodaysDate" type="button" class="button">Today</button>
        <div @click="changeDate('prev', isWeek)" class="p-1.5 hover:bg-rose-100 dark:hover:bg-[#1d1d1dcf] cursor-pointer rounded-lg select-none">
          <img class="size-6 cursor-pointer" :src="`/assets/icons/${toggleStore.darkModeState}/leftarrow.svg`" alt="Left Arrow Icon" />
        </div>
        <div @click="changeDate('next', isWeek)" class="p-1.5 hover:bg-rose-100 dark:hover:bg-[#1d1d1dcf] cursor-pointer rounded-lg select-none">
          <img class="size-6" :src="`/assets/icons/${toggleStore.darkModeState}/rightarrow.svg`" alt="Right Arrow Icon" />
        </div>
        <p class="text-[16px] gap-1 flex flex-row mx-1">
          <span v-show="appointmentStore.selectedCalendarRange === 'Day'"> {{ currentWeekDay }} {{ currentDay }}</span>
          <span v-show="appointmentStore.selectedCalendarRange === 'Week'"> {{ weekMonthOverLapString }}</span>
          <span v-show="appointmentStore.selectedCalendarRange !== 'Week'">{{ months[currentMonth] }}</span>
          <span>{{ currentYear }}</span>
        </p>
      </section>

      <section class="flex gap-1 items-center z-51">
        <Selection :data="calendarRanges" storeName="appointment" stateName="selectedCalendarRange" toggle-state-name="showCalendarRangeSelection" />
        <button class="button" type="button">
          <span class="hidden lg:block">+ New Appointment</span>
          <span class="lg:hidden">+</span>
        </button>
      </section>
    </header>

    <MonthCalendar v-show="appointmentStore.selectedCalendarRange === 'Month'" :total-days-for-current-month="totalDaysForCurrentMonth"
      :todays-date="todaysDate" :current-month="currentMonth"></MonthCalendar>
    <WeekCalendar v-show="appointmentStore.selectedCalendarRange === 'Week'" :todays-date="todaysDate" :current-week-day="currentWeekDay"
      :current-week-days="currentWeekDays" :current-time-top-pixel-value="currentTimeTopPixelValue" :current-month="currentMonth"></WeekCalendar>
    <DayCalendar v-show="appointmentStore.selectedCalendarRange === 'Day'" :todays-date="todaysDate" :current-day="currentDay"
      :current-time-top-pixel-value="currentTimeTopPixelValue"></DayCalendar>

  </main>
</template>
