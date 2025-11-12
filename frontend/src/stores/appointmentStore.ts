import { defineStore } from 'pinia';
import type { AppointmentFormData } from '../util/types/types';
import api from './apiInterceptor';

export const useAppointmentStore = defineStore('appointmentStore', {
  state: () => ({
    // Appointment Calendar
    selectedDepartment: 'Pediatrics' as string,
    selectedCalendarRange: 'Month' as string,
    // Appointment Form
    clickedAppointmentData: {} as AppointmentFormData,
    selectedDate: {
      date: '' as string,
      endDate: '' as string,
      time: '09:00' as string,
      endTime: '10:00' as string,
    },
    isSelectedDateInThePast: false as boolean,

    //Appointments Data
    futureAppointmentCount: 0 as number,
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
    fillSelectedDate(date: string, endDate: string, time: string, endTime: string) {
      this.selectedDate.date = date;
      this.selectedDate.endDate = endDate;
      this.selectedDate.time = time;
      this.selectedDate.endTime = endTime;
      this.isInThePast(date, time);
    },
    isInThePast(date: string, time: string) {
      const year = Number(date.slice(0, 4));
      const month = Number(date.slice(5, 7));
      const day = Number(date.slice(8, 10));
      const hours = Number(time.slice(0, 2));
      const minutes = Number(time.slice(3, 5));

      const selectedDate = new Date(year, month-1, day, hours, minutes)
      const now = new Date();
      this.isSelectedDateInThePast =  selectedDate < now;
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

      createdAppointment.appointmentTime = createdAppointment.appointmentTime.slice(0, 5);
      createdAppointment.appointmentEndTime = createdAppointment.appointmentEndTime.slice(0, 5);

      const dateKey = createdAppointment.appointmentDate;

      if (!this.cachedDays[dateKey]) {
        this.cachedDays[dateKey] = [];
      }
      this.cachedDays[dateKey].push(createdAppointment);

      const [year, monthStr] = createdAppointment.appointmentDate.split('-');
      const month = Number(monthStr);

      const key = `${year}-${month}`;
      if (!this.cachedMonths[key]) {
        this.cachedMonths[key] = [];
      }
      this.cachedMonths[key].push(createdAppointment);
    },
    async updateAppointment(appointmentData: AppointmentFormData) {
      const response = await api.patch(`/appointment/${appointmentData.id}`, appointmentData);
      const updatedAppointment = response.data as AppointmentFormData;

      updatedAppointment.appointmentTime = updatedAppointment.appointmentTime.slice(0, 5);
      updatedAppointment.appointmentEndTime = updatedAppointment.appointmentEndTime.slice(0, 5);

      const oldDate = this.clickedAppointmentData.appointmentDate;
      const newDate = updatedAppointment.appointmentDate;

      // remove from current location if date changed
      if (oldDate.localeCompare(newDate) && this.cachedDays[oldDate]) {
        this.cachedDays[oldDate] = this.cachedDays[oldDate].filter((a) => a.id !== updatedAppointment.id);
      }

      // create new array if date is new
      if (!this.cachedDays[newDate]) {
        this.cachedDays[newDate] = [];
      }

      // push into new array or replace in current location. findIndex returns -1 if it doesnt find the appointment
      const index = this.cachedDays[newDate].findIndex((a) => a.id === updatedAppointment.id);
      if (index === -1) {
        this.cachedDays[newDate].push(updatedAppointment);
      } else {
        this.cachedDays[oldDate][index] = updatedAppointment;
      }

      const [year, monthStr] = updatedAppointment.appointmentDate.split('-');
      const month = Number(monthStr);

      const key = `${year}-${month}`;
      if (!this.cachedMonths[key]) {
        this.cachedMonths[key] = [];
      }
      this.cachedMonths[key].push(updatedAppointment);
    },
    async countFutureAppointments() {
      const response = await api.get('appointment/count');
      this.futureAppointmentCount = response.data;
    },
    async getAppointmentsByMonthRange(year: number, month: number) {
      const key = `${year}-${month}`;

      if (this.cachedMonths[key]) {
        return;
      }
      const response = await api.get(`/appointment/monthRange/${year}/${month}`);
      const appointments = response.data as AppointmentFormData[];

      for (const appointment of appointments) {
        const startDate = appointment.appointmentDate;

        appointment.appointmentTime = appointment.appointmentTime.slice(0, 5);
        appointment.appointmentEndTime = appointment.appointmentEndTime.slice(0, 5);

        if (!this.cachedDays[startDate]) {
          this.cachedDays[startDate] = [];
        }
        this.cachedDays[startDate].push(appointment);
      }

      this.cachedMonths[key] = appointments;
    },
  },
});
