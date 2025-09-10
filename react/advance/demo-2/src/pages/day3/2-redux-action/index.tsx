import { addToNum, decrease, increase } from "@/store/modules/counterStore";
import { useDispatch, useSelector } from "react-redux";

import "./index.scss";

function ReduxActionDemo() {
  const { count } = useSelector((state: any) => state.counter);

  const dispatch = useDispatch();

  /**
   * 点击加1
   */
  const handleIncrease = () => {
    dispatch(increase());
  };

  /**
   * 点击减1
   */
  const handleDescrease = () => {
    dispatch(decrease());
  };

  /**
   * 点击加20
   */
  const handleIncrease20 = () => {
    // 增加数值的常量定义
    const INCREASE_VALUE = 20;

    dispatch(addToNum(INCREASE_VALUE));
  };

  return (
    <div className="redux-action-demo">
      <div className="counter-card">
        <h3 className="card-title">Redux Action</h3>

        <div className="counter-display">
          <span className="counter-value">{count}</span>
        </div>

        <div className="button-group">
          <button className="btn btn-primary" onClick={handleDescrease}>
            -
          </button>
          <button className="btn btn-primary" onClick={handleIncrease}>
            +
          </button>
        </div>

        <div className="button-group">
          <button className="btn btn-secondary" onClick={handleIncrease20}>
            +20
          </button>
        </div>
      </div>
    </div>
  );
}

export default ReduxActionDemo;
