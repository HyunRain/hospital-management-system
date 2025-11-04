<script setup lang="ts">
import { useAppointmentStore } from '@/stores/appointmentStore';
import { useToggleStore } from '@/stores/toggleStore';
import { hours, slots } from '@/util/types/constants';

const appointmentStore = useAppointmentStore();
const toggleStore = useToggleStore();

const props = defineProps({
  currentDay: {
    type: Number,
    required: true
  },
  currentMonth: {
    type: Number,
    required: true,
  },
  currentYear: {
    type: Number,
    required: true,
  },
  todaysDate: {
    type: Date,
    required: true,
  },
  currentTimeTopPixelValue: {
    type: Number,
    required: true,
  }
})

const slotTime: Record<number, number> = {
  1: 15,
  2: 30,
  3: 45,
};

function handleClick(day: number, hour: number, slot: number) {
  const date = new Date(props.currentYear, props.currentMonth, day);
  const endDate = new Date(props.currentYear, props.currentMonth, day);

  date.setHours(hour);
  date.setMinutes(slot === 4 ? 0 : slotTime[slot]);
  endDate.setHours(hour + 1);
  endDate.setMinutes(slot === 4 ? 0 : slotTime[slot]);

  const localDate = date.getFullYear() + '-' + String(date.getMonth() + 1).padStart(2, '0') + '-' + String(date.getDate()).padStart(2, '0');
  const endLocalDate = endDate.getFullYear() + '-' + String(endDate.getMonth() + 1).padStart(2, '0') + '-' + String(endDate.getDate()).padStart(2, '0');
  const time = date.toTimeString().split(' ')[0].slice(0,5);
  const endTime = endDate.toTimeString().split(' ')[0].slice(0,5);

  appointmentStore.fillSelectedDate(localDate, endLocalDate, time, endTime);
  toggleStore.toggleAppointmentForm();
}
</script>

<template>
  <main class="relative">
    <div v-if="props.todaysDate.getDate() === props.currentDay" :style="{ top: `${props.currentTimeTopPixelValue}px` }"
      class="flex items-center absolute w-[calc(100%-80px)] ml-20 z-50 pointer-events-none">
      <div class="bg-red-600 dark:bg-red-400 rounded-full size-3"></div>
      <div class="border-t h-0 border-red-600 dark:border-red-400 w-full"></div>
    </div>
    <ul>
      <li>
        <time class="absolute">00:00</time>
        <div class="ml-20 flex-1 flex items-center cursor-pointer mainHover rounded-sm h-[21px]" role="button" aria-label="time slot">
          <span class="flex border-t h-0 w-full border-neutral-300 dark:border-neutral-900"></span>
        </div>
      </li>
      <li v-for="hour in hours" :key="hour" class="relative">
        <ul>
          <li v-for="slot in slots">

            <div v-if="slot !== 4" class="flex items-center">
              <time v-if="appointmentStore.dayCalendarTimeSlotValues.hour === hour && appointmentStore.dayCalendarTimeSlotValues.slot === slot"
                class="absolute">
                <span v-show="hour < 10">0</span>{{ hour - 1 }}:{{ slotTime[slot] }}
              </time>
              <div class="ml-20 flex-1 flex cursor-pointer mainHover rounded-sm h-[21px] py-[3px]" role="button" aria-label="time slot"
                @click="handleClick(props.currentDay, hour-1, slot)"
                @mouseover="appointmentStore.setDayCalendarTimeSlot(hour, slot)" @mouseleave="appointmentStore.setDayCalendarTimeSlot(0, 0)"></div>
            </div>

            <div v-if="slot === 4 && hour !== 24" class="flex items-center">
              <time class="absolute"><span v-show="hour < 10">0</span>{{ hour }}:00</time>
              <div class="ml-20 flex-1 flex items-center cursor-pointer mainHover rounded-sm h-[21px]" role="button" aria-label="time slot"
                @click="handleClick(props.currentDay, hour, slot)"
                @mouseover="appointmentStore.setDayCalendarTimeSlot(hour, slot)" @mouseleave="appointmentStore.setDayCalendarTimeSlot(0, 0)">
                <span class="flex border-t h-0 w-full border-neutral-300 dark:border-neutral-900"></span>
              </div>
            </div>

          </li>
        </ul>
      </li>
    </ul>
  </main>
</template>
