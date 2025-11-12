import { useToggleStore } from '@/stores/toggleStore';
import { computed } from 'vue';
import type { Ref } from 'vue';
import type { ChartOptions } from 'chart.js';
import type { ChartData, selectedRange } from '@/util/types/types';

export function usePieChart(data: ChartData) {
  const toggleStore = useToggleStore();

  const isDark = computed(() => toggleStore.darkModeState === 'darkMode');

  const chartDataPie = computed(() => ({
    labels: data.labels,
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
        data: data.data,
      },
    ],
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

  return {
    chartDataPie,
    chartOptionsPie,
  };
}
