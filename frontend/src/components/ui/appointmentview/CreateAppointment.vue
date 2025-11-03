<script setup lang="ts">
import { useAppointmentStore } from '@/stores/appointmentStore';
import { useToggleStore } from '@/stores/toggleStore';
import { computed, onUnmounted, reactive, ref, watch } from 'vue';
import { type AppointmentFormData, AppointmentType } from '@/util/types/types';
import FormSteps from '../form/FormSteps.vue';
import FormSearchInput from '../form/FormSearchInput.vue';
import FormInput from '../form/FormInput.vue';
import FormHeader from '../form/FormHeader.vue';
import Selection from '../misc/Selection.vue';
import FormInputArea from '../form/FormInputArea.vue';
import { timeSlots } from '@/util/types/constants';
import { useEnumByKeyByValue } from '@/composables/useEnumKeyByValue';
import { useDepartmentStore } from '@/stores/departmentStore';
import SearchDialog from '../form/SearchDialog.vue';
import { usePatientStore } from '@/stores/patientStore';
import { useDoctorStore } from '@/stores/doctorStore';
import debounce from 'lodash.debounce';
import SearchResults from '../form/SearchResults.vue';
import { useTableSelectStore } from '@/stores/tableSelectStore';

const toggleStore = useToggleStore();
const appointmentStore = useAppointmentStore();
const departmentStore = useDepartmentStore();
const patientStore = usePatientStore();
const doctorStore = useDoctorStore();
const tableSelectStore = useTableSelectStore();

const props = defineProps({
  currentDepartment: {
    type: String,
    required: true,
  }
});

onUnmounted(() => {
  patientStore.patients = [];
  doctorStore.doctors = [];
});

// Form related

async function createAppointment() {
  await appointmentStore.createAppointment(appointmentForm);
  toggleStore.toggleAppointmentForm();
}

const appointmentForm = reactive<AppointmentFormData>({
  patientId: "",
  doctorId: "",
  departmentId: "",
  appointmentDate: "",
  appointmentTime: "00:15",
  appointmentEndDate: "",
  appointmentEndTime: "00:30",
  appointmentStatus: "SCHEDULED",
  appointmentType: "",
  reason: "",
});

const allAppointmentTypesValues = Object.values(AppointmentType);
const currentAppointmentTypeValueAsKey = ref(AppointmentType.CONSULTATION);
const currentDepartmentName = ref(props.currentDepartment);
const { getEnumKeyByValue } = useEnumByKeyByValue();

watch(currentAppointmentTypeValueAsKey, (newLabel) => {
  appointmentForm.appointmentType = getEnumKeyByValue(AppointmentType, newLabel) ?? "";
}, { immediate: true });

watch(currentDepartmentName, (newDepartmentName) => {
  appointmentForm.departmentId = departmentStore.departments.find((department) => department.name === newDepartmentName)?.id;
}, { immediate: true });

const isStepFilled = [
  computed(() => !!appointmentForm.patientId && !!appointmentForm.doctorId && !!appointmentForm.departmentId
    && !!appointmentForm.appointmentDate && !!appointmentForm.appointmentStatus && !!appointmentForm.appointmentTime
    && !!appointmentForm.appointmentType && !!appointmentForm.reason)
];

//Form: Patient / Doctor Search

async function handlePatientSearch(newInput: string) {
  debouncedSearch(newInput, true);
}

async function handleDoctorSearch(newInput: string) {
  debouncedSearch(newInput, false);
}

const debouncedSearch = debounce(async (input: string, isPatientSearch: boolean) => {
  if (input.length > 1) {
    if (isPatientSearch) {
      await patientStore.searchPatients(input, 0, 10);
    } else {
      await doctorStore.searchDoctors(input, 0, 10, true);
    }
  } else {
  }
}, 300); // delay in ms

const patientColumns = [
  { key: 'fullName', label: 'Name', class: 'border-0 rounded-l-lg' },
  { key: 'patientId', label: 'Patient ID', class: '' },
  { key: 'email', label: 'Email', class: 'border-0 rounded-r-lg' },
];
const doctorColumns = [
  { key: 'fullName', label: 'Name', class: 'border-0 rounded-l-lg' },
  { key: 'staffId', label: 'Doctor ID', class: '' },
  { key: 'email', label: 'Email', class: 'border-0 rounded-r-lg' },
];


const currentPatientName = ref('');
const currentDoctorName = ref('');

