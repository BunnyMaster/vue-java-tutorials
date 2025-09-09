import { Link, Outlet, useNavigate } from "react-router-dom";

const Day2 = () => {
  const navigate = useNavigate();

  const list = [
    { name: "受控组件", path: "/day2/use-state" },
    // { name: "修改form", path: "/day2/form" },
  ];

  const handleGoHome = () => {
    navigate("/");
  };

  return (
    <div className="min-h-screen bg-gradient-to-br from-blue-50 to-indigo-100">
      <div className="container mx-auto px-4 py-8">
        <div className="bg-white rounded-2xl shadow-xl p-6 md:p-8 max-w-2xl mx-auto">
          <div className="flex justify-between items-center mb-8">
            <h1 className="text-3xl font-bold text-gray-800">练习列表</h1>
            <button
              onClick={handleGoHome}
              className="bg-indigo-500 hover:bg-indigo-600 text-white px-4 py-2 rounded-lg transition duration-300"
            >
              首页
            </button>
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

        <div className="mt-8 max-w-4xl mx-auto">
          <Outlet />
        </div>
      </div>
    </div>
  );
};

export default Day2;
