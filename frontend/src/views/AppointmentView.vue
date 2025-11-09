<script setup lang="ts">
import { useToggleStore } from '@/stores/toggleStore';
import { useErrorAlert } from '@/composables/useErrorAlert';
import ErrorAlert from '@/components/ui/misc/ErrorAlert.vue';
import { onBeforeMount, ref } from 'vue';
import Calendar from '@/components/ui/calendar/Calendar.vue';
import Selection from '@/components/ui/misc/Selection.vue';
import { departments } from '@/util/types/constants';
import AppointmentForm from '@/components/ui/appointmentview/AppointmentForm.vue';
import { useDepartmentStore } from '@/stores/departmentStore';

const toggleStore = useToggleStore();
const departmentStore = useDepartmentStore();

const { showError, errorMessage, errorAlertKey, triggerBackendError } = useErrorAlert();

onBeforeMount(async () => {
  await departmentStore.fetchDepartments();
});

const selectedDepartment = ref<string>('Pediatrics');
</script>

<template>
  <main class="appointmentView">
    <header class="flex items-center pb-5 justify-between">

      <section class="flex gap-2 items-center">
        <img class="size-6" :src="`/assets/icons/${toggleStore.darkModeState}/appointment.svg`" alt="AppointmentIcon" />
        <h2 class="text-[20px]">Appointments</h2>
        <ErrorAlert for="backend error" class="ml-5" :show="showError" :alert-key="errorAlertKey" :message="errorMessage" />
      </section>

      <section class="z-49">
        <Selection :data="departments" v-model="selectedDepartment" :is-open="toggleStore.showDepartmentSelection"
          @toggle="toggleStore.toggleDepartmentSelection" min-width="166px">
          <template v-slot:Mobile>
            <span class="inline lg:hidden"> {{ selectedDepartment.slice(0, 4) }} </span>
          </template>
        </Selection>
      </section>
    </header>

    <section class="h-full">
      <Calendar></Calendar>
      <AppointmentForm :current-department="selectedDepartment" v-if="toggleStore.showAppointmentForm"></AppointmentForm>
    </section>

    <footer>

    </footer>
  </main>
</template>
