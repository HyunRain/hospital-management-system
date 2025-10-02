<script setup lang="ts">
import { computed } from 'vue';
import TrendingUp from '/assets/icons/trending-up.svg';
import TrendingDown from '/assets/icons/trending-down.svg';
import { useToggleStore } from '@/stores/toggleStore';

const toggleStore = useToggleStore();

const props = defineProps({
  title: {
    type: String,
    required: true
  },
  amount: {
    type: Number,
    required: true
  },
});

const darkModeState = computed(() => toggleStore.darkModeState);

const iconNameMap: Record<string, string> = {
  Patients: 'patients',
  Appointments: 'appointment',
  Bedroom: 'patient',
  Doctors: 'doctor',
};

const imgSrc = computed(() => {
  const iconName: string = iconNameMap[props.title];
  return `/assets/icons/${darkModeState.value}/${iconName}.svg`;
});
</script>

<template>
  <div class="flex flex-col p-5 shadow-lg rounded-xl bg-white dark:bg-[#0d1016] cursor-pointer">
    <div class="flex items-center justify-between">
      <div class="flex items-center gap-3">
        <img class="h-6 w-6" :src="imgSrc" alt="Card Icon">
        <p class="font-medium text-[16px]">{{ props.title }}</p>
      </div>
      <img class="h-6 w-6" :src="`/assets/icons/${toggleStore.darkModeState}/options.svg`" alt="3 Dots Icon">
    </div>
    <div class="flex items-center mt-6 justify-between">
      <p class="text-2xl font-bold "> {{ props.amount }}</p>

      <div class="flex items-center gap-1 bg-[#c4ffc8] rounded-xl p-1">
        <img class="h-5 w-5" :src="TrendingUp" alt="Trending Icon">
        <div class="text-[#4c4c4c]">+2.32%</div>
      </div>
    </div>
    <p class="mt-3 text-[#898989]">32 more than last week</p>
  </div>
</template>

<style scoped></style>
