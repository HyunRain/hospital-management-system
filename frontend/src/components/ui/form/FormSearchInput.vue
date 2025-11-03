<script setup lang="ts">
import { ref } from 'vue';

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
});

const emit = defineEmits(['update:modelValue', 'toggle']);

const triggerInput = ref<HTMLInputElement | null>(null);
</script>

<template>
  <section>
    <div class="flex justify-between">
      <label class="ml-1" :for="labelName">{{ labelName }}</label>
      <p class="text-[#898989] opacity-80 mr-1 text-[13px]">{{ labelText }}</p>
    </div>
    <input class="input mt-2" :id="labelName" :readonly="true" type="text" autocomplete="off" placeholder="Search ..." :value="modelValue"
      @input="$emit('update:modelValue', ($event.target as HTMLInputElement).value)" ref="triggerInput" @click="emit('toggle'), triggerInput?.blur()">
  </section>
</template>
