import { defineStore } from 'pinia';

export const useToggleStore = defineStore('toggle', {
  state: () => ({
    // Navbar
    showUserDropdown: false as boolean,
    darkModeState: localStorage.getItem('vueuse-color-scheme') === 'light' ? 'lightMode' : 'darkMode',
    // LoginView
    showPassword: false as boolean,
    // User
    isDemo: localStorage.getItem('isDemo') === 'true',
    // PatientView
    showAddPatientModal: false as boolean,
    // DoctorView
    showAddDoctorModal: false as boolean,
    // StaffView
    showAddStaffModal: false as boolean,
    // Misc
    showLoader: false as boolean,
    // BillingDropDown
    showBillingDropdown: false as boolean,
    // DepartmentSelection
    showDepartmentSelection: false as boolean,
    showCalendarRangeSelection: false as boolean,
    // AppointmentView
    showAppointmentForm: false as boolean,
    showAppointmentTypeSelection: false as boolean,
    showAppointmentTimeSelection: false as boolean,
    showAppointmentEndTimeSelection: false as boolean,
    showSearchPatientDialog: false as boolean,
    showSearchDoctorDialog: false as boolean,
    showStartTimePicker: false as boolean,
    showEndTimePicker: false as boolean,
    showAppointmentFormWarning: false as boolean,
    showFullMonthAppointmentCards: false as boolean,
    clickedDayIndex: -1 as number,
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
      this.isDemo ? localStorage.setItem('isDemo', 'true') : localStorage.removeItem('isDemo');
    },
    toggleAddPatientModel() {
      this.showAddPatientModal = !this.showAddPatientModal;
    },
    toggleAddDoctorModal() {
      this.showAddDoctorModal = !this.showAddDoctorModal;
    },
    toggleAddStaffModal() {
      this.showAddStaffModal = !this.showAddStaffModal;
    },
    toggleLoader() {
      this.showLoader = !this.showLoader;
    },
    toggleBillingDropdown() {
      this.showBillingDropdown = !this.showBillingDropdown;
    },
    toggleDepartmentSelection() {
      this.showDepartmentSelection = !this.showDepartmentSelection;
    },
    toggleCalendarRangeSelection() {
      this.showCalendarRangeSelection = !this.showCalendarRangeSelection;
    },
    toggleAppointmentTypeSelection() {
      this.showAppointmentTypeSelection = !this.showAppointmentTypeSelection;
    },
    toggleAppointmentForm() {
      this.showAppointmentForm = !this.showAppointmentForm;
    },
    toggleAppointmentTimeSelection() {
      this.showAppointmentTimeSelection = !this.showAppointmentTimeSelection;
    },
    toggleAppointmentEndTimeSelection() {
      this.showAppointmentEndTimeSelection = !this.showAppointmentEndTimeSelection;
    },
    toggleSearchPatientDialog() {
      this.showSearchPatientDialog = !this.showSearchPatientDialog;
    },
    toggleSearchDoctorDialog() {
      this.showSearchDoctorDialog = !this.showSearchDoctorDialog;
    },
    toggleStartDatePicker() {
      this.showStartTimePicker = !this.showStartTimePicker;
    },
    toggleEndDatePicker() {
      this.showEndTimePicker = !this.showEndTimePicker;
    },
    toggleAppointmentFormWarning() {
      this.showAppointmentFormWarning = !this.showAppointmentFormWarning;
    },
    toggleFullMonthAppointmentCards(index: number) {
      this.clickedDayIndex = index;
      this.showFullMonthAppointmentCards = !this.showFullMonthAppointmentCards;
    },
  },
});
