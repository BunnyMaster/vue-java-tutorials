import ErrorPage from '@/pages/error/error-pages';
import home from './home';


const routes = [
  ...home.routes,
  {
    path: '/error',
    element: <ErrorPage />,
    errorElement: <ErrorPage />,
  },
];

export default routes;
