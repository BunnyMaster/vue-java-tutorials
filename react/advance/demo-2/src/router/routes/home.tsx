import App from "@/App";
import ErrorPage from "@/pages/error/error-pages";
import { createBrowserRouter } from "react-router-dom";

const home = createBrowserRouter([
  {
    id: "Home",
    path: "/",
    element: <App />,
    errorElement: <ErrorPage />,
  },
]);

export default home;
