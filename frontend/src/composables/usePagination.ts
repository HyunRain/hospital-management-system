import { ref, watch } from 'vue';
import debounce from 'lodash.debounce';

export function usePagination(
  rangeValue: string,
  store: any,
  searchMethod: (input: string, page: number, size: number, doctorSearch?: boolean,) => Promise<void>,
  getPageOfMethod: (page: number, size: number) => Promise<void>,
  isDoctorSearch: boolean,
  totalEntity: string,
) {
  const range = ref<string>(rangeValue);
  const pageSize = ref<number>(store.size);
  const currentPage = ref(store.page);
  const searchInput = ref('');

  async function handlePageChange(page: number) {
    currentPage.value = page;
    store.page = page;

    if (searchInput.value && searchInput.value.length > 1) {
      await searchMethod(searchInput.value, page - 1, pageSize.value, isDoctorSearch);
    } else {
      await getPageOfMethod(page - 1, pageSize.value);
    }

    updateRange(page);
  }

  // Range = eg. Showing  --> 16-30 <-- of 80 patients
  function updateRange(page: number) {
    const total = store[totalEntity];
    const size = store.size;
    const start = (page - 1) * size + 1;
    const end = Math.min(size * page, total);
    range.value = `${start}-${end}`;
  }

  // ------------------------ Search Entities ------------------------

  const debouncedSearch = debounce(async (input: string) => {
    if (input.length > 1) await searchMethod(input, 0, pageSize.value, isDoctorSearch);
    if (input.length < 1) {
      // if search field becomes empty, fetch first page of all patients and set the visual current page back to 1
      await getPageOfMethod(0, pageSize.value);
      currentPage.value = 1;
    }
    updateRange(store.page);
  }, 300); // delay in ms

  watch(searchInput, (newInput) => {
    debouncedSearch(newInput);
  });

  return {
    range,
    currentPage,
    pageSize,
    handlePageChange,
    searchInput,
  };
}
