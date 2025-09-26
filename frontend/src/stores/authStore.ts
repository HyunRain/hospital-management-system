import { defineStore } from "pinia";
import { type User, UserRole } from "@/util/types/types";
import { createProfilPicture } from "@/util/functions/createProfilePicture";
import api from "./apiInterceptor";

export const useAuthStore = defineStore("auth", {
  state: () => ({
    user: null as User | null,
    email: null as string | null,
    role: null as UserRole | null,
    isInitialised: false as boolean,
  }),

  getters: {
    isLoggedIn(): boolean {
      return !!this.user;
    },
    profileImage(): string {
      return createProfilPicture(this.user?.firstName ?? "Demo");
    }
  },

  actions: {
      async login(userInput: Record<string,string>) {
        const response = await api.post("/auth/login", userInput);
        this.email = response.data.email;
        this.role = response.data.role;
      },

      async fetchStaffData(role: UserRole, email: string) {
        if(role === UserRole.DOCTOR || role === UserRole.NURSE || role === UserRole.RECEPTIONIST) {
          const responseStaff = await api.get(`/staff/get/${email}`);
          this.user = responseStaff.data;
        } else if(this.role === UserRole.ADMIN) {
            this.user =  {
              userId: '',
              firstName: 'Admin',
              lastName: '',
              gender: 'OTHER',
              dateOfBirth: '',
              phoneNumber: '',
              email: email ?? '',
              addressLine1: '',
              addressLine2: null,
              city: '',
              state: '',
              country: '',
              postalCode: '',
              departmentId: '',
            } as User;
        }
      },

      async refreshToken() {
        try {
          await api.post("/auth/refresh", {});
          return this.isInitialised;
        } catch (error) {
          console.error("Error refreshing token:", error);
          this.user = null;
          this.email = null;
          this.role = null;
          this.isInitialised = false;
          return false;
        }
      },

      async reauthenticate() {
        const response = await api.get("/auth/get/currentUser");
        this.email = response.data.email;
        this.role = response.data.role;
        this.isInitialised = true;
      },

      async logout() {
        await api.post("/auth/logout", {});
        this.user = null;
        this.email = null;
        this.role = null;
      },
  },
})

