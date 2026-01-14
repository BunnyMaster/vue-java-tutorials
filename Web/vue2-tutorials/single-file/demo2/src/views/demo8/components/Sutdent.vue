<template>
  <div class="card">
    <div class="card-header">
      <div class="card-header-tabs">
        <h3>{{ title }}</h3>
      </div>

      <div class="card-header-pills">
        <h3>名称：{{ userinfo.sutdentName }}</h3>
        <h3>地址：{{ userinfo.address }}</h3>
      </div>
    </div>

    <div class="card-body btn-group">
      <button class="btn btn-primary" @click="getUserinfo">获取用户信息</button>
      <button class="btn btn-danger" @click="unbind">解除绑定</button>
    </div>
  </div>
</template>

<script>
export default {
  name: "StudentInfo",
  props: {
    title: {
      type: String,
      default: "标题显示",
    },
  },
  data() {
    return {
      userinfo: {
        sutdentName: "Bunny",
        address: "昆山印象花园",
      },
    };
  },
  methods: {
    /* 触发用户事件 */
    getUserinfo() {
      this.$emit("getUserinfo", this.userinfo);
    },

    /* 接触绑定 */
    unbind() {
      console.info("解除绑定");

      // 解绑单个
      this.$off("getUserinfo");

      // 解绑多个
      //   this.$off(["getUserinfo"]);

      // 解绑所有
      //   this.$off();
    },
  },
};
</script>
