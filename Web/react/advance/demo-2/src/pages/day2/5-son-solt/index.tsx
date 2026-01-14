import "./index.scss";

interface SonProps {
  children: React.ReactNode;
  title?: string;
}

function Son({ children, title = "子组件" }: SonProps) {
  return (
    <div className="son-container">
      <h4 className="son-title">{title}</h4>
      <div className="son-content">{children}</div>
    </div>
  );
}

function MainAppSolt() {
  return (
    <div className="main-container">
      <h3 className="title">父组件</h3>
      <div className="content-wrapper">
        <Son title="子组件区域">
          <button className="son-button">父组件按钮</button>
        </Son>
      </div>
    </div>
  );
}

export default MainAppSolt;
