<template>
  <div>
    <h2>学生姓名：{{ name }}</h2>
    <h2>学习性别：{{ sex }}</h2>
    <h2>学生年龄：{{ age + 1 }}</h2>
  </div>
</template>

<script>
export default {
  name: "SchoolInfo",
  // 第一种传入方法---简单接受
  // props: ["name", "sex", "age"],

  // 第二种传入方法---接受同时对数据进行限制
  // props: {
  //   name: String,
  //   sex: String,
  //   age: Number,
  // },
  props: {
    name: { type: String, required: true /* 必须要传入的 */ },
    sex: { type: String, default: "男🚹" /* 给一个默认值 */ },
    age: { type: Number, required: true },
  },
  data() {
    return {};
  },
};
</script>
