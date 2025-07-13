/*
    // pagination 类型
    const pagination = {
        // 当前页
        pageNo: 0,
        // 分页大小
        pageSize: 0,
        // 总分页数
        pages: 0,
    }
*/

const Pagination = defineComponent({
    name: "Pagination",
    template: `
    <nav aria-label="Page navigation" class="mt-3">
        <ul class="pagination justify-content-center">
            <li class="page-item" :class="{disabled:this.pagination.pageNo == 1}">
                <a @click="pageNoDecrease" class="page-link" href="javascript:">上一页</a>
            </li>
            
            <li :class="{active:page===pagination.pageNo}" :key="index" class="page-item"
                v-for="(page,index) in pagination.pages">
                <a class="page-link" href="javascript:" @click="onCurrentPageClick(page)">{{page}}</a>
            </li>
            
            <li class="page-item" :class="{disabled:this.pagination.pageNo >= this.pagination.pages}">
                <a @click="pageNoIncrease"  class="page-link" href="javascript:">下一页</a>
            </li>
        </ul>
    </nav>
    `,
    props: {
        pagination: {type: Object, required: true},
        // 初始化加载数据
        onSearch: {type: Function, required: true},
    },
    data() {
        return {};
    },
    methods: {
        /* 当前分页+1 */
        pageNoIncrease() {
            this.pagination.pageNo++;
            this.$emit("update:pagination", this.pagination);
            this.onSearch();
        },
        /* 当前分页-1 */
        pageNoDecrease() {
            this.pagination.pageNo--;
            this.$emit("update:pagination", this.pagination);
            this.onSearch();
        },
        /* 点击当前页 */
        onCurrentPageClick(page) {
            this.pagination.pageNo = page;
            this.$emit("update:pagination", this.pagination);
            this.onSearch();
        }
    }
})