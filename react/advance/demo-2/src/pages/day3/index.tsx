import TutorialList from "@/components/tutorial-list";
import day from "@/router/routes/day";
import findRoutes from "@/utils/findRoutes";
import { useEffect, useState } from "react";

function Day3() {
  const [list, setList] = useState<any>([]);

  useEffect(() => {
    const list = findRoutes(day.routes, "Day3");
    setList(list);
  }, []);

  return <TutorialList list={list} />;
}

export default Day3;
