<script setup lang="ts">
import DashboardCard from '@/components/ui/homeview/DashboardCard.vue';
import { Line, Pie, Bar } from 'vue-chartjs'
import { Chart as ChartJS, Title, Tooltip, Legend, LineElement, BarElement, PointElement, ArcElement, CategoryScale, LinearScale, Filler } from 'chart.js'
import type { ChartOptions } from 'chart.js'
import { useToggleStore } from '@/stores/toggleStore';
import { computed, ref} from 'vue';

const toggleStore = useToggleStore();

const isDark = computed(() => toggleStore.darkModeState === 'darkMode');

ChartJS.register(Title, Tooltip, Legend, LineElement, PointElement, BarElement, CategoryScale, LinearScale, Filler, ArcElement);


// ----------------------- Patients Overview Chart -----------------------

const selectedRangeLinePatients = ref<'week' | 'month' | 'year'>('week');

// Plan is 3 Sections: Last Week, Last Month, Last Year
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
    borderColor: '#da4353',
    backgroundColor: isDark.value ? '#da43521a' : '#da435264',
    tension: 0.4,
    fill: true,
    },
  ],
}));


const chartOptionsLinePatients = computed<ChartOptions<'line'>> (() => ({
  responsive: true,
  maintainAspectRatio: false,
  plugins: {
    legend: {
      labels: {
        color: isDark.value ? '#dfdfd6': '#4c4c4c', // Adjust for dark mode if needed
      },
      position: 'bottom',
    },
  },
  scales: {
    x: {
      ticks: {
        color: isDark.value ? '#dfdfd6': '#4c4c4c', // Adjust for dark mode
      },
      grid: {
        color: '#333', // Grid color
        display: false,
      },
    },
    y: {
      min: 0,
      ticks: {
        color: isDark.value ? '#dfdfd6': '#4c4c4c',
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

// Plan is 3 Sections: Last Week, Last Month, Last Year
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
    backgroundColor: isDark.value ? '#da43521a' : '#da435264',
    },
  ],
}));


const chartOptionsBarAppointments = computed<ChartOptions<'bar'>> (() => ({
  responsive: true,
  maintainAspectRatio: false,
  plugins: {
    legend: {
      labels: {
        color: isDark.value ? '#dfdfd6': '#4c4c4c', // Adjust for dark mode if needed
      },
      position: 'bottom',
    },
  },
  scales: {
    x: {
      ticks: {
        color: isDark.value ? '#dfdfd6': '#4c4c4c', // Adjust for dark mode
      },
      grid: {
        color: '#333', // Grid color
        display: false,
      },
    },
    y: {
      min: 0,
      ticks: {
        color: isDark.value ? '#dfdfd6': '#4c4c4c',
        stepSize: 10,
      },
      grid: {
        color: isDark.value ? '#333' : '#dfdfd6',
      },
    },
  },
}));

// ----------------------- Revenue Chart -----------------------

const chartDataLineRevenue = computed(() => ({
  labels: ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'June', 'Jul', 'Sep', 'Oct', 'Nov', 'Dec'],
  datasets: [{
    label: 'Revenue in €',
    data: [30, 40, 10, 20, 60, 30],
    borderColor: '#da4353',
    backgroundColor: isDark.value ? '#da43521a' : '#da435264',
    tension: 0.4,
    fill: true,
  }],
}));

const chartOptionsLineRevenue = computed<ChartOptions<'line'>> (() => ({
  responsive: true,
  maintainAspectRatio: false,
  plugins: {
    legend: {
      labels: {
        color: isDark.value ? '#dfdfd6': '#4c4c4c', // Adjust for dark mode if needed
      },
      position: 'bottom',
    },
  },
  scales: {
    x: {
      ticks: {
        color: isDark.value ? '#dfdfd6': '#4c4c4c', // Adjust for dark mode
      },
      grid: {
        color: '#333', // Grid color
        display: false,
      },
    },
    y: {
      min: 0,
      ticks: {
        color: isDark.value ? '#dfdfd6': '#4c4c4c',
        stepSize: 10,
      },
      grid: {
        color: isDark.value ? '#333' : '#dfdfd6',
      },
    },
  },
}));

