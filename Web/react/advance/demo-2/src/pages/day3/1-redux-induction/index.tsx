import { decrease, increase } from "@/store/modules/counterStore";
import { useDispatch, useSelector } from "react-redux";
import "./index.scss"; // 引入样式文件

function ReduxInduction() {
  const { count } = useSelector((state: any) => state.counter);
  const dispatch = useDispatch();

  const handleIncrease = () => {
    dispatch(increase());
  };

  const handleDecrease = () => {
    dispatch(decrease());
  };

  return (
    <div className="counter-container">
      <h3 className="counter-title">Redux入门</h3>

      <div className="counter-card">
        <button
          className="counter-btn counter-btn-decrease"
          onClick={handleDecrease}
        >
          -
        </button>

        <div className="counter-display">
          <span className="counter-label">当前计数：</span>
          <span className="counter-value">{count}</span>
        </div>

        <button
          className="counter-btn counter-btn-increase"
          onClick={handleIncrease}
        >
          +
        </button>
      </div>
    </div>
  );
}

export default ReduxInduction;
