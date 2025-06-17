<script setup lang="tsx">
import { ref } from 'vue';

defineProps({
  peiqi: Number,
  modelValue: Number,
  timestamp: String,
});

const emit = defineEmits<{
  (e: 'update:modelValue', value: number): void;
  (e: 'update:peiqi123', value: number): void;
}>();

// 定义子组件的值，传给父组件
const childCount = ref(99);

// 定义子组件的值，传给父组件 --- 2
const childCount2 = ref(888);

/* 修改值 */
const onClick = () => {
  childCount.value++;
  childCount2.value++;

  // 第一种，子传父
  emit('update:modelValue', childCount.value);
  // 第二种，子传父
  emit('update:peiqi123', childCount2.value);
};
</script>

<template>
  <div class="about">
    <h1>子页面</h1>

    <p>childCount：{{ childCount }}</p>
    <p>childCount2：{{ childCount2 }}</p>
    <p @click="onClick">时间戳：{{ timestamp }}</p>
  </div>
</template>

<style>
@media (min-width: 1024px) {
  .about {
    min-height: 50vh;
    align-items: center;
  }
}

h1 {
  display: block;
  width: 100%;
}

p {
  font-weight: 800;
  font-size: 24px;
  color: purple;
}
</style>
