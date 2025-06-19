import axios from "axios";
import { defineStore } from "pinia";
import { type User, UserRole } from "@/util/types/types";
import { createProfilPicture } from "@/util/functions/createProfilePicture";

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
      async login(userInput: { email: string; password: string }) {
        const response = await axios.post("http://localhost:8079/api/auth/login", userInput, { withCredentials: true });
        this.email = response.data.email;
        this.role = response.data.role;
      },

      async fetchStaffData(role: UserRole, email: string) {
        if(role === UserRole.DOCTOR || role === UserRole.NURSE || role === UserRole.RECEPTIONIST) {
          const responseStaff = await axios.get(`http://localhost:8079/api/staff/get/${email}`, { withCredentials: true });
          this.user = responseStaff.data;
          console.log("User logged in:", this.user);
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
            console.log("Admin user logged in:", this.user);
        }
      },

      async reauthenticate() {
        const response = await axios.get("http://localhost:8079/api/auth/get/currentUser", { withCredentials: true });
        this.email = response.data.email;
        this.role = response.data.role;
        console.log("Reauthentication successful: ", this.email, this.role);
      },

      async logout() {
        await axios.post("http://localhost:8079/api/auth/logout", {}, { withCredentials: true });
        this.user = null;
        this.email = null;
        this.role = null;
      },
  },
})

