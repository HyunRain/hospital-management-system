import { defineStore } from 'pinia'
import api from './apiInterceptor';
import type { DepartmentDto } from '@/util/types/types';

export const useDepartmentStore = defineStore('department', {
  state: () => ({
    departments: [] as DepartmentDto[],
  }),

  actions: {
    async fetchDepartments() {
      const response = await api.get('/department/all');
      this.departments = response.data;
      this.departments = this.departments.map((d) => ({
        ...d,
        status: d.isActive ? 'Open' : 'Closed',
      }));
    }
  },
})
