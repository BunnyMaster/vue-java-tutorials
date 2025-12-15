import type { RouteInfo } from "@/components/tutorial-list";
import { type RouteObject } from "react-router-dom";

export const findRoutes = (routes: RouteObject[], id: number | string) => {
  const routeList = routes.find((route) => route.id === id);
  if (!routeList) return [];

  const children = routeList.children;
  if (!children) return [];

  function findChildren(childrenRoutes: RouteObject[]): RouteInfo[] {
    if (!childrenRoutes || childrenRoutes.length === 0) return [];

    return childrenRoutes.map((route) => ({
      id: route.id,
      name: route.id,
      path: route.path || "",
      children: route.children ? findChildren(route.children) : undefined,
    }));
  }

  return findChildren(children as RouteObject[]);
};

export default findRoutes;
