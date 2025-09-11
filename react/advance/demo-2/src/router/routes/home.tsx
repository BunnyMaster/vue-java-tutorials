import App from "@/App";
import RouterDemo1Detail from "@/components/router-detail";
import ErrorPage from "@/pages/error/error-pages";
import { createBrowserRouter } from "react-router-dom";

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
