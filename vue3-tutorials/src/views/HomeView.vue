<script setup lang="ts">
import { onMounted, ref } from 'vue';

import AboutView from '@/views/AboutView.vue';

// 父传子
const timestamp = ref(new Date().getTime() + '');

// 父组件的count
const parentCount = ref(0);
const parentCount2 = ref(444);

onMounted(() => {
  setInterval(() => {
    timestamp.value = new Date().getTime() + '';
  }, 1000);
});
</script>

<template>
  <main>
    <div class="parent">
      <h1>父页面</h1>

      <p>parentCount：{{ parentCount }}</p>
      <p>parentCount2：{{ parentCount2 }}</p>
    </div>

    <AboutView :timestamp="timestamp" v-model="parentCount" v-model:peiqi123="parentCount2" />
  </main>
</template>

<style scoped lang="css">
main {
  width: 100%;
  height: 100%;
  display: flex;
  flex-direction: column;
}

p {
  color: yellowgreen;
}

.parent {
  min-height: 50vh;
}
</style>
