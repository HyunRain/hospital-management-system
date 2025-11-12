<script setup lang="ts">
import { useToggleStore } from '@/stores/toggleStore';
import { usePatientStore } from '@/stores/patientStore';
import { ref, computed, reactive, watch } from 'vue';
import Select from './Select.vue';
import { bloodTypes, gender, maritalStatus } from '@/util/types/constants';

const toggleStore = useToggleStore();
const patientStore = usePatientStore();

const allergiesString = ref('');
const chronicDisString = ref('');
const medicationString = ref('');
const immuneString = ref('');
const surgHistString = ref('');

const patientFormData = reactive({
  firstName: "",
  lastName: "",
  gender: computed(() => patientStore.gender),
  dateOfBirth: "",
  bloodGroup: computed(() => patientStore.bloodType),
  maritalStatus: computed(() => patientStore.maritalStatus),
  phoneNumber: "",
  email: "",
  emergencyContactName: "",
  emergencyContactNumber: "",
  relationshipToEmergencyContact: "",
  addressLine1: "",
  addressLine2: "",
  city: "",
  state: "",
  country: "",
  postalCode: "",
  status: "ACTIVE",
  knownAllergies: [] as string[],
  pastMedicalHistory: [] as string[],
  chronicDiseases: [] as string[],
  currentMedications: [] as string[],
  immunizationStatus: [] as string[],
  surgicalHistory: [] as string[],
  insuranceProvider: "",
  insurancePolicyNumber: "",
  insuranceExpiryDate: ""
});

function parseCommaList(str: string): string[] {
  console.log(str);
  return str.split(',').map(s => s.trim()).filter(Boolean);
}

// Watchers
watch(allergiesString, (newVal) => {
  console.log("allergies changed");
  patientFormData.knownAllergies = parseCommaList(newVal);
});

watch(chronicDisString, (newVal) => {
  patientFormData.chronicDiseases = parseCommaList(newVal);
});

watch(medicationString, (newVal) => {
  patientFormData.currentMedications = parseCommaList(newVal);
});

watch(immuneString, (newVal) => {
  patientFormData.immunizationStatus = parseCommaList(newVal);
});

watch(surgHistString, (newVal) => {
  patientFormData.surgicalHistory = parseCommaList(newVal);
});


async function handleAddPatient() {
  console.log(patientFormData);
  await patientStore.addPatient(patientFormData);
  resetSelects();
  toggleStore.toggleAddPatientModel();
}


// Form Header Stuff
const isStep1 = computed(() => {
  return !!patientFormData.firstName &&
    !!patientFormData.lastName &&
    !!patientFormData.gender &&
    !!patientFormData.dateOfBirth &&
    !!patientFormData.email &&
    !!patientFormData.phoneNumber &&
    !!patientFormData.maritalStatus;
});

const isStep2 = computed(() => {
  return !!patientFormData.addressLine1 &&
    !!patientFormData.city &&
    !!patientFormData.postalCode &&
    !!patientFormData.state &&
    !!patientFormData.country;
});

const isStep3 = computed(() => {
  return !!patientFormData.bloodGroup;
});

const isStep4 = computed(() => {
  return !!patientFormData.bloodGroup &&
    !!patientFormData.insuranceProvider &&
    !!patientFormData.insurancePolicyNumber;
});

const stepsCompleted = computed(() => {
  return isStep1.value && isStep2.value && isStep3.value && isStep4.value;
});

function stepTextColor(step: number) {
  if (currentStep.value === step) {
    return toggleStore.darkModeState === 'darkMode' ? 'text-[#e7523b]' : 'text-red-600';
  } else {
    return toggleStore.darkModeState === 'darkMode' ? 'text-[#eeeeee]' : 'text-[#4c4c4c]';
  }
}

function resetSelects() {
  patientStore.bloodType = '';
  patientStore.gender = '';
  patientStore.maritalStatus = '';
}

// Form Footer Navigation Stuff
const currentStep = ref(1);
const totalSteps = 4;

function previousStep() {
  if (currentStep.value > 1) currentStep.value -= 1;
}

function nextStep() {
  if (currentStep.value < totalSteps) currentStep.value += 1;
}
</script>

