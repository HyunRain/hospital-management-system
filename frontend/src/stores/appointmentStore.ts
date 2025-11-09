import { defineStore } from 'pinia';
import type { AppointmentFormData } from '../util/types/types';
import api from './apiInterceptor';

export const useAppointmentStore = defineStore('appointmentStore', {
  state: () => ({
    // Appointment Calendar
    selectedDepartment: 'Pediatrics' as string,
    selectedCalendarRange: 'Month' as string,
    dayCalendarTimeSlotValues: { hour: 0, slot: 0 },

    // Appointment Form
    clickedAppointmentData: {} as AppointmentFormData,
    selectedDate: {
      date: '' as string,
      endDate: '' as string,
      time: '09:00' as string,
      endTime: '10:00' as string,
    },

    //Appointments Data
    cachedMonths: {} as Record<string, AppointmentFormData[]>,
    cachedDays: {} as Record<string, AppointmentFormData[]>,
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
    resetClickedAppointmentFormData() {
      this.clickedAppointmentData = {} as AppointmentFormData;
    },
    async createAppointment(appointmentData: AppointmentFormData) {
      const response = await api.post('/appointment', appointmentData);
      const createdAppointment = response.data as AppointmentFormData;

      if (!this.cachedDays[createdAppointment.appointmentDate]) {
        this.cachedDays[createdAppointment.appointmentDate] = [];
      }
      this.cachedDays[createdAppointment.appointmentDate].push(createdAppointment);

      const [year, monthStr] = createdAppointment.appointmentDate.split('-');
      const month = Number(monthStr);

      const key = `${year}-${month}`;
      if (!this.cachedMonths[key]) {
        this.cachedMonths[key] = [];
      }
      this.cachedMonths[key].push(createdAppointment);
    },
    async getAppointmentsByMonthRange(year: number, month: number) {
      const key = `${year}-${month}`;

      if (this.cachedMonths[key]) {
        return;
      }
      const response = await api.get(`/appointment/monthRange/${year}/${month}`);
      const appointments = response.data as AppointmentFormData[];

      for(const appointment of appointments) {
        const startDate = appointment.appointmentDate;

        appointment.appointmentTime = appointment.appointmentTime.slice(0,5);
        appointment.appointmentEndTime = appointment.appointmentEndTime.slice(0, 5);

        if(!this.cachedDays[startDate]) {
          this.cachedDays[startDate] = [];
        }
        this.cachedDays[startDate].push(appointment);
      }

      this.cachedMonths[key] = appointments;
    },
  },
});

