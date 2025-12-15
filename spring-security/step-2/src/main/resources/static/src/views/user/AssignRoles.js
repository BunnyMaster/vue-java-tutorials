const AssignRoles = defineComponent({
    name: "AssignRoles",
    template: `
        <div class="offcanvas offcanvas-start" data-bs-scroll="true" id="assignRoleOffCanvas" 
            aria-labelledby="assignRoleOffCanvasLabel" ref="assignRoleOffCanvasRef">
          <div class="offcanvas-header">
            <h5 class="offcanvas-title" id="assignRoleOffCanvasLabel">
                为用户分配角色
                <a href="JavaScript:" class="icon-link icon-link-hover text-decoration-none" @click="onSave">
                    <i class="fas fa-save"></i>
                    保存
                </a>                
            </h5>
            <button type="button" class="btn-close" data-bs-dismiss="offcanvas" aria-label="Close"></button>
          </div>
          <div class="offcanvas-body">
            <div class="form-check" v-for="(role,index) in roleList" :key="role.id">
              <input class="form-check-input" type="checkbox" v-model="role.checked" :id="role.roleCode">
              <label class="form-check-label" :for="role.roleCode">
                {{role.remark}}
              </label> 
            </div>
          </div>
        </div>
    `,
    props: {
        userinfo: {type: Object, required: true},
    },
    data() {
        return {
            // 所有的角色列表
            roleList: ref([]),
            // 用户角色列表
            userRoleIds: ref([]),
            modalInstance: ref(null)
        };
    },
    methods: {
        /* 获取角色列表 */
        async getRoleList() {
            const {data} = await axiosInstance.get("/role/all");
            this.roleList = data;
        },

        /* 保存分配用户角色 */
        async onSave() {
            // 过滤出已经被选择的角色
            const checkedRoleList = this.roleList
                .filter(role => role.checked)
                .map(role => role.id);

            // 分配的数据内容
            const data = {
                userId: this.userinfo.id,
                roleIds: checkedRoleList,
            }

            // 为用户分配角色
            const {code, message} = await axiosInstance.post("/user-role/assign-role", data)
            if (code === 200) {
                this.modalInstance.hide();
                this.userRoleIds = [];
                antd.message.success(message);
            }
        },
    },
    watch: {
        /* 监视用户信息 */
        async userinfo(value) {
            const {id} = value;
            // 如果没有id直接返回
            if (!id) return;

            // 获取角色列表
            await this.getRoleList();

            // 获取用户拥有的角色
            const {data} = await axiosInstance.get("/user-role/roles", {params: {userId: id}});
            // 提取用户拥有的角色ID数组
            const userRoleIds = data.map(role => role.roleId + "");

            // 遍历所有角色，检查用户是否拥有该角色
            this.roleList.forEach(role => {
                const hasRole = userRoleIds.includes(role.id);
                if (hasRole) role.checked = true;
            })
        }
    },
    mounted() {
        // 初始化模态框实例
        const modalEl = this.$refs.assignRoleOffCanvasRef;
        this.modalInstance = new bootstrap.Offcanvas(modalEl);
    }
});