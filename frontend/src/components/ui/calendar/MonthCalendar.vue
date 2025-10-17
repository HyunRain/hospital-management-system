<script setup lang="ts">
import { days } from '@/util/types/constants';
import { type PropType } from 'vue';

const props = defineProps({
  totalDaysForCurrentMonth: {
    type: Array as PropType<{ value: number, type: string; }[]>,
    required: true,
  },
  todaysDate: {
    type: Date,
    required: true,
  },
  currentMonth: {
    type: Number,
    required: true,
  }
});

</script>

<template>
  <main class="grid grid-cols-7 mt-1.5 grid-rows-[auto_1fr] h-full">
    <header class="grid grid-cols-7 col-span-7 text-center mb-3">
      <p v-for="value in days">{{ value }}</p>
    </header>

    <section class="border border-neutral-300 dark:border-[#33333380] grid grid-cols-7 col-span-7 rounded-lg overflow-hidden h-full">
      <section v-for="(day, index) in props.totalDaysForCurrentMonth"
        class="flex flex-col text-start border-b border-neutral-300 dark:border-[#33333380] p-1"
        :class="[day.type === 'prev' || day.type === 'next' ? 'bg-neutral-200 dark:bg-[#141414]' : '', (index + 1) % 7 === 0 ? '' : 'border-r', index >= props.totalDaysForCurrentMonth.length - 7 ? 'border-b-0' : '']">
        <p class="size-6 rounded-full inline-flex items-center justify-center leading-[24px]"
          :class="[day.value === props.todaysDate.getDate() && props.todaysDate.getMonth() === props.currentMonth ? 'bg-red-200 dark:bg-red-400 dark:text-black' : '']">
          {{ day.value }} </p>
        <section class="flex-1 flex items-center justify-center">
          <p class=""></p>
        </section>
      </section>
    </section>

  </main>
</template>
