<script setup lang="ts">
import { useAppointmentStore } from '@/stores/appointmentStore';
import { useToggleStore } from '@/stores/toggleStore';
import { days, hours, slotTime, slots } from '@/util/types/constants';
import type { PropType } from 'vue';


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
    type: Array as PropType<number[]>,
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
  <main class="flex flex-col mt-1.5 h-full relative">
    <header class="grid grid-cols-[50px_repeat(7,1fr)] lg:grid-cols-[100px_repeat(7,1fr)] col-span-8 text-center mb-3">
      <p class="w-full"></p>
      <p v-for="(day, index) in days" :key="index"
        :class="[day === currentWeekDay && currentWeekDays[index] === todaysDate.getDate() &&  props.currentMonth === props.todaysDate.getMonth() ? 'text-red-400' : '']">
        {{ day }} {{ currentWeekDays[index] }}</p>
    </header>

    <section class="grid grid-cols-[50px_repeat(7,1fr)] lg:grid-cols-[100px_repeat(7,1fr)] col-span-8">
      <section class="py-[10.5px]">
        <ul>
          <li v-for="hour in hours" :key="hour" class="relative">
            <ul>
              <li v-for="slot in slots">

                <div v-if="slot !== 4" class="flex items-center">
                  <time v-if="appointmentStore.dayCalendarTimeSlotValues.hour === hour && appointmentStore.dayCalendarTimeSlotValues.slot === slot"
                    class="absolute">
                    <span v-show="hour < 10">0</span>{{ hour - 1 }}:{{ slotTime[slot] }}
                  </time>
                  <div class="ml-20 flex-1 flex rounded-sm h-[21px] py-[3px]" role="button" aria-label="time slot"></div>
                </div>

                <div v-if="slot === 4 && hour !== 24" class="flex items-center">
                  <time class="absolute"><span v-show="hour < 10">0</span>{{ hour }}:00</time>
                  <div class="ml-15 flex-1 flex items-center rounded-sm h-[21px]" role="button" aria-label="time slot">
                  </div>
                </div>

              </li>
            </ul>
          </li>
        </ul>
      </section>

      <section v-for="(value, index) in 7" :key="index"
        class="relative pt-[10.5px] border-b border-r border-t border-neutral-300 dark:border-[#33333380]"
        :class="[value === 1 ? 'border-l' : '', value === 1 ? 'rounded-l-lg' : '', value === 7 ? 'rounded-r-lg' : '']">
        <div
          v-if="index === props.todaysDate.getDay() && currentWeekDays[index] === props.todaysDate.getDate() && props.currentMonth === props.todaysDate.getMonth()"
          :style="{ top: `${props.currentTimeTopPixelValue-10.5}px` }" class="flex items-center absolute w-full z-50 pointer-events-none">
          <div class="bg-red-600 dark:bg-red-400 rounded-full size-3"></div>
          <div class="border-t h-0 border-red-600 dark:border-red-400 w-full"></div>
        </div>
        <ul>
          <li v-for="hour in hours" :key="hour" class="relative">
            <ul>
              <li v-for="slot in slots">
                <div v-if="slot !== 4" class="flex items-center">
                  <div class="flex-1 flex cursor-pointer mainHover rounded-sm h-[21px] py-[3px]" role="button" aria-label="time slot"
                    @click="handleClick(currentWeekDays[index], hour-1, slot)" @mouseover="appointmentStore.setDayCalendarTimeSlot(hour, slot)"
                    @mouseleave="appointmentStore.setDayCalendarTimeSlot(0, 0)">
                  </div>
                </div>

                <div v-if="slot === 4 && hour !== 24" class="flex items-center">
                  <div class="flex-1 flex items-center cursor-pointer mainHover rounded-sm h-[21px]" role="button" aria-label="time slot"
                    @click="handleClick(currentWeekDays[index], hour, slot)" @mouseover="appointmentStore.setDayCalendarTimeSlot(hour, slot)"
                    @mouseleave="appointmentStore.setDayCalendarTimeSlot(0, 0)">
                    <span class="flex border-t h-0 w-full border-neutral-300 dark:border-[#33333380]"></span>
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
