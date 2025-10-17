import { useToggleStore } from '@/stores/toggleStore';
import { computed } from 'vue';
import type { Ref } from 'vue';
import type { ChartOptions } from 'chart.js';
import type { TimeRangeData, selectedRange } from '@/util/types/types';

export function useLineChart(
  label: string,
  data: TimeRangeData,
  selectedRange: Ref<selectedRange>,
) {
  const toggleStore = useToggleStore();

  const isDark = computed(() => toggleStore.darkModeState === 'darkMode');

  const chartDataLine = computed(() => ({
    labels: data[selectedRange.value].labels,
    datasets: [
      {
        label: label,
        data: data[selectedRange.value].data,
        borderColor: isDark.value ? '#e7523b95' : '#f87171',
        backgroundColor: isDark.value ? '#e7523b20' : '#fca5a5',
        tension: 0.4,
        fill: true,
      },
    ],
  }));

  const chartOptionsLine = computed<ChartOptions<'line'>>(() => ({
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
          stepSize: 50,
        },
        grid: {
          color: isDark.value ? '#33333380' : '#dfdfd6',
        },
      },
    },
  }));

  return {
    chartDataLine,
    chartOptionsLine,
  };
}
