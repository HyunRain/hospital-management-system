<script setup lang="ts">

</script>

<template>
  <div class="flex min-h-[calc(100vh-123px)] px-10 py-10">
    <div class="rounded-lg max-w-[220px] w-full text-start flex flex-col">
      <ul class="flex flex-col gap-4 items-start">
        <li>Dashboard</li>
        <li>Users</li>
        <li>Settings</li>
      </ul>
    </div>
    <RouterView/>
  </div>
</template>

<style scoped>

</style>
