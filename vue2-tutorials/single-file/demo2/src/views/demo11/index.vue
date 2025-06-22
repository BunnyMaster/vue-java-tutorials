<template>
  <div class="container card">
    <div class="card-header btn-group">
      <!-- 路由导航 -->
      <button class="btn btn-success" @click="$router.push({ name: 'About' })">
        显示About
      </button>
      <button class="btn btn-success" @click="$router.push({ name: 'Home' })">
        显示Home
      </button>

      <!-- 前进和后退 -->
      <button class="btn btn-primary" @click="back">后退</button>
      <button class="btn btn-dark" @click="forward">前进</button>
    </div>

    <div class="card-body">
      <router-view />
    </div>
  </div>
</template>

<script>
export default {
  name: "Demo-11",
  data() {
    return {};
  },
  methods: {
    /* 后退 */
    back() {
      this.$router.back();
    },

    /* 前进 */
    forward() {
      this.$router.forward();
    },

    /* 跳转指定步数 */
    go() {
      this.$router.go(-2);
    },
  },
};
</script>
