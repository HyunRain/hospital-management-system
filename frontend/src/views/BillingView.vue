<script setup lang="ts">
import { useBillingStore } from '@/stores/billingStore';
import { useAuthStore } from '@/stores/authStore';
import { computed, ref, onBeforeMount } from 'vue';
import { useToggleStore } from '@/stores/toggleStore';
import { Line, Pie, Bar } from 'vue-chartjs';
import { Chart as ChartJS, Title, Tooltip, Legend, LineElement, BarElement, PointElement, ArcElement, CategoryScale, LinearScale, Filler } from 'chart.js';
import type { ChartOptions } from 'chart.js';
import DataTable from '@/components/ui/DataTable.vue';
import { isAxiosError } from 'axios';
import ErrorAlert from '@/components/ui/misc/ErrorAlert.vue';
import { useErrorAlert } from '@/composables/useErrorAlert';
import PaginationWrapper from '@/components/ui/pagination/PaginationWrapper.vue';
import { usePagination } from '@/composables/usePagination';
import NonAdminState from '@/components/ui/misc/NonAdminState.vue';

const billingStore = useBillingStore();
const authStore = useAuthStore();
const toggleStore = useToggleStore();

ChartJS.register(Title, Tooltip, Legend, LineElement, PointElement, BarElement, CategoryScale, LinearScale, Filler, ArcElement);

const isAdmin = computed(() => authStore.role === 'ADMIN');
const isDark = computed(() => toggleStore.darkModeState === 'darkMode');
const { showError, errorMessage, errorAlertKey, triggerBackendError } = useErrorAlert();

onBeforeMount(async () => {
  if (billingStore.billingItems.length === 0 && isAdmin.value) {
    try {
      await billingStore.fetchBillings(0, billingStore.size);
    } catch (error: unknown) {
      if (isAxiosError(error) && (error.message.includes('Network Error') || error.response?.status === 503)) {
        triggerBackendError("Billing Service is currently unavailable.");
      }
    }
  }
});

const billingColumns = [
  { key: 'invoice', label: 'Invoice', class: '' },
  { key: 'status', label: 'Status', class: '' },
  { key: 'billingItemType', label: 'Type', class: '' },
  { key: 'totalPrice', label: 'Amount', class: '' },
  { key: 'startDate', label: 'Date', class: '' },
  { key: 'patientName', label: 'Patient Name', class: 'md:px-0 px-13' },
  { key: 'dueDate', label: 'Due Date', class: 'md:px-0 px-3' },
  { key: 'download', label: 'Download', class: '' },
];

// ----------------------- Billings Bar Chart -----------------------

const selectedRangeBarBillings = ref<'week' | 'month' | 'year'>('week');

const chartDataSetsBarBillings = {
  week: {
    labels: ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun'],
    data: [10, 12, 8, 15, 20, 18, 9],
  },
  month: {
    labels: ['Week 1', 'Week 2', 'Week 3', 'Week 4'],
    data: [50, 70, 40, 90],
  },
  year: {
    labels: ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Sep', 'Oct', 'Nov', 'Dec'],
    data: [120, 90, 140, 100, 180, 160, 120, 90, 130, 180, 150, 120],
  },
};

const chartDataBarBillings = computed(() => ({
  labels: chartDataSetsBarBillings[selectedRangeBarBillings.value].labels,
  datasets: [{
    label: 'Billings',
    data: chartDataSetsBarBillings[selectedRangeBarBillings.value].data,
    backgroundColor: isDark.value ? '#e7523b20' : '#e7523b80',
  },
  ],
}));

const chartOptionsBarBillings = computed<ChartOptions<'bar'>>(() => ({
  responsive: true,
  maintainAspectRatio: false,
  plugins: {
    legend: {
      labels: {
        color: isDark.value ? '#dfdfd6' : '#4c4c4c',
      },
      position: 'bottom',
    },
  },
  elements: {
    bar: {
      borderWidth: 3,
      borderRadius: 10,
      borderColor: isDark.value ? '#e7523b95' : '#e7523b',
    }
  },
  scales: {
    x: {
      ticks: {
        color: isDark.value ? '#dfdfd6' : '#4c4c4c',
      },
      grid: {
        color: '#333',
        display: false,
      },
    },
    y: {
      min: 0,
      ticks: {
        color: isDark.value ? '#dfdfd6' : '#4c4c4c',
        stepSize: 10,
      },
      grid: {
        color: isDark.value ? '#333' : '#dfdfd6',
      },
    },
  },
}));

// ----------------------- Reveneue Overview Line Chart -----------------------

const selectedRangeLineRevenue = ref<'week' | 'month' | 'year'>('week');

const chartDataSetsLineRevenue = {
  week: {
    labels: ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun'],
    data: [10, 12, 8, 15, 20, 18, 9],
  },
  month: {
    labels: ['Week 1', 'Week 2', 'Week 3', 'Week 4'],
    data: [50, 70, 40, 90],
  },
  year: {
    labels: ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Sep', 'Oct', 'Nov', 'Dec'],
    data: [120, 90, 140, 100, 180, 160, 120, 90, 130, 180, 150, 120],
  },
};

const chartDataLineRevenue = computed(() => ({
  labels: chartDataSetsLineRevenue[selectedRangeLineRevenue.value].labels,
  datasets: [{
    label: 'Revenue',
    data: chartDataSetsLineRevenue[selectedRangeLineRevenue.value].data,
    borderColor: isDark.value ? '#e7523b95' : '#e7523b',
    backgroundColor: isDark.value ? '#e7523b20' : '#e7523b80',
    tension: 0.4,
    fill: true,
  },
  ],
}));


