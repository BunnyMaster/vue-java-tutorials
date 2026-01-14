import Cart from './components/Cart';
import FoodsCategory from './components/FoodsCategory';
import Menu from './components/Menu';
import NavBar from './components/NavBar';

import { useEffect } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import './App.scss';
import { fetchFoodList } from './store/modules/takeaway';

const App = () => {
  const { foodList, activeIndex } = useSelector((state) => state.foods);
  const dispatch = useDispatch();

  useEffect(() => {
    dispatch(fetchFoodList());
  }, [dispatch]);

  return (
    <div className="home">
      {/* 导航 */}
      <NavBar />

      {/* 内容 */}
      <div className="content-wrap">
        <div className="content">
          <Menu foodList={foodList} />

          <div className="list-content">
            <div className="goods-list">
              {/* 外卖商品列表 */}
              {foodList.map((item, index) => {
                return (
                  activeIndex === index && (
                    <FoodsCategory
                      key={item.tag}
                      // 列表标题
                      name={item.name}
                      // 列表商品
                      foods={item.foods}
                    />
                  )
                );
              })}
            </div>
          </div>
        </div>
      </div>

      {/* 购物车 */}
      <Cart />
    </div>
  );
};

export default App;
