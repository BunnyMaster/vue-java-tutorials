<template>
  <div class="demo">
    <h2>学校名称：{{ name }}</h2>
    <h2>学校地址：{{ address }}</h2>
  </div>
</template>

<script>
export default {
  name: "SchoolInfo",
  data() {
    return {
      name: "Bunny",
      address: "昆山市印象花园",
    };
  },
};
</script>
