<script setup lang="ts">
import { useToggleStore } from '@/stores/toggleStore';
import { useErrorAlert } from '@/composables/useErrorAlert';
import ErrorAlert from '@/components/ui/misc/ErrorAlert.vue';
import { onBeforeMount } from 'vue';
import Calendar from '@/components/ui/calendar/Calendar.vue';
import Selection from '@/components/ui/misc/Selection.vue';
import { departments } from '@/util/types/constants';

const toggleStore = useToggleStore();

const { showError, errorMessage, errorAlertKey, triggerBackendError } = useErrorAlert();

onBeforeMount(async () => {

});

</script>

<template>
  <main class="appointmentView">
    <header class="flex items-center pb-5 justify-between">

      <section class="flex gap-2 items-center">
        <img class="size-6" :src="`/assets/icons/${toggleStore.darkModeState}/appointment.svg`" alt="AppointmentIcon" />
        <h2 class="text-[20px]">Appointments</h2>
        <ErrorAlert for="backend error" class="ml-5" :show="showError" :alert-key="errorAlertKey" :message="errorMessage" />
      </section>

      <section class="z-52">
        <Selection :data="departments" storeName="appointment" stateName="selectedDepartment" toggle-state-name="showDepartmentSelection"></Selection>
      </section>
    </header>

    <section class="h-full">
      <Calendar></Calendar>
    </section>

    <footer>

    </footer>
  </main>
</template>
