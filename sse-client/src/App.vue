<template>
  <div>

  </div>
</template>

<script setup>
import { onMounted } from 'vue';

onMounted(async () => {
  const response = await fetch("http://localhost:7777/posts");
  const data = await response.json();
  console.log(data);
})
</script>

<style scoped>

</style>