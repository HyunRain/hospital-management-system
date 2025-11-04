<script setup lang="ts">
import { useIsMobile } from '@/composables/useIsMobile';
import { useAppointmentStore } from '@/stores/appointmentStore';
import { useToggleStore } from '@/stores/toggleStore';
import type { PropType } from 'vue';
import { ref } from 'vue';

const toggleStore = useToggleStore();
const appointmentStore = useAppointmentStore();

const props = defineProps({
  data: {
    type: Array as PropType<Object[]>,
    required: true,
  },
  modelValue: {
    type: String,
    required: true,
  },
  isOpen: {
    type: Boolean,
    required: true,
  },
  isFormInput: {
    type: Boolean,
    required: false,
  },
  labelName: {
    type: String,
    required: false,
  },
  labelText: {
    type: String,
    required: false,
  },
  minWidth: {
    type: String,
    required: true,
  }
});

const emit = defineEmits(['update:modelValue', 'toggle']);
const isMobile = useIsMobile();
</script>

<template>
  <div class="relative" :style="isMobile ? { minWidth: 'fit-content' } : { minWidth: minWidth }">
    <div v-if="isFormInput" class="flex justify-between">
      <label class="ml-1" :for="labelName">{{ labelName }}</label>
      <p class="text-[#898989] opacity-80 mr-1 text-[13px]">{{ labelText }}</p>
    </div>
    <button @click="emit('toggle')" type="button" :class="[isFormInput ? 'input formSelectButton' : 'chartRangeButton']">
      <span class="hidden lg:inline"> {{ modelValue }} </span>
      <slot name="Mobile"></slot>
      <img class="size-4.5" :src="`/assets/icons/${toggleStore.darkModeState}/downarrow.svg`" alt="Chevron Down Icon">
    </button>

    <div v-if="isOpen" v-click-outside="() => emit('toggle')"
      :class="[isFormInput ? 'formSelectDropdown' : 'chartRangeDropdown', isMobile ? 'right-0' : 'w-full']">
      <div v-for="(item, key) in props.data" :key="key" @click.stop="emit('update:modelValue', item); emit('toggle')" class="chartRangeDropdownItem">
        <p> {{ item }} </p>
        <img v-if="item === modelValue" class="size-3.5" :src="`/assets/icons/${toggleStore.darkModeState}/checkmark.svg`" alt="Checkmark Icon">
      </div>
    </div>
  </div>
</template>
