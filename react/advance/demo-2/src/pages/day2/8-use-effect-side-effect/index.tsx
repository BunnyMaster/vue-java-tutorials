import { useEffect, useState } from "react";

function MainAppUseEffectSideEffect() {
  const [count, setCount] = useState(0);

  /**
   * 1.依赖项为空时，初始化会执行一次
   * 如果为严格模式会执行两次
   */
  //   useEffect(() => {
  //     console.log("副作用useEffect");
  //   });

  /**
   * 2.空数组 初始化执行一次，后面就再也不执行了
   * 如果为严格模式会执行两次
   */
  //   useEffect(() => {
  //     console.log("副作用useEffect count:", count);
  //   }, []);

  /**
   * 3.依赖项为count时，初始化 + count变化时执行
   * 如果为严格模式会执行两次
   */
  useEffect(() => {
    console.log("副作用useEffect count:", count);
  }, [count]);

  return (
    <div>
      演示useEffect副作用：
      <button
        onClick={() => {
          setCount(count + 1);
        }}
      >
        点击
      </button>
    </div>
  );
}

export default MainAppUseEffectSideEffect;
