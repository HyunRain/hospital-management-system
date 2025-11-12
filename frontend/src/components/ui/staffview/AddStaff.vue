<script setup lang="ts">
import { useToggleStore } from '@/stores/toggleStore';
import { ref, computed, reactive } from 'vue';
import Select from '@/components/ui/patientview/Select.vue';
import { gender, staffTypes } from '@/util/types/constants';
import { useStaffStore } from '@/stores/staffStore';

const toggleStore = useToggleStore();
const staffStore = useStaffStore();

const staffFormData = reactive({
  password: "",
  repeatedPassword: "",
  role: computed(() => staffStore.role),
  firstName: "",
  lastName: "",
  gender: computed(() => staffStore.gender),
  dateOfBirth: "",
  phoneNumber: "",
  email: "",
  addressLine1: "",
  addressLine2: "",
  city: "",
  state: "",
  country: "",
  postalCode: "",
  departmentName: "NonMedical",
});


async function handleAddStaff() {
  //console.log(staffFormData);
  await staffStore.addStaff(staffFormData);
  resetSelects();
  toggleStore.toggleAddStaffModal();
}


// Form Header Stuff
const isStep1 = computed(() => {
  return !!staffFormData.password &&
    !!staffFormData.repeatedPassword &&
    !!staffFormData.email &&
    !!staffFormData.role;
});

const isStep2 = computed(() => {
  return !!staffFormData.phoneNumber &&
    !!staffFormData.firstName &&
    !!staffFormData.lastName &&
    !!staffFormData.gender &&
    !!staffFormData.dateOfBirth &&
    !!staffFormData.departmentName;
});

const isStep3 = computed(() => {
  return !!staffFormData.addressLine1 &&
    !!staffFormData.city &&
    !!staffFormData.postalCode &&
    !!staffFormData.state &&
    !!staffFormData.country;
});

const stepsCompleted = computed(() => {
  return isStep1.value && isStep2.value && isStep3.value;
});

function stepTextColor(step: number) {
  if (currentStep.value === step) {
    return toggleStore.darkModeState === 'darkMode' ? 'text-[#e7523b]' : 'text-red-600';
  } else {
    return toggleStore.darkModeState === 'darkMode' ? 'text-[#eeeeee]' : 'text-[#4c4c4c]';
  }
}

function resetSelects() {
  staffStore.gender = '';
  staffStore.role = '';
}

