import { useRef } from 'react';

function UseRefDemo() {
  const inputRef = useRef(null);

  const showDom = () => {
    console.log(inputRef.current);
  };

  return (
    <div className="rounded-2xl p-8 shadow-lg max-w-md mx-auto bg-gradient-to-br from-green-50 to-teal-100">
      <h2 className="text-2xl font-bold text-gray-800 mb-6 text-center">Ref的使用</h2>

      <div className="mb-4 text-center">
        <p className="text-fuchsia-600">打开控制台</p>
        <input
          type="text"
          ref={inputRef}
        />
      </div>

      <div className="flex justify-center">
        <button
          className="bg-gradient-to-r from-green-500 to-teal-600 hover:from-green-600 hover:to-teal-700 text-white font-bold py-3 px-6 rounded-full shadow-lg transform transition duration-300 hover:scale-105"
          onClick={showDom}
        >
          获取真实DOM
        </button>
      </div>
    </div>
  );
}

export default UseRefDemo;
