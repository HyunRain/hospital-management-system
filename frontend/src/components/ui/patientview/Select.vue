<script setup lang="ts">
import type { PropType } from 'vue';
import { ref } from 'vue';
import { useToggleStore } from '@/stores/toggleStore';
import { usePatientStore } from '@/stores/patientStore';

const toggleStore = useToggleStore();
const patientStore = usePatientStore();

type SelectItem = {
  label: string;
  value: string;
};

const props = defineProps({
  data: {
    type: Array as PropType<SelectItem[]>,
    required: true,
  },
  type: {
    type: String,
    required: true,
  }
});

const isMobile = window.innerWidth <= 640;
const selection = ref(isMobile ? 'Select' : `Select a ${props.type}`);

const showDropdown = ref(false);
function toggleDropdown() {
  showDropdown.value = !showDropdown.value;
}
</script>

<template>
  <div class="w-full relative">
    <button @click="toggleDropdown" type="button" :class="[selection.startsWith('Select a') ? 'text-zinc-400' : '']"
      class="input relative mt-2 flex items-center cursor-pointer truncate">
      {{ selection }}
      <img class="size-4.5 ml-auto" :src="`/assets/icons/${toggleStore.darkModeState}/downarrow.svg`"
        alt="Chevron Down Icon">
    </button>

    <!-- Select Dropdown -->
    <div v-click-outside="toggleDropdown" v-if="showDropdown" class="border flex flex-col w-full mt-2 absolute z-50 bg-white dark:bg-[#02050e] border-red-300
      dark:border-zinc-800 rounded-xl px-2 py-3">
      <!-- Select Items-->
      <div v-for="item in props.data" :key="item.value"
        @click.stop="() => { selection = item.label; patientStore.storeSelectInput(item.value, type); showDropdown = false; }"
        class="flex justify-between h-[37px] items-center py-1 px-3 rounded-xl font-medium hover:bg-red-200 dark:hover:bg-[#1d1d1dcf]">
        <p> {{ item.label }} </p>
        <img v-if="item.label === selection" class="size-3.5"
          :src="`/assets/icons/${toggleStore.darkModeState}/checkmark.svg`" alt="Checkmark Icon">
      </div>
    </div>
  </div>
</template>

<style scoped></style>