// Form Footer Navigation Stuff
const currentStep = ref(1);
const totalSteps = 3;

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
    <div v-click-outside="() => { toggleStore.toggleAddStaffModal(); resetSelects(); }" class="flex flex-col bg-white dark:bg-[#0a0a0a] p-5 border border-gray-300 dark:border-zinc-800 min-h-[90vh] max-h-[90vh]
      md:min-h-fit rounded-lg w-full max-w-4xl overflow-y-auto">
      <div class="flex justify-between items-center">
        <div class="flex gap-2 items-center">
          <h3 class="text-[18px]">+ Add Staff</h3>
        </div>
        <img class="size-4.5 cursor-pointer hover:size-5" @click="() => { toggleStore.toggleAddStaffModal(); resetSelects(); }"
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
            Account Data</p>
        </div>

        <div class="border-l"></div>

        <!-- Header 2-->
        <div class="flex gap-2 items-center cursor-pointer" @click="currentStep = 2">
          <div class="border border-gray-300 size-6.5 dark:border-zinc-800 rounded-full p-2 flex items-center justify-center">
            <img v-if="isStep2" :src="`/assets/icons/${toggleStore.darkModeState}/checkmark.svg`" alt="PatientIcon" />
            <p v-else :class="[stepTextColor(2)]">2</p>
          </div>
          <p :class="[stepTextColor(2)]">
            Personal Data </p>
        </div>

        <div class="border-l"></div>

        <!-- Header 3-->
        <div class="flex gap-2 items-center cursor-pointer" @click="currentStep = 3">
          <div class="border border-gray-300 size-6.5 dark:border-zinc-800 rounded-full p-2 flex items-center justify-center">
            <img v-if="isStep3" :src="`/assets/icons/${toggleStore.darkModeState}/checkmark.svg`" alt="PatientIcon" />
            <p v-else :class="[stepTextColor(3)]">2</p>
          </div>
          <p :class="[stepTextColor(3)]">
            Address </p>
        </div>



      </div>
      <form @submit.prevent="handleAddStaff" class="flex flex-col flex-1">
        <!-- Form Body-->
        <div class="md:mx-30 md:max-h-[60vh] md:min-h-[60vh] 3xl:max-h-[40vh] 3xl:min-h-[40vh]">

          <!-- Step 1 UserData Doctor Information -->
          <div v-show="currentStep === 1" class="flex flex-col gap-4 mt-10 mb-10">
            <p class="ml-1 text-[16px] mb-1">User Data</p>
            <div>
              <label class="ml-1" for="email">Email</label>
              <input class="input mt-2" id="email" type="text" v-model="staffFormData.email">
            </div>
            <div>
              <div class="flex justify-between">
                <label class="ml-1" for="password">Password</label>
                <p class="text-[#898989] opacity-80 mr-1 text-[13px]">Atleast 8 characters long</p>
              </div>
              <input class="input mt-2" id="password" type="text" v-model="staffFormData.password">
            </div>
            <div>
              <div class="flex justify-between">
                <label class="ml-1" for="repeatedPassword">Repeated Password</label>
                <p class="text-[#898989] opacity-80 mr-1 text-[13px]">Atleast 8 characters long</p>
              </div>
              <input class="input mt-2" id="repeatedPassword" type="text" v-model="staffFormData.repeatedPassword">
            </div>
            <div class="lg:w-1/3 w-1/2">
              <label class="ml-1" for="staffRole">Role</label>
              <Select id="staffRole" for-view="staff" :data="staffTypes" type="role" />
            </div>
          </div>

          <!-- Step 2 Basic Staff Information -->
          <div v-show="currentStep === 2" class="flex flex-col gap-4 mt-10 mb-10">
            <p class="ml-1 text-[16px] mb-1">Personal Data</p>

            <div class="flex w-full gap-5">
              <div class="w-1/2">
                <label class="ml-1" for="fName">First Name</label>
                <input class="input mt-2" id="fName" type="text" v-model="staffFormData.firstName">
              </div>
              <div class="w-1/2">
                <label class="ml-1" for="lName">Last Name</label>
                <input class="input mt-2" id="lName" type="text" v-model="staffFormData.lastName">
              </div>
            </div>
            <div class="flex gap-4 items-center flex-wrap">
              <div class="w-1/3">
                <label class="ml-1" for="gender">Gender</label>
                <Select :data="gender" for-view="staff" type="gender" />
              </div>
              <div class="w-2/3">
                <div class="flex justify-between">
                  <label class="ml-1" for="birthdate">Date of Birth</label>
                  <p class="text-[#898989] opacity-80 mr-1 text-[13px]">YYYY-MM-DD</p>
                </div>
                <input class="input mt-2" id="birthdate" type="text" v-model="staffFormData.dateOfBirth">
              </div>
            </div>
            <div>
              <label class="ml-1" for="phoneNumber">Phone Number</label>
              <input class="input mt-2" id="phoneNumber" type="text" v-model="staffFormData.phoneNumber">
            </div>
            <div class="lg:w-1/3 w-1/2">
              <label class="ml-1" for="departmentName">Role</label>
              <input id="departmentName" type="text" class="input mt-2" v-model="staffFormData.departmentName" disabled />
            </div>
          </div>

          <!-- Step 3 Address -->
          <div v-show="currentStep === 3" class="flex flex-col gap-4 mt-10 mb-10">
            <p class="ml-1 text-[16px] mb-1">Address</p>
            <div>
              <label class="ml-1" for="address1">Address 1</label>
              <input class="input mt-2" id="address1" type="text" v-model="staffFormData.addressLine1">
            </div>
            <div>
              <div class="flex justify-between">
                <label class="ml-1" for="address2">Address 2</label>
                <p class="text-[#898989] opacity-80 mr-1 text-[13px]">Optional</p>
              </div>
              <input class="input mt-2" id="address2" type="text">
            </div>
            <div>
              <label class="ml-1" for="city">City</label>
              <input class="input mt-2" id="city" type="text" v-model="staffFormData.city">
            </div>
            <div>
              <label class="ml-1" for="postalCode">Postal Code</label>
              <input class="input mt-2" id="postalCode" type="text" v-model="staffFormData.postalCode">
            </div>
            <div>
              <label class="ml-1" for="state">State</label>
              <input class="input mt-2" id="state" type="text" v-model="staffFormData.state">
            </div>
            <div>
              <label class="ml-1" for="country">Country</label>
              <input class="input mt-2" id="country" type="text" v-model="staffFormData.country">
            </div>
          </div>
        </div>

        <!-- Form Footer -->
        <div class="flex items-center">
          <button class="form-button" type="button" @click="previousStep" v-show="currentStep > 1">Back</button>
          <div class="flex ml-auto">
            <button class="form-button" type="button" @click="nextStep" v-show="currentStep < totalSteps">Next</button>
            <button :disabled="!stepsCompleted" class="form-button w-[65.32px]" type="submit" v-if="currentStep === totalSteps">Add</button>
          </div>
        </div>

      </form>

    </div>

  </div>
</template>
