import ErrorPage from "@/pages/error/error-pages";
import day from "./day";
import home from "./home";

const routes = [
  ...home.routes,
  ...day.routes,
  {
    path: "/error",
    element: <ErrorPage />,
    errorElement: <ErrorPage />,
  },
];

export default routes;
