<template>
  <div
    class="card-footer d-flex justify-content-between align-items-center"
    v-if="count"
  >
    <div @click="onSelectAll(selectAll)">
      <input type="checkbox" class="me-2" :checked="selectAll" />
      <span>已完成 {{ accomplish }} / 全部 {{ count }}</span>
    </div>

    <button class="btn btn-danger" @click="onDeletedCommpleted">
      清除已完成
    </button>
  </div>
</template>

<script>
export default {
  name: "TodoFooter",
  props: ["list", "onSelectAll", "deletedTodo"],
  computed: {
    /* 完成的数量 */
    accomplish() {
      return this.list.filter((todo) => todo.completed).length;
    },

    /* 总数量 */
    count() {
      return this.list.length;
    },

    /* 是否已经全选 */
    selectAll() {
      const flag = this.list.filter((todo) => todo.completed === true);
      return flag.length === this.list.length;
    },
  },
  methods: {
    onDeletedCommpleted() {
      this.$emit("deleted-commpleted");
    },
  },
};
</script>
