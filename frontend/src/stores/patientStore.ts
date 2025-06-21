import { defineStore } from "pinia";
import axios from "axios";
import type { PatientDto } from "@/util/types/types";
import { apiUrl } from "@/util/api/apiUrl";

export const usePatientStore = defineStore("patient", {
  state: () => ({
    patients: [] as PatientDto[],
    size: 15 as number,
    totalPages: 0 as number,
    totalPatients: 0 as number,
  }),

  actions: {
    async getPageOfPatients(page: number, size: number) {
      const response = await axios.get(apiUrl + 'patient/all', {
        params: {
          page: page,
          size: size,
        },
        withCredentials: true,
      });
      this.patients = response.data.patients;
      this.totalPages = response.data.totalPages;
      this.totalPatients = response.data.totalPatients;
    },

    async searchPatients(input: string, page: number, size: number) {
      const response = await axios.get(apiUrl + 'patient/search', {
        params: {
          input: input,
          page: page,
          size: size,
        },
        withCredentials: true,
      });
      this.patients = response.data.patients;
      this.totalPages = response.data.totalPages;
      this.totalPatients = response.data.totalPatients;
    }
  },
});
