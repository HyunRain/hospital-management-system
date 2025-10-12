import { useToggleStore } from '@/stores/toggleStore';
import { computed } from 'vue';
import type { Ref } from 'vue';
import type { ChartOptions } from 'chart.js';
import type { TimeRangeData, selectedRange } from '@/util/types/types';

export function useBarChart(
  label: string,
  data: TimeRangeData,
  selectedRange: Ref<selectedRange>,
) {

  const toggleStore = useToggleStore();

  const isDark = computed(() => toggleStore.darkModeState === 'darkMode');

  const chartDataBar = computed(() => ({
    labels: data[selectedRange.value].labels,
    datasets: [
      {
        label: label,
        data: data[selectedRange.value].data,
        backgroundColor: isDark.value ? '#e7523b20' : '#e7523b80',
      },
    ],
  }));

  const chartOptionsBar = computed<ChartOptions<'bar'>>(() => ({
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
          color: isDark.value ? '#33333380' : '#dfdfd6',
        },
      },
    },
  }));

  return {
    chartDataBar,
    chartOptionsBar,
  };
}
