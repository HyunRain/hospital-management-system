<script setup lang="ts">
import { useAppointmentStore } from '@/stores/appointmentStore';
import { useToggleStore } from '@/stores/toggleStore';
import type { AppointmentFormData } from '@/util/types/types';
import { computed, ref, type PropType } from 'vue';
import { appointmentTypeColors } from '@/util/types/constants';

const props = defineProps({
  appointment: {
    type: Object as PropType<AppointmentFormData>,
    required: true
  }
});

const toggleStore = useToggleStore();
const appointmentStore = useAppointmentStore();

const startHour = computed(() => Number(props.appointment.appointmentTime.slice(0, 2)));
const startMinutes = computed(() => Number(props.appointment.appointmentTime.slice(3, 5)));

const endHour = computed(() => Number(props.appointment.appointmentEndTime.slice(0, 2)));
const endMinutes = computed(() => Number(props.appointment.appointmentEndTime.slice(3, 5)));

const topPosition = computed(() => {
  return startHour.value * 100 + (startMinutes.value / 15) * 25;
});

const cardHeight = computed(() => {
  return (endHour.value - startHour.value) * 100 + ((endMinutes.value - startMinutes.value) / 15) * 25;
});

const isHovered = ref(false);

function handleClick() {
  appointmentStore.clickedAppointmentData = props.appointment;
  appointmentStore.isInThePast(appointmentStore.clickedAppointmentData.appointmentDate, appointmentStore.clickedAppointmentData.appointmentTime);
  toggleStore.toggleAppointmentForm();
}
</script>

<template>
  <div @mouseover="isHovered = true" @mouseleave="isHovered = false" @click.stop="handleClick()"
    class="absolute flex flex-col gap-1 w-full z-49 rounded-lg lg:px-2 text-xs text-neutral-200 py-1 justify-start items-center lg:items-start cursor-pointer select-none overflow-hidden whitespace-nowrap text-nowrap text-ellipsis"
    :style="{
      backgroundColor: isHovered ? appointmentTypeColors[props.appointment.appointmentType][1] : appointmentTypeColors[props.appointment.appointmentType][0],
      top: `${topPosition}px`, height: `${cardHeight}px`
    }">
    <p class="hidden lg:inline-block"> {{ props.appointment.patientName }} </p>
    <p> {{ props.appointment.appointmentTime.slice(0, 5) }} - {{ props.appointment.appointmentEndTime.slice(0, 5) }}</p>
  </div>
</template>
