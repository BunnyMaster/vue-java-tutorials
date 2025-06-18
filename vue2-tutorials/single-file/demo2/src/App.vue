<template>
  <div id="app">
    <Demo1 />
    <hr />
    <Demo2 />
  </div>
</template>

<script>
import Demo1 from "@/views/demo1/index.vue";
import Demo2 from "@/views/demo2/index.vue";

export default {
  name: "App",
  components: { Demo1, Demo2 },
};
</script>
