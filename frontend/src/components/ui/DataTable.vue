<script setup lang="ts">

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
  }
});

interface Column {
  key: string;
  label: string;
  class?: string; // css
}

</script>

<template>
  <table class="min-w-full">
    <thead>
      <tr v-if="props.data.length === 0">
        <td colspan="7" class="text-center py-4">
          {{ 'No results found.' }}
        </td>
      </tr>

      <tr v-else class=" h-[30px] text-[15px]">
        <th v-for="col in props.columns" :key="col.key" :class="col.class"> {{ col.label }}</th>
      </tr>
    </thead>
    <tbody>
      <tr v-for="(data, index) in props.data" :key="index"
        class="text-center w-full h-[45px] cursor-pointer border-zinc-800 hover:bg-red-100 dark:hover:bg-[#1d1d1dcf]">
        <td v-for="(col, colIndex) in props.columns" :key="col.key"
          :class="[col.class, colIndex === 0 ? 'rounded-l-lg' : '', colIndex === props.columns.length - 1 ? 'rounded-r-lg' : '']">

          <slot v-if="colIndex === 0" name="invoice"></slot>

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
