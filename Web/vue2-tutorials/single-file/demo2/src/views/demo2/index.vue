<template>
  <div>
    <SchoolInfo name="Bunny1" sex="女🚺" :age="16" />

    <hr />
    <SchoolInfo name="Bunny2" sex="女🚺" :age="16" />

    <hr />
    <SchoolInfo name="Bunny3" :age="16" />
  </div>
</template>

<script>
import SchoolInfo from "./components/SchoolInfo.vue";

export default {
  name: "Demo-2",
  components: { SchoolInfo },
};
</script>
