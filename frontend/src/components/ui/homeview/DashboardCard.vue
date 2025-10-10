<script setup lang="ts">
import { computed, reactive } from 'vue';
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
  trend: {
    type: Number,
    required: true
  }
});

const darkModeState = computed(() => toggleStore.darkModeState);

const iconNameMap: Record<string, string> = {
  Patients: 'patients',
  Appointments: 'appointment',
  Staff: 'employees',
  Doctors: 'doctor',
};

const imgSrc = computed(() => {
  const iconName: string = iconNameMap[props.title];
  return `/assets/icons/${darkModeState.value}/${iconName}.svg`;
});

const textColor = computed(() => {
  if(darkModeState.value === 'darkMode') {
    return props.trend >= 0 ? 'text-green-400' : 'text-[#e7523b]';
  }
  return props.trend >= 0 ? 'text-green-600' : 'text-[#e7523b]';
});
</script>

<template>
  <div class="flex flex-col p-5 shadow-md rounded-lg bg-white dark:bg-[#0a0a0a] dark:border dark:border-neutral-900  cursor-pointer">
    <div class="flex items-center justify-between">
      <div class="flex items-center gap-3">
        <img class="h-6 w-6" :src="imgSrc" alt="Card Icon">
        <p class="font-medium text-[16px]">{{ props.title }}</p>
      </div>
      <img class="h-6 w-6" :src="`/assets/icons/${toggleStore.darkModeState}/options.svg`" alt="3 Dots Icon">
    </div>
    <div class="flex items-center mt-6 justify-between">
      <p class="text-2xl font-bold "> {{ props.amount }}</p>

      <div class="flex items-center gap-1 rounded-lg p-1">

        <svg v-if="props.trend < 0" class="h-5 w-5 text-red-500" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg"
          stroke="currentColor">
          <path
            d="M20.0005 17L14.1543 11.0625C14.0493 10.9559 13.9962 10.9024 13.9492 10.8604C13.1899 10.1807 12.0416 10.1807 11.2822 10.8604C11.2352 10.9024 11.1817 10.9558 11.0767 11.0625C10.9716 11.1692 10.9191 11.2226 10.8721 11.2646C10.1127 11.9443 8.96397 11.9443 8.20461 11.2646C8.15759 11.2226 8.10506 11.1692 8 11.0625L4 7M20.0005 17L20 11M20.0005 17H14"
            stroke-width="2" stroke-linecap="round" stroke-linejoin="round" />
        </svg>
        <svg v-else class="h-5 w-5 text-green-500" stroke="currentColor" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg"
          width="64px" height="64px">
          <g id="SVGRepo_bgCarrier" stroke-width="0" />
          <g id="SVGRepo_tracerCarrier" stroke-linecap="round" stroke-linejoin="round" />
          <g id="SVGRepo_iconCarrier">
            <path
              d="M21 7L14.4142 13.5858C13.6332 14.3668 12.3668 14.3668 11.5858 13.5858L10.4142 12.4142C9.63316 11.6332 8.36683 11.6332 7.58579 12.4142L3 17M21 7H15M21 7V13"
              stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round" />
          </g>
        </svg>

        <div :class="textColor">{{ props.trend >= 0 ? '+' : '' }}{{ props.trend }}%</div>
      </div>
    </div>
    <p class="mt-3 text-[#898989]">32 more than last week</p>
  </div>
</template>

<style scoped></style>
