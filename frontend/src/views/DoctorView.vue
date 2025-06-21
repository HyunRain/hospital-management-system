<script setup lang="ts">
import PatientTable from '@/components/ui/patientview/PatientTable.vue';
import { useToggleStore } from '@/stores/toggleStore';
import { Pagination, PaginationContent, PaginationEllipsis, PaginationItem, PaginationNext, PaginationPrevious } from '@/components/ui/pagination'


const toggleStore = useToggleStore();
</script>

<template>
  <div class="flex flex-col w-full mt-5 p-5 bg-gray-50 dark:bg-[#030712] dark:border border-zinc-800 min-h-[calc(100vh-147px)] overflow-y-auto rounded-xl">
      <div class="flex justify-between items-center mb-2">
        <div class="flex gap-2 items-center">
          <img class="size-6" :src="`/assets/icons/${toggleStore.darkModeState}/doctor.svg`" alt="Doctor Icon">
          <h2 class="text-[20px]">Doctors</h2>
        </div>
        <div class="items-center flex gap-2">
          <input type="text" placeholder="Search by Name, ID, ..." class="h-[35px] shadow-sm hidden md:block px-3 border bg-white border-gray-300 dark:border-[#1f1f23] dark:bg-[#1f1f23] dark:placeholder-[#979797] rounded-xl focus:outline-none">
          <button class="px-3 h-[35px] bg-red-100 shadow-sm hover:bg-red-200 font-medium dark:bg-[#1f1f23] rounded-xl cursor-pointer dark:hover:bg-zinc-800">
            + Add Doctor
          </button>
        </div>
      </div>
      <div class="h-full items-center justify-center flex">
          <PatientTable/>
      </div>
      <div class="flex items-center justify-between py-3">
        <p>Showing 15 of 321 Patients</p>
        <div>
          <Pagination v-slot="{ page }" :items-per-page="10" :total="100" :default-page="1">
            <PaginationContent v-slot="{ items }">
              <PaginationPrevious />
              <template v-for="(item, index) in items" :key="index">
                <PaginationItem
                  v-if="item.type === 'page'"
                  :value="item.value"
                  :is-active="item.value === page"
                >
                  {{ item.value }}
                </PaginationItem>
              </template>
              <PaginationEllipsis :index="4" />
              <PaginationNext />
            </PaginationContent>
          </Pagination>
        </div>
      </div>
  </div>
</template>

<style scoped>

</style>
