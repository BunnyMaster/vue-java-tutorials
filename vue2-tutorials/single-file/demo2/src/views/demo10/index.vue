<template>
  <div class="container card">
    <div class="card-header">
      <h1>动画效果:{{ visibility }}</h1>
    </div>

    <div class="card-body">
      <Transition name="bunny" :appear="true">
        <h3 v-show="visibility">显示动画</h3>
      </Transition>

      <ul>
        <TransitionGroup
          name="animate__animated animate__bounce"
          appear
          enter-active-class="animate__flipInX"
          leave-active-class="animate__flipOutX"
        >
          <li key="1" v-show="visibility">嗬嗬嗬嗬嗬嗬嗬嗬嗬嗬嗬</li>
          <li key="2" v-show="!visibility">哈哈哈哈哈哈哈哈哈哈哈</li>
        </TransitionGroup>
      </ul>
    </div>

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
