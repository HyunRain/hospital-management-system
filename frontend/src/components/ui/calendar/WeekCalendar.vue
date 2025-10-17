<script setup lang="ts">
import { days } from '@/util/types/constants';
import type { PropType } from 'vue';

const props = defineProps({
  todaysDate: {
    type: Date,
    required:true,
  },
  currentWeekDay: {
    type: String,
    required: true,
  },
  currentWeekDays: {
    type: Array as PropType<number[]>,
    required: true,
  },
})
</script>

<template>
  <main class="grid grid-cols-8 mt-1.5 grid-rows-[auto_1fr] h-full">
    <header class="grid grid-cols-8 col-span-8 text-center mb-3">
      <p>time</p>
      <p v-for="(day, index) in days" :class="[day === currentWeekDay ? 'text-red-400' : '']"> {{ day }} {{ currentWeekDays[index] }}</p>
    </header>
  </main>
</template>
