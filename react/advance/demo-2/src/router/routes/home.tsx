import App from '@/App';
import Counter from '@/pages/demo/1-counter-page';
import FormPage from '@/pages/demo/2-form-page';
import CommentList from '@/pages/demo/3-comment-list';
import ErrorPage from '@/pages/error/error-pages';
import { createBrowserRouter } from 'react-router-dom';

const demo = createBrowserRouter([
  {
    id: 'Demo',
    path: '/demo',
    errorElement: <ErrorPage />,
    children: [
      { id: 'Conter', path: 'conter', element: <Counter /> },
      { id: 'Form', path: 'form', element: <FormPage /> },
      { id: 'CommentList', path: 'comment-list', element: <CommentList /> },
    ],
  },
]);

const home = createBrowserRouter([
  {
    id: 'Home',
    path: '/',
    element: <App />,
    errorElement: <ErrorPage />,
    children: [...demo.routes],
  },
]);

export default home;