const chartOptionsLineRevenue = computed<ChartOptions<'line'>>(() => ({
  responsive: true,
  maintainAspectRatio: false,
  plugins: {
    legend: {
      labels: {
        color: isDark.value ? '#dfdfd6' : '#4c4c4c',
      },
      position: 'bottom',
    },
  },
  scales: {
    x: {
      ticks: {
        color: isDark.value ? '#dfdfd6' : '#4c4c4c',
      },
      grid: {
        color: '#333',
        display: false,
      },
    },
    y: {
      min: 0,
      ticks: {
        color: isDark.value ? '#dfdfd6' : '#4c4c4c',
        stepSize: 10,
      },
      grid: {
        color: isDark.value ? '#333' : '#dfdfd6',
      },
    },
  },
}));

// ----------------------- Billing Item PDF Fetching -----------------------

async function handleFetchPdf() {
  const pdfUrl = await billingStore.fetchPdfUrl("billing-items/Besucherfolder_Willkommen-im-AKH.pdf");
  window.open(pdfUrl, '_blank');
}

const {
  range,
  currentPage,
  pageSize,
  handlePageChange,
  searchInput
} = usePagination('1-8', billingStore, billingStore.searchBillingItems, billingStore.getPageOfBillingItems, 'totalBillingItems')
</script>

<template>
  <main
    class="flex flex-col w-full mt-5 p-5 bg-gray-50 dark:bg-[#0a0a0a] dark:border border-zinc-800 min-h-[calc(100vh-147px)] overflow-y-auto rounded-lg">

    <section v-if="isAdmin" class="justify-between items-center mb-2">

      <header class="flex gap-2 items-center mb-5">
        <img class="size-6" :src="`/assets/icons/${toggleStore.darkModeState}/billing.svg`" alt="Billing Icon">
        <h2 class="text-[20px]">Billings</h2>
        <ErrorAlert for="backend error" class="ml-5" :show="showError" :alert-key="errorAlertKey" :message="errorMessage" />
      </header>

      <!-- Charts Section -->
      <section class="grid grid-cols-1 md:grid-cols-2 gap-5 w-full px-5 pb-5">
        <div class="flex flex-col h-[300px] w-full rounded-lg bg-white dark:bg-[#0a0a0a] dark:border border-neutral-900 shadow-sm p-5">
          <div class="flex mb-5 items-center justify-between">
            <p class="text-[16px] text-zinc-800 dark:text-zinc-200">Billings</p>
            <div class="flex gap-4">
              <button class="hover:bg-red-100 dark:hover:bg-neutral-800 p-2 rounded-lg cursor-pointer" @click="selectedRangeBarBillings = 'week'">Last
                Week</button>
              <button class="hover:bg-red-100 dark:hover:bg-neutral-800 p-2 rounded-lg cursor-pointer"
                @click="selectedRangeBarBillings = 'month'">Last Month</button>
              <button class="hover:bg-red-100 dark:hover:bg-neutral-800 p-2 rounded-lg cursor-pointer" @click="selectedRangeBarBillings = 'year'">Last
                Year</button>
            </div>
          </div>
          <div class="flex-1">
            <Bar :data="chartDataBarBillings" :options="chartOptionsBarBillings" />
          </div>
        </div>
        <div class="flex flex-col h-[300px] w-full rounded-lg bg-white dark:bg-[#0a0a0a] dark:border border-neutral-900 shadow-sm p-5">
          <div class="flex mb-5 items-center justify-between">
            <p class="text-[16px] text-zinc-800 dark:text-zinc-200">Revenue Overview</p>
            <div class="flex gap-5">
              <button class="hover:bg-red-100 dark:hover:bg-neutral-800 p-2 rounded-lg cursor-pointer" @click="selectedRangeLineRevenue = 'week'">Last
                Week</button>
              <button class="hover:bg-red-100 dark:hover:bg-neutral-800 p-2 rounded-lg cursor-pointer"
                @click="selectedRangeLineRevenue = 'month'">Last
                Month</button>
              <button class="hover:bg-red-100 dark:hover:bg-neutral-800 p-2 rounded-lg cursor-pointer" @click="selectedRangeLineRevenue = 'year'">Last
                Year</button>
            </div>
          </div>
          <div class="flex-1">
            <Line :data="chartDataLineRevenue" :options="chartOptionsLineRevenue" />
          </div>
        </div>
      </section>

      <!-- Billing History -->
      <section class="p-5">
        <h3 class="text-[18px] mb-5">Recent Billings</h3>
        <DataTable :data="billingStore.billingItems" :columns="billingColumns" :billing-table="true">
          <template #invoice>
            <div @click="handleFetchPdf()" class="flex gap-3 items-center justify-center">
              <img class="h-7 w-7" src="/assets/icons/darkMode/pdf.svg" alt="PDF Icon">
              Invoice
            </div>
          </template>
          <template #download>
            <button class=" cursor-grab border bg-white dark:bg-black hover:bg-red-300 dark:hover:bg-[#101010] p-2 rounded-lg">Download</button>
          </template>
        </DataTable>
      </section>

      <PaginationWrapper @page-change="handlePageChange" :range="range" :current-page=currentPage :total="billingStore.totalBillingItems"
        :size-per-page="pageSize" type="Billing-Items">
      </PaginationWrapper>

    </section>

    <NonAdminState v-else message="You do not have permission to view billing data."></NonAdminState>
  </main>
</template>
