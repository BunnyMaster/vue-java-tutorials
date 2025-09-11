import { lazy } from "react";
import { createBrowserRouter } from "react-router-dom";
const App = lazy(() => import("@/App"));
const RouterDemo1Detail = lazy(() => import("@/components/router-detail"));
const ErrorPage = lazy(() => import("@/pages/error/error-pages"));

const home = createBrowserRouter([
  {
    id: "Home",
    path: "/",
    element: <App />,
    errorElement: <ErrorPage />,
  },
  {
    id: "路由SearchParams详情页",
    path: "router-detail-search-params",
    element: <RouterDemo1Detail />,
    errorElement: <ErrorPage />,
  },
  {
    id: "路由Params详情页",
    path: "router-detail/:id/:name",
    element: <RouterDemo1Detail />,
    errorElement: <ErrorPage />,
  },
]);

export default home;
