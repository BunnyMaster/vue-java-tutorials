# Vuex 状态映射与模块化最佳实践

## 一、状态访问方式对比

### 1. 直接访问方式
```javascript
// 原始访问方式（不推荐）
this.$store.state.schoolInfo.schoolName
this.$store.getters['schoolInfo/getSchoolName']
```

### 2. 映射辅助函数
```javascript
// 推荐使用mapState/mapGetters
import { mapState, mapGetters } from 'vuex'

computed: {
  // 数组写法（同名映射）
  ...mapState('schoolInfo', ['schoolName']),
  ...mapGetters('schoolInfo', ['getSchoolName']),
  
  // 对象写法（重命名）
  ...mapState('schoolInfo', {
    mySchoolName: 'schoolName' 
  }),
  ...mapGetters('schoolInfo', {
    formattedName: 'getSchoolName'
  })
}
```

## 二、模块化配置详解

### 1. 模块定义规范
```javascript
const schoolInfo = {
  namespaced: true,  // 必须开启命名空间
  state: () => ({    // 使用函数返回状态对象
    schoolName: "BunnySchool",
    schoolAddress: "昆山市印象欧洲"
  }),
  getters: {
    getSchoolName(state) {
      // 可组合其他getters
      return `${state.schoolName}---${state.schoolAddress}`
    }
  },
  mutations: {
    // 使用常量类型
    UPDATE_NAME(state, payload) {
      state.schoolName = payload
    }
  },
  actions: {
    async fetchSchoolInfo({ commit }) {
      const res = await api.getSchoolInfo()
      commit('UPDATE_NAME', res.data.name)
    }
  }
}
```

### 2. 模块注册方式
```javascript
// store/index.js
import schoolInfo from './modules/schoolInfo'

export default new Vuex.Store({
  modules: {
    schoolInfo  // 键名将作为命名空间前缀
  }
})
```

## 三、最佳实践指南

### 1. 命名规范建议
| 类型     | 命名规则    | 示例              |
| -------- | ----------- | ----------------- |
| 模块文件 | kebab-case  | `school-info.js`  |
| state    | camelCase   | `studentCount`    |
| mutation | 大写+下划线 | `UPDATE_INFO`     |
| getter   | 动词短语    | `getFilteredList` |
| action   | 业务动作    | `fetchSchoolData` |

### 2. 组件中使用建议
```javascript
export default {
  computed: {
    // 优先使用映射辅助函数
    ...mapState('schoolInfo', ['schoolName']),
    
    // 复杂计算使用本地计算属性
    formattedAddress() {
      return this.$store.state.schoolInfo.schoolAddress.replace('市', '')
    }
  },
  methods: {
    ...mapMutations('schoolInfo', ['UPDATE_NAME']),
    
    // 组合多个action
    async refreshData() {
      await this.$store.dispatch('schoolInfo/fetchSchoolInfo')
      this.loadComplete()
    }
  }
}
```

### 3. 代码组织技巧
```
store/
├── index.js          # 主入口
├── modules/
│   ├── school-info.js # 学校模块
│   └── user.js       # 用户模块
└── types.js          #  mutation类型常量
```

## 四、高级应用场景

### 1. 动态模块注册
```javascript
// 按需加载模块
export default {
  created() {
    import('./store/modules/school-info').then(module => {
      this.$store.registerModule('schoolInfo', module.default)
    })
  },
  destroyed() {
    this.$store.unregisterModule('schoolInfo')
  }
}
```

### 2. 模块复用
```javascript
// 创建可复用模块工厂
function createSchoolModule(initialName) {
  return {
    namespaced: true,
    state: () => ({
      schoolName: initialName
    }),
    // ...其他配置
  }
}
```

### 3. 插件开发
```javascript
// 状态持久化插件
const persistPlugin = store => {
  // 初始化时读取
  const savedState = localStorage.getItem('vuex_state')
  if (savedState) {
    store.replaceState(JSON.parse(savedState))
  }

  // 订阅mutation变化
  store.subscribe((mutation, state) => {
    localStorage.setItem('vuex_state', JSON.stringify(state))
  })
}
```

## 五、常见问题解决方案

1. **模块热更新**：
   ```javascript
   if (module.hot) {
     module.hot.accept(['./modules/school-info'], () => {
       store.hotUpdate({
         modules: {
           schoolInfo: require('./modules/school-info').default
         }
       })
     })
   }
   ```

2. **循环依赖处理**：
   - 使用全局getter解决跨模块访问
   - 通过rootState参数访问根状态

3. **TypeScript支持**：
   ```typescript
   // 定义模块类型
   interface SchoolState {
     schoolName: string
     schoolAddress: string
   }
   
   const schoolInfo: Module<SchoolState, RootState> = {
     namespaced: true,
     state: () => ({ /* 初始状态 */ })
   }
   ```
