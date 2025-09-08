import { Link } from 'react-router-dom';

function App() {
  const list = [
    { name: '计数器', path: '/demo/conter' },
    { name: '计数器2', path: '/demo/2' },
  ];

  return (
    <>
      <h1>练习列表</h1>
      <ul>
        {list.map((item) => (
          <li key={item.name}>
            <Link to={item.path}>
              <span>{item.name}</span>
            </Link>
          </li>
        ))}
      </ul>
    </>
  );
}

export default App;
