import VueRouter from "vue-router";

const router = new VueRouter({
  routes: [
    {
      path: "/about",
      component: () => import("@/views/demo6/components/About.vue"),
    },
    {
      path: "/home",
      component: () => import("@/views/demo6/components/Home.vue"),
    },
  ],
});

export default router;
