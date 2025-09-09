import { useState } from "react";

const UseStateDemo = () => {
  const [count, setCount] = useState("0");

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setCount(e.target.value);
  };
  return (
    <div>
      <h1>受控组件</h1>
      <p>显示：{count}</p>
      <input type="text" value={count} onChange={handleChange} />
    </div>
  );
};

export default UseStateDemo;
