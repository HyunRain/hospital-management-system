export const bloodTypes = [
  { label: 'A+', value: 'A_POSITIVE' },
  { label: 'A-', value: 'A_NEGATIVE' },
  { label: 'B+', value: 'B_POSITIVE' },
  { label: 'B-', value: 'B_NEGATIVE' },
  { label: 'O+', value: 'O_POSITIVE' },
  { label: 'O-', value: 'O_NEGATIVE' },
  { label: 'AB+', value: 'AB_POSITIVE' },
  { label: 'AB-', value: 'AB_NEGATIVE' },
];

export const gender = [
  { label: 'Male', value: 'MALE' },
  { label: 'Female', value: 'FEMALE' },
  { label: 'Other', value: 'OTHER' },
];

export const maritalStatus = [
  { label: 'Single', value: 'SINGLE' },
  { label: 'Married', value: 'MARRIED' },
];

export const staffTypes = [
  { label: 'Nurse', value: 'NURSE' },
  { label: 'Receptionist', value: 'RECEPTIONIST' },
  { label: 'Pharmacist', value: 'PHARMACIST' },
  { label: 'Radiologist', value: 'RADIOLOGIST' },
  { label: 'Security', value: 'SECURITY' },
  { label: 'Accountant', value: 'ACCOUNTANT' },
  { label: 'Dietician', value: 'DIETICIAN' },
  { label: 'Anesthesiologist', value: 'ANESTHESIOLOGIST' },
  { label: 'Cleaning Staff', value: 'CLEANING_STAFF' },
  { label: 'Physiotherapist', value: 'PHYSIOTHERAPIST' },
  { label: 'Lab Technician', value: 'LAB_TECHNICIAN' },
  { label: 'Surgeon', value: 'SURGEON' },
];

export const billingItemTypes = [{ label: 'Nurse' }];

export const days = ['Sun', 'Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat'];

export const months = [
  'January',
  'February',
  'March',
  'April',
  'May',
  'June',
  'July',
  'August',
  'September',
  'October',
  'November',
  'December',
];

export const shortMonths = [
  'Jan',
  'Feb',
  'Mar',
  'Apr',
  'May',
  'Jun',
  'Jul',
  'Aug',
  'Sep',
  'Oct',
  'Nov',
  'Dec',
];

export const departments = [
  'Pediatrics',
  'Dermatology',
  'Ophthalmology',
  'Pathology',
  'Surgery',
  'Orthopedics',
  'Gynecology',
  'Gastroenterology',
  'Neurology',
  'Urology',
  'Oncology',
  'Nephrology',
  'Cardiology',
  'Pulmonology',
  'Emergency',
  'Endocrinology',
  'Radiology',
];

export const calendarRanges = ['Month', 'Week', 'Day'];

export const slotTime: Record<number, number> = {
  1: 15,
  2: 30,
  3: 45,
};

export const hours = 24;
export const slots = 4;

export const timeSlots: string[] = Array.from({ length: 24 * 4 }, (_, i) => {
  const hours = Math.floor(i / 4).toString().padStart(2, '0');
  const minutes = ((i % 4) * 15).toString().padStart(2, '0');
  return `${hours}:${minutes}`;
});
