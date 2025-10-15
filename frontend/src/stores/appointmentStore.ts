import { defineStore } from 'pinia';

export const useAppointmentStore = defineStore('appointmentStore', {
  state: () => ({
    // Appointment Calendar Related
    selectedDepartment: 'Pediatrics' as string,
    selectedCalendarRange: 'Month' as string,

    daysInMonth: 0 as number,
  }),

  actions: {
    storeSelectInput(value: string, stateName: string) {
      switch (stateName) {
        case 'selectedDepartment':
          this.selectedDepartment = value;
          break;
        case 'selectedCalendarRange':
          this.selectedCalendarRange = value;
          break;
      }
    }
  },
});

