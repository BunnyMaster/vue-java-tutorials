import { createHashRouter } from "react-router-dom"; // 修正导入来源
import routes from "./routes";

// History 模式
// const router = creteBrowserRouter(routes, {});

// 使用 Hash 模式
const router = createHashRouter(routes, {});

export default router;
