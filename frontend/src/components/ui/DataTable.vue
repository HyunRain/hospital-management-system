<script setup lang="ts">
import { useTableSelectStore } from '@/stores/tableSelectStore';
import type { Column } from '@/util/types/types';

const props = defineProps({
  data: {
    type: Array as () => any[],
    required: true
  },
  columns: {
    type: Array as () => Column[],
    required: true
  },
  billingTable: {
    type: Boolean,
    default: false
  },
  tableName: {
    type: String,
    required: true,
  }
});

const tableSelectStore = useTableSelectStore();
</script>

<template>
  <table class="min-w-full">
    <thead>
      <tr v-if="props.data.length === 0">
        <td colspan="7" class="text-center py-4">
          {{ 'No results found.' }}
        </td>
      </tr>

      <tr v-else class=" h-[30px] text-[15px] border-b text-nowrap">
        <th class="pb-2" v-for="col in props.columns" :key="col.key" :class="col.class"> {{ col.label }}</th>
      </tr>
    </thead>
    <tbody>
      <tr v-for="(data, index) in props.data" :key="index" @click.stop="tableSelectStore.selectedIdx = index, tableSelectStore.selectedTable = tableName"
        class="text-center w-full h-[45px] cursor-pointer border-zinc-800 hover:bg-red-100 dark:hover:bg-[#1d1d1dcf] text-nowrap">
        <td v-for="(col, colIndex) in props.columns" :key="col.key"
          :class="[col.class, 'py-0', colIndex === 0 ? 'rounded-l-lg' : '', colIndex === props.columns.length - 1 ? 'rounded-r-lg' : '']">

          <slot class="w-fit" v-if="colIndex === 0" name="invoice"></slot>

          <slot v-if="col.key === 'status' && (data.status === 'Open' || data.status === 'Closed')" name="status" :statusValue="data.status"></slot>
          <template v-else>
            {{ data[col.key] }}
          </template>

          <slot v-if="colIndex === props.columns.length - 1" name="download"></slot>
        </td>
      </tr>
    </tbody>
  </table>
</template>
