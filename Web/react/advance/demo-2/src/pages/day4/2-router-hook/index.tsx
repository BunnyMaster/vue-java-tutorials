import {
  useInRouterContext,
  useNavigationType,
  useOutlet,
  useResolvedPath,
} from "react-router-dom";
import "./index.scss";

function RouterDemoHook() {
  /**
   * 判断当前是否处于路由上下文环境中
   * 如果路由器没有包含组件，那么则为false
   * 使用场景：封装第三方组件
   */
  const routerContext = useInRouterContext();

  /**
   * 返回当前导航类型（用户如何来到这个页面）
   * POP、PUSH、REPLACE
   * POP：用户点击浏览器的回退按钮
   */
  const navigateType = useNavigationType();

  /**
   * 用来呈现当前组件中渲染的嵌套路由
   * 如果嵌套路由没有挂在，则result为null
   * 如果嵌套路由已经挂载，则展示嵌套路由对象
   */
  const resultOutLet = useOutlet();

  /**
   * 给定URL值，解析其中的 path、search、hash
   */
  const resovlePath = useResolvedPath("/day2?id=001&name=tom#qwe");

  return (
    <div className="router-hook-demo">
      <div className="container">
        <h3>Router Hook 示例</h3>

        <div className="hook-info-card">
          <div className="info-item">
            <h4>useInRouterContext</h4>
            <div className="info-content">
              <span className="label">是否处于路由上下文环境:</span>
              <span className={`value ${routerContext ? "success" : "error"}`}>
                {routerContext ? "是" : "否"}
              </span>
            </div>
            <p className="description">
              判断当前组件是否在 React Router 的上下文中渲染
            </p>
          </div>

          <div className="info-item">
            <h4>useNavigationType</h4>
            <div className="info-content">
              <span className="label">路由跳转类型:</span>
              <span className="value navigation-type">{navigateType}</span>
            </div>
            <p className="description">
              显示当前导航类型: POP(回退)、PUSH(前进)、REPLACE(替换)
            </p>
          </div>

          <div className="info-item">
            <h4>useOutlet</h4>
            <div className="info-content">
              <span className="label">嵌套路由组件:</span>
              <span className="value">
                {resultOutLet ? "已挂载" : "未挂载"}
              </span>
            </div>
            <p className="description">用于呈现当前组件中渲染的嵌套路由</p>
          </div>

          <div className="info-item">
            <h4>useResolvedPath</h4>
            <div className="info-content vertical">
              <span className="label">
                解析路径 "/day2?id=001&name=tom#qwe":
              </span>
              <div className="path-details">
                <div className="path-item">
                  <span className="path-label">Path:</span>
                  <span className="path-value">{resovlePath.pathname}</span>
                </div>
                <div className="path-item">
                  <span className="path-label">Search:</span>
                  <span className="path-value">
                    {resovlePath.search || "无"}
                  </span>
                </div>
                <div className="path-item">
                  <span className="path-label">Hash:</span>
                  <span className="path-value">{resovlePath.hash || "无"}</span>
                </div>
              </div>
            </div>
            <p className="description">
              解析给定URL值中的 path、search、hash 部分
            </p>
          </div>
        </div>
      </div>
    </div>
  );
}

export default RouterDemoHook;
