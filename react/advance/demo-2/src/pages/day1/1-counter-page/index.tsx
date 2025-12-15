import { useState } from 'react';
import './index.scss';

function Counter() {
  const [count, setCount] = useState(0);

  const handleClick = () => {
    setCount(count + 1);
  };

  return (
    <div className="counter-container rounded-2xl p-8 shadow-lg max-w-md mx-auto">
      <div className="counter-card">
        <h2 className="counter-title">计数器</h2>
        <button
          className="counter-button"
          onClick={handleClick}
        >
          <p className="counter-value">{count}</p>
          <p>点击增加</p>
        </button>
      </div>
    </div>
  );
}
 
export default Counter;
