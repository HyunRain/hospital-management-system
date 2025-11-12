<script setup lang="ts">
import { useCalendar } from '@/composables/useCalendar';
import { useAppointmentStore } from '@/stores/appointmentStore';
import { useToggleStore } from '@/stores/toggleStore';
import { hours, slots, slotTime } from '@/util/types/constants';
import AppointmentCard from './AppointmentCard.vue';

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
});


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

const { getAppointmentsForDay, isPast } = useCalendar();
</script>

<template>
  <main class="relative">
    <div v-if="props.todaysDate.getDate() === props.currentDay" :style="{ top: `${props.currentTimeTopPixelValue + 39}px` }"
      class="flex items-center absolute w-[calc(100%-80px)] ml-20 z-50 pointer-events-none">
      <div class="bg-red-600 dark:bg-red-400 rounded-full size-3"></div>
      <div class="border-t h-0 border-red-600 dark:border-red-400 w-full"></div>
    </div>

    <section class="flex mt-[39px]">
      <section class="flex flex-col min-w-[100px]">
        <ul>
          <li v-for="hour in hours" :key="hour" class="relative">
            <ul>
              <li v-for="slot in slots">

                <div v-if="slot === 1" class="flex items-center h-[100px]">
                  <p class="absolute top-0 -translate-y-[8px]">
                    <span v-show="hour <= 10">0</span>{{ hour - 1 }}:0{{ slotTime[1] }}
                  </p>
                </div>

              </li>
            </ul>
          </li>
        </ul>
      </section>
      <section class="flex-col w-full relative">
        <AppointmentCard
          v-for="(appointment, indexApp) in getAppointmentsForDay({ value: currentDay, type: 'curr' }, props.currentMonth, props.currentYear).appointments"
          :key="indexApp" :appointment="appointment">
        </AppointmentCard>
        <ul>
          <li v-for="hour in hours" :key="hour" class="relative">
            <ul class="h-[100px] border-t">
              <li v-for="slot in slots">
                <div class="flex items-center h-[25px]">
                  <div v-show="!isPast(currentDay, hour, slot)"
                    class="flex-1 flex cursor-pointer mainHover rounded-sm h-[25px] py-[3px]" role="button" aria-label="time slot"
                    @click="handleClick(props.currentDay, hour - 1, slot)"></div>
                </div>
              </li>
            </ul>
          </li>
        </ul>
      </section>
    </section>
  </main>
</template>
