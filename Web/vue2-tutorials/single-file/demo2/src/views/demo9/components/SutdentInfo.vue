<template>
  <div class="card">
    <div class="card-header">
      <h3>学生姓名：{{ studenName }}</h3>
    </div>

    <div class="card-body">
      <div class="btn-group">
        <button class="btn btn-primary" @click="onEmitStudentName">
          向父组件传递学生姓名
        </button>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "StudentInfo",
  data() {
    return {
      studenName: "Bunny",
    };
  },
  methods: {
    onEmitStudentName() {
      this.$emit("getStudentName", this.studenName);
    },
  },
};
</script>
