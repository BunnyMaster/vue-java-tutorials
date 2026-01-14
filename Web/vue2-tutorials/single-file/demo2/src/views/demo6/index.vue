<template>
  <div class="container mt-3">
    <div class="row">
      <div class="col-sm-2 col-xs-offset-2 text-center">
        <!-- 路由跳转内容 -->
        <div class="list-group">
          <router-link
            class="list-group-item"
            active-class="active"
            to="/about"
          >
            about
          </router-link>
          <router-link class="list-group-item" active-class="active" to="/home"
            >home</router-link
          >
        </div>
      </div>

      <!-- 内容显示区域 -->
      <div class="col-sm-6">
        <div class="panel">
          <div class="panel-body">
            <router-view />
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "Demo-6",
  data() {
    return {};
  },
};
</script>
