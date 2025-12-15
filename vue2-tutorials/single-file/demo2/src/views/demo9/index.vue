<template>
  <div class="container">
    <h4>
      学生姓名：
      <span class="badge text-bg-secondary"> {{ studenName }}</span>
    </h4>

    <!-- 需要在前面加上native，否则会当成自定义事件 -->
    <StudentInfo ref="studenInfo" @click.native="showMessage" />
  </div>
</template>

<script>
import StudentInfo from "./components/SutdentInfo.vue";

export default {
  name: "Demo-9",
  components: { StudentInfo },
  data() {
    return {
      studenName: "",
    };
  },
  methods: {
    /* 获取学生名 */
    getStudentName(name, ...params) {
      console.log(name, params);

      this.studenName = name;
    },

    /* 点击子组件显示消息 */
    showMessage() {
      alert("点击子组件触发点击事件。。。");
    },
  },
  mounted() {
    this.$refs.studenInfo.$on("getStudentName", this.getStudentName);

    // 绑定事件，只能写箭头函数，否则当前this是Vue被绑定的组件
    // this.$refs.studenInfo.$on("getStudentName", (name, ...params) => {
    //   console.log(name, params);
    //   this.studenName = name;
    // });
  },
};
</script>
