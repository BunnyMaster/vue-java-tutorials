<template>
  <div>
    <h3>{{ $store.state.schoolInfo.schoolName }}</h3>
    <h3>{{ schoolName }}</h3>
    <h3>{{ $store.getters["schoolInfo/getSchoolName"] }}</h3>
    <h3>{{ getSchoolName }}</h3>
  </div>
</template>

<script>
import { mapState, mapGetters } from "vuex";
export default {
  name: "Demo-5",
  data() {
    return {};
  },
  computed: {
    // mapState和mapGetters对象写法
    // ...mapState("schoolInfo", { schoolName: "schoolName" }),

    // mapState和mapGetters数组写法
    ...mapState("schoolInfo", ["schoolName"]),
    ...mapGetters("schoolInfo", ["getSchoolName"]),
  },
  mounted() {
    const data = mapState("schoolInfo", { schoolName: "schoolName" });
    console.log(data);
  },
};
</script>
