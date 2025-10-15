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
  bloodGroup: 'A_POSITIVE' | 'A_NEGATIVE' | 'B_POSITIVE' | 'B_NEGATIVE' | 'AB_POSITIVE' | 'AB_NEGATIVE' | 'O_POSITIVE' | 'O_NEGATIVE';
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
};

export interface StaffDto {
  userId: string;
  staffId: string;
  firstName: string;
  lastName: string;
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
};

export interface DepartmentDto {
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

import type { Ref, ComputedRef } from "vue";

export interface CalendarState {
  date: Ref<Date, Date>;
  currentYear: Ref<number>;
  currentMonth: Ref<number>;
  currentDay: Ref<number>;
  isLeapYear: ComputedRef<boolean>
  daysInCurrentMonth: ComputedRef<number>;
  nextMonth: () => void;
  previousMonth: () => void;
  applyTodaysDate: () => void;
  firstWeekDayOfMonth: ComputedRef<string>;
  daysOfPreviousMonth: ComputedRef<number[]>;
}
