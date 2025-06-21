<script setup lang="ts">
import PatientTable from '@/components/ui/patientview/PatientTable.vue';
import { useToggleStore } from '@/stores/toggleStore';
import { Pagination, PaginationContent, PaginationEllipsis, PaginationItem, PaginationNext, PaginationPrevious } from '@/components/ui/pagination'
import { usePatientStore } from '@/stores/patientStore';
import { ref, watch } from 'vue';
import debounce from 'lodash.debounce';

const toggleStore = useToggleStore();
const patientStore = usePatientStore();

const patientRange = ref<string>("1-15");
const pageSize = ref<number>(patientStore.size);

async function handlePageChange(page: number) {
  if(searchInput.value.length > 1) {
    await patientStore.searchPatients(searchInput.value, page-1, pageSize.value);
  } else {
    await patientStore.getPageOfPatients(page-1, pageSize.value);
  }
  updatePatientsRange(page);
}

function updatePatientsRange(page: number) {
  const size = patientStore.size;
  patientRange.value = `${(page-1)*size+1}-${patientStore.totalPatients < size*page ? patientStore.totalPatients : size*page}`;
}

const searchInput = ref('');

const debouncedSearch = debounce(async (input: string) => {
    if (input.length > 1) await patientStore.searchPatients(input, 0, pageSize.value);
    if(input.length < 1) await patientStore.getPageOfPatients(0, pageSize.value);
}, 300); // delay in ms

watch(searchInput, (newInput) => {
  debouncedSearch(newInput);
});


</script>

<template>
  <div class="flex flex-col w-full mt-5 p-5 bg-gray-50 dark:bg-[#030712] dark:border border-zinc-800 min-h-[calc(100vh-147px)] rounded-xl">
      <div class="flex justify-between items-center mb-5">
        <div class="flex gap-2 items-center">
          <img class="size-6" :src="`/assets/icons/${toggleStore.darkModeState}/patients.svg`" alt="PatientIcon">
          <h2 class="text-[20px]">Patients</h2>
        </div>
        <div class="items-center flex gap-2">
          <input type="text" placeholder="Search by Name, ID, ..." v-model="searchInput" class="h-[35px] shadow-sm hidden md:block px-3 border bg-white border-gray-300 dark:border-[#1f1f23] dark:bg-[#1f1f23] dark:placeholder-[#979797] rounded-xl focus:outline-none">
          <button class="px-3 h-[35px] bg-red-100 shadow-sm hover:bg-red-200 font-medium dark:bg-[#1f1f23] rounded-xl cursor-pointer dark:hover:bg-zinc-800">+ Add Patient</button>
        </div>
      </div>
      <div class="h-full overflow-x-auto items-start flex">
          <PatientTable/>
      </div>
      <div class="flex flex-col md:flex-row gap-3  items-center justify-between py-3">
        <p class="md:mt-0 mt-3">Showing {{ patientRange }} of {{ patientStore.totalPatients }} patients</p>
        <div>
          <Pagination v-slot="{ page }" :items-per-page="patientStore.size" :total="patientStore.totalPatients" @update:page="handlePageChange" :default-page="1">
            <PaginationContent v-slot="{ items }">
              <PaginationPrevious class="cursor-pointer" />
              <template v-for="(item, index) in items" :key="index">
                <PaginationItem class="cursor-pointer" v-if="item.type === 'page'" :value="item.value" :is-active="item.value === page">
                  {{ item.value }}
                </PaginationItem>
              </template>
              <PaginationEllipsis :index="4" />
              <PaginationNext class="cursor-pointer"/>
            </PaginationContent>
          </Pagination>
        </div>
      </div>
  </div>
</template>

<style scoped>

</style>