// ----------------------- Department Pie Chart -----------------------
const chartDataPie = {
  labels: ['VueJs', 'EmberJs', 'ReactJs', 'AngularJs'],
  datasets: [
    {
      backgroundColor: ['#da4353', 'Yellow', 'Orange', '#34ad53'],
      data: [40, 20, 80, 10]
    }
  ]
};

const chartOptionsPie = computed<ChartOptions<'pie'>> (() => ({
  responsive: true,
  maintainAspectRatio: false,
  plugins: {
    legend: {
      labels: {
        color: isDark.value ? '#dfdfd6': '#4c4c4c', // Adjust for dark mode if needed
      },
      position: 'bottom',
    },
  },
}));
</script>

<template>
  <div class="flex flex-col items-start w-full bg-gray-50 dark:bg-[#030712] rounded-xl">
    <div class="flex flex-wrap gap-4 w-full justify-center p-5">
      <DashboardCard class="flex-1 min-w-[200px] max-w-sm" title="Patients" />
      <DashboardCard class="flex-1 min-w-[200px] max-w-sm" title="Appointments" />
      <DashboardCard class="flex-1 min-w-[200px] max-w-sm" title="Bedroom" />
      <DashboardCard class="flex-1 min-w-[200px] max-w-sm" title="Doctors" />
    </div>

    <div class="grid grid-cols-1 md:grid-cols-2 gap-6 w-full p-5">
      <div class="flex flex-col h-[400px] w-full rounded-xl bg-white dark:bg-[#0d1016] p-4">
        <div class="flex mb-4 items-center justify-between">
          <p class="text-[16px] text-zinc-800 dark:text-zinc-200">Patients Overview</p>
          <div class="flex gap-4">
            <button class="hover:bg-red-100 dark:hover:bg-neutral-800 p-2 rounded-xl cursor-pointer" @click="selectedRangeLinePatients = 'week'">Last Week</button>
            <button class="hover:bg-red-100 dark:hover:bg-neutral-800 p-2 rounded-xl cursor-pointer" @click="selectedRangeLinePatients = 'month'">Last Month</button>
            <button class="hover:bg-red-100 dark:hover:bg-neutral-800 p-2 rounded-xl cursor-pointer" @click="selectedRangeLinePatients = 'year'">Last Year</button>
          </div>
        </div>
        <div class="flex-1">
          <Line :data="chartDataLinePatients" :options="chartOptionsLinePatients" />
        </div>
      </div>

      <div class="flex flex-col h-[400px] w-full rounded-xl bg-white dark:bg-[#0d1016] p-4">
        <div class="flex mb-4 items-center justify-between">
          <p class="text-[16px] text-zinc-800 dark:text-zinc-200">Appointments</p>
          <div class="flex gap-4">
            <button class="hover:bg-red-100 dark:hover:bg-neutral-800 p-2 rounded-xl cursor-pointer" @click="selectedRangeBarAppoitments = 'week'">Last Week</button>
            <button class="hover:bg-red-100 dark:hover:bg-neutral-800 p-2 rounded-xl cursor-pointer" @click="selectedRangeBarAppoitments = 'month'">Last Month</button>
            <button class="hover:bg-red-100 dark:hover:bg-neutral-800 p-2 rounded-xl cursor-pointer" @click="selectedRangeBarAppoitments = 'year'">Last Year</button>
          </div>
        </div>
        <div class="flex-1">
          <Bar :data="chartDataBarAppointments" :options="chartOptionsBarAppointments" />
        </div>
      </div>
    </div>

    <div class="grid grid-cols-1 md:grid-cols-[33%_65.3%] gap-6 w-full p-5">
      <div class="flex flex-col h-[400px] w-full rounded-xl bg-white dark:bg-[#0d1016] p-4">
        <p class="text-[16px] mb-4 text-zinc-800 dark:text-zinc-200">Department Breakdown</p>
        <div class="flex-1">
          <Pie :options="chartOptionsPie" :data="chartDataPie"/>
        </div>
      </div>
       <div class="flex flex-col h-[400px] w-full rounded-xl bg-white dark:bg-[#0d1016] p-4">
        <p class="text-[16px] mb-4 text-zinc-800 dark:text-zinc-200">Revenue in €</p>
        <div class="flex-1">
          <Line :data="chartDataLineRevenue" :options="chartOptionsLineRevenue" />
        </div>
      </div>
    </div>
  </div>
</template>


<style scoped>
</style>
