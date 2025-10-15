<script setup lang="ts">
import { useToggleStore } from '@/stores/toggleStore';
import AddDoctor from '@/components/ui/doctorview/AddDoctor.vue';
import { useDoctorStore } from '@/stores/doctorStore';
import { onBeforeMount, computed } from 'vue';
import { isAxiosError } from 'axios';
import ErrorAlert from '@/components/ui/misc/ErrorAlert.vue';
import { useAuthStore } from '@/stores/authStore';
import { useErrorAlert } from '@/composables/useErrorAlert';
import { usePagination } from '@/composables/usePagination';
import PaginationWrapper from '@/components/ui/pagination/PaginationWrapper.vue';
import DataTable from '@/components/ui/DataTable.vue';

const toggleStore = useToggleStore();
const authStore = useAuthStore();
const doctorStore = useDoctorStore();

const isAdmin = computed(() => authStore.role === 'ADMIN');
const { showError, errorMessage, errorAlertKey, triggerBackendError } = useErrorAlert();

onBeforeMount(async () => {
  if (doctorStore.doctors.length === 0 && isAdmin.value) {
    try {
      await doctorStore.getPageOfDoctors(0, doctorStore.size);
    } catch (error: unknown) {
      if (
        isAxiosError(error) &&
        (error.message.includes('Network Error') || error.response?.status === 503)
      ) {
        triggerBackendError("Doctor Service is currently unavailable.");
      }
    }
  }
});

const doctorColumns = [
  { key: 'fullName', label: 'Name', class: 'border-0 rounded-l-lg' },
  { key: 'staffId', label: 'StaffId', class: '' },
  { key: 'gender', label: 'Gender', class: '' },
  { key: 'dateOfBirth', label: 'DateOfBirth', class: '' },
  { key: 'email', label: 'Email', class: '' },
  { key: 'city', label: 'City', class: 'min-w-[80px]' },
  { key: 'departmentName', label: 'Department', class: 'border-0 rounded-r-lg' },
]

const isDoctorSearch = true;

const {
  range,
  currentPage,
  pageSize,
  handlePageChange,
  searchInput
} = usePagination('1-15', doctorStore, doctorStore.searchDoctors, doctorStore.getPageOfDoctors, isDoctorSearch, 'totalDoctors');
</script>

<template>
  <main class="baseView">
    <template v-if="isAdmin">
      <header class="flex justify-between items-center mb-10">
        <section class="flex gap-2 items-center">
          <img class="size-6" :src="`/assets/icons/${toggleStore.darkModeState}/doctor.svg`" alt="PatientIcon" />
          <h2 class="text-[20px]">Doctors</h2>
          <ErrorAlert for="backend error" class="ml-5" :show="showError" :alert-key="errorAlertKey" :message="errorMessage" />
        </section>
        <section class="items-center flex gap-2">
          <input type="search" placeholder="Search ..." v-model="searchInput" class="search-input" />
          <button @click="toggleStore.toggleAddDoctorModal" class="button flex items-center min-w-fit">
            + Add Doctor
          </button>
          <AddDoctor v-if="toggleStore.showAddDoctorModal" />
        </section>
      </header>

      <section class="h-full overflow-auto items-start flex">
        <DataTable :data="doctorStore.doctors" :columns="doctorColumns"/>
      </section>

      <PaginationWrapper @page-change="handlePageChange" :range="range" :total="doctorStore.totalDoctors" :current-page="currentPage"
        :size-per-page="pageSize" type="doctors">
      </PaginationWrapper>

    </template>

    <section v-else class="flex items-center justify-center w-full h-full text-center p-5">
      <p class="text-lg font-semibold">
        You do not have permission to view doctor data.
      </p>
    </section>
  </main>
</template>
