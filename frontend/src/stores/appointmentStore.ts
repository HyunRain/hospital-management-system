import { defineStore } from 'pinia';

export const useAppointmentStore = defineStore('appointmentStore', {
  state: () => ({
    // Appointment Calendar Related
    selectedDepartment: 'Pediatrics' as string,
    selectedCalendarRange: 'Month' as string,
    dayCalendarTimeSlotValues: { hour: 0, slot: 0 },
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
    },
    setDayCalendarTimeSlot(hour: number, slot: number) {
      this.dayCalendarTimeSlotValues.hour = hour;
      this.dayCalendarTimeSlotValues.slot = slot;
    },
  },
});

