<script setup lang="ts">
import type { PropType } from 'vue';
import { ref } from 'vue';
import { useToggleStore } from '@/stores/toggleStore';
import { usePatientStore } from '@/stores/patientStore';
import { useDoctorStore } from '@/stores/doctorStore';
import { useStaffStore } from '@/stores/staffStore';

const toggleStore = useToggleStore();
const patientStore = usePatientStore();
const doctorStore = useDoctorStore();
const staffStore = useStaffStore();

type SelectItem = {
  label: string;
  value: string;
};

const storeMap = {
  patient: patientStore,
  doctor: doctorStore,
  staff: staffStore,
};

const props = defineProps({
  data: {
    type: Array as PropType<SelectItem[]>,
    required: true,
  },
  type: {
    type: String,
    required: true,
  },
  forView: {
    type: String as PropType<'patient' | 'doctor' | 'staff'>,
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
      <img class="size-4.5 ml-auto" :src="`/assets/icons/${toggleStore.darkModeState}/downarrow.svg`" alt="Chevron Down Icon">
    </button>

    <div v-click-outside="toggleDropdown" v-if="showDropdown" class="border flex flex-col w-full overflow-scroll overflow-y-scroll max-h-[175px] lg:max-h-[275px] mt-2 absolute z-50 bg-white dark:bg-[#0a0a0a] border-rose-200
      dark:border-neutral-900 rounded-lg px-2 py-3">
      <div v-for="item in props.data" :key="item.value"
        @click.stop="() => { selection = item.label; const store = storeMap[forView]; store.storeSelectInput(item.value, type); showDropdown = false; }"
        class="flex justify-between h-[37px] items-center py-1 px-3 rounded-lg font-medium hover:bg-rose-200 dark:hover:bg-[#1d1d1dcf]">
        <p> {{ item.label }} </p>
        <img v-if="item.label === selection" class="size-3.5" :src="`/assets/icons/${toggleStore.darkModeState}/checkmark.svg`" alt="Checkmark Icon">
      </div>
    </div>
  </div>
</template>
