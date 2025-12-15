import NotFound from "@/components/not-found";
import Day1 from "@/pages/day1";
import Counter from "@/pages/day1/1-counter-page";
import FormPage from "@/pages/day1/2-form-page";
import CommentList from "@/pages/day1/3-comment-list";
import Day2 from "@/pages/day2";
import UseStateDemo from "@/pages/day2/1-useState";
import MainAppCustomerHook from "@/pages/day2/10-customer-hook-1";
import UseRefDemo from "@/pages/day2/2-useRef";
import PublishComment from "@/pages/day2/3-publish-comment";
import MainAppFunc from "@/pages/day2/4-son-func";
import MainAppSolt from "@/pages/day2/5-son-solt";
import MainAppContext from "@/pages/day2/6-context";
import MainAppUseEffect from "@/pages/day2/7-use-effect";
import MainAppUseEffectSideEffect from "@/pages/day2/8-use-effect-side-effect";
import MainAppEffectClearEffect from "@/pages/day2/9-use-effect-clear-side-effect";
import Day3 from "@/pages/day3";
import ReduxInduction from "@/pages/day3/1-redux-induction";
import ReduxActionDemo from "@/pages/day3/2-redux-action";
import ReduxAsyncDemo from "@/pages/day3/3-redux-async";
import Day4 from "@/pages/day4";
import RouterDemo1 from "@/pages/day4/1-router";
import RouterDemoHook from "@/pages/day4/2-router-hook";
import ErrorPage from "@/pages/error/error-pages";
import { createBrowserRouter } from "react-router-dom";

const day = createBrowserRouter([
  {
    id: "Day1",
    path: "/day1",
    element: <Day1 />,
    errorElement: <ErrorPage />,
    children: [
      { id: "计数器", path: "conter", element: <Counter /> },
      { id: "修改Form", path: "form", element: <FormPage /> },
      { id: "评论列表", path: "comment-list", element: <CommentList /> },
    ],
  },
  {
    id: "Day2",
    path: "/day2",
    element: <Day2 />,
    errorElement: <ErrorPage />,
    children: [
      {
        id: "UseState",
        // 设置默认路由
        index: true,
        element: <UseStateDemo />,
      },
      { id: "UseRef", path: "use-ref", element: <UseRefDemo /> },
      { id: "发布评论", path: "publish-comment", element: <PublishComment /> },
      { id: "父传子-函数", path: "son-fun", element: <MainAppFunc /> },
      { id: "父传子-插槽", path: "son-solt", element: <MainAppSolt /> },
      { id: "父传子-上下文", path: "son-context", element: <MainAppContext /> },
      {
        id: "useEffect入门",
        path: "use-effect-1",
        element: <MainAppUseEffect />,
      },
      {
        id: "useEffect副作用",
        path: "use-effect-2",
        element: <MainAppUseEffectSideEffect />,
      },
      {
        id: "useEffect副作用清楚",
        path: "use-effect-3",
        element: <MainAppEffectClearEffect />,
      },
      {
        id: "自定义Hook",
        path: "use-custom-hook",
        element: <MainAppCustomerHook />,
      },
    ],
  },
  {
    id: "Day3",
    path: "day3",
    element: <Day3 />,
    children: [
      { id: "Redux入门", path: "redux-1", element: <ReduxInduction /> },
      { id: "Redux-Action", path: "redux-2", element: <ReduxActionDemo /> },
      { id: "Redux-Async", path: "redux-3", element: <ReduxAsyncDemo /> },
    ],
  },
  {
    id: "Day4",
    // 严格模式，区分大小写
    // caseSensitive: true,
    path: "day4",
    element: <Day4 />,
    children: [
      { id: "路由-1", path: "router-1", element: <RouterDemo1 /> },
      { id: "路由钩子", path: "router-hook", element: <RouterDemoHook /> },
    ],
  },
  { path: "*", element: <NotFound /> },
]);

export default day;
