import {useEffect, useState} from "react";
import "./index.scss";

interface Todos {
    id: number;
    title: string;
    completed: boolean;
    userId: number;
}

function MainAppUseEffect() {
    const [list, setList] = useState<Todos[]>([]);
    const [loading, setLoading] = useState<boolean>(true);
    const [error, setError] = useState<string | null>(null);

    /**
     * 获取列表
     */
    const fetchList = async () => {
        try {
            setLoading(true);
            setError(null);
            const response = await fetch("https://jsonplaceholder.typicode.com/todos");

            if (!response.ok) {
                throw new Error("获取数据失败");
            }

            const json: Todos[] = await response.json();
            // 只显示前20项以提高性能
            setList(json.slice(0, 20));
        } catch (err) {
            setError(err instanceof Error ? err.message : "未知错误");
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => {
        fetchList();
    }, []);

    // 渲染加载状态
    if (loading) {
        return (
            <div className="main-container">
                <div className="header">
                    <h2 className="title">待办 List</h2>
                    <p className="subtitle">数据加载中...</p>
                </div>
                <div className="loading-container">
                    <div className="spinner"></div>
                    <p>正在加载数据，请稍候...</p>
                </div>
            </div>
        );
    }

    // 渲染错误状态
    if (error) {
        return (
            <div className="main-container">
                <div className="header">
                    <h2 className="title">待办 List</h2>
                </div>
                <div className="error-container">
                    <p className="error-message">错误: {error}</p>
                    <button className="retry-button" onClick={fetchList}>
                        重新加载
                    </button>
                </div>
            </div>
        );
    }

    return (
        <div className="main-container">
            <div className="header">
                <h2 className="title">待办 List</h2>
                <p className="subtitle">来自 JSONPlaceholder 的示例数据</p>
            </div>

            <div className="todos-container">
                <ul className="todos-list">
                    {list.map((item) => (
                        <li key={item.id} className={`todo-item ${item.completed ? 'completed' : ''}`}>
                            <span className="todo-id">#{item.id}</span>
                            <span className="todo-title">{item.title}</span>
                            <span className={`status ${item.completed ? 'done' : 'pending'}`}>
                {item.completed ? '已完成' : '待完成'}
              </span>
                        </li>
                    ))}
                </ul>
            </div>

            <div className="footer">
                <button className="refresh-button" onClick={fetchList}>
                    刷新数据
                </button>
            </div>
        </div>
    );
}

export default MainAppUseEffect;