<script setup lang="ts">
import { useBillingStore } from '@/stores/billingStore';
import { useAuthStore } from '@/stores/authStore';
import { computed, onBeforeMount } from 'vue';
import { useToggleStore } from '@/stores/toggleStore';
import { Line, Bar } from 'vue-chartjs';
import { Chart as ChartJS, Title, Tooltip, Legend, LineElement, BarElement, PointElement, ArcElement, CategoryScale, LinearScale, Filler } from 'chart.js';
import DataTable from '@/components/ui/DataTable.vue';
import { isAxiosError } from 'axios';
import ErrorAlert from '@/components/ui/misc/ErrorAlert.vue';
import { useErrorAlert } from '@/composables/useErrorAlert';
import PaginationWrapper from '@/components/ui/pagination/PaginationWrapper.vue';
import { usePagination } from '@/composables/usePagination';
import NonAdminState from '@/components/ui/misc/NonAdminState.vue';
import { useLineChart } from '@/composables/useLineChart';
import { useBarChart } from '@/composables/useBarChart';
import { useChartStore } from '@/stores/chartStore';
import ChartRangeSelection from '@/components/ui/misc/ChartRangeSelection.vue';

const billingStore = useBillingStore();
const authStore = useAuthStore();
const toggleStore = useToggleStore();
const chartStore = useChartStore();

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
  { key: 'invoice', label: 'Invoice', class: 'md:px-0 px-10' },
  { key: 'status', label: 'Status', class: 'md:px-0 pr-7' },
  { key: 'billingItemType', label: 'Type', class: '' },
  { key: 'totalPrice', label: 'Amount', class: 'md:px-0 px-5' },
  { key: 'startDate', label: 'Date', class: 'md:px-0 px-5' },
  { key: 'patientName', label: 'Patient Name', class: 'md:px-0 px-13' },
  { key: 'dueDate', label: 'Due Date', class: 'md:px-0 px-5' },
  { key: 'download', label: 'Download', class: '' },
];


const { chartDataBar: chartDataBarBillings, chartOptionsBar: chartOptionsBarBillings } = useBarChart(
  'Billing Items',
  chartStore.billingChartData,
  computed(() => chartStore.billingSelectedRange)
);

const { chartDataLine: chartDataLineRevenue, chartOptionsLine: chartOptionsLineRevenue } = useLineChart(
  'Revenue',
  chartStore.revenueChartData,
  computed(() => chartStore.revenueSelectedRange)
);


// Fetching billing invoices
async function handleFetchPdf() {
  const pdfUrl = await billingStore.fetchPdfUrl("billing-items/Besucherfolder_Willkommen-im-AKH.pdf");
  window.open(pdfUrl, '_blank');
}

const isDoctorSearch = false;

const {
  range,
  currentPage,
  pageSize,
  handlePageChange,
  searchInput
} = usePagination('1-8', billingStore, billingStore.searchBillingItems, billingStore.getPageOfBillingItems, isDoctorSearch, 'totalBillingItems');
</script>

<template>
  <main class="baseView">
    <section v-if="isAdmin" class="justify-between items-center mb-2">
      <header class="flex gap-2 items-center mb-5">
        <img class="size-6" :src="`/assets/icons/${toggleStore.darkModeState}/billing.svg`" alt="Billing Icon">
        <h2 class="text-[20px]">Billings</h2>
        <ErrorAlert for="backend error" class="ml-5" :show="showError" :alert-key="errorAlertKey" :message="errorMessage" />
      </header>

      <!-- Charts Section -->
      <section class="grid grid-cols-1 md:grid-cols-2 gap-5 w-full pb-5">
        <div class="chart">
          <div class="flex mb-5 items-center justify-between">
            <p class="chartHeader">Billings</p>
            <ChartRangeSelection chart-key="billing" default-value="Last Week"></ChartRangeSelection>
          </div>
          <div class="flex-1">
            <Bar :data="chartDataBarBillings" :options="chartOptionsBarBillings" />
          </div>
        </div>
        <div class="chart">
          <div class="flex mb-5 items-center justify-between">
            <p class="chartHeader">Revenue €</p>
            <ChartRangeSelection chart-key="revenue" default-value="Last Year"></ChartRangeSelection>
          </div>
          <div class="flex-1">
            <Line :data="chartDataLineRevenue" :options="chartOptionsLineRevenue" />
          </div>
        </div>
      </section>

      <!-- Billing History -->
      <section class="pb-5">
        <h3 class="text-[18px] mb-5">Recent Billings</h3>
        <section class="h-full overflow-auto items-start flex">
          <DataTable :data="billingStore.billingItems" :columns="billingColumns" :billing-table="true" table-name="billing">
            <template #invoice>
              <div @click="handleFetchPdf()" class="flex gap-3 items-center justify-center w-fit">
                <img class="h-7 w-7" src="/assets/icons/darkMode/pdf.svg" alt="PDF Icon">
                Invoice
              </div>
            </template>
            <template #download>
              <button class=" cursor-grab border bg-white dark:bg-black hover:bg-red-300 dark:hover:bg-[#101010] p-2 rounded-lg">Download</button>
            </template>
          </DataTable>
        </section>
      </section>

      <PaginationWrapper @page-change="handlePageChange" :range="range" :current-page=currentPage :total="billingStore.totalBillingItems"
        :size-per-page="pageSize" type="Billing-Items">
      </PaginationWrapper>

    </section>

    <NonAdminState v-else message="You do not have permission to view billing data."></NonAdminState>
  </main>
</template>
