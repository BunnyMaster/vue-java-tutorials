<template>
  <div class="container card">
    <todo-header @onAdd="onAdd" />
    <todo-list :list="todolist" />
    <todo-footer
      :list="todolist"
      :on-select-all="onSelectAll"
      @deleted-commpleted="onDeletedCommpleted"
    />
  </div>
</template>

<script>
import TodoFooter from "./components/todo-footer";
import TodoHeader from "./components/todo-header";
import TodoList from "./components/todo-list";

export default {
  name: "TODOList",
  components: { TodoHeader, TodoList, TodoFooter },
  data() {
    return {
      todolist: [
        { id: 1, content: "吃饭", completed: true },
        { id: 2, content: "吃饭", completed: false },
        { id: 3, content: "吃饭", completed: true },
      ],
    };
  },
  methods: {
    /* 添加Todo内容 */
    onAdd(todo) {
      this.todolist.unshift(todo);
    },

    /* 修改todo状态是否完成 */
    changeTodoStatus() {
      // 先移除旧的监听器避免重复
      this.$root.$off("change-todo-status");
      this.$root.$on("change-todo-status", (id) => {
        this.todolist.forEach((todo) => {
          if (todo.id === id) {
            todo.completed = !todo.completed;
          }
        });
      });
    },

    /* 删除todo */
    deletedTodo() {
      this.$root.$off("delete-todo");
      this.$root.$on("delete-todo", (id) => {
        const list = this.todolist.filter((todo) => todo.id !== id);
        this.todolist = list;
      });
    },

    /* 删除已完成的todo */
    onDeletedCommpleted() {
      const ids = this.todolist
        .filter((todo) => todo.completed === true)
        .map((todo) => todo.id);

      const completedList = this.todolist.filter(
        (todo) => todo.completed === false
      );

      // 删除时的提示
      const result = confirm(`是否确认删除：${ids}`);
      if (result) {
        this.todolist = completedList;
        alert(`清除已完成id：${ids}`);
      }
    },

    /* 选择全部 */
    onSelectAll(selectAll) {
      this.todolist = this.todolist.map((todo) => ({
        ...todo,
        completed: !selectAll,
      }));
    },
  },
  mounted() {
    this.changeTodoStatus();
    this.deletedTodo();
  },
};
</script>
