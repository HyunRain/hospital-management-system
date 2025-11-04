<script setup lang="ts">
import type { PatientDto, StaffDto, Column } from '@/util/types/types';
import { type PropType } from 'vue';
import DataTable from '../DataTable.vue';

const props = defineProps({
  results: {
    type: Array as PropType<PatientDto[] | StaffDto[]>,
    required: true,
  },
  columns: {
    type: Array as PropType<Column[]>,
    required: true,
  },
  tableName: {
    type: String,
    required: true,
  }
})

const emit = defineEmits(['toggle']);
</script>

<template>
  <section class="mt-5">
    <DataTable :columns="columns" :data="results" :table-name="tableName" @toggle="emit('toggle')"></DataTable>
  </section>
</template>
