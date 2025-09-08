import App from '@/App';
import ErrorPage from '@/pages/error/error-pages';
import { createBrowserRouter } from 'react-router-dom'; // 修正导入来源

const router = createBrowserRouter([
  {
    path: '/',
    element: <App />,
    errorElement: <ErrorPage />,
  }, 
  {
    path: '/error',
    element: <ErrorPage />,
    errorElement: <ErrorPage />,
  }
]);

export default router;