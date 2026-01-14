import { useState } from "react";
import './index.scss'; // 引入样式文件

/**
 * 自定义Hook函数，用于管理一个布尔值状态及其切换功能
 * 使用规则：
 * 只能在组件中或者其他自定义Hook函数中调用
 * 只能在组件的顶层调用，不能再if、for、其他函数中
 *
 * @returns 包含以下属性的对象：
 *   - value: 当前的布尔值状态
 *   - toggle: 切换布尔值状态的函数
 */
function useCustomerHook() {
  // 使用useState Hook创建一个布尔值状态，默认值为true
  const [value, setValue] = useState(true);

  // 定义切换函数，用于反转当前的布尔值状态
  const toggle = () => {
    setValue(!value);
  };

  // 返回当前状态值和切换函数
  return { value, toggle };
}

function MainAppCustomerHook() {
  const { value, toggle } = useCustomerHook();

  return (
    <div className="customer-hook-container">
      <div className="card">
        <h2>自定义 Hook 示例</h2>
        <div className="status-display">
          <span className="status-label">当前状态:</span>
          <span className={`status-value ${value ? 'active' : 'inactive'}`}>
            {value ? '开启' : '关闭'}
          </span>
        </div>
        {value && (
          <div className="content-box">
            <div className="content-text">Customer Hook</div>
          </div>
        )}
        <div className="button-container">
          <button className="toggle-button" onClick={toggle}>
            切换状态
          </button>
        </div>
      </div>
    </div>
  );
}

export default MainAppCustomerHook;