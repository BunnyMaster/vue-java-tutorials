import { Link, useNavigate } from "react-router-dom";

import "./index.scss";

function RouterDemo1() {
  const navigate = useNavigate();

  return (
    <div className="router-demo">
      <h3>RouterDemo1</h3>

      <Link to="/day2" className="nav-link">
        跳转到day2
      </Link>
      <button onClick={() => navigate("/day3")} className="nav-button">
        跳转到day3
      </button>
    </div>
  );
}

export default RouterDemo1;
