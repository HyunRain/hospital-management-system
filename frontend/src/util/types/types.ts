export interface User {
  userId: string; // UUID as string
  firstName: string;
  lastName: string;
  gender: 'MALE' | 'FEMALE' | 'OTHER';
  dateOfBirth: string; // LocalDate as string
  phoneNumber: string;
  email: string;
  addressLine1: string;
  addressLine2: string | null;
  city: string;
  state: string;
  country: string;
  postalCode: string;
  departmentId: string; // UUID as string

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
  insuranceExpiryDate?: string; //
};

export interface DoctorDto {
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
  departmentName: string; // UUID as string
};

export interface DepartmentDto {
  name: string;
  headOfDepartmentName: string;
  headOfDepartmentId: string; // UUID as string
  staffCount: number;
  bedCapacity: number;
  currentBedCount: number;
  isActive: boolean;
}

export type ValidationResult = { success: true } | { success: false; field: string; validator: string; error: string };
