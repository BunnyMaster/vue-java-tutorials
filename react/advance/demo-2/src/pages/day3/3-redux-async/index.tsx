import { fetchChannelList } from "@/store/modules/channelStore";
import { useEffect } from "react";
import { useDispatch, useSelector } from "react-redux";
import "./index.scss";

function ReduxAsyncDemo() {
  const { channelList } = useSelector((state: any) => state.channel);

  const dispatch = useDispatch();

  useEffect(() => {
    dispatch(fetchChannelList() as any);
  }, [dispatch]);

  if (channelList.length === 0) {
    return (
      <div className="redux-async-demo">
        <h3 className="title">Redux异步</h3>
        <ul className="channel-list loading">
          <div className="loading-spinner">
            <div className="spinner"></div>
            <div className="loading-text">频道列表加载中...</div>
          </div>
        </ul>
      </div>
    );
  }

  return (
    <div className="redux-async-demo">
      <h3 className="title">Redux异步</h3>

      <ul className="channel-list">
        {channelList.map((channel: any) => (
          <li className="channel-item" key={channel.id}>
            <span className="channel-id">#{channel.id}</span>
            <span className="channel-title">{channel.title}</span>
          </li>
        ))}
      </ul>
    </div>
  );
}

export default ReduxAsyncDemo;
