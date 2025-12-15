export const test1 = {
  data() {
    return {
      username: "Bunny",
    };
  },
  methods: {
    showMessage() {
      alert(`展示消息：${this.username}`);
    },
  },
};
