<script setup lang="ts">
import { useToggleStore } from '@/stores/toggleStore';
import { ref, type PropType } from 'vue';
import type { ComputedRef } from 'vue';

const toggleStore = useToggleStore();

const props = defineProps({
  stepsAmount: {
    type: Number,
    required: true,
  },
  stepNames: {
    type: Array as PropType<string[]>,
    required: true,
  },
  isStepFilledArray: {
    type: Array as PropType<ComputedRef<boolean>[]>,
    required: true,
  }
})

const currentStep = ref<Number>(1);

function stepTextColor(step: number) {
  if (currentStep.value === step) {
    return toggleStore.darkModeState === 'darkMode' ? 'text-[#e7523b]' : 'text-red-600';
  } else {
    return toggleStore.darkModeState === 'darkMode' ? 'text-[#eeeeee]' : 'text-[#4c4c4c]';
  }
}

</script>

<template>
  <section v-if="stepsAmount > 1" class="formStepsFrame">
    <section v-for="(value, index) in props.stepsAmount" :key="index">
      <div class="flex gap-2 items-center cursor-pointer" @click="currentStep = value">
        <div class="border border-gray-300 size-6.5 dark:border-zinc-800 rounded-full p-2 flex items-center justify-center">
          <img v-if="isStepFilledArray[value]" :src="`/assets/icons/${toggleStore.darkModeState}/checkmark.svg`" alt="PatientIcon" />
          <p v-else :class="[stepTextColor(value)]">1</p>
        </div>
        <p  class="min-w-24" :class="[stepTextColor(value)]"> {{ stepNames[value - 1] }}</p>
      </div>
    </section>
  </section>
</template>
