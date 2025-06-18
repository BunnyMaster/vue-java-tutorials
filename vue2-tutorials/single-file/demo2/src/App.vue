<template>
  <div id="app">
    <Demo1 />
    <hr />
    <Demo2 />
    <hr />
    <Demo3 />
  </div>
</template>

<script>
import Demo1 from "@/views/demo1/index.vue";
import Demo2 from "@/views/demo2/index.vue";
import Demo3 from "@/views/demo3/index.vue";

export default {
  name: "App",
  components: { Demo1, Demo2, Demo3 },
};
</script>
