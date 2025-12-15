import { useState } from "react";

const UseStateDemo = () => {
  const [count, setCount] = useState("0");

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setCount(e.target.value);
  };
  return (
    <div className="rounded-2xl p-8 shadow-lg max-w-md mx-auto bg-gradient-to-br from-green-50 to-teal-100">
      <h2 className="text-2xl font-bold text-gray-800 mb-6 text-center">useState的使用-受控组件</h2>

      <div className="mb-8">
        <div className="text-center mb-4">
          <span className="text-lg text-gray-700">显示：</span>
          <span className="text-xl font-bold text-indigo-600">{count}</span>
        </div>
      </div>

      <input
        className="w-full px-4 py-2 border border-green-500 rounded-lg focus:outline-none focus:ring-2 focus:ring-indigo-500 focus:border-transparent"
        type="text"
        value={count}
        onChange={handleChange}
      />
    </div>
  );
};

export default UseStateDemo;
