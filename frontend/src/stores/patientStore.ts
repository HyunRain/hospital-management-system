import { defineStore } from "pinia";
import axios from "axios";
import type { PatientDto } from "@/util/types/types";
import api from "./apiInterceptor";

export const usePatientStore = defineStore("patient", {
  state: () => ({
    patients: [] as PatientDto[],
    size: 15 as number,
    page: 1 as number,
    totalPages: 0 as number,
    totalPatients: 0 as number,

    // Add Patient select data fields
    bloodType: '' as string,
    gender: '' as string,
    maritalStatus: '' as string,
  }),

  actions: {
    // Main CRUD ACTIONS
    async getPageOfPatients(page: number, size: number) {
      const response = await api.get('/patient/all', {
        params: {
          page: page,
          size: size,
        },
        withCredentials: true,
      });
      this.patients = response.data.patients;
      this.fullNameConcatenation(this.patients);
      this.totalPages = response.data.totalPages;
      this.totalPatients = response.data.totalPatients;
    },

    async searchPatients(input: string, page: number, size: number) {
      const response = await api.get('/patient/search', {
        params: {
          input: input,
          page: page,
          size: size,
        },
        withCredentials: true,
      });
      this.patients = response.data.patients;
      this.fullNameConcatenation(this.patients);
      this.totalPages = response.data.totalPages;
      this.totalPatients = response.data.totalPatients;
    },

    async addPatient(formData: object) {
      const response = await api.post('/patient', formData, {
        withCredentials: true,
      });
      console.log(response.data);
    },

    // Miscellaneous actions
    storeSelectInput(input: string, type: string) {
      switch (type) {
        case "gender":
          this.gender = input;
          break;
        case "blood type":
          this.bloodType = input;
          break;
        case "marital status":
          this.maritalStatus = input;
          break;
      }
    },

    fullNameConcatenation(patients: PatientDto[]) {
      this.patients = patients.map(p => ({
        ...p,
        fullName: p.firstName + ' ' + p.lastName
      }));
    }
  },
});
