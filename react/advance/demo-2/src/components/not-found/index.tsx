import "./index.scss";

function NotFound() {
  return (
    <div className="not-found">
      <div className="not-found-container">
        <div className="error-code">404</div>
        <h3>页面未找到</h3>
        <p className="error-message">抱歉，您访问的页面不存在</p>
        <div className="error-description">
          <p>可能的原因：</p>
          <ul>
            <li>页面已被移除</li>
            <li>地址输入有误</li>
            <li>链接已过期</li>
          </ul>
        </div>
        <a href="/" className="back-home">
          返回首页
        </a>
      </div>
    </div>
  );
}

export default NotFound;
