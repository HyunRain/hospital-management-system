<script setup lang="ts">
import { useToggleStore } from '@/stores/toggleStore';

const toggleStore = useToggleStore();

const props = defineProps({
  headerText: {
    type: String,
    required: true,
  },
})

const emit = defineEmits(['close']);
</script>

<template>
  <header class="flex justify-between items-center">
    <p class=" text-lg">{{ headerText }}</p>
    <img class="size-4.5 cursor-pointer hover:size-5" @click="emit('close')"
      :src="`/assets/icons/${toggleStore.darkModeState}/close.svg`" alt="Close Icon">
  </header>
</template>
