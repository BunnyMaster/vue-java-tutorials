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

    addToNum(state, action) {
      state.count += action.payload;
    },
  },
});

// 解构 action 的函数，并按需导出
export const { increase, decrease, addToNum } = counterStore.actions;

// 获取reducer
const counterReducer = counterStore.reducer;

export default counterReducer;
