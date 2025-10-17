<script setup lang="ts">
import { useAppointmentStore } from '@/stores/appointmentStore';
import { useToggleStore } from '@/stores/toggleStore';
import type { PropType } from 'vue';

const toggleStore = useToggleStore();
const appointmentStore = useAppointmentStore();

const props = defineProps({
  data: {
    type: Array as PropType<string[]>,
    required: true,
  },
  storeName: {
    type: String as PropType<'appointment'>,
    required: true,
  },
  stateName: {
    type: String as PropType<'selectedDepartment' | 'selectedCalendarRange'>,
    required: true,
  },
  toggleStateName: {
    type: String as PropType<'showDepartmentSelection' | 'showCalendarRangeSelection'>,
    required: true,
  },
});

const storeMap = {
  appointment: appointmentStore,
}

</script>

<template>
  <main class="relative lg:min-w-[180px]">
    <button @click="() => toggleStore.toggleAppointmentCalendar(toggleStateName)" type="button" class="chartRangeButton">

      <span class="hidden lg:inline">
        {{ appointmentStore[stateName] }}
      </span>
      <span v-if="props.stateName === 'selectedCalendarRange'" class="inline lg:hidden">
        {{ appointmentStore[stateName].slice(0, 1) }}
      </span>

      <img class="size-4.5" :src="`/assets/icons/${toggleStore.darkModeState}/downarrow.svg`" alt="Chevron Down Icon">
    </button>

    <section v-click-outside="() => toggleStore.toggleAppointmentCalendar(toggleStateName)" v-if="toggleStore[toggleStateName]"
      class="chartRangeDropdown">
      <div v-for="(item, key) in props.data" :key="key" @click.stop="
        () => {
          const store = storeMap[storeName];
          store.storeSelectInput(item, stateName);
          toggleStore.toggleAppointmentCalendar(toggleStateName)
        }
      " class="chartRangeDropdownItem">
        <p> {{ item }} </p>
        <img v-if="item === appointmentStore[stateName]" class="size-3.5" :src="`/assets/icons/${toggleStore.darkModeState}/checkmark.svg`"
          alt="Checkmark Icon">
      </div>
    </section>
  </main>
</template>
