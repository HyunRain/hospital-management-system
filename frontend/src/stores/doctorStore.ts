import { defineStore } from 'pinia'
import axios from 'axios'
import type { StaffDto } from '@/util/types/types'
import api from './apiInterceptor'

export const useDoctorStore = defineStore('doctor', {
  state: () => ({
    doctors: [] as StaffDto[],
    size: 15 as number,
    page: 1 as number,
    totalPages: 0 as number,
    totalDoctors: 0 as number,

    // Add Doctor select data fields
    gender: '' as string,
  }),

  actions: {
    // Main CRUD ACTIONS
    async getPageOfDoctors(page: number, size: number) {
      const response = await api.get('/staff/all', {
        params: {
          page: page,
          size: size,
          role: 'DOCTOR',
        },
        withCredentials: true,
      })
      this.doctors = response.data.staffResponseDtos
      this.totalPages = response.data.totalPages
      this.totalDoctors = response.data.totalStaff
    },

    async searchDoctors(input: string, page: number, size: number) {
      const response = await api.get('/staff/search', {
        params: {
          input: input,
          page: page,
          size: size,
        },
        withCredentials: true,
      })
      this.doctors = response.data.staffResponseDtos
      this.totalPages = response.data.totalPages
      this.totalDoctors = response.data.totalPatients
    },

    async addDoctor(formData: object) {
      const response = await api.post('/staff', formData, {
        withCredentials: true,
      })
      console.log(response.data)
    },

    // Miscellaneous actions
    storeSelectInput(input: string, type: string) {
      switch (type) {
        case 'gender':
          this.gender = input
          break
      }
    },
  },
})
