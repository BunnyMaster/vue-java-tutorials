<template>
  <School />
  <Student />
</template>

<script>
import School from "./compontens/School.vue";
import Student from "./compontens/Student.vue";

export default {
  name: "App",
  data() {
    return {};
  },
  compontens: {
    School,
    Student,
  },
};
</script>
