export interface User {
  userId: string;
  firstName: string;
  lastName: string;
  gender: 'MALE' | 'FEMALE' | 'OTHER';
  dateOfBirth: string;
  phoneNumber: string;
  email: string;
  addressLine1: string;
  addressLine2: string | null;
  city: string;
  state: string;
  country: string;
  postalCode: string;
  departmentId: string;
}

export enum UserRole {
  ADMIN = 'ADMIN',
  DOCTOR = 'DOCTOR',
  NURSE = 'NURSE',
  RECEPTIONIST = 'RECEPTIONIST',
}

export interface PatientDto {
  firstName: string;
  lastName: string;
  fullName: string;
  gender: 'MALE' | 'FEMALE' | 'OTHER';
  dateOfBirth: string; //
  bloodGroup:
    | 'A_POSITIVE'
    | 'A_NEGATIVE'
    | 'B_POSITIVE'
    | 'B_NEGATIVE'
    | 'AB_POSITIVE'
    | 'AB_NEGATIVE'
    | 'O_POSITIVE'
    | 'O_NEGATIVE';
  maritalStatus: 'SINGLE' | 'MARRIED';
  phoneNumber: string;
  email: string;
  emergencyContactName?: string;
  emergencyContactNumber?: string;
  relationshipToEmergencyContact?: string;
  addressLine1: string;
  addressLine2?: string;
  city: string;
  state: string;
  country: string;
  postalCode: string;
  status: 'ACTIVE' | 'INACTIVE' | 'DECEASED';
  patientId?: string;
  referredBy?: string;
  knownAllergies?: string[];
  pastMedicalHistory?: string[];
  chronicDiseases?: string[];
  currentMedications?: string[];
  immunizationStatus?: string[];
  surgicalHistory?: string[];
  insuranceProvider?: string;
  insurancePolicyNumber?: string;
  insuranceExpiryDate?: string;
}

export interface StaffDto {
  userId: string;
  staffId: string;
  firstName: string;
  lastName: string;
  fullName: string;
  gender: 'MALE' | 'FEMALE' | 'OTHER';
  dateOfBirth: string;
  phoneNumber: string;
  email: string;
  addressLine1: string;
  addressLine2?: string;
  city: string;
  state: string;
  country: string;
  postalCode: string;
  role: string;
  departmentName: string;
}

export interface DepartmentDto {
  id: string;
  name: string;
  headOfDepartmentName: string;
  headOfDepartmentId: string;
  staffCount: number;
  bedCapacity: number;
  currentBedCount: number;
  isActive: boolean;
}

export interface BillingData {
  billingId: string;
  billingAccountId: string;
  billingItemType: string;
  unitPrice: number;
  quantity: number;
  totalPrice: number;
  status: 'ACTIVE' | 'CLOSED';
  startDate: string;
  dueDate: string;
  patientName: string;
}

export type selectedRange = 'week' | 'month' | 'year';

export interface Selection {
  key: selectedRange;
  value: string;
}

export interface ChartStoreState {
  patientChartData: TimeRangeData;
  patientSelectedRange: selectedRange;
  appointmentChartData: TimeRangeData;
  appointmentSelectedRange: selectedRange;
  revenueChartData: TimeRangeData;
  revenueSelectedRange: selectedRange;
  billingChartData: TimeRangeData;
  billingSelectedRange: selectedRange;
  departmentChartData: ChartData;
  departmentSelectedRange: selectedRange;
}

export interface TimeRangeData {
  week: ChartData;
  month: ChartData;
  year: ChartData;
}

export interface ChartData {
  labels: string[];
  data: number[];
}

export type ValidationResult = { success: true } | { success: false; field: string; validator: string; error: string };

import type { Ref, ComputedRef } from 'vue';

