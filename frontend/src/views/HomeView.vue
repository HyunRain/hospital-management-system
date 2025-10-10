<script setup lang="ts">
import DashboardCard from '@/components/ui/homeview/DashboardCard.vue';
import { Line, Pie, Bar } from 'vue-chartjs';
import { Chart as ChartJS, Title, Tooltip, Legend, LineElement, BarElement, PointElement, ArcElement, CategoryScale, LinearScale, Filler } from 'chart.js';
import type { ChartOptions } from 'chart.js';
import { useToggleStore } from '@/stores/toggleStore';
import { computed, ref, onMounted, onBeforeMount } from 'vue';
import { usePatientStore } from '@/stores/patientStore';
import { useDoctorStore } from '@/stores/doctorStore';
import { useAuthStore } from '@/stores/authStore';
import { useDepartmentStore } from '@/stores/departmentStore';
import { useStaffStore } from '@/stores/staffStore';

const toggleStore = useToggleStore();
const authStore = useAuthStore();
const patientStore = usePatientStore();
const doctorStore = useDoctorStore();
const departmentStore = useDepartmentStore();
const staffStore = useStaffStore();

onBeforeMount(async () => {
  if (patientStore.patients.length === 0) {
    await patientStore.getPageOfPatients(0, patientStore.size);
  }
  if (doctorStore.doctors.length === 0 && authStore.role === 'ADMIN') {
    await doctorStore.getPageOfDoctors(0, doctorStore.size);
  }
  if (departmentStore.departments.length === 0) {
    await departmentStore.fetchDepartments();
  }
  if (staffStore.staff.length === 0 && authStore.role === 'ADMIN') {
    await staffStore.getPageOfAllStaff(0, staffStore.size);
  }
});

const isDark = computed(() => toggleStore.darkModeState === 'darkMode');

ChartJS.register(Title, Tooltip, Legend, LineElement, PointElement, BarElement, CategoryScale, LinearScale, Filler, ArcElement);


// ----------------------- Patients Overview Chart -----------------------

const selectedRangeLinePatients = ref<'week' | 'month' | 'year'>('week');

