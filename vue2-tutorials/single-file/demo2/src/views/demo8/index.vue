<template>
  <div class="container">
    <!-- 可以只触发一次 -->
    <Student @getUserinfo.once="getUserinfo" title="可以只触发一次" />
    <Student @getUserinfo="getUserinfo" title="普通触发" />
  </div>
</template>

<script>
import Student from "./components/Sutdent";
export default {
  name: "Demo-8",
  components: { Student },
  data() {
    return {};
  },
  methods: {
    /* 获取用户信息 */
    getUserinfo(userinfo) {
      const data = { ...userinfo };
      console.log(data);
    },
  },
};
</script>
