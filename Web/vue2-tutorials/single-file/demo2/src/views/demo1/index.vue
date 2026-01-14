<template>
  <div id="app">
    <h1 ref="title" v-text="message"></h1>
    <button @click="ShowDom">输出上方DOM元素</button>
    <SchoolInfo ref="schoolInfoRef" />
  </div>
</template>

<script>
import SchoolInfo from "@/views/demo1/components/SchoolInfo.vue";

export default {
  name: "Demo-1",
  components: { SchoolInfo },
  data() {
    return {
      message: "学习Vue2",
    };
  },

  methods: {
    ShowDom() {
      // 获取当前Ref对象内容
      console.log("@@@", this.$refs.title);
      // 获取子组件对象实例
      console.log("@@@", this.$refs.schoolInfoRef);
    },
  },
};
</script>
