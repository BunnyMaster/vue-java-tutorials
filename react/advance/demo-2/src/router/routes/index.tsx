import ErrorPage from '@/pages/error/error-pages';
import demo from './demo';
import home from './home';


const routes = [
  ...home.routes,
  ...demo.routes,
  {
    path: '/error',
    element: <ErrorPage />,
    errorElement: <ErrorPage />,
  },
];

export default routes;
