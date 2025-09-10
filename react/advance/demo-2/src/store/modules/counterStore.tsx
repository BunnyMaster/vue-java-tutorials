import { createSlice } from "@reduxjs/toolkit";

const counterStore = createSlice({
  name: "counter",
  initialState: { count: 0 },
  reducers: {
    increase(state) {
      state.count++;
    },

    decrease(state) {
      state.count--;
    },
  },
});

// 解构 action 的函数
const { increase, decrease } = counterStore.actions;

// 获取reducer
const counterReducer = counterStore.reducer;

// 按需导出
export { decrease, increase };

export default counterReducer;
