<script setup lang="ts">
const prop = defineProps({
  message: {
    type: String,
    required: true
  }
})
</script>

<template>
  <section class="flex items-center justify-center w-full h-full text-center p-5">
    <p class="text-lg font-semibold">
      {{ message }}
    </p>
  </section>
</template>

