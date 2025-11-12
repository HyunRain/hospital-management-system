import { defineStore } from 'pinia'
import api from './apiInterceptor'

export const useBillingStore = defineStore('billing', {
  state: () => ({
    billingItems: [] as any[],
    page: 0 as number,
    size: 8 as number,
    totalPages: 0 as number,
    totalBillingItems: 0 as number,
  }),

  actions: {
    // most recent billings first (handled in backend)
    async fetchBillings(page: number, size: number) {
      const response = await api.get('/billings-with-patients', {
        params: {
          page: page,
          size: size
        },
        withCredentials: true,
      });

      this.billingItems = response.data.billingItemResponseDtos;
      this.totalPages = response.data.totalPages;
      this.totalBillingItems = response.data.totalBillingItems;
    },

    async searchBillingItems(input: string, page: number, size: number) {

    },
    
    async getPageOfBillingItems(page: number, size: number) {

    },

    async fetchPdfUrl(fileName: string) {
      const response = await api.get('/billing-item/pdf/get', {
        params: {
          fileName: fileName
        },
        withCredentials: true,
      });
      return response.data.pdfUrl;
    }
  },
})
