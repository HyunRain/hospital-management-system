<script setup lang="ts">
import DashboardCard from '@/components/ui/DashboardCard.vue';
import { Line } from 'vue-chartjs'
import { Chart as ChartJS, Title, Tooltip, Legend, LineElement, PointElement, CategoryScale, LinearScale, Filler } from 'chart.js'
import type { ChartOptions } from 'chart.js'
import { useToggleStore } from '@/stores/toggleStore';
import { computed } from 'vue';

const toggleStore = useToggleStore();

const isDark = computed(() => toggleStore.darkModeState === 'darkMode');

ChartJS.register(Title, Tooltip, Legend, LineElement, PointElement, CategoryScale, LinearScale, Filler);

const chartData = {
  labels: ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'June'],
  datasets: [{
    label: 'Patients',
    data: [30,40,10,20,60,30],
    borderColor: '#da4353',
    backgroundColor: '#da435264',
    tension: 0.4,
    fill: true,
    },
  ],
};

const chartOptions = computed<ChartOptions<'line'>> (() => ({
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
</script>

<template>
  <div class="flex flex-col items-start w-full bg-gray-50 dark:bg-[#1f1f23] rounded-xl">
    <div class="flex flex-wrap gap-4 w-full justify-center p-5">
      <DashboardCard class="flex-1 min-w-[200px] max-w-sm" title="Patients" />
      <DashboardCard class="flex-1 min-w-[200px] max-w-sm" title="Appointments" />
      <DashboardCard class="flex-1 min-w-[200px] max-w-sm" title="Bedroom" />
      <DashboardCard class="flex-1 min-w-[200px] max-w-sm" title="Doctors" />
    </div>

    <div class="grid grid-cols-1 md:grid-cols-2 gap-6 w-full p-5">
      <div class="flex flex-col h-[400px] w-full rounded-xl bg-white dark:bg-zinc-900 p-4">
        <p class="text-[18px] mb-4 text-zinc-800 dark:text-zinc-200">Patients</p>
        <div class="flex-1">
          <Line :data="chartData" :options="chartOptions" />
        </div>
      </div>

      <div class="flex flex-col h-[400px] w-full rounded-xl bg-white dark:bg-zinc-900 p-4">
        <p class="text-[18px] mb-4 text-zinc-800 dark:text-zinc-200">Patients</p>
        <div class="flex-1">
          <Line :data="chartData" :options="chartOptions" />
        </div>
      </div>
    </div>
  </div>
</template>


<style scoped>
</style>
