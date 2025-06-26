import { defineStore } from "pinia";


export const useToggleStore = defineStore("toggle", {
  state: () => ({
    // Navbar
    showUserDropdown: false as boolean,
    darkModeState: localStorage.getItem('vueuse-color-scheme') === 'light' ? 'lightMode' : 'darkMode',
    // LoginView
    showPassword: false as boolean,
    // User
    isDemo: localStorage.getItem('isDemo') === "true",
    // PatientView
    showAddPatientModal: false as boolean,
    // DoctorView
    showAddDoctorModal: false as boolean,
    // Misc
    showLoader: false as boolean,
  }),

  actions: {
    toggleUserDropdown() {
      this.showUserDropdown = !this.showUserDropdown;
    },
    togglePassword() {
      this.showPassword = !this.showPassword;
    },
    toggleDemo() {
      this.isDemo = !this.isDemo;
      this.isDemo ? localStorage.setItem('isDemo', "true") : localStorage.removeItem('isDemo');
    },
    toggleAddPatientModel() {
      this.showAddPatientModal = !this.showAddPatientModal;
    },
    toggleAddDoctorModal() {
      this.showAddDoctorModal = !this.showAddDoctorModal;
    },
    toggleLoader() {
      this.showLoader = !this.showLoader;
    }
  },
});
