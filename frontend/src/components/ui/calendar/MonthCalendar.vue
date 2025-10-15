<script setup lang="ts">
import { days, months } from '@/util/types/constants';
import type { PropType } from 'vue';

const props = defineProps({
  daysInMonth: {
    type: Number,
    required: true,
  },
  daysOfPreviousMonth: {
    type: Array as PropType<number[]>,
    required: true,
  }
});
</script>

<template>
  <main class="grid grid-cols-7">
    <header class="grid grid-cols-7 col-span-7 text-center mb-3">
      <p v-for="value in days">{{ value }}</p>
    </header>

    <section class="border border-neutral-300 dark:border-[#33333380] grid grid-cols-7 col-span-7 rounded-lg overflow-hidden">
      <section class="text-start h-37 p-2 border-b border-r border-neutral-300 dark:border-[#33333380] bg-neutral-200 dark:bg-neutral-900"
        v-for="(dayOfPreviousMonth, index) in props.daysOfPreviousMonth">
        {{ dayOfPreviousMonth }}
        {{ index }}
      </section>

      <section class="flex flex-col text-start border-b border-r border-neutral-300 dark:border-[#33333380] h-37 p-2"
        v-for="(dayInMonth, index) in props.daysInMonth">
        {{ dayInMonth }}
        {{ index }}
        <section class="h-full">
          hi
        </section>
      </section>
    </section>

  </main>
</template>
