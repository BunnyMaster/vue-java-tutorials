<template>
  <ul class="list-group card-body">
    <todo-item v-for="todo in list" :key="todo.id" :todo="todo" />
  </ul>
</template>

<script>
import TodoItem from "./todo-item";

export default {
  name: "TodoList",
  components: { TodoItem },
  props: ["list"],
};
</script>
