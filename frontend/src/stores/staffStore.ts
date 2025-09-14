import { defineStore } from 'pinia'
import axios from 'axios'
import api from './apiInterceptor'
import type { StaffDto } from '@/util/types/types';

export const useStaffStore = defineStore('staff', {
  state: () => ({
    staff: [] as StaffDto[],
    size: 15 as number,
    page: 1 as number,
    totalPages: 0 as number,
    totalStaff: 0 as number,


    // Add Staff select data fields
    gender: '' as string,
    role: '' as string
  }),

  actions: {
    // Main CRUD ACTIONS
    async getPageOfStaff(page: number, size: number, role: String) {
      const response = await api.get('/staff/all', {
        params: {
          page: page,
          size: size,
          role: role,
        },
        withCredentials: true,
      })
      this.staff = response.data.staffResponseDtos
      this.totalPages = response.data.totalPages
      this.totalStaff = response.data.totalStaff
    },

    async getPageOfAllStaff(page: number, size: number) {
      const response = await api.get('/staff/allRoles', {
        params: {
          page: page,
          size: size,
        },
        withCredentials: true,
      })
      this.staff = response.data.staffResponseDtos
      this.totalPages = response.data.totalPages
      this.totalStaff = response.data.totalStaff
    },

    async searchStaff(input: string, page: number, size: number) {
      const response = await api.get('/staff/search', {
        params: {
          input: input,
          page: page,
          size: size,
        },
        withCredentials: true,
      })
      this.staff = response.data.staffResponseDtos
      this.totalPages = response.data.totalPages
      this.totalStaff = response.data.totalStaff
    },

    async addStaff(formData: object) {
      const response = await api.post('/staff', formData, {
        withCredentials: true,
      })
      console.log(response.data)
    },
  },
})
