import { Link, useParams, useSearchParams } from "react-router-dom";
import "./index.scss";

function RouterDemo1Detail() {
  const [searParams] = useSearchParams();
  const params = useParams();

  return (
    <div className="router-detail">
      <div className="detail-container">
        <h3>路由详情页</h3>

        <Link to="/" className="back-link">
          ← 返回首页
        </Link>

        <div className="param-section">
          <h4>查询参数 (useSearchParams)</h4>
          <div className="param-item">
            <span className="param-label">ID:</span>
            <span className="param-value">{searParams.get("id") || "无"}</span>
          </div>
        </div>

        <div className="param-section">
          <h4>路径参数 (useParams)</h4>
          <div className="param-item">
            <span className="param-label">ID:</span>
            <span className="param-value">{params.id || "无"}</span>
          </div>
          <div className="param-item">
            <span className="param-label">Name:</span>
            <span className="param-value">{params.name || "无"}</span>
          </div>
        </div>
      </div>
    </div>
  );
}

export default RouterDemo1Detail;