const chartDataSetsLinePatients = {
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

const chartDataLinePatients = computed(() => ({
  labels: chartDataSetsLinePatients[selectedRangeLinePatients.value].labels,
  datasets: [{
    label: 'Patients',
    data: chartDataSetsLinePatients[selectedRangeLinePatients.value].data,
    borderColor: isDark.value ? '#e7523b95' : '#e7523b',
    backgroundColor: isDark.value ? '#e7523b20' : '#e7523b80',
    tension: 0.4,
    fill: true,
  },
  ],
}));

const chartOptionsLinePatients = computed<ChartOptions<'line'>>(() => ({
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

// ----------------------- Appointment Chart -----------------------

const selectedRangeBarAppoitments = ref<'week' | 'month' | 'year'>('week');

const chartDataSetsBarAppointments = {
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

const chartDataBarAppointments = computed(() => ({
  labels: chartDataSetsBarAppointments[selectedRangeBarAppoitments.value].labels,
  datasets: [{
    label: 'Appointments',
    data: chartDataSetsBarAppointments[selectedRangeBarAppoitments.value].data,
    backgroundColor: isDark.value ? '#e7523b20' : '#e7523b80',
  },
  ],
}));


const chartOptionsBarAppointments = computed<ChartOptions<'bar'>>(() => ({
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

// ----------------------- Revenue Chart -----------------------

const selectedRangeLineRevenue = ref<'week' | 'month' | 'year'>('year');

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
    label: 'Revenue in €',
    data: chartDataSetsLineRevenue[selectedRangeLineRevenue.value].data,
    borderColor: isDark.value ? '#e7523b95' : '#e7523b',
    backgroundColor: isDark.value ? '#e7523b20' : '#e7523b80',
    tension: 0.4,
    fill: true,
  }],
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

// ----------------------- Department Pie Chart -----------------------

const departmentCount = computed(() => departmentStore.departments.length);
const chartDataPie = computed(() => ({
  labels: departmentStore.departments.map(dept => dept.name),
  datasets: [
    {
      backgroundColor: [
        '#FF6384',
        '#36A2EB',
        '#FFCE56',
        '#4BC0C0',
        '#9966FF',
        '#FF9F40',
        '#C9CBCF',
        '#8B0000',
        '#008000',
        '#00008B',
      ],
      data: departmentStore.departments.map(dept => dept.staffCount)
    }
  ]
}));

const chartOptionsPie = computed<ChartOptions<'pie'>>(() => ({
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
}));
</script>

<template>
  <div class="flex flex-col items-start w-full bg-gray-50 dark:bg-[#000000] rounded-lg">
    <div class="flex flex-wrap gap-5 w-full justify-center p-5">
      <DashboardCard class="flex-1 min-w-[200px] max-w-sm" title="Patients" :amount="patientStore.totalPatients" :trend="3.15" />
      <DashboardCard class="flex-1 min-w-[200px] max-w-sm" title="Appointments" :amount="15" :trend="-1.25" />
      <DashboardCard class="flex-1 min-w-[200px] max-w-sm" title="Staff" :amount="staffStore.totalStaff" :trend="2.24" />
      <DashboardCard class="flex-1 min-w-[200px] max-w-sm" title="Doctors" :amount="doctorStore.totalDoctors" :trend="2.5" />
    </div>

    <div class="grid grid-cols-1 md:grid-cols-2 gap-5 w-full px-5 pb-5">
      <div class="flex flex-col h-[400px] w-full rounded-lg bg-white dark:bg-[#0a0a0a] dark:border dark:border-neutral-900 shadow-md p-5">
        <div class="flex mb-5 items-center justify-between">
          <p class="text-[16px] text-zinc-800 dark:text-zinc-200">Patients Overview</p>
          <div class="flex gap-5">
            <button class="hover:bg-red-100 dark:hover:bg-[#1d1d1dcf] p-2 rounded-lg cursor-pointer" @click="selectedRangeLinePatients = 'week'">Last
              Week</button>
            <button class="hover:bg-red-100 dark:hover:bg-[#1d1d1dcf] p-2 rounded-lg cursor-pointer" @click="selectedRangeLinePatients = 'month'">Last
              Month</button>
            <button class="hover:bg-red-100 dark:hover:bg-[#1d1d1dcf] p-2 rounded-lg cursor-pointer" @click="selectedRangeLinePatients = 'year'">Last
              Year</button>
          </div>
        </div>
        <div class="flex-1">
          <Line :data="chartDataLinePatients" :options="chartOptionsLinePatients" />
        </div>
      </div>

      <div class="flex flex-col h-[400px] w-full rounded-lg bg-white dark:bg-[#0a0a0a] shadow-md dark:border dark:border-neutral-900 p-5">
        <div class="flex mb-5 items-center justify-between">
          <p class="text-[16px] text-zinc-800 dark:text-zinc-200">Appointments</p>
          <div class="flex gap-4">
            <button class="hover:bg-red-100 dark:hover:bg-[#1d1d1dcf] p-2 rounded-lg cursor-pointer"
              @click="selectedRangeBarAppoitments = 'week'">Last Week</button>
            <button class="hover:bg-red-100 dark:hover:bg-[#1d1d1dcf] p-2 rounded-lg cursor-pointer"
              @click="selectedRangeBarAppoitments = 'month'">Last Month</button>
            <button class="hover:bg-red-100 dark:hover:bg-[#1d1d1dcf] p-2 rounded-lg cursor-pointer"
              @click="selectedRangeBarAppoitments = 'year'">Last Year</button>
          </div>
        </div>
        <div class="flex-1">
          <Bar :data="chartDataBarAppointments" :options="chartOptionsBarAppointments" />
        </div>
      </div>
    </div>

    <div class="grid grid-cols-1 md:grid-cols-[2fr_3fr] gap-5 w-full px-5 pb-5">
      <div class="flex flex-col h-[400px] w-full rounded-lg bg-white dark:bg-[#0a0a0a] dark:border dark:border-neutral-900 shadow-md p-5">
        <p class="text-[16px] mb-5 text-zinc-800 dark:text-zinc-200">Department Breakdown</p>
        <div class="flex-1">
          <Pie :options="chartOptionsPie" :data="chartDataPie" />
        </div>
      </div>
      <div class="flex flex-col h-[400px] w-full rounded-lg bg-white dark:bg-[#0a0a0a] dark:border dark:border-neutral-900  shadow-md  p-5">
        <div class="flex mb-5 items-center justify-between">
          <p class="text-[16px] mb-5 text-zinc-800 dark:text-zinc-200">Revenue in €</p>
          <div class="flex gap-5">
            <button class="hover:bg-red-100 dark:hover:bg-[#1d1d1dcf] p-2 rounded-lg cursor-pointer" @click="selectedRangeLineRevenue = 'week'">Last
              Week</button>
            <button class="hover:bg-red-100 dark:hover:bg-[#1d1d1dcf] p-2 rounded-lg cursor-pointer" @click="selectedRangeLineRevenue = 'month'">Last
              Month</button>
            <button class="hover:bg-red-100 dark:hover:bg-[#1d1d1dcf] p-2 rounded-lg cursor-pointer" @click="selectedRangeLineRevenue = 'year'">Last
              Year</button>
          </div>
        </div>
        <div class="flex-1">
          <Line :data="chartDataLineRevenue" :options="chartOptionsLineRevenue" />
        </div>
      </div>
    </div>
  </div>
</template>


<style scoped></style>
