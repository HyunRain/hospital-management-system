<script setup lang="ts">
import { useAppointmentStore } from '@/stores/appointmentStore';
import { useToggleStore } from '@/stores/toggleStore';
import type { AppointmentFormData } from '@/util/types/types';
import { ref, type PropType } from 'vue';


const props = defineProps({
  appointment: {
    type: Object as PropType<AppointmentFormData>,
    required: true
  },
  isFullView: {
    type: Boolean,
    required: true,
  }
});

const toggleStore = useToggleStore();
const appointmentStore = useAppointmentStore();

const appointmentTypeColors: Record<string, string[]> = {
  'ROUTINE_CHECKUP': ['#113649', '#11364999'],
  'CONSULTATION': ['#0e3c2d', '#0e3c2d99'],
  'EMERGENCY': ['#482128', '#48212899'],
  'FOLLOW_UP': ['#473506', '#47350699']
};

const isHovered = ref(false);

function handleClick() {
  appointmentStore.clickedAppointmentData = props.appointment;
  toggleStore.toggleAppointmentForm();
}
</script>

<template>
  <div @mouseover="isHovered = true" @mouseleave="isHovered = false" @click.stop="handleClick()" class="flex gap-2 w-full rounded-lg lg:px-2 h-[24px] text-xs mt-1 items-center justify-center lg:justify-start cursor-pointer select-none overflow-hidden whitespace-nowrap text-nowrap text-ellipsis"
    :style="{ backgroundColor: isHovered ? appointmentTypeColors[props.appointment.appointmentType][1]: appointmentTypeColors[props.appointment.appointmentType][0] }">
    <p> {{ props.appointment.appointmentTime.slice(0, 5) }} </p>
    <p class="hidden lg:inline-block"> {{ props.appointment.patientName }} </p>
  </div>
</template>
