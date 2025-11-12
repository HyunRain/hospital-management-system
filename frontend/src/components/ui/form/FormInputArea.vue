<script setup lang="ts">

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
  }
});

const emit = defineEmits(['update:modelValue']);
</script>

<template>
  <section>
    <div class="flex justify-between">
      <label class="ml-1" :for="labelName">{{ labelName }}</label>
      <p class="text-[#898989] opacity-80 mr-1 text-[13px]">{{ labelText }}</p>
    </div>
    <textarea class="inputArea mt-2" :id="labelName" type="text" :value="modelValue"
      @input="$emit('update:modelValue', ($event.target as HTMLInputElement).value)">
    </textarea>
  </section>
</template>
