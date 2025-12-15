import "@/styles/index.css";
import { StrictMode, Suspense } from "react";
import { createRoot } from "react-dom/client";
import { Provider } from "react-redux";
import { RouterProvider } from "react-router-dom";
import Loading from "./components/Loading";
import router from "./router/index";
import store from "./store";

createRoot(document.getElementById("root")!).render(
  <StrictMode>
    <Provider store={store}>
      <Suspense fallback={<Loading />}>
        <RouterProvider router={router} future={{ v7_startTransition: true }} />
      </Suspense>
    </Provider>
  </StrictMode>
);
