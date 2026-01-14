import Vue from "vue";
import Vuex from "vuex";
Vue.use(Vuex);

const count = {
  namespaced: true,
  state: { num: 1, sum: 0 },
  getters: {
    bigSum(state) {
      return state.sum * 10;
    },
  },
  actions: {
    // 奇数相加
    incrementOdd(context) {
      // 可以从上下文中获取sum
      if (context.state.sum % 2) {
        context.commit("INCREMENT_ODD");
      }
    },
    incrementWait(context) {
      setTimeout(() => {
        context.commit("INCREMENT_WAIT");
      }, 500);
    },
  },
  mutations: {
    // 相加
    INCREMENT(state) {
      state.sum += state.num;
    },
    // 相减
    DECREMENT(state) {
      state.sum -= state.num;
    },
    // 奇数相加
    INCREMENT_ODD(state) {
      state.sum += state.num;
    },
    INCREMENT_WAIT(state) {
      state.sum += state.num;
    },
  },
};

export default count;
