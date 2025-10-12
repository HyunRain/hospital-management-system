<script setup lang="ts">
import { useToggleStore } from '@/stores/toggleStore';
import { useStaffStore } from '@/stores/staffStore';
import { onBeforeMount, computed } from 'vue';
import { useAuthStore } from '@/stores/authStore';
import { isAxiosError } from 'axios';
import AddStaff from '@/components/ui/staffview/AddStaff.vue';
import ErrorAlert from '@/components/ui/misc/ErrorAlert.vue';
import { useErrorAlert } from '@/composables/useErrorAlert';
import PaginationWrapper from '@/components/ui/pagination/PaginationWrapper.vue';
import { usePagination } from '@/composables/usePagination';
import NonAdminState from '@/components/ui/misc/NonAdminState.vue';
import DataTable from '@/components/ui/DataTable.vue';

const toggleStore = useToggleStore();
const authStore = useAuthStore();
const staffStore = useStaffStore();


const isAdmin = computed(() => authStore.role === 'ADMIN');
const { showError, errorMessage, errorAlertKey, triggerBackendError } = useErrorAlert();

onBeforeMount(async () => {
  if (staffStore.staff.length === 0 && isAdmin.value) {
    try {
      await staffStore.getPageOfAllStaff(0, staffStore.size);
    } catch (error: unknown) {
      if (
        isAxiosError(error) &&
        (error.message.includes('Network Error') || error.response?.status === 503)
      ) {
        triggerBackendError("Staff Service is currently unavailable.");
      }
    }
  }
});

const staffColumns = [
  { key: 'fullName', label: 'Name', class: 'border-0 rounded-l-lg' },
  { key: 'staffId', label: 'StaffID', class: '' },
  { key: 'gender', label: 'Gender', class: '' },
  { key: 'dateOfBirth', label: 'DateOfBirth', class: '' },
  { key: 'email', label: 'Email', class: '' },
  { key: 'city', label: 'City', class: 'min-w-[80px]' },
  { key: 'role', label: 'Role', class: 'border-0 rounded-r-lg' },
]

const {
  range,
  currentPage,
  pageSize,
  handlePageChange,
  searchInput
} = usePagination('1-15', staffStore, staffStore.searchStaff, staffStore.getPageOfAllStaff, 'totalStaff');

</script>

<template>
  <main class="flex flex-col w-full mt-5 p-5 bg-gray-50 dark:bg-[#0a0a0a] dark:border-neutral-900
    min-h-[calc(100vh-147px)] overflow-y-auto rounded-lg border border-neutral-200">

    <template v-if="isAdmin">

      <header class="flex justify-between items-center mb-10">
        <section class="flex gap-2 items-center">
          <img class="size-6" :src="`/assets/icons/${toggleStore.darkModeState}/employees.svg`" alt="Doctor Icon">
          <h2 class="text-[20px]">Staff</h2>
          <ErrorAlert for="backend error" class="ml-5" :show="showError" :alert-key="errorAlertKey" :message="errorMessage" />
        </section>
        <section class="items-center flex gap-2">
          <input v-model="searchInput" type="search" placeholder="Search by Name, ID, ..." class="search-input">
          <button @click="toggleStore.toggleAddStaffModal" class="button flex items-center min-w-fit">
            + Add Staff
          </button>
          <AddStaff v-if="toggleStore.showAddStaffModal" />
        </section>
      </header>

      <section class="h-full overflow-auto items-start flex">
        <DataTable :data="staffStore.staff" :columns="staffColumns"></DataTable>
      </section>

      <PaginationWrapper @page-change="handlePageChange" :range="range" :total="staffStore.totalStaff" :current-page="currentPage"
        :size-per-page="pageSize" type="Staff">
      </PaginationWrapper>

    </template>

    <NonAdminState v-else message="You do not have permission to view employee data."></NonAdminState>
  </main>
</template>
