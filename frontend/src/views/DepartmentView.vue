<script setup lang="ts">
import { useToggleStore } from '@/stores/toggleStore';
import { useDepartmentStore } from '@/stores/departmentStore';
import { useAuthStore } from '@/stores/authStore';
import { onBeforeMount, ref, computed } from 'vue';
import { isAxiosError } from 'axios';
import ErrorAlert from '@/components/ui/misc/ErrorAlert.vue';
import DataTable from '@/components/ui/DataTable.vue';
import { useErrorAlert } from '@/composables/useErrorAlert';
import NonAdminState from '@/components/ui/misc/NonAdminState.vue';

const toggleStore = useToggleStore();
const departmentStore = useDepartmentStore();
const authStore = useAuthStore();

const isAdmin = computed(() => authStore.role === 'ADMIN');
const { showError, errorMessage, errorAlertKey, triggerBackendError } = useErrorAlert();

onBeforeMount(async () => {
  if (departmentStore.departments.length > 0) return;

  try {
    await departmentStore.fetchDepartments();
  } catch (error: unknown) {
    if (
      isAxiosError(error) &&
      (error.message.includes('Network Error') || error.response?.status === 503)
    ) {
      triggerBackendError("Department Service is currently unavailable.");
    }
  }
});

const departmentColumns = [
  { key: 'name', label: 'Name', class: 'border-0 rounded-l-xl' },
  { key: 'headOfDepartmentName', label: 'Head of Dept', class: '' },
  { key: 'staffCount', label: 'Staff Count', class: '' },
  { key: 'bedCapacity', label: 'Bed Capacity', class: '' },
  { key: 'currentBedCount', label: 'Current Bed Count', class: '' },
  { key: 'status', label: 'Status', class: '' }
];
</script>

<template>
  <main class="baseView">
    <header v-if="isAdmin" class="flex justify-between items-center">
      <section class="flex gap-2 items-center">
        <img class="size-6" :src="`/assets/icons/${toggleStore.darkModeState}/department.svg`" alt="DepartmentIcon" />
        <h2 class="text-[20px]">Departments</h2>
        <ErrorAlert for="backend error" class="ml-5" :show="showError" :alert-key="errorAlertKey" :message="errorMessage" />
      </section>
    </header>
    <section v-if="isAdmin" class="h-full overflow-auto items-center flex">
      <DataTable :data="departmentStore.departments" :columns="departmentColumns">
        <template #status="{ statusValue }">
          <div class="flex items-center gap-2 justify-center">
            <span class="inline-block size-3 rounded-full" :class="statusValue === 'Open' ? 'bg-green-500' : 'bg-red-500'"></span>
            <span>{{ statusValue }}</span>
          </div>
        </template>
      </DataTable>
    </section>
    <NonAdminState v-else message="You do not have permission to view department data."></NonAdminState>
  </main>
</template>