export interface CalendarState {
  date: Ref<Date, Date>;
  toLocalDateString: (dateObj: Date) => string;
  todaysDate: Ref<Date, Date>;
  currentYear: Ref<number>;
  currentMonth: Ref<number>;
  currentDay: Ref<number>;
  currentWeekDay: Ref<string>;
  currentWeekDays: ComputedRef<{ value: number; type: string }[]>;
  containsPrevMonthDays: Ref<boolean>;
  containsNextMonthDays: Ref<boolean>;
  weekMonthOverLapString: ComputedRef<string>;
  currentTimeTopPixelValue: Ref<number>;
  daysInMonth: (year: number, month: number) => number;
  isLeapYear: ComputedRef<boolean>;
  daysInCurrentMonth: ComputedRef<number>;
  changeDate: (direction: 'prev' | 'next', isWeek: boolean, selectedCalendarRange: string) => void;
  applyTodaysDate: () => void;
  firstWeekDayOfMonth: ComputedRef<string>;
  lastWeekDayOfMonth: ComputedRef<string>;
  daysOfPreviousMonth: ComputedRef<number[]>;
  daysOfNextMonth: ComputedRef<number[]>;
  totalDaysForCurrentMonth: ComputedRef<{ value: number; type: string }[]>;
  fetchAppointmentsForMonthRange: () => Promise<void>;
  isEndBeforeStart: (startDate: string, startTime: string, endDate: string, endTime: string) => boolean;
  getAppointmentsForDay: (
    day: { value: number; type: string },
    month: number,
    year: number,
  ) => {
    appointments: AppointmentFormData[];
    length: number;
  };
  isPast: (day: number, hour: number, slot: number) => boolean;
}

export enum AppointmentStatus {
  SCHEDULED = 'Scheduled',
  CANCELLED = 'Cancelled',
  COMPLETED = 'Completed',
}

export enum AppointmentType {
  CONSULTATION = 'Consultation',
  FOLLOW_UP = 'Follow up',
  EMERGENCY = 'Emergency',
  ROUTINE_CHECKUP = 'Check up',
}

export interface AppointmentFormData {
  id: string;
  patientId: string;
  patientName: string;
  doctorId: string;
  doctorName: string;
  departmentId: string | undefined;
  appointmentDate: string;
  appointmentEndDate: string;
  appointmentTime: string;
  appointmentEndTime: string;
  appointmentStatus: string;
  appointmentType: string;
  reason: string;
}

export interface Column {
  key: string;
  label: string;
  class?: string; // css
}

export const departmentMap: Record<string, string> = {
  Orthopedics: '7f41661a-f57b-4e24-9ffe-65a25059c4e0',
  Radiology: 'fa272fa1-f2f9-4bdf-a485-f4dad0117879',
  Oncology: 'b457c18f-decd-473c-b842-813babc69645',
  Dermatology: '329c8097-82cc-425f-b42f-753d56bfabcd',
  Surgery: '71a69f03-7c80-46cf-a26d-c3052d0e3583',
  Pulmonology: 'e715266f-bb9d-4ce9-bf14-dd4c43a5cf56',
  Cardiology: 'e6d8c94f-1ee3-485a-8bd1-a5715741ac66',
  Gynecology: '805d7a26-1b34-4448-90d1-2b65dc0192b4',
  Emergency: 'ee8caf04-0d3f-4354-88e1-112cc63e99bd',
  Endocrinology: 'f6da2313-7e09-4d22-8617-4f1d1341a588',
  Pediatrics: '0bf7d11d-b39f-48fc-b711-d218a9fb0426',
  Ophthalmology: '4a33abcf-d513-4a95-8e54-a727f1cd0c47',
  Pathology: '5b9e5c85-dcc4-4bc9-9e7b-57a6d502b6ad',
  Nephrology: 'c1dd63c1-2342-47c8-b995-d44e4f27d8f4',
  Neurology: '8b646fc9-752d-4a74-a767-ec7c65eca626',
  Urology: '8cb70f15-cb7a-4fad-a4e5-7529ea287a44',
  Gastroenterology: '871ee1ff-9c31-48e9-a15c-42d2585c43f8',
};