watch(() => tableSelectStore.selectedIdx, (newIdx) => {
  switch (tableSelectStore.selectedTable) {
    case "patient":
      const { patientId, fullName: fullPName } = patientStore.patients[newIdx];
      patientId && (appointmentForm.patientId = patientId);
      fullPName && (currentPatientName.value = fullPName);
      toggleStore.toggleSearchPatientDialog();
      break;
    case "doctor":
      const { staffId, fullName: fullDName } = doctorStore.doctors[newIdx];
      staffId && (appointmentForm.doctorId = staffId);
      fullDName && (currentDoctorName.value = fullDName);
      toggleStore.toggleSearchDoctorDialog();
      break;
  }
});
</script>

<template>
  <!-- Overlay -->
  <div class="formOverlay">
    <!-- Form -->
    <form @submit.stop.prevent="createAppointment()" v-click-outside="() => toggleStore.toggleAppointmentForm()" class="formFrame">
      <FormHeader header-text="+ Create Appointment" @close="toggleStore.toggleAppointmentForm()"></FormHeader>
      <FormSteps :steps-amount="1" :step-names="['Appointment']" :is-step-filled-array="isStepFilled"></FormSteps>

      <div class="flex flex-col gap-4 py-8 mx-4 lg:mx-8">
        {{ appointmentForm }}

        <FormSearchInput v-model="currentPatientName" label-name="Patient" @toggle="toggleStore.toggleSearchPatientDialog"></FormSearchInput>
        <SearchDialog v-if="toggleStore.showSearchPatientDialog" @toggle="toggleStore.toggleSearchPatientDialog" @search="handlePatientSearch"
          placeholder="Search Patient">
          <template #results>
            <SearchResults :results="patientStore.patients" :columns="patientColumns" table-name="patient"></SearchResults>
          </template>
        </SearchDialog>
        <FormSearchInput v-model="currentDoctorName" label-name="Doctor" @toggle="toggleStore.toggleSearchDoctorDialog"></FormSearchInput>
        <SearchDialog v-if="toggleStore.showSearchDoctorDialog" @toggle="toggleStore.toggleSearchDoctorDialog" @search="handleDoctorSearch"
          placeholder="Search Doctor">
          <template #results>
            <SearchResults :results="doctorStore.doctors" :columns="doctorColumns" table-name="doctor"></SearchResults>
          </template>
        </SearchDialog>

        <div class="flex justify-between gap-4">
          <Selection class="w-3/5" :data="allAppointmentTypesValues" v-model="currentAppointmentTypeValueAsKey"
            :is-open="toggleStore.showAppointmentTypeSelection" :is-form-input="true" label-name="Appointment Type"
            @toggle="toggleStore.toggleAppointmentTypeSelection" min-width="140px">
            <template v-slot:Mobile>
              <span class="inline lg:hidden"> {{ currentAppointmentTypeValueAsKey }} </span>
            </template>
          </Selection>

          <FormInput class="w-3/5" label-name="Department" v-model="currentDepartmentName" :is-readonly="true"></FormInput>
        </div>

        <div class="flex justify-between gap-4">
          <FormInput class="w-4/5" v-model="appointmentForm.appointmentDate" label-name="Start Date"></FormInput>

          <Selection :data="timeSlots" v-model="appointmentForm.appointmentTime" :is-open="toggleStore.showAppointmentTimeSelection"
            :is-form-input="true" label-name="Start Time" @toggle="toggleStore.toggleAppointmentTimeSelection" min-width="100px">
            <template v-slot:Mobile>
              <span class="inline lg:hidden"> {{ appointmentForm.appointmentTime }} </span>
            </template>
          </Selection>
        </div>

        <div class="flex justify-between gap-4">
          <FormInput class="w-4/5" v-model="appointmentForm.appointmentEndDate" label-name="End Date"></FormInput>

          <Selection :data="timeSlots" v-model="appointmentForm.appointmentEndTime" :is-open="toggleStore.showAppointmentEndTimeSelection"
            :is-form-input="true" label-name="End Time" @toggle="toggleStore.toggleAppointmentEndTimeSelection" min-width="100px">
            <template v-slot:Mobile>
              <span class="inline lg:hidden"> {{ appointmentForm.appointmentEndTime }} </span>
            </template>
          </Selection>
        </div>

        <FormInputArea v-model="appointmentForm.reason" label-name="Reason"></FormInputArea>
      </div>

      <div class="flex justify-between items-center">
        <button @click="toggleStore.toggleAppointmentForm()" class="form-button" type="button">Cancel</button>
        <button class="form-button" type="submit" :disabled="!isStepFilled[0].value">Create</button>
      </div>
    </form>
  </div>
</template>
