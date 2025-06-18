import { defineStore } from "pinia";


export const useToggleStore = defineStore("toggle", {
  state: () => ({
    showUserDropdown: false as boolean,
    darkModeState: localStorage.getItem('vueuse-color-scheme') === 'light' ? 'lightMode' : 'darkMode',
  }),

  actions: {
    toggleUserDropdown() {
      this.showUserDropdown = !this.showUserDropdown;
    }
  },
});
