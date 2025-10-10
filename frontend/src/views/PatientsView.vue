<script setup lang="ts">
import PatientTable from '@/components/ui/patientview/PatientTable.vue';
import { useToggleStore } from '@/stores/toggleStore';
import { Pagination, PaginationContent, PaginationEllipsis, PaginationItem, PaginationNext, PaginationPrevious, } from '@/components/ui/pagination';
import AddPatient from '@/components/ui/patientview/AddPatient.vue';
import { usePatientStore } from '@/stores/patientStore';
import { ref, watch, onBeforeMount } from 'vue';
import debounce from 'lodash.debounce';
import { isAxiosError } from 'axios';
import ErrorAlert from '@/components/ui/misc/ErrorAlert.vue';
import { useAuthStore } from '@/stores/authStore';
import { useErrorAlert } from '@/composables/useErrorAlert';

const toggleStore = useToggleStore();
const patientStore = usePatientStore();
const authStore = useAuthStore();

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

const patientRange = ref<string>('1-15');
const pageSize = ref<number>(patientStore.size);
const currentPage = ref(patientStore.page);

async function handlePageChange(page: number) {
  patientStore.page = page;

  if (searchInput.value.length > 1) {
    await patientStore.searchPatients(searchInput.value, page - 1, pageSize.value);
  } else {
    await patientStore.getPageOfPatients(page - 1, pageSize.value);
  }
  updatePatientsRange(page);
}

// PatientRange = eg. 'Showing 16-30 of 80 patients'
function updatePatientsRange(page: number) {
  const size = patientStore.size;
  const start = (page - 1) * size + 1;
  const end = Math.min(size * page, patientStore.totalPatients);

  patientRange.value = `${start}-${end}`;
}

// ------------------------ Search Patients ------------------------

const searchInput = ref('');

const debouncedSearch = debounce(async (input: string) => {
  if (input.length > 1) {
    await patientStore.searchPatients(input, 0, pageSize.value);
  }
  if (input.length < 1) {
    // if search field becomes empty, fetch first page of all patients and set the visual current page back to 1
    await patientStore.getPageOfPatients(0, pageSize.value);
    currentPage.value = 1;
  }
}, 300); // delay in ms

watch(searchInput, (newInput) => {
  debouncedSearch(newInput);
});
</script>

<template>
  <div class="flex flex-col w-full mt-5 p-5 bg-gray-50 dark:bg-[#0a0a0a] dark:border border-neutral-900 min-h-[calc(100vh-147px)] rounded-lg">
    <div class="flex justify-between items-center mb-10">
      <div class="flex gap-2 items-center">
        <img class="size-6" :src="`/assets/icons/${toggleStore.darkModeState}/patients.svg`" alt="PatientIcon" />
        <h2 class="text-[20px]">Patients</h2>
        <ErrorAlert class="ml-5" :show="showError" :alert-key="errorAlertKey" :message="errorMessage" />
      </div>
      <div class="items-center flex gap-2">
        <input type="search" placeholder="Search ..." v-model="searchInput" class="search-input" />
        <button @click="toggleStore.toggleAddPatientModel" class="button flex items-center min-w-fit">
          + Add Patient
        </button>
        <AddPatient v-if="toggleStore.showAddPatientModal" />
      </div>
    </div>
    <div class="h-full overflow-auto items-start flex">
      <PatientTable />
    </div>
    <div class="flex flex-col md:flex-row gap-3 items-center justify-between py-3">
      <p class="md:mt-0 mt-3">
        Showing {{ patientRange }} of {{ patientStore.totalPatients }} patients
      </p>
      <div>
        <Pagination v-slot="{ page }" :items-per-page="patientStore.size" v-model:page="currentPage" :total="patientStore.totalPatients"
          @update:page="handlePageChange" :default-page="1">
          <PaginationContent v-slot="{ items }">
            <PaginationPrevious class="cursor-pointer" />
            <template v-for="(item, index) in items" :key="index">
              <PaginationItem class="cursor-pointer" v-if="item.type === 'page'" :value="item.value" :is-active="item.value === page">
                {{ item.value }}
              </PaginationItem>
            </template>
            <PaginationEllipsis :index="4" />
            <PaginationNext class="cursor-pointer" />
          </PaginationContent>
        </Pagination>
      </div>
    </div>
  </div>
</template>

<style scoped></style>
