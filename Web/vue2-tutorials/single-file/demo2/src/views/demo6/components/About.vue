<template>
  <div>
    <h2>About内容</h2>
  </div>
</template>

<script>
export default {
  name: "AboutPage",
  beforeDestroy() {
    console.log("AboutPage销毁");
  },
  mounted() {
    console.log("AboutPage挂载");
  },
};
</script>