<template>
  <!-- Overlay Container -->
  <div class="fixed inset-0 z-50 flex items-center justify-center bg-[#00000087] p-4">

    <!-- AddPatientModal -->
    <div v-click-outside="() => { toggleStore.toggleAddPatientModel(); resetSelects(); }" class="flex flex-col bg-white dark:bg-[#0a0a0a] p-5 border border-gray-300 dark:border-neutral-900 min-h-[90vh] max-h-[90vh]
      md:min-h-fit rounded-lg w-full max-w-4xl overflow-y-auto">
      <div class="flex justify-between items-center">
        <div class="flex gap-2 items-center">
          <h3 class="text-[18px]">+ Add Patient</h3>
        </div>
        <img class="size-4.5 cursor-pointer hover:size-5" @click="() => { toggleStore.toggleAddPatientModel(); resetSelects(); }"
          :src="`/assets/icons/${toggleStore.darkModeState}/close.svg`" alt="Close Icon">
      </div>



      <!-- Steps Window-->
      <div class="flex justify-evenly px-5 mt-9 md:py-3 py-6 dark:bg-[#000] overflow-x-auto gap-5 md:mx-15 rounded-lg border ">

        <!-- Header 1-->
        <div class="flex gap-2 items-center cursor-pointer" @click="currentStep = 1">
          <div class="border border-gray-300 size-6.5 dark:border-zinc-800 rounded-full p-2 flex items-center justify-center">
            <img v-if="isStep1" :src="`/assets/icons/${toggleStore.darkModeState}/checkmark.svg`" alt="PatientIcon" />
            <p v-else :class="[stepTextColor(1)]">1</p>
          </div>
          <p class="min-w-24" :class="[stepTextColor(1)]">
            Personal Data</p>
        </div>
        <div class="border-l"></div>

        <!-- Header 2-->
        <div class="flex gap-2 items-center cursor-pointer" @click="currentStep = 2">
          <div class="border border-gray-300 size-6.5 dark:border-zinc-800 rounded-full p-2 flex items-center justify-center">
            <img v-if="isStep2" :src="`/assets/icons/${toggleStore.darkModeState}/checkmark.svg`" alt="PatientIcon" />
            <p v-else :class="[stepTextColor(2)]">2</p>
          </div>
          <p :class="[stepTextColor(2)]">
            Address </p>
        </div>

        <div class="border-l"></div>

        <!-- Header 3-->
        <div class="flex gap-2 items-center cursor-pointer" @click="currentStep = 3">
          <div class="border border-gray-300 size-6.5 dark:border-zinc-800 rounded-full p-2 flex items-center justify-center">
            <img v-if="isStep3" :src="`/assets/icons/${toggleStore.darkModeState}/checkmark.svg`" alt="PatientIcon" />
            <p v-else :class="[stepTextColor(3)]">3</p>
          </div>
          <p :class="[stepTextColor(3)]">
            Medical</p>
        </div>

        <div class="border-l"></div>

        <!-- Header 4-->
        <div class="flex gap-2 items-center cursor-pointer" @click="currentStep = 4">
          <div class="border border-gray-300 size-6.5 dark:border-zinc-800 rounded-full p-2 flex items-center justify-center">
            <img v-if="isStep4" :src="`/assets/icons/${toggleStore.darkModeState}/checkmark.svg`" alt="PatientIcon" />
            <p v-else :class="[stepTextColor(4)]">4</p>
          </div>
          <p class="min-w-55" :class="[stepTextColor(4)]">
            Insurance & Emergency Contact </p>
        </div>


      </div>
      <form @submit.prevent="handleAddPatient" class="flex flex-col flex-1">
        <!-- Form Body-->
        <div class="md:mx-30 md:max-h-[70vh] md:min-h-[70vh] 3xl:max-h-[40vh] 3xl:min-h-[40vh]">

          <!-- Step 1 Basic Patient Information -->
          <div v-show="currentStep === 1" class="flex flex-col gap-4 mt-10 mb-10">
            <p class="ml-1 text-[16px] mb-1">Personal Data</p>
            <div>
              <label class="ml-1" for="fName">First Name</label>
              <input class="input mt-2" id="fName" type="text" v-model="patientFormData.firstName">
            </div>
            <div>
              <label class="ml-1" for="lName">Last Name</label>
              <input class="input mt-2" id="lName" type="text" v-model="patientFormData.lastName">
            </div>
            <div class="flex gap-4 items-center flex-wrap">
              <div class="w-1/3">
                <label class="ml-1" for="gender">Gender</label>
                <Select :data="gender" for-view="patient" type="gender" />
              </div>
              <div class="w-2/3">
                <div class="flex justify-between">
                  <label class="ml-1" for="birthdate">Date of Birth</label>
                  <p class="text-[#898989] opacity-80 mr-1 text-[13px]">YYYY-MM-DD</p>
                </div>
                <input class="input mt-2" id="birthdate" type="text" v-model="patientFormData.dateOfBirth">
              </div>
            </div>
            <div>
              <label class="ml-1" for="email">Email</label>
              <input class="input mt-2" id="email" type="email" v-model="patientFormData.email">
            </div>
            <div>
              <label class="ml-1" for="phoneNumber">Phone Number</label>
              <input class="input mt-2" id="phoneNumber" type="text" v-model="patientFormData.phoneNumber">
            </div>
            <div class="w-1/3">
              <label class="ml-1" for="maritalStatus">Marital Status</label>
              <Select id="maritalStatus" for-view="patient" :data="maritalStatus" type="marital status" />
            </div>
          </div>

          <!-- Step 2 Address & Emergency Contact -->
          <div v-show="currentStep === 2" class="flex flex-col gap-4 mt-10 mb-10">
            <p class="ml-1 text-[16px] mb-1">Address</p>
            <div>
              <label class="ml-1" for="address1">Address 1</label>
              <input class="input mt-2" id="address1" type="text" v-model="patientFormData.addressLine1">
            </div>
            <div>
              <div class="flex justify-between">
                <label class="ml-1" for="address2">Address 2</label>
                <p class="text-[#898989] opacity-80 mr-1 text-[13px]">Optional</p>
              </div>
              <input class="input mt-2" id="address2" type="text" v-model="patientFormData.addressLine2">
            </div>
            <div>
              <label class="ml-1" for="city">City</label>
              <input class="input mt-2" id="city" type="text" v-model="patientFormData.city">
            </div>
            <div>
              <label class="ml-1" for="postalCode">Postal Code</label>
              <input class="input mt-2" id="postalCode" type="text" v-model="patientFormData.postalCode">
            </div>
            <div>
              <label class="ml-1" for="state">State</label>
              <input class="input mt-2" id="state" type="text" v-model="patientFormData.state">
            </div>
            <div>
              <label class="ml-1" for="country">Country</label>
              <input class="input mt-2" id="country" type="text" v-model="patientFormData.country">
            </div>
          </div>

          <!-- Step 3 Medical History and Insurance -->
          <div v-show="currentStep === 3" class="flex flex-col gap-4 mt-10 mb-10">
            <p class="ml-1 text-[16px] mb-1">Medical</p>
            <div class="md:w-1/3 w-1/2">
              <label class="ml-1" for="Blood Type">Blood Type</label>
              <Select id="Blood Type" for-view="patient" :data="bloodTypes" type="blood type" />
            </div>
            <div>
              <div class="flex justify-between">
                <label class="ml-1" for="allergies">Known Allergies</label>
                <p class="text-[#898989] opacity-80 mr-1 text-[13px]">Optional</p>
              </div>
              <input class="input mt-2" id="allergies" type="text" v-model="allergiesString">
              <p class="text-[#898989] text-[12px] mt-1 ml-1">
                List all known allergies, separated by commas (e.g. penicillin, peanuts, pollen).
              </p>

            </div>
            <div>
              <div class="flex justify-between">
                <label class="ml-1" for="chronicDiseases">Chronic Diseases</label>
                <p class="text-[#898989] opacity-80 mr-1 text-[13px]">Optional</p>
              </div>
              <input class="input mt-2" id="chronicDiseases" type="text" v-model="chronicDisString">
              <p class="text-[#898989] text-[12px] mt-1 ml-1">
                Enter any chronic conditions, separated by commas (e.g. asthma, diabetes).
              </p>
            </div>
            <div>
              <div class="flex justify-between">
                <label class="ml-1" for="currentMedications">Current Medications</label>
                <p class="text-[#898989] opacity-80 mr-1 text-[13px]">Optional</p>
              </div>
              <input class="input mt-2" id="currentMedications" type="text" v-model="medicationString">
              <p class="text-[#898989] text-[12px] mt-1 ml-1">
                Include all current medications, separated by commas (e.g. ibuprofen, insulin).
              </p>
            </div>
            <div>
              <div class="flex justify-between">
                <label class="ml-1" for="immunizations">Immunizations</label>
                <p class="text-[#898989] opacity-80 mr-1 text-[13px]">Optional</p>
              </div>
              <input class="input mt-2" id="immunizations" type="text" v-model="immuneString">
              <p class="text-[#898989] text-[12px] mt-1 ml-1">
                List received immunizations, separated by commas (e.g. tetanus, hepatitis B).
              </p>
            </div>
            <div>
              <div class="flex justify-between">
                <label class="ml-1" for="surgicalHistory">Surgical History</label>
                <p class="text-[#898989] opacity-80 mr-1 text-[13px]">Optional</p>
              </div>
              <input class="input mt-2" id="surgicalHistory" type="text" v-model="surgHistString">
              <p class="text-[#898989] text-[12px] mt-1 ml-1">
                Note any past surgeries, separated by commas (e.g. appendectomy, knee replacement).
              </p>
            </div>
          </div>

          <!-- Step 4 Insurance & Emergency Contact -->
          <div v-show="currentStep === 4" class="flex flex-col gap-4 mt-10 mb-10">
            <p class="ml-1 text-[16px] mb-1">Insurance</p>
            <div>
              <label class="ml-1" for="insuranceProvider">Provider</label>
              <input class="input mt-2" id="insuranceProvider" type="text" v-model="patientFormData.insuranceProvider">
            </div>
            <div>
              <label class="ml-1" for="insurancePolicyNumber">Policy Number</label>
              <input class="input mt-2" id="insurancePolicyNumber" type="text" v-model="patientFormData.insurancePolicyNumber">
            </div>
            <div>
              <label class="ml-1" for="insuranceExpiryDate">Expiry Date</label>
              <input class="input mt-2" id="insuranceExpiryDate" type="text" v-model="patientFormData.insuranceExpiryDate">
            </div>
            <p class="ml-1 text-[16px] my-1">Emergency Contact</p>
            <div>
              <div class="flex justify-between">
                <label class="ml-1" for="emergencyContactName">Name</label>
                <p class="text-[#898989] opacity-80 mr-1 text-[13px]">Optional</p>
              </div>
              <input class="input mt-2" id="emergencyContactName" type="text" v-model="patientFormData.emergencyContactName">
            </div>
            <div>
              <div class="flex justify-between">
                <label class="ml-1" for="emergencyContactNumber">Phone Number</label>
                <p class="text-[#898989] opacity-80 mr-1 text-[13px]">Optional</p>
              </div>
              <input class="input mt-2" id="emergencyContactNumber" type="text" v-model="patientFormData.emergencyContactNumber">
            </div>
            <div>
              <div class="flex justify-between">
                <label class="ml-1" for="relationshipToEmergencyContact">Relationship</label>
                <p class="text-[#898989] opacity-80 mr-1 text-[13px]">Optional</p>
              </div>
              <input class="input mt-2" id="relationshipToEmergencyContact" type="text" v-model="patientFormData.relationshipToEmergencyContact">
            </div>
          </div>


        </div>

        <!-- Form Footer -->
        <div class="flex items-center">
          <button class="form-button" type="button" @click="previousStep" v-show="currentStep > 1">Back</button>
          <div class="flex ml-auto">
            <button class="form-button" type="button" @click="nextStep" v-show="currentStep < totalSteps">Next</button>
            <button :disabled="!stepsCompleted" class="form-button" type="submit" v-if="currentStep === totalSteps">Add</button>
          </div>
        </div>

      </form>

    </div>

  </div>
</template>

<style scoped></style>
