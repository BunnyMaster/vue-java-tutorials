// 从 react 中导入 createContext 和 useContext 方法
import { createContext, useContext } from "react";
import "./index.scss";

// 创建上下文对象，用于跨层级组件间传递数据
// 泛型参数<string | null>指定上下文值的类型为字符串或null
// 初始值设为null
const MainContext = createContext<string | null>(null);

// SonA 子组件 - 演示 useContext 钩子的基本用法
function SonA() {
  // 使用 useContext 钩子获取 MainContext 上下文的值
  const context = useContext(MainContext);

  // 渲染组件内容，显示从上下文获取的值
  return (
    <div className="son-card">
      <h3 className="son-title">子组件 A</h3>
      <div className="son-content">
        <h3 className="mb-2">接收到的消息:</h3>
        <p className="context-message"> {context}</p>
      </div>
    </div>
  );
}

// SonB 子组件 - 同样使用 useContext 获取上下文数据
function SonB() {
  // 通过 useContext 获取祖先组件提供的上下文值
  const context = useContext(MainContext);

  // 渲染组件内容，显示从上下文获取的值
  return (
    <div className="son-card">
      <h3 className="son-title">子组件 B</h3>
      <div className="son-content">
        <h3 className="mb-2">接收到的消息:</h3>
        <p className="context-message">{context}</p>
      </div>
    </div>
  );
}

// MainAppContext 主组件 - 演示 Context API 的完整使用流程
function MainAppContext() {
  // 定义要通过上下文传递的数据
  const message = "这是父组件传递给子组件的参数";

  return (
    // 返回 JSX 元素
    <div className="main-container">
      <div className="header">
        <h2 className="title">Context API 示例</h2>
        <p className="subtitle">跨组件数据传递</p>
      </div>

      {/* 使用 MainContext.Provider 包裹子组件，提供上下文值 */}
      {/* value 属性设置要传递给后代组件的数据 */}
      <MainContext.Provider value={message}>
        <div className="sons-container">
          {/* 渲染子组件 A 和 B，它们可以通过上下文获取数据 */}
          <SonA />
          <SonB />
        </div>
      </MainContext.Provider>
    </div>
  );
}

// 导出主组件供其他模块使用
export default MainAppContext;
