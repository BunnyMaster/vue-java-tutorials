import VueRouter from "vue-router";

const router = new VueRouter({
  routes: [
    {
      name: "About",
      path: "/about",
      component: () => import("@/views/demo6/components/About.vue"),
    },
    {
      name: "Home",
      path: "/home",
      component: () => import("@/views/demo6/components/Home.vue"),
    },
  ],
});

export default router;
