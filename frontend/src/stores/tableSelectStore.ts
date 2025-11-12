import { defineStore } from 'pinia';

export const useTableSelectStore = defineStore('tableSelectStore', {
  state: () => ({
    selectedPatientIdx: -1,
    selectedDoctorIdx: -1,
    selectedTable: '' as string,
  }),
  actions: {

  },
});

