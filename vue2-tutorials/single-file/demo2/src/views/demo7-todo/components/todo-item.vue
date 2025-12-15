<template>
  <li
    class="list-group-item d-flex justify-content-between align-items-center list-group-item-action"
  >
    <div class="py-2" @click="onChange">
      <input type="checkbox" class="me-2" :checked="todo.completed" />
      <span> {{ todo.content }}</span>
    </div>

    <button class="btn btn-danger deleted" @click="onDeleted">删除</button>
  </li>
</template>

<script>
export default {
  name: "TodoItem",
  props: ["todo"],
  methods: {
    /* 修改todo的状态 */
    onChange() {
      const id = this.todo.id;

      this.$root.$emit("change-todo-status", id);
    },

    /* 删除todo */
    onDeleted() {
      const id = this.todo.id;

      const result = confirm(`确认删除：${id}`);
      if (result) {
        this.$root.$emit("delete-todo", id);
      }
    },
  },
};
</script>

<style scoped>
.deleted {
  visibility: hidden;
}

li:hover .deleted {
  visibility: visible !important;
}
</style>
