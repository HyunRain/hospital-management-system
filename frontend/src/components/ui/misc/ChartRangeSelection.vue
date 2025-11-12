<script setup lang="ts">
import { useToggleStore } from '@/stores/toggleStore';
import { ref, computed, watch } from 'vue';
import type { Selection } from '@/util/types/types';
import { useChartStore } from '@/stores/chartStore';

const toggleStore = useToggleStore();
const chartStore = useChartStore();

const props = defineProps({
  chartKey: {
    type: String,
    required: true,
  },
  defaultValue: {
    type: String,
    required: true,
  }
})

const isMobile = computed(() => window.innerWidth <= 640);

const selectionData: Selection[] = [
  { key: 'week', value: 'Last Week' },
  { key: 'month', value: 'Last Month' },
  { key: 'year', value: 'Last Year' }
];

const selection = ref<Selection>({ key: 'year', value: props.defaultValue});

const showDropdown = ref(false);
function toggleDropdown() {
  showDropdown.value = !showDropdown.value;
}

watch(selection, (newValue) => {
  switch (props.chartKey) {
    case 'patient':
      chartStore.patientSelectedRange = newValue.key;
      break;
    case 'revenue':
      chartStore.revenueSelectedRange = newValue.key;
      break;
    case 'appointment':
      chartStore.appointmentSelectedRange = newValue.key;
      break;
    case 'billing':
      chartStore.billingSelectedRange = newValue.key;
      break;
  }
});
</script>

<template>
  <div class="relative min-w-[132px]">
    <button @click="toggleDropdown" type="button" class="chartRangeButton">
      <p> {{ selection.value }} </p>
      <img class="size-4.5" :src="`/assets/icons/${toggleStore.darkModeState}/downarrow.svg`" alt="Chevron Down Icon">
    </button>


    <div v-click-outside="toggleDropdown" v-if="showDropdown" class="chartRangeDropdown">
      <div v-for="item in selectionData" :key="item.key" @click.stop="() => { selection = item; toggleDropdown(); }" class="chartRangeDropdownItem text-nowrap">
        <p> {{ item.value }} </p>
        <img v-if="item.value === selection.value" class="size-3.5" :src="`/assets/icons/${toggleStore.darkModeState}/checkmark.svg`"
          alt="Checkmark Icon">
      </div>
    </div>
  </div>
</template>
