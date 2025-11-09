<script setup lang="ts">
import { useAppointmentStore } from '@/stores/appointmentStore';
import { useToggleStore } from '@/stores/toggleStore';
import { days, shortMonths } from '@/util/types/constants';
import { computed, type PropType } from 'vue';
import MonthAppointmentCard from './MonthAppointmentCard.vue';
import { useCalendar } from '@/composables/useCalendar';

const props = defineProps({
  totalDaysForCurrentMonth: {
    type: Array as PropType<{ value: number, type: string; }[]>,
    required: true,
  },
  todaysDate: {
    type: Date,
    required: true,
  },
  currentMonth: {
    type: Number,
    required: true,
  },
  currentYear: {
    type: Number,
    required: true,
  },
  currentDate: {
    type: Date,
    required: true,
  },
});

const toggleStore = useToggleStore();
const appointmentStore = useAppointmentStore();
const { toLocalDateString } = useCalendar();

function handleClick(day: { value: number, type: string; }) {
  console.log("handleClick called")

  appointmentStore.resetClickedAppointmentFormData();
  const now = new Date();
  const tempDate = new Date(props.currentDate);
  tempDate.setDate(day.value);

  if (day.type === 'next') {
    tempDate.setMonth(props.currentMonth + 1);
  } else if (day.type === 'prev') {
    tempDate.setMonth(props.currentMonth - 1);
  }

  tempDate.setDate(day.value);

  if (tempDate.setHours(0, 0, 0, 0) < now.setHours(0, 0, 0, 0)) return;

  const localDate = toLocalDateString(tempDate);

  appointmentStore.fillSelectedDate(localDate, localDate, '09:00', '10:00');
  toggleStore.toggleAppointmentForm();
}

function getAppointmentsForDay(day: { value: number, type: string; }) {
  const temp = new Date(props.currentYear, props.currentMonth, day.value);

  if (day.type === 'next') temp.setMonth(props.currentMonth + 1);
  if (day.type === 'prev') temp.setMonth(props.currentMonth - 1);

  // format date as yyyy-mm-dd to return the appointments of the clicked day
  const key = toLocalDateString(temp);

  const appointments = appointmentStore.cachedDays[key] || [];
  // sort them by appointment time
  return { appointments: appointments.slice().sort((a, b) => a.appointmentTime.localeCompare(b.appointmentTime)), length: appointments.length };
}

const gridRowHeight = computed(() => {
  const dayAmount = props.totalDaysForCurrentMonth.length;
  switch (true) {
    case dayAmount < 35:
      return "140px";
      break;
    case dayAmount === 35:
      return "111px";
      break;
    case dayAmount > 35:
      return "85px";
      break;
  }
});
</script>

<template>

  <div class=" grid grid-cols-7 mt-1.5 grid-rows-[auto_1fr] h-full">
    <div class="grid grid-cols-7 col-span-7 text-center mb-3">
      <p v-for="value in days">{{ value }}</p>
    </div>

    <section class="border border-neutral-300 dark:border-[#33333380] grid grid-cols-7 col-span-7 rounded-lg overflow-hidden h-full">
      <section v-for="(day, index) in props.totalDaysForCurrentMonth" @click="handleClick(day)"
        class="flex flex-col text-start border-b border-neutral-300 dark:border-[#33333380] p-1"
        :class="[day.type === 'prev' || day.type === 'next' ? 'bg-neutral-200 dark:bg-[#101010]' : '', (index + 1) % 7 === 0 ? '' : 'border-r', index >= props.totalDaysForCurrentMonth.length - 7 ? 'border-b-0' : '']">
        <p class="size-6 rounded-full inline-flex items-center justify-center leading-[24px]"
          :class="[day.value === props.todaysDate.getDate() && props.todaysDate.getMonth() === props.currentMonth && props.todaysDate.getFullYear() === props.currentYear && day.type === 'curr' ? 'bg-red-200 dark:bg-red-400 dark:text-black' : '']">
          {{ day.value }} </p>

        <section class="flex-1 flex-col items-start justify-center relative" :style="{ minHeight: gridRowHeight }">
          <MonthAppointmentCard v-for="(appointment, indexApp) in getAppointmentsForDay(day).appointments.slice(0, 3)" :key="indexApp"
            :appointment="appointment" :is-full-view="false"></MonthAppointmentCard>
          <p @click.stop="toggleStore.toggleFullMonthAppointmentCards(index)" v-if="getAppointmentsForDay(day).length > 3"
            class="text-xs px-2 py-1 text-neutral-400 cursor-pointer dark:hover:bg-[#1d1d1dcf] hover:bg-red-200 rounded-lg mt-1 select-none">+ 1 <span
              class="hidden lg:inline-block">more</span></p>

          <div class="cardOverlay" v-if="toggleStore.showFullMonthAppointmentCards && toggleStore.clickedDayIndex === index"
            @click.stop="toggleStore.toggleFullMonthAppointmentCards(index)"></div>
          <div
            class="absolute top-full bg-neutral-200 dark:bg-[#101010] z-50 border px-2 py-2 rounded-lg select-none min-w-[250px] left-1/2 -translate-x-1/2"
            v-if="toggleStore.showFullMonthAppointmentCards && toggleStore.clickedDayIndex === index" @click.stop>
            <p> {{ shortMonths[currentMonth] }} {{ day.value }} </p>
            <MonthAppointmentCard v-for="(appointment, indexApp) in getAppointmentsForDay(day).appointments" :key="indexApp"
              :appointment="appointment" :is-full-view="true"></MonthAppointmentCard>
          </div>

        </section>
      </section>
    </section>
  </div>
</template>
