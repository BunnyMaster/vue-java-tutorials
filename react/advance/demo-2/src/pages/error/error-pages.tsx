// src/pages/error/error-pages.tsx
import { useRouteError } from 'react-router-dom';

function ErrorPage() {
  const error: unknown = useRouteError();
  console.error(error);

  return (
    <div className="min-h-screen bg-gradient-to-br from-red-50 to-orange-100 flex items-center justify-center p-4">
      <div className="bg-white rounded-2xl shadow-2xl p-8 max-w-2xl w-full text-center">
        <div className="text-6xl mb-4">⚠️</div>
        <h1 className="text-4xl font-bold text-red-600 mb-4">Oops!</h1>
        <p className="text-xl text-gray-700 mb-6">Sorry, an unexpected error has occurred.</p>

        <div className="bg-red-50 border-l-4 border-red-500 p-4 rounded mb-6">
          <p className="text-red-700 font-mono text-sm">
            <i>{(error as Error)?.message || (error as { statusText?: string })?.statusText}</i>
          </p>
        </div>

        <button
          onClick={() => (window.location.href = '/')}
          className="bg-indigo-600 hover:bg-indigo-700 text-white font-bold py-3 px-6 rounded-full transition duration-300 transform hover:scale-105"
        >
          返回首页
        </button>
      </div>
    </div>
  );
}

export default ErrorPage;
