import "./index.scss";

interface SonProps {
  onHandleSon: () => void;
}

function Son({ onHandleSon }: SonProps) {
  return (
    <div className="son-container">
      <button onClick={onHandleSon} className="son-button">
        Click Me (Son)
      </button>
    </div>
  );
}

function MainAppFunc() {
  const handleSon = () => {
    alert("son click");
  };

  return (
    <div className="main-container">
      <h2 className="title">父传子-函数方式</h2>

      <div className="content">
        <Son onHandleSon={handleSon} />
      </div>
    </div>
  );
}

export default MainAppFunc;
