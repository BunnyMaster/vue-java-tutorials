import Day1 from '@/pages/day1';
import Counter from '@/pages/day1/1-counter-page';
import FormPage from '@/pages/day1/2-form-page';
import CommentList from '@/pages/day1/3-comment-list';
import Day2 from '@/pages/day2';
import UseStateDemo from '@/pages/day2/1-useState';
import UseRefDemo from '@/pages/day2/2-useRef';
import ErrorPage from '@/pages/error/error-pages';
import { createBrowserRouter } from 'react-router-dom';

const day = createBrowserRouter([
  {
    id: 'Day1',
    path: '/day1',
    element: <Day1 />,
    errorElement: <ErrorPage />,
    children: [
      { id: '计数器', path: 'conter', element: <Counter /> },
      { id: '修改Form', path: 'form', element: <FormPage /> },
      { id: '评论列表', path: 'comment-list', element: <CommentList /> },
    ],
  },
  {
    id: 'Day2',
    path: '/day2',
    element: <Day2 />,
    errorElement: <ErrorPage />,
    children: [
      { id: 'UseState', path: 'use-state', element: <UseStateDemo /> },
      { id: 'UseRef', path: 'use-ref', element: <UseRefDemo /> },
    ],
  },
]);

export default day;
