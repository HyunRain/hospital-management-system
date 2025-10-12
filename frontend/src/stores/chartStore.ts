import { defineStore } from 'pinia';
import type { ChartStoreState } from '@/util/types/types';

export const useChartStore = defineStore('chartStore', {
  state: (): ChartStoreState => ({
    patientChartData: {
      week: {
        labels: ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun'],
        data: [10, 12, 8, 15, 20, 18, 9],
      },
      month: {
        labels: ['Week 1', 'Week 2', 'Week 3', 'Week 4'],
        data: [50, 70, 40, 90],
      },
      year: {
        labels: ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Sep', 'Oct', 'Nov', 'Dec'],
        data: [120, 90, 140, 100, 180, 160, 120, 90, 130, 180, 150, 120],
      },
    },
    patientSelectedRange: 'year',
    appointmentChartData: {
      week: {
        labels: ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun'],
        data: [20, 32, 18, 25, 24, 28, 19],
      },
      month: {
        labels: ['Week 1', 'Week 2', 'Week 3', 'Week 4'],
        data: [50, 70, 40, 90],
      },
      year: {
        labels: ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Sep', 'Oct', 'Nov', 'Dec'],
        data: [420, 220, 340, 150, 180, 300, 120, 190, 300, 250, 150, 220],
      },
    },
    appointmentSelectedRange: 'week',
    revenueChartData: {
      week: {
        labels: ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun'],
        data: [10, 12, 8, 15, 20, 18, 9],
      },
      month: {
        labels: ['Week 1', 'Week 2', 'Week 3', 'Week 4'],
        data: [50, 70, 40, 90],
      },
      year: {
        labels: ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Sep', 'Oct', 'Nov', 'Dec'],
        data: [10230, 13540, 12050, 14820, 16010, 18560, 17090, 20570, 18030, 17580, 19020, 16540],
      },
    },
    revenueSelectedRange: 'year',
    billingChartData: {
      week: {
        labels: ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun'],
        data: [10, 12, 8, 15, 20, 18, 9],
      },
      month: {
        labels: ['Week 1', 'Week 2', 'Week 3', 'Week 4'],
        data: [50, 70, 40, 90],
      },
      year: {
        labels: ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Sep', 'Oct', 'Nov', 'Dec'],
        data: [85, 130, 145, 95, 155, 170, 160, 120, 125, 140, 135, 110],
      },
    },
    billingSelectedRange: 'year',
    departmentChartData: {
      labels: [
        'Orthopedics',
        'Radiology',
        'Oncology',
        'Dermatology',
        'Surgery',
        'Pulmonology',
        'Cardiology',
        'Gynecology',
        'Emergency',
        'Endocrinology',
        'Pediatrics',
        'Ophthalmology',
        'Pathology',
        'Nephrology',
        'Neurology',
        'Urology',
        'Gastroenterology',
      ],
      data: [8, 6, 4, 8, 6, 9, 7, 8, 12, 9, 4, 7, 1, 5, 2, 3, 4],
    },
    departmentSelectedRange: 'year',
  }),

  actions: {},
});
