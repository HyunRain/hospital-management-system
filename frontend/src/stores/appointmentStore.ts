import { defineStore } from 'pinia';
import type { AppointmentFormData } from '../util/types/types';
import { apiUrl } from '@/util/api/apiUrl';
import api from './apiInterceptor';

export const useAppointmentStore = defineStore('appointmentStore', {
  state: () => ({
    // Appointment Calendar
    selectedDepartment: 'Pediatrics' as string,
    selectedCalendarRange: 'Month' as string,
    dayCalendarTimeSlotValues: { hour: 0, slot: 0 },

    // Appointment Form
    createAppointmentData: {} as AppointmentFormData,
    selectedDate: {
      date: '' as string,
      endDate: '' as string,
      time: '09:00' as string,
      endTime: '10:00' as string,
    }
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
    async createAppointment(appointmentData: AppointmentFormData) {
      console.log(appointmentData);
      const response = await api.post('/appointment', appointmentData, {
        withCredentials: true,
      });
      console.log(response.data);
    },
    fillSelectedDate(date: string, endDate: string, time: string, endTime: string) {
      this.selectedDate.date = date;
      this.selectedDate.endDate = endDate;
      this.selectedDate.time = time;
      this.selectedDate.endTime = endTime;
    },
    resetSelectedDate() {
      this.selectedDate.date = '';
      this.selectedDate.endDate = '';
      this.selectedDate.time = '09:00';
      this.selectedDate.endTime = '10:00';
    },
  },
});

