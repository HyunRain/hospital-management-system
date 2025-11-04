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
import { computed, ref } from 'vue';
import { useIsMobile } from '@/composables/useIsMobile';

const toggleStore = useToggleStore();
const appointmentStore = useAppointmentStore();

const {
  todaysDate,
  currentYear,
  currentMonth,
  currentDay,
  currentWeekDay,
  currentWeekDays,
  weekMonthOverLapString,
  currentTimeTopPixelValue,
  changeDate,
  applyTodaysDate,
  totalDaysForCurrentMonth
} = useCalendar();

const isWeek = computed(() => selectedCalendarRange.value === 'Week');

const selectedCalendarRange = ref<string>('Month');
const isMobile = useIsMobile();

function handleCreateAppointmentClick() {
  appointmentStore.resetSelectedDate();
  toggleStore.toggleAppointmentForm();
}
</script>

<template>
  <main class="flex flex-col h-full">
    <header class="flex items-center justify-between" :class="[selectedCalendarRange !== 'Day' ? 'pb-5' : 'pb-5']">

      <section class="flex items-center gap-1.5">
        <button @click="applyTodaysDate" type="button" class="button">Today</button>
        <div @click="changeDate('prev', isWeek, selectedCalendarRange)"
          class="p-1.5 hover:bg-rose-100 dark:hover:bg-[#1d1d1dcf] cursor-pointer rounded-lg select-none">
          <img class="size-6 cursor-pointer" :src="`/assets/icons/${toggleStore.darkModeState}/leftarrow.svg`" alt="Left Arrow Icon" />
        </div>
        <div @click="changeDate('next', isWeek, selectedCalendarRange)"
          class="p-1.5 hover:bg-rose-100 dark:hover:bg-[#1d1d1dcf] cursor-pointer rounded-lg select-none">
          <img class="size-6" :src="`/assets/icons/${toggleStore.darkModeState}/rightarrow.svg`" alt="Right Arrow Icon" />
        </div>
        <p class="text-[16px] gap-1 flex flex-row mx-1">
          <span v-show="selectedCalendarRange === 'Day'"> {{ currentWeekDay }} {{ currentDay }} </span>
          <span v-show="selectedCalendarRange === 'Week'"> {{ weekMonthOverLapString }} </span>
          <span v-if="selectedCalendarRange !== 'Week' && !isMobile"> {{ months[currentMonth] }} </span>
          <span v-if="selectedCalendarRange !== 'Week' && isMobile"> {{ shortMonths[currentMonth] }} </span>
          <span>{{ currentYear }}</span>
        </p>
      </section>

      <section class="flex gap-1 items-center z-48">
        <Selection :data="calendarRanges" v-model="selectedCalendarRange" :is-open="toggleStore.showCalendarRangeSelection"
          @toggle="toggleStore.toggleCalendarRangeSelection" min-width="100px">
          <template v-slot:Mobile>
            <span class="inline lg:hidden"> {{ selectedCalendarRange.slice(0, 1) }} </span>
          </template>
        </Selection>
        <button class="button" type="button" @click="handleCreateAppointmentClick()">
          <span class="hidden lg:block">+ New Appointment</span>
          <span class="lg:hidden">+</span>
        </button>
      </section>
    </header>

    <MonthCalendar v-show="selectedCalendarRange === 'Month'" :total-days-for-current-month="totalDaysForCurrentMonth" :todays-date="todaysDate"
      :current-month="currentMonth" :current-year="currentYear"></MonthCalendar>
    <WeekCalendar v-show="selectedCalendarRange === 'Week'" :todays-date="todaysDate" :current-week-day="currentWeekDay"
      :current-week-days="currentWeekDays" :current-time-top-pixel-value="currentTimeTopPixelValue" :current-month="currentMonth"
      :current-year="currentYear"></WeekCalendar>
    <DayCalendar v-show="selectedCalendarRange === 'Day'" :todays-date="todaysDate" :current-day="currentDay"
      :current-time-top-pixel-value="currentTimeTopPixelValue" :current-month="currentMonth" :current-year="currentYear"></DayCalendar>

  </main>
</template>
