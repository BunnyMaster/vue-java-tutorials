import { configureStore } from "@reduxjs/toolkit";
import reducerChannel from "./modules/channelStore";
import counterReducer from "./modules/counterStore";

const store = configureStore({
  reducer: {
    counter: counterReducer,
    channel: reducerChannel,
  },
});

export default store;
