import { createSlice } from "@reduxjs/toolkit";
import axios from "axios";

const channelStore = createSlice({
  name: "channelStore",
  initialState: {
    channelList: [],
  },
  reducers: {
    setChannel: (state, action) => {
      state.channelList = action.payload;
    },
  },
});

export const { setChannel } = channelStore.actions;
const fetchChannelList = () => {
  return async (dispacth: any) => {
    const response = await axios.get(
      "https://jsonplaceholder.typicode.com/todos"
    );

    dispacth(setChannel(response.data));
  };
};

const reducerChannel = channelStore.reducer;

export { fetchChannelList };

export default reducerChannel;
