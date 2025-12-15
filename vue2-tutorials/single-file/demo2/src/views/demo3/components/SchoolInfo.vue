<template>
  <div>
    <h2 @click="showMessage">学生姓名-showMessage：{{ username }}</h2>
    <h2>学习性别：{{ sex }}</h2>
    <h2>学生年龄：{{ age + 1 }}</h2>
  </div>
</template>

<script>
import { test1 } from "../mixins/test1";

export default {
  name: "SchoolInfo",
  data() {
    return {
      username: "Bunny",
      sex: "🚺",
      age: 16,
    };
  },
  mixins: [test1],
};
</script>
