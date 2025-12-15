import { useEffect, useState } from "react";
import "./index.scss";

function Son() {
  const [tickCount, setTickCount] = useState(0);

  useEffect(() => {
    const timer = setInterval(() => {
      console.log("tick");
      setTickCount((prev) => prev + 1);
    }, 1000);

    return () => {
      console.log("cleanup");
      clearInterval(timer);
    };
  }, []);

  return (
    <div className="son-component">
      <h3 className="son-title">子组件</h3>

      <div className="timer-section">
        <div className="timer-display">
          <span className="timer-label">定时器计数:</span>
          <span className="timer-value">{tickCount}</span>
        </div>
        <div className="timer-info">
          <p>⏰ 定时器每秒自动增加计数</p>
          <p>🔄 组件卸载时会自动清理定时器</p>
        </div>
      </div>

      <div className="explanation">
        <h4>清理机制说明:</h4>
        <ul>
          <li>useEffect 返回的函数会在组件卸载时执行</li>
          <li>这确保了定时器等资源被正确清理</li>
          <li>避免内存泄漏和不必要的执行</li>
        </ul>
      </div>
    </div>
  );
}

function MainAppEffectClearEffect() {
  const [show, setShow] = useState(true);
  const [mountCount, setMountCount] = useState(1);

  const toggleSonComponent = () => {
    setShow(!show);
    if (!show) {
      setMountCount((prev) => prev + 1);
    }
  };

  return (
    <div className="main-container">
      <div className="header">
        <h2 className="title">useEffect 清理副作用演示</h2>
        <p className="subtitle">观察组件卸载时如何清理定时器等资源</p>
      </div>

      <div className="content-wrapper">
        <div className="parent-component">
          <h3 className="parent-title">父组件</h3>

          <div className="status-section">
            <div className="mount-info">
              <span className="info-label">子组件挂载次数:</span>
              <span className="info-value">{mountCount}</span>
            </div>

            <div className="component-status">
              <span className="status-label">当前状态:</span>
              <span
                className={`status-value ${show ? "mounted" : "unmounted"}`}
              >
                {show ? "已挂载" : "已卸载"}
              </span>
            </div>
          </div>

          <div className="component-container">{show && <Son />}</div>

          <div className="controls">
            <button
              className={`toggle-button ${show ? "unmount" : "mount"}`}
              onClick={toggleSonComponent}
            >
              {show ? "卸载子组件" : "挂载子组件"}
            </button>

            <div className="instruction">
              <p>💡 点击按钮切换子组件的挂载状态</p>
              <p>💡 打开控制台查看 "tick" 和 "cleanup" 日志</p>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}

export default MainAppEffectClearEffect;
