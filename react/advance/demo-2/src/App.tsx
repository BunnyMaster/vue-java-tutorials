// src/App.tsx
import { Link } from "react-router-dom";
import "./styles/index.css";

function App() {
  const list = [
    { name: "Day1", path: "/day1" },
    { name: "Day2", path: "/day2" },
    { name: "Day3", path: "/day3" },
    { name: "Day4", path: "/day4" },
  ];

  return (
    <div className="min-h-screen bg-gradient-to-br from-blue-50 to-indigo-100">
      <div className="container mx-auto px-4 py-8">
        <div className="bg-white rounded-2xl shadow-xl p-6 md:p-8 max-w-2xl mx-auto">
          <div className="flex justify-between items-center mb-8">
            <h1 className="text-3xl font-bold text-gray-800">日期</h1>
          </div>
          <ul className="space-y-4">
            {list.map((item) => (
              <li key={item.name}>
                <Link
                  to={item.path}
                  className="block bg-gradient-to-r from-indigo-500 to-purple-600 text-white rounded-xl p-6 shadow-lg hover:shadow-xl transform hover:-translate-y-1 transition-all duration-300"
                >
                  <span className="text-xl font-semibold">{item.name}</span>
                </Link>
              </li>
            ))}
          </ul>
        </div>
      </div>
    </div>
  );
}

export default App;
