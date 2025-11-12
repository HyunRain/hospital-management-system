<script setup lang="ts">
import { ref, watch } from 'vue';
import { Pagination, PaginationContent, PaginationEllipsis, PaginationItem, PaginationNext, PaginationPrevious, } from '@/components/ui/pagination';

const props = defineProps({
  range: {
    type: String,
    required: true,
  },
  total: {
    type: Number,
    required: true,
  },
  currentPage: {
    type: Number,
    required: true,
  },
  sizePerPage: {
    type: Number,
    required: true,
  },
  type: {
    type: String,
    required:true,
  }
});

const localCurrentPage = ref(props.currentPage);
watch(() => props.currentPage, (newPage) => {
  localCurrentPage.value = newPage;
})

const emit = defineEmits(['page-change']);
function handlePageChange(newPage: Number) {
  emit('page-change', newPage);
}
</script>

<template>
  <main class="flex flex-col md:flex-row gap-3 items-center justify-between py-3">
    <p class="md:mt-0 mt-3">
      Showing {{ range }} of {{ total }} {{ type }}
    </p>
    <div>
      <Pagination v-slot="{ page }" :items-per-page="sizePerPage" v-model:page="localCurrentPage" :total="total" @update:page="handlePageChange"
        :default-page="1">
        <PaginationContent v-slot="{ items }">
          <PaginationPrevious class="cursor-pointer" />
          <template v-for="(item, index) in items" :key="index">
            <PaginationItem class="cursor-pointer" v-if="item.type === 'page'" :value="item.value" :is-active="item.value === page">
              {{ item.value }}
            </PaginationItem>
          </template>
          <PaginationEllipsis :index="4" />
          <PaginationNext class="cursor-pointer" />
        </PaginationContent>
      </Pagination>
    </div>
  </main>
</template>

<style scoped></style>
