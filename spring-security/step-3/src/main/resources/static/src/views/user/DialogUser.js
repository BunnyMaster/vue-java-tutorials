const DialogUser = defineComponent({
    name: "DialogUser",
    template: `
        <div class="modal fade" id="userBackdrop" data-bs-backdrop="static" data-bs-keyboard="false" tabindex="-1"
            aria-labelledby="userBackdropLabel" aria-hidden="true" ref="modalRef">
            <div class="modal-dialog">
                <div class="modal-content">
        
                    <!-- 头部 -->
                    <div class="modal-header">
                        <h5 class="modal-title">{{isAdd?"新增用户":"修改用户"}}</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>
        
                    <form @submit.prevent="onSubmit">
                        <!-- 内容 -->
                        <div class="modal-body">
        
                            <div class="mb-3">
                                <label class="form-label" for="dialogUsername"><i class="fas fa-user me-1"></i>用户名</label>
                                <input autocomplete="false" class="form-control" id="dialogUsername" placeholder="请输入用户名"
                                    type="text" v-model="form.username" required>
                                <div class="form-text">在这里输入你的用户名。</div>
                            </div>
        
                            <div class="mb-3">
                                <label class="form-label" for="dialogPassword"><i class="fas fa-lock me-1"></i>密码</label>
                                <input autocomplete="false" class="form-control" id="dialogPassword" placeholder="请输入密码"
                                    type="password" v-model="form.password">
                                <div class="form-text">如果不修改或添加不填写此项。</div>
                            </div>
        
                            <div class="mb-3">
                                <label class="form-label" for="dialogEmail"><i class="fas fa-envelope me-1"></i>邮箱</label>
                                <input autocomplete="false" class="form-control" id="dialogEmail" placeholder="请输入邮箱"
                                    type="email" v-model="form.email" required>
                                <div class="form-text">在这里输入你的邮箱。</div>
                            </div>
                        </div>
        
                        <!-- 底部 -->
                        <div class="modal-footer">
                            <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">取消</button>
                            <button type="submit" class="btn btn-primary">确认</button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    `,
    props: {
        // 是否添加
        isAdd: {type: Boolean, default: false},
        // 用户信息
        userinfo: {type: Object, required: true},
        // 加载函数
        onSearch: {type: Function, required: true},
    },
    data() {
        return {
            modalInstance: ref(null),
            form: ref({}),
        }
    },
    methods: {
        async onSubmit() {
            // 是否添加表单
            const {code, message} = this.isAdd ?
                await axiosInstance.post("/user", this.form) :
                await axiosInstance.put("/user", this.form);

            if (code === 200) {
                antd.message.success(message);
                // 关闭模态框
                this.modalInstance.hide();
                this.onSearch();
            }
        }
    },
    watch: {
        userinfo(val) {
            // 创建深拷贝，而不是直接赋值
            this.form = JSON.parse(JSON.stringify(val));
        },
    },
    mounted() {
        // 初始化模态框实例
        const modalEl = this.$refs.modalRef;
        this.modalInstance = new bootstrap.Modal(modalEl, {
            backdrop: 'static',
            keyboard: false
        });
    },
    beforeUnmount() {
        // 组件销毁时清理模态框实例
        if (this.modalInstance) {
            this.modalInstance.dispose();
        }
    }
});