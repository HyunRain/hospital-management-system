<script setup lang="ts">
import { useCalendar } from '@/composables/useCalendar';
import { useToggleStore } from '@/stores/toggleStore';
import { months, shortDays } from '@/util/types/constants';

const toggleStore = useToggleStore();

const {
  totalDaysForCurrentMonth,
  todaysDate,
  currentMonth,
  currentYear,
  changeDate,

} = useCalendar();

function handleClick(dayValue: number) {
  emit('dateSelected', {
    localDate: currentYear.value + '-' + String(currentMonth.value + 1).padStart(2, '0') + '-' + String(dayValue).padStart(2, '0'),
  });
  emit('toggle');
}

const emit = defineEmits(['dateSelected', 'toggle']);
</script>

<template>
  <div class="flex flex-col">
    <div class="flex justify-between items-center">
      <div @click="changeDate('prev', false, 'Month')"
        class="p-1.5 hover:bg-rose-100 dark:hover:bg-[#1d1d1dcf] cursor-pointer rounded-lg select-none">
        <img class="size-5 cursor-pointer" :src="`/assets/icons/${toggleStore.darkModeState}/leftarrow.svg`" alt="Left Arrow Icon" />
      </div>
      <p> {{ months[currentMonth] }} {{ currentYear }}</p>
      <div @click="changeDate('next', false, 'Month')"
        class="p-1.5 hover:bg-rose-100 dark:hover:bg-[#1d1d1dcf] cursor-pointer rounded-lg select-none">
        <img class="size-5" :src="`/assets/icons/${toggleStore.darkModeState}/rightarrow.svg`" alt="Right Arrow Icon" />
      </div>
    </div>

    <div class="grid grid-cols-7 grid-rows-[auto_1fr] h-full">
      <div class="grid grid-cols-7 col-span-7 text-center my-2.5">
        <p v-for="value in shortDays">{{ value }}</p>
      </div>

      <div class="grid grid-cols-7 col-span-7 overflow-hidden h-full">
        <div v-for="(day, index) in totalDaysForCurrentMonth" @click.stop="handleClick(day.value)"
          class="flex flex-col text-start py-1.5  items-center justify-center rounded-lg"
          :class="[day.type === 'prev' || day.type === 'next' ? 'text-neutral-200 dark:text-neutral-600 line-through cursor-default' : 'cursor-pointer hover:bg-red-300 dark:hover:bg-neutral-800']">
          <p class="size-6 rounded-full inline-flex items-center justify-center leading-[24px]"
            :class="[day.value === todaysDate.getDate() && todaysDate.getMonth() === currentMonth && todaysDate.getFullYear() === currentYear && day.type === 'curr' ? 'bg-red-200 dark:bg-red-400 dark:text-black' : '']">
            {{ day.value }}
          </p>
        </div>
      </div>
    </div>
  </div>
</template>
