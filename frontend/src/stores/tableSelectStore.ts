import { defineStore } from 'pinia';

export const useTableSelectStore = defineStore('tableSelectStore', {
  state: () => ({
    selectedIdx: -1,
    selectedTable: '' as string,
  }),
  actions: {

  },
});

