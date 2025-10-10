<script setup lang="ts">
import StaffTable from '@/components/ui/staffview/StaffTable.vue';
import { useToggleStore } from '@/stores/toggleStore';
import { Pagination, PaginationContent, PaginationEllipsis, PaginationItem, PaginationNext, PaginationPrevious } from '@/components/ui/pagination';
import { useStaffStore } from '@/stores/staffStore';
import { onBeforeMount, ref, computed, watch } from 'vue';
import { useAuthStore } from '@/stores/authStore';
import { isAxiosError } from 'axios';
import debounce from 'lodash.debounce';
import AddStaff from '@/components/ui/staffview/AddStaff.vue';
import ErrorAlert from '@/components/ui/misc/ErrorAlert.vue';
import { useErrorAlert } from '@/composables/useErrorAlert';

const toggleStore = useToggleStore();
const authStore = useAuthStore();
const staffStore = useStaffStore();


const isAdmin = computed(() => authStore.role === 'ADMIN');
const { showError, errorMessage, errorAlertKey, triggerBackendError } = useErrorAlert();

onBeforeMount(async () => {
  if (staffStore.staff.length === 0 && isAdmin.value) {
    try {
      await staffStore.getPageOfAllStaff(0, staffStore.size);
      updateEmployeesRange(1);
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

const staffRange = ref<string>('1-15');
const pageSize = ref<number>(staffStore.size);
const currentPage = ref(staffStore.page);

async function handlePageChange(page: number) {
  staffStore.page = page;
  if (searchInput.value.length > 1) {
    await staffStore.searchStaff(searchInput.value, page - 1, pageSize.value);
  } else {
    await staffStore.getPageOfAllStaff(page - 1, pageSize.value);
  }
  updateEmployeesRange(page);
}

// EmployeeRange = eg. 'Showing 16-30 of 80 employees'
function updateEmployeesRange(page: number) {
  const size = staffStore.size;
  const start = (page - 1) * size + 1;
  const end = Math.min(size * page, staffStore.totalStaff);
  staffRange.value = `${start}-${end}`;
}

// ------------------------ Search Employees ------------------------

const searchInput = ref('');

const debouncedSearch = debounce(async (input: string) => {
  if (input.length > 1) await staffStore.searchStaff(input, 0, pageSize.value);
  if (input.length < 1) {
    // if search field becomes empty, fetch first page of all employees and set the visual current page back to 1
    await staffStore.getPageOfAllStaff(0, pageSize.value);
    currentPage.value = 1;
  }

  //if(staffStore.staff.length === 0)
}, 300); // delay in ms

watch(searchInput, (newInput) => {
  if (isAdmin.value) debouncedSearch(newInput);
});

</script>

<template>
  <div
    class="flex flex-col w-full mt-5 p-5 bg-gray-50 dark:bg-[#0a0a0a] dark:border border-neutral-900 min-h-[calc(100vh-147px)] overflow-y-auto rounded-lg">
    <div v-if="isAdmin" class="flex justify-between items-center mb-10">
      <div class="flex gap-2 items-center">
        <img class="size-6" :src="`/assets/icons/${toggleStore.darkModeState}/employees.svg`" alt="Doctor Icon">
        <h2 class="text-[20px]">Staff</h2>
        <ErrorAlert class="ml-5" :show="showError" :alert-key="errorAlertKey" :message="errorMessage" />
      </div>
      <div class="items-center flex gap-2">
        <input v-model="searchInput" type="search" placeholder="Search by Name, ID, ..." class="search-input">
        <button @click="toggleStore.toggleAddStaffModal" class="button flex items-center min-w-fit">
          + Add Staff
        </button>
        <AddStaff v-if="toggleStore.showAddStaffModal" />
      </div>
    </div>
    <div v-if="isAdmin" class="h-full overflow-auto items-start flex">
      <StaffTable />
    </div>
    <div v-else class="flex items-center justify-center w-full h-full text-center p-5">
      <p class="text-lg font-semibold">
        You do not have permission to view employee data.
      </p>
    </div>
    <div v-if="isAdmin" class="flex flex-col md:flex-row gap-3 items-center justify-between py-3">
      <p>Showing {{ staffRange }} of {{ staffStore.totalStaff }} Employees</p>
      <div>
        <Pagination v-slot="{ page }" :items-per-page="staffStore.size" :total="staffStore.totalStaff" :default-page="1"
          @update:page="handlePageChange">
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
