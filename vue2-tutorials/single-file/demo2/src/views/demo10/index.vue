<template>
  <div class="card">
    <div class="card-header">
      <h1>动画效果:{{ visibility }}</h1>
    </div>

    <Transition name="bunny" :appear="true">
      <div class="card-body" v-show="visibility">
        <h3>显示动画</h3>
      </div>
    </Transition>

    <div class="card-footer">
      <div class="btn-group">
        <button class="btn btn-primary" @click="visibility = true">显示</button>
        <button class="btn btn-success" @click="visibility = false">
          隐藏
        </button>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "Demo-10",
  data() {
    return {
      visibility: true,
    };
  },
};
</script>

<style>
.bunny-enter-active {
  animation: slip 0.5s linear;
}

.bunny-leave-active {
  animation: slip 0.5s linear reverse;
}

@keyframes slip {
  from {
    transform: translateX(-300px);
  }
  to {
    transform: translateX(0);
  }
}
</style>
