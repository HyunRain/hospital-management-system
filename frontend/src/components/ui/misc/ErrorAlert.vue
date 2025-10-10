import { required } from '../../../util/functions/validation/rules';
<script setup lang="ts">

const props = defineProps({
  alertKey: {
    type: Number,
    required: true,
  },
  show: {
    type: Boolean,
    required: true,
  },
  message: {
    type: String,
    required: true,
  }
})
</script>

<template>
  <transition enter-active-class="transition-transform transition-opacity duration-200 ease-out" enter-from-class="-translate-x-10 opacity-0"
    enter-to-class="translate-x-0 opacity-100">
    <p v-if="show" :key="alertKey" class=" text-[#e7523b]"> {{ message }}
    </p>
  </transition>
</template>

<style scoped>

</style>
