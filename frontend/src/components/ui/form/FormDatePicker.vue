<script setup lang="ts">
import { useToggleStore } from '@/stores/toggleStore';
import MiniMonthCalendar from '../calendar/MiniMonthCalendar.vue';
import { useIsMobile } from '@/composables/useIsMobile';

const toggleStore = useToggleStore();

const props = defineProps({
  labelName: {
    type: String,
    required: true,
  },
  labelText: {
    type: String,
    required: false,
  },
  modelValue: {
    type: String,
    required: true,
  },
  isReadonly: {
    type: Boolean,
    required: false,
  },
  isOpen: {
    type: Boolean,
    required: true,
  },
});

const isMobile = useIsMobile()
const emit = defineEmits(['update:modelValue', 'toggle']);
</script>

<template>
  <div class="relative">
    <div class="flex justify-between">
      <label class="ml-1" :for="labelName">{{ labelName }}</label>
      <p class="text-[#898989] opacity-80 mr-1 text-[13px]">{{ labelText }}</p>
    </div>
    <button type="button" class="formSelectButton w-full" @click="emit('toggle')">
      <div class="flex gap-4 items-center">
        <img class="size-4" :src="`/assets/icons/${toggleStore.darkModeState}/calendar.svg`" alt="Chevron Down Icon">
        <p v-if="modelValue.length !== 0"> {{ modelValue }}</p>
        <p v-else> Pick a date </p>
      </div>
      <img class="size-4.5" :src="`/assets/icons/${toggleStore.darkModeState}/downarrow.svg`" alt="Chevron Down Icon">
    </button>

    <div v-if="isOpen" class="formSelectDropdownDatePicker" :class="[isMobile ? 'left-0 min-w-[250px]' : 'w-full']" v-click-outside="() => emit('toggle')">
      <MiniMonthCalendar @date-selected="emit('update:modelValue', $event.localDate)" @toggle="emit('toggle')"></MiniMonthCalendar>
    </div>
  </div>
</template>
