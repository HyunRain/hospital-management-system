<script setup lang="ts">
import { useToggleStore } from '@/stores/toggleStore';
import { Pagination, PaginationContent, PaginationEllipsis, PaginationItem, PaginationNext, PaginationPrevious, } from '@/components/ui/pagination';
import AddDoctor from '@/components/ui/doctorview/AddDoctor.vue';
import { useDoctorStore } from '@/stores/doctorStore';
import { ref, watch, onBeforeMount, computed } from 'vue';
import debounce from 'lodash.debounce';
import { isAxiosError } from 'axios';
import ErrorAlert from '@/components/ui/misc/ErrorAlert.vue';
import DoctorTable from '@/components/ui/doctorview/DoctorTable.vue';
import { useAuthStore } from '@/stores/authStore';
import { useErrorAlert } from '@/composables/useErrorAlert';

const toggleStore = useToggleStore();
const authStore = useAuthStore();
const doctorStore = useDoctorStore();

const isAdmin = computed(() => authStore.role === 'ADMIN');
const { showError, errorMessage, errorAlertKey, triggerBackendError } = useErrorAlert();


onBeforeMount(async () => {
  if (doctorStore.doctors.length === 0 && isAdmin.value) {
    try {
      await doctorStore.getPageOfDoctors(0, doctorStore.size);
      updatePatientsRange(1);
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

const doctorRange = ref<string>('1-15');
const pageSize = ref<number>(doctorStore.size);
const currentPage = ref(doctorStore.page);

async function handlePageChange(page: number) {
  doctorStore.page = page;
  if (searchInput.value.length > 1) {
    await doctorStore.searchDoctors(searchInput.value, page - 1, pageSize.value);
  } else {
    await doctorStore.getPageOfDoctors(page - 1, pageSize.value);
  }
  updatePatientsRange(page);
}

// PatientRange = eg. 'Showing 16-30 of 80 patients'
function updatePatientsRange(page: number) {
  const size = doctorStore.size;
  const start = (page - 1) * size + 1;
  const end = Math.min(size * page, doctorStore.totalDoctors);
  doctorRange.value = `${start}-${end}`;
}

// ------------------------ Search Patients ------------------------

const searchInput = ref('');

const debouncedSearch = debounce(async (input: string) => {
  if (input.length > 1) await doctorStore.searchDoctors(input, 0, pageSize.value);
  if (input.length < 1) {
    // if search field becomes empty, fetch first page of all patients and set the visual current page back to 1
    await doctorStore.getPageOfDoctors(0, pageSize.value);
    currentPage.value = 1;
  }
}, 300); // delay in ms

watch(searchInput, (newInput) => {
  if (isAdmin.value) debouncedSearch(newInput);
});
</script>

<template>
  <div
    class="flex flex-col w-full mt-5 p-5 bg-gray-50 dark:bg-[#0a0a0a] shadow-md dark:border dark:border-neutral-900 min-h-[calc(100vh-147px)] rounded-lg">
    <div v-if="isAdmin" class="flex justify-between items-center mb-10">
      <div class="flex gap-2 items-center">
        <img class="size-6" :src="`/assets/icons/${toggleStore.darkModeState}/doctor.svg`" alt="PatientIcon" />
        <h2 class="text-[20px]">Doctors</h2>
        <ErrorAlert class="ml-5" :show="showError" :alert-key="errorAlertKey" :message="errorMessage" />
      </div>
      <div class="items-center flex gap-2">
        <input type="search" placeholder="Search ..." v-model="searchInput" class="search-input" />
        <button @click="toggleStore.toggleAddDoctorModal" class="button flex items-center min-w-fit">
          + Add Doctor
        </button>
        <AddDoctor v-if="toggleStore.showAddDoctorModal" />
      </div>
    </div>
    <div v-if="isAdmin" class="h-full overflow-auto items-start flex">
      <DoctorTable />
    </div>
    <div v-else class="flex items-center justify-center w-full h-full text-center p-5">
      <p class="text-lg font-semibold">
        You do not have permission to view doctor data.
      </p>
    </div>
    <div v-if="isAdmin" class="flex flex-col md:flex-row gap-3 items-center justify-between py-3">
      <p class="md:mt-0 mt-3">
        Showing {{ doctorRange }} of {{ doctorStore.totalDoctors }} doctors
      </p>
      <div>
        <Pagination v-slot="{ page }" :items-per-page="doctorStore.size" v-model:page="currentPage" :total="doctorStore.totalDoctors"
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
