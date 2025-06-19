import Vue from "vue";
import Vuex from "vuex";
import count from "./modules/count";
Vue.use(Vuex);

const store = new Vuex.Store({
  state: {},
  mutations: {},
  actions: {},
  modules: { count },
});

export default store;
