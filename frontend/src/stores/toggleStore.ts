import { defineStore } from "pinia";


export const useToggleStore = defineStore("toggle", {
  state: () => ({
    showUserDropdown: false as boolean,
    darkModeState: localStorage.getItem('vueuse-color-scheme') === 'light' ? 'lightMode' : 'darkMode',
    isDemo: localStorage.getItem('isDemo') === "true",
  }),

  actions: {
    toggleUserDropdown() {
      this.showUserDropdown = !this.showUserDropdown;
    },
    toggleDemo() {
      this.isDemo = !this.isDemo;
      this.isDemo ? localStorage.setItem('isDemo', "true") : localStorage.removeItem('isDemo');
    }
  },
});
