<template>
  <div>
    <h3>Home内容</h3>
  </div>
</template>

<script>
export default {
  name: "HomePage",
  beforeDestroy() {
    console.log("HomePage销毁");
  },
  mounted() {
    console.log("HomePage挂载");
  },
};
</script>
