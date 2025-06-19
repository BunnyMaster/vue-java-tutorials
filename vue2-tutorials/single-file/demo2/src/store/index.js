import Vue from "vue";
import Vuex from "vuex";
import count from "./modules/count";
import schoolInfo from "./modules/schoolInfo";

Vue.use(Vuex);

const store = new Vuex.Store({
  state: {},
  mutations: {},
  actions: {},
  modules: { count, schoolInfo },
});

export default store;
