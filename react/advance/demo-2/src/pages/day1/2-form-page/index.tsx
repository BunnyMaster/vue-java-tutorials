// src/pages/demo/2-form-page/index.tsx
import { useState } from 'react';

function FormPage() {
  const [form, setForm] = useState({ name: '呵呵' });

  const handleClick = () => {
    setForm({ name: '哈哈' });
  };

  return (
    <div className="rounded-2xl p-8 shadow-lg max-w-md mx-auto bg-gradient-to-br from-green-50 to-teal-100">
      <h2 className="text-2xl font-bold text-gray-800 mb-6 text-center">表单示例</h2>

      <div className="mb-8">
        <div className="text-center mb-4">
          <span className="text-lg text-gray-700">当前名称: </span>
          <span className="text-xl font-bold text-indigo-600">{form.name}</span>
        </div>
      </div>

      <div className="flex justify-center">
        <button
          onClick={handleClick}
          className="bg-gradient-to-r from-green-500 to-teal-600 hover:from-green-600 hover:to-teal-700 text-white font-bold py-3 px-6 rounded-full shadow-lg transform transition duration-300 hover:scale-105"
        >
          修改表单
        </button>
      </div>
    </div>
  );
}

export default FormPage;
