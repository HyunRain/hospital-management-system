<script setup lang="ts">
import { useAppointmentStore } from '@/stores/appointmentStore';
import { useToggleStore } from '@/stores/toggleStore';
import type { AppointmentFormData } from '@/util/types/types';
import { ref, type PropType } from 'vue';
import { appointmentTypeColors } from '@/util/types/constants';


const props = defineProps({
  appointment: {
    type: Object as PropType<AppointmentFormData>,
    required: true
  }
});

const toggleStore = useToggleStore();
const appointmentStore = useAppointmentStore();

const isHovered = ref(false);

function handleClick() {
  appointmentStore.clickedAppointmentData = props.appointment;
  appointmentStore.isInThePast(appointmentStore.clickedAppointmentData.appointmentDate, appointmentStore.clickedAppointmentData.appointmentTime);
  toggleStore.toggleAppointmentForm();
}
</script>

<template>
  <div @mouseover="isHovered = true" @mouseleave="isHovered = false" @click.stop="handleClick()" class="flex gap-2 w-full rounded-lg lg:px-2 h-[24px] text-xs text-neutral-200 mt-1 items-center justify-center lg:justify-start cursor-pointer select-none overflow-hidden whitespace-nowrap text-nowrap text-ellipsis"
    :style="{ backgroundColor: isHovered ? appointmentTypeColors[props.appointment.appointmentType][1]: appointmentTypeColors[props.appointment.appointmentType][0] }">
    <p> {{ props.appointment.appointmentTime.slice(0, 5) }} </p>
    <p class="hidden lg:inline-block"> {{ props.appointment.patientName }} </p>
  </div>
</template>
