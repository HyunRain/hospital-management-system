<script setup lang="ts">
import DashboardCard from '@/components/ui/homeview/DashboardCard.vue';
import { Line, Pie, Bar } from 'vue-chartjs';
import { Chart as ChartJS, Title, Tooltip, Legend, LineElement, BarElement, PointElement, ArcElement, CategoryScale, LinearScale, Filler } from 'chart.js';
import { computed, ref, onBeforeMount } from 'vue';
import { usePatientStore } from '@/stores/patientStore';
import { useDoctorStore } from '@/stores/doctorStore';
import { useAuthStore } from '@/stores/authStore';
import { useDepartmentStore } from '@/stores/departmentStore';
import { useStaffStore } from '@/stores/staffStore';
import ChartRangeSelection from '@/components/ui/misc/ChartRangeSelection.vue';
import { useLineChart } from '@/composables/useLineChart';
import { useBarChart } from '@/composables/useBarChart';
import { useChartStore } from '@/stores/chartStore';
import { usePieChart } from '@/composables/usePieChart';

const authStore = useAuthStore();
const patientStore = usePatientStore();
const doctorStore = useDoctorStore();
const departmentStore = useDepartmentStore();
const staffStore = useStaffStore();
const chartStore = useChartStore();

onBeforeMount(async () => {
  if (patientStore.patients.length === 0) {
    await patientStore.getPageOfPatients(0, patientStore.size);
  }
  if (authStore.role === 'ADMIN') {
    if (doctorStore.doctors.length === 0) {
      await doctorStore.getPageOfDoctors(0, doctorStore.size);
    }
    if (departmentStore.departments.length === 0) {
      await departmentStore.fetchDepartments();
    }
    if (staffStore.staff.length === 0) {
      await staffStore.getPageOfAllStaff(0, staffStore.size);
    }
  }
});

ChartJS.register(Title, Tooltip, Legend, LineElement, PointElement, BarElement, CategoryScale, LinearScale, Filler, ArcElement);


// Patients Chart
const { chartDataLine: chartDataLinePatients, chartOptionsLine: chartOptionsLinePatients } = useLineChart(
  'Patients',
  chartStore.patientChartData,
  computed(() => chartStore.patientSelectedRange)
);

// Appointments Chart
const { chartDataBar: chartDataBarAppointments, chartOptionsBar: chartOptionsBarAppointments } = useBarChart(
  'Appointments',
  chartStore.appointmentChartData,
  computed(() => chartStore.appointmentSelectedRange)
);

// Revenue Chart
const { chartDataLine: chartDataLineRevenue, chartOptionsLine: chartOptionsLineRevenue } = useLineChart(
  'Revenue',
  chartStore.revenueChartData,
  computed(() => chartStore.revenueSelectedRange)
);

// Departments Chart
const { chartDataPie: chartDataPieDepartments, chartOptionsPie: chartOptionsPieDepartments } = usePieChart(
  chartStore.departmentChartData,
);
</script>

<template>
  <main class="flex flex-col items-start w-full border border-neutral-100 dark:border-0 bg-gray-50 dark:bg-[#000000] rounded-lg">

    <section class="flex flex-wrap gap-5 w-full justify-center lg:px-5 py-5">
      <DashboardCard class="flex-1 min-w-[200px] max-w-sm" title="Patients" :amount="patientStore.totalPatients" :trend="3.15" />
      <DashboardCard class="flex-1 min-w-[200px] max-w-sm" title="Appointments" :amount="15" :trend="-1.25" />
      <DashboardCard class="flex-1 min-w-[200px] max-w-sm" title="Staff" :amount="staffStore.totalStaff" :trend="2.24" />
      <DashboardCard class="flex-1 min-w-[200px] max-w-sm" title="Doctors" :amount="doctorStore.totalDoctors" :trend="2.5" />
    </section>

    <section class="grid grid-cols-1 md:grid-cols-2 gap-5 w-full lg:px-5 pb-5">
      <div class="flex flex-col h-[400px] w-full rounded-lg bg-white dark:bg-[#0a0a0a] dark:border dark:border-neutral-900 shadow-md p-5">
        <div class="flex mb-5 items-center justify-between">
          <p class="text-[16px] text-zinc-800 dark:text-zinc-200">Patients Overview</p>
          <ChartRangeSelection chart-key="patient" default-value="Last Year" />
        </div>
        <div class="flex-1">
          <Line :data="chartDataLinePatients" :options="chartOptionsLinePatients" />
        </div>
      </div>
      <div class="flex flex-col h-[400px] w-full rounded-lg bg-white dark:bg-[#0a0a0a] shadow-md dark:border dark:border-neutral-900 p-5">
        <div class="flex mb-5 items-center justify-between">
          <p class="text-[16px] text-zinc-800 dark:text-zinc-200">Appointments</p>
          <ChartRangeSelection chart-key="appointment" default-value="Last Week"></ChartRangeSelection>
        </div>
        <div class="flex-1">
          <Bar :data="chartDataBarAppointments" :options="chartOptionsBarAppointments" />
        </div>
      </div>
    </section>

    <section class="grid grid-cols-1 md:grid-cols-[2fr_3fr] gap-5 w-full lg:px-5 pb-5">
      <div class="flex flex-col h-[400px] w-full rounded-lg bg-white dark:bg-[#0a0a0a] dark:border dark:border-neutral-900 shadow-md p-5">
        <p class="text-[16px] mb-5 text-zinc-800 dark:text-zinc-200">Department Breakdown</p>
        <div class="flex-1">
          <Pie :data="chartDataPieDepartments" :options="chartOptionsPieDepartments" />
        </div>
      </div>
      <div class="flex flex-col h-[400px] w-full rounded-lg bg-white dark:bg-[#0a0a0a] dark:border dark:border-neutral-900  shadow-md  p-5">
        <div class="flex mb-5 items-center justify-between">
          <p class="text-[16px] mb-5 text-zinc-800 dark:text-zinc-200">Revenue in €</p>
          <ChartRangeSelection chart-key="revenue" default-value="Last Year" />
        </div>
        <div class="flex-1">
          <Line :data="chartDataLineRevenue" :options="chartOptionsLineRevenue" />
        </div>
      </div>
    </section>

  </main>
</template>
