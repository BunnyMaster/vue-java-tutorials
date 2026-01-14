import TutorialList from "@/components/tutorial-list";
import day from "@/router/routes/day";
import findRoutes from "@/utils/findRoutes";
import { useEffect, useState } from "react";

const Day2 = () => {
  const [list, setList] = useState<any>([]);

  useEffect(() => {
    const list = findRoutes(day.routes, "Day2");
    setList(list);
  }, []);

  return <TutorialList list={list} />;
};

export default Day2;
