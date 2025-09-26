<script setup lang="ts">
import DepartmentTable from '@/components/ui/departmentview/DepartmentTable.vue';
import { useToggleStore } from '@/stores/toggleStore';
import { useDepartmentStore } from '@/stores/departmentStore';
import { useAuthStore } from '@/stores/authStore';
import { onBeforeMount, ref, computed } from 'vue';
import { isAxiosError } from 'axios';
import ErrorAlert from '@/components/ui/misc/ErrorAlert.vue';

const toggleStore = useToggleStore();
const departmentStore = useDepartmentStore();
const authStore = useAuthStore();

const isAdmin = computed(() => authStore.role === 'ADMIN');


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

const showDepartmentsFailed = ref(false);
const errorMessage = ref('');
const errorAlertKey = ref(Date.now());
let timer: ReturnType<typeof setTimeout>;

function triggerBackendError(message: string) {
  window.clearTimeout(timer);
  showDepartmentsFailed.value = true;
  errorAlertKey.value = Date.now();
  errorMessage.value = message;
  timer = setTimeout(() => {
    showDepartmentsFailed.value = false;
    errorMessage.value = '';
  }, 10000);
}

</script>

<template>
  <div class="flex flex-col w-full mt-5 p-5 bg-gray-50 dark:bg-[#030712] dark:border border-zinc-800 min-h-[calc(100vh-147px)] rounded-xl">
    <div v-if="isAdmin" class="flex justify-between items-center mb-5">
      <div class="flex gap-2 items-center">
        <img class="size-6" :src="`/assets/icons/${toggleStore.darkModeState}/department.svg`" alt="DepartmentIcon" />
        <h2 class="text-[20px]">Departments</h2>
        <ErrorAlert class="ml-5" :show="showDepartmentsFailed" :alert-key="errorAlertKey" :message="errorMessage" />
      </div>
    </div>
    <div v-if="isAdmin" class="h-full overflow-auto items-start flex">
      <DepartmentTable />
    </div>
    <div v-else class="flex items-center justify-center w-full h-full text-center p-5">
      <p class="text-lg font-semibold">
        You do not have permission to view department data.
      </p>
    </div>
  </div>
</template>

<style scoped>

</style>
