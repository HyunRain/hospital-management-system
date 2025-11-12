<script setup lang="ts">
import { useToggleStore } from '@/stores/toggleStore';
import { onMounted, ref, watch } from 'vue';

onMounted(() => {
  searchRef.value?.focus();
});

const props = defineProps({
  placeholder: {
    type: String,
    required: true,
  }
});

const toggleStore = useToggleStore();

const emit = defineEmits(['toggle', 'search']);

const input = ref('');
const showResults = ref(false);

watch(input, (newInput) => {
  newInput.length > 1 ? showResults.value = true : showResults.value = false;
  emit('search', newInput);
});

const searchRef = ref<HTMLInputElement | null>(null);
</script>

<template>
  <div class="searchOverlay">
    <div class="searchFrame" v-click-outside="() => emit('toggle')">
      <div class="flex items-center gap-1 p-5">
        <img class="size-9" :src="`/assets/icons/${toggleStore.darkModeState}/search.svg`" alt="AppointmentIcon" />
        <input type="search" class="input" v-model="input" :placeholder="placeholder" ref="searchRef">
      </div>
      <div class="border-t px-5 pb-5" v-if="showResults">
        <slot name="results"></slot>
      </div>
    </div>
  </div>
</template>
