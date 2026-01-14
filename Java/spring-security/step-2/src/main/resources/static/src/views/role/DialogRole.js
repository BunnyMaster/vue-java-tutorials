const DialogRole = defineComponent({
    name: "DialogRole",
    template: `
        <div class="modal fade" id="roleBackdrop" data-bs-backdrop="static" data-bs-keyboard="false" tabindex="-1"
            aria-labelledby="roleBackdropLabel" ref="modalRef">
            <div class="modal-dialog">
                <div class="modal-content">
        
                    <!-- 头部 -->
                    <div class="modal-header">
                        <h5 class="modal-title">{{isAdd?"新增角色":"修改角色"}}</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>
        
                    <form @submit.prevent="onSubmit">
                        <!-- 内容 -->
                        <div class="modal-body">
                            <div class="mb-3">
                                <label class="form-label" for="dialogRoleCode"><i class="fas fa-user-alt me-1"></i>角色码</label>
                                <input autocomplete="false" class="form-control" id="dialogRoleCode" placeholder="请输入角色名"
                                    type="text" v-model="form.roleCode" required>
                                <div class="form-text">在这里输入你的角色码。</div>
                            </div>
                            <div class="mb-3">
                                <label class="form-label" for="dialogDescription"><i class="fas fa-user-alt me-1"></i>描述</label>
                                <input autocomplete="false" class="form-control" id="dialogDescription" placeholder="请输入描述"
                                    type="text" v-model="form.description" required>
                                <div class="form-text">在这里输入你的描述。</div>
                            </div>
                            <div class="mb-3">
                                <label class="form-label" for="dialogRemark"><i class="fas fa-user-alt me-1"></i>简述</label>
                                <input autocomplete="false" class="form-control" id="dialogRemark" placeholder="请输入简述"
                                    type="text" v-model="form.remark" required>
                                <div class="form-text">在这里输入你的简述。</div>
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
        // 角色信息
        roleInfo: {type: Object, required: true},
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
                await axiosInstance.post("/role", this.form) :
                await axiosInstance.put("/role", this.form);

            if (code === 200) {
                antd.message.success(message);
                // 关闭模态框
                this.modalInstance.hide();
                this.onSearch();
            }
        }
    },
    watch: {
        roleInfo(val) {
            // 创建深拷贝，而不是直接赋值
            this.form = JSON.parse(JSON.stringify(val));
        }
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