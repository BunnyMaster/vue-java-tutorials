<template>
  <div>
    <h1>当前求和为:{{ $store.state.count.sum }}</h1>
    <h1>当前求和为:{{ $store.getters["count/bigSum"] }}</h1>
    <select v-model.number="$store.state.count.num">
      <option :value="1">1</option>
      <option :value="2">2</option>
      <option :value="3">3</option>
    </select>

    <button @click="increment">+</button>
    <button @click="decrement">-</button>
    <button @click="incrementOdd">当前求和为奇数再加</button>
    <button @click="incrementWait">等一等再加</button>
  </div>
</template>

<script>
export default {
  name: "CountInfo",
  data() {
    return {};
  },
  methods: {
    increment() {
      this.$store.commit("count/INCREMENT");
    },
    decrement() {
      this.$store.commit("count/DECREMENT");
    },
    incrementOdd() {
      this.$store.dispatch("count/incrementOdd");
    },
    incrementWait() {
      this.$store.dispatch("count/incrementWait");
    },
  },
};
</script>

<style>
button {
  margin: 0 4px;
}
</style>
