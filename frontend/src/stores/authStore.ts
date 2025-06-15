import axios from "axios";
import { defineStore } from "pinia";

export const useAuthStore = defineStore("auth", {
  state: () => ({
    user: null,
    
  }),

  getters: {
    isLoggedIn(): boolean {
      return !!this.user;
    }
  },

  actions: {

  },
})

