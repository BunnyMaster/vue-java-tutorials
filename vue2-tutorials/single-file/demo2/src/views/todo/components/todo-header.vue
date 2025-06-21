<template>
  <div class="card-header my-3">
    <input
      type="text"
      v-model.trim="content"
      class="form-control"
      placeholder="用户的输入..."
      @keyup.enter="onAdd"
    />
  </div>
</template>

<script>
import { nanoid } from "nanoid";

export default {
  name: "TodoHeader",
  data() {
    return {
      content: "",
    };
  },
  methods: {
    onAdd(ev) {
      if (!this.content) {
        alert("没有内容！！！");
        return;
      }

      const todoObj = {
        id: nanoid(),
        content: ev.target.value.trim(),
        completed: false,
      };

      this.$emit("onAdd", todoObj);

      // 清空输入框
      this.content = "";
    },
  },
};
</script>
