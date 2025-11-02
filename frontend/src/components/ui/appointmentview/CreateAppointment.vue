<script setup lang="ts">
import { useAppointmentStore } from '@/stores/appointmentStore';
import { useToggleStore } from '@/stores/toggleStore';
import { reactive } from 'vue';
import { type AppointmentFormData, AppointmentStatus, AppointmentType } from '@/util/types/types';
import FormSteps from '../misc/FormSteps.vue';

const toggleStore = useToggleStore();
const appointmentStore = useAppointmentStore();

async function createAppointment() {
  await appointmentStore.createAppointment(appointmentForm);
  toggleStore.toggleAppointmentForm();
}

const appointmentForm = reactive<AppointmentFormData>({
  patientId: "",
  doctorId: "",
  departmentId: "",
  appointmentDate: "",
  appointmentTime: "",
  appointmentStatus: AppointmentStatus.SCHEDULED,
  appointmentType: AppointmentType.CONSULTATION,
  reason: "",
});

</script>

<template>
  <!-- Overlay -->
  <div class="formOverlay">
    <!-- Form -->
    <form @submit.stop.prevent="createAppointment()" v-click-outside="() => toggleStore.toggleAppointmentForm()" class="formFrame">
      <header class="flex justify-between items-center">
        <p class=" text-lg">+ Create Appointment</p>
        <img class="size-4.5 cursor-pointer hover:size-5" @click="toggleStore.toggleAppointmentForm"
          :src="`/assets/icons/${toggleStore.darkModeState}/close.svg`" alt="Close Icon">
      </header>

      <!-- <FormSteps :setp></FormSteps> -->

    </form>
  </div>
</template>
