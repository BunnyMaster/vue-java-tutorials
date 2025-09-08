import Conter from '@/pages/demo/1-counter-page';
import ErrorPage from '@/pages/error/error-pages';
import { createBrowserRouter } from 'react-router-dom';

const demo = createBrowserRouter([
  {
    id: 'Demo',
    path: '/demo',
    errorElement: <ErrorPage />,
    children: [
      { id: 'Conter', path: 'conter', element: <Conter /> },
    //   { id: 'Conter', path: 'conter', element: <Conter /> },
    ],
  },
]);

export default demo;
