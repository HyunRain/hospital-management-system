<script setup lang="ts">
import { useCalendar } from '@/composables/useCalendar';
import { useAppointmentStore } from '@/stores/appointmentStore';
import { useToggleStore } from '@/stores/toggleStore';
import { days, hours, slotTime, slots } from '@/util/types/constants';
import type { PropType } from 'vue';
import AppointmentCard from './AppointmentCard.vue';


const props = defineProps({
  todaysDate: {
    type: Date,
    required: true,
  },
  currentWeekDay: {
    type: String,
    required: true,
  },
  currentWeekDays: {
    type: Array as PropType<{value: number, type: string}[]>,
    required: true,
  },
  currentTimeTopPixelValue: {
    type: Number,
    required: true,
  },
  currentMonth: {
    type: Number,
    required: true,
  },
  currentYear: {
    type: Number,
    required: true,
  }
});

const appointmentStore = useAppointmentStore();
const toggleStore = useToggleStore();

function handleClick(day: number, hour: number, slot: number) {
  appointmentStore.resetClickedAppointmentFormData();

  const date = new Date(props.currentYear, props.currentMonth, day);
  const endDate = new Date(props.currentYear, props.currentMonth, day);

  date.setHours(hour);
  date.setMinutes(slotTime[slot]);
  endDate.setHours(hour + 1);
  endDate.setMinutes(slotTime[slot]);

  const localDate = date.getFullYear() + '-' + String(date.getMonth() + 1).padStart(2, '0') + '-' + String(date.getDate()).padStart(2, '0');
  const endLocalDate = endDate.getFullYear() + '-' + String(endDate.getMonth() + 1).padStart(2, '0') + '-' + String(endDate.getDate()).padStart(2, '0');
  const time = date.toTimeString().split(' ')[0].slice(0, 5);
  const endTime = endDate.toTimeString().split(' ')[0].slice(0, 5);

  appointmentStore.fillSelectedDate(localDate, endLocalDate, time, endTime);
  toggleStore.toggleAppointmentForm();
}

const { getAppointmentsForDay, isPast} = useCalendar();
</script>

<template>
  <main class="flex flex-col mt-1.5 h-full relative">
    <header
      class="grid grid-cols-[50px_repeat(7,1fr)] lg:grid-cols-[100px_repeat(7,1fr)] col-span-8 text-center mb-3 sticky top-16 z-40">
      <p class="w-full"></p>
      <p v-for="(day, index) in days" :key="index"
        :class="[day === currentWeekDay && currentWeekDays[index].value === todaysDate.getDate() && props.currentMonth === props.todaysDate.getMonth() ? 'text-red-400' : '']">
        {{ day }} {{ currentWeekDays[index].value }}</p>
    </header>

    <section class="grid grid-cols-[50px_repeat(7,1fr)] lg:grid-cols-[100px_repeat(7,1fr)] col-span-8">
      <section>
        <ul>
          <li v-for="hour in hours" :key="hour" class="relative">
            <ul>
              <li v-for="slot in slots">

                <div v-if="slot === 1" class="flex items-center h-[100px]">
                  <p class="absolute top-0 -translate-y-[8px]">
                    <span v-show="hour < 10">0</span>{{ hour - 1 }}:0{{ slotTime[1] }}
                  </p>
                </div>

              </li>
            </ul>
          </li>
        </ul>
      </section>

      <section v-for="(value, index) in 7" :key="index" class="relative border-b border-r border-t border-neutral-300 dark:border-[#33333380]"
        :class="[value === 1 ? 'border-l' : '', value === 1 ? 'rounded-l-lg' : '', value === 7 ? 'rounded-r-lg' : '']">
        <div
          v-if="index === props.todaysDate.getDay() && currentWeekDays[index].value === props.todaysDate.getDate() && props.currentMonth === props.todaysDate.getMonth()"
          :style="{ top: `${props.currentTimeTopPixelValue}px` }" class="flex items-center absolute w-full z-49 pointer-events-none">
          <div class="bg-red-600 dark:bg-red-400 rounded-full size-3"></div>
          <div class="border-t h-0 border-red-600 dark:border-red-400 w-full"></div>
        </div>
        <AppointmentCard
          v-for="(appointment, indexApp) in getAppointmentsForDay({ value: currentWeekDays[index].value, type: currentWeekDays[index].type }, props.currentMonth, props.currentYear).appointments"
          :key="indexApp" :appointment="appointment">
        </AppointmentCard>
        <ul>
          <li v-for="hour in hours" :key="hour" class="relative h-[100px] border-neutral-300 dark:border-[#33333380]"
            :class="hour !== 24 ? 'border-b' : ''">
            <ul>
              <li v-for="slot in slots">
                <div class="flex items-center h-[25px]">
                  <div v-show="!isPast(currentWeekDays[index].value, hour, slot)" class="flex-1 flex cursor-pointer mainHover rounded-sm h-[25px] py-[3px]" role="button" aria-label="time slot"
                    @click="handleClick(currentWeekDays[index].value, hour - 1, slot)">
                  </div>
                </div>
              </li>
            </ul>
          </li>
        </ul>
      </section>
    </section>

  </main>
</template>
