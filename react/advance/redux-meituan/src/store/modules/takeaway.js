import { createSlice } from '@reduxjs/toolkit';
import axios from 'axios';

const foodsStore = createSlice({
  name: 'foods',
  initialState: {
    // 商品列表
    foodList: [],

    // 菜单激活下表
    activeIndex: 0,

    // 购物车列表
    cartList: [],
  },
  reducers: {
    /**
     * 设置商品列表
     * @param {Object} state - Redux store 的当前状态
     * @param {Object} action - Redux action 对象
     * @param {Array} action.payload - 商品列表数据
     */
    setFoodList(state, action) {
      state.foodList = action.payload;
    },

    /**
     * 设置菜单激活下标
     * @param {Object} state - Redux store 的当前状态
     * @param {Object} action - Redux action 对象
     * @param {number} action.payload - 菜单激活下标
     */
    changeActiveIndex(state, action) {
      state.activeIndex = action.payload;
    },

    addCartItem(state, action) {
      const { id } = action.payload;
      const item = state.cartList.find((item) => item.id === id);

      if (item) {
        item.count++;
      } else {
        action.payload.count = 1;
        state.cartList.push(action.payload);
      }
    },
  },
});

const { setFoodList, changeActiveIndex, addCartItem } = foodsStore.actions;

/**
 * 异步获取外卖商品列表的 thunk 函数
 * @param {Function} dispatch - Redux dispatch 函数，用于触发 action
 * @returns {Promise<void>} 返回一个 Promise，不包含具体返回值
 */
const fetchFoodList = () => {
  return async (dispatch) => {
    // 发送 HTTP GET 请求获取外卖商品数据
    const res = await axios.get('http://localhost:3004/takeaway');

    // 将获取到的商品数据通过 dispatch 分发给 Redux store
    dispatch(setFoodList(res.data));
  };
};

const foodsReducer = foodsStore.reducer;

export { addCartItem, changeActiveIndex, fetchFoodList };
export default foodsReducer;
