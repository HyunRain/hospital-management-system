<script setup lang="ts">
import { useToggleStore } from '@/stores/toggleStore';
import AddPatient from '@/components/ui/patientview/AddPatient.vue';
import { usePatientStore } from '@/stores/patientStore';
import { onBeforeMount } from 'vue';
import { isAxiosError } from 'axios';
import ErrorAlert from '@/components/ui/misc/ErrorAlert.vue';
import { useErrorAlert } from '@/composables/useErrorAlert';
import PaginationWrapper from '@/components/ui/pagination/PaginationWrapper.vue';
import { usePagination } from '@/composables/usePagination';
import DataTable from '@/components/ui/DataTable.vue';

const toggleStore = useToggleStore();
const patientStore = usePatientStore();
const { showError, errorMessage, errorAlertKey, triggerBackendError } = useErrorAlert();

onBeforeMount(async () => {
  if (patientStore.patients.length === 0) {
    try {
      await patientStore.getPageOfPatients(0, patientStore.size);
    } catch (error: unknown) {
      if (
        isAxiosError(error) &&
        (error.message.includes('Network Error') || error.response?.status === 503)
      ) {
        triggerBackendError("Patient Service is currently unavailable.");
      }
    }
  }
});

const patientColumns = [
  { key: 'fullName', label: 'Name', class: 'border-0 rounded-l-lg' },
  { key: 'patientId', label: 'PatientId', class: '' },
  { key: 'gender', label: 'Gender', class: '' },
  { key: 'dateOfBirth', label: 'DateOfBirth', class: '' },
  { key: 'email', label: 'Email', class: '' },
  { key: 'city', label: 'City', class: 'min-w-[80px]' },
  { key: 'status', label: 'Status', class: 'border-0 rounded-r-lg' },
];

const isDoctorSearch = false;

const {
  range,
  currentPage,
  pageSize,
  handlePageChange,
  searchInput
} = usePagination('1-15', patientStore, patientStore.searchPatients, patientStore.getPageOfPatients, isDoctorSearch, 'totalPatients');
</script>

<template>
  <main class="baseView">
    <header class="flex justify-between items-center mb-10">
      <section class="flex gap-2 items-center">
        <img class="size-6" :src="`/assets/icons/${toggleStore.darkModeState}/patients.svg`" alt="PatientIcon" />
        <h2 class="text-[20px]">Patients</h2>
        <ErrorAlert for="backend error" class="ml-5" :show="showError" :alert-key="errorAlertKey" :message="errorMessage" />
      </section>
      <section class="items-center flex gap-2">
        <input type="search" placeholder="Search ..." v-model="searchInput" class="search-input" />
        <button @click="toggleStore.toggleAddPatientModel" class="button flex items-center min-w-fit">
          + Add Patient
        </button>
        <AddPatient v-if="toggleStore.showAddPatientModal" />
      </section>
    </header>

    <section class="h-full overflow-auto items-start flex">
      <DataTable :data="patientStore.patients" :columns="patientColumns" />
    </section>

    <PaginationWrapper @page-change="handlePageChange" :range="range" :total="patientStore.totalPatients" :current-page="currentPage"
      :size-per-page="pageSize" type="patients">
    </PaginationWrapper>

  </main>
</template>
