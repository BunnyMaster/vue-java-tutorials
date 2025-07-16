const AssignPermission = defineComponent({
    name: "AssignPermission",
    template: `
        <div class="offcanvas offcanvas-start" data-bs-scroll="true" id="assignPermissionOffCanvas" 
            aria-labelledby="assignPermissionOffCanvasLabel" ref="assignPermissionOffCanvasRef">
          <div class="offcanvas-header">
            <h5 class="offcanvas-title" id="assignPermissionOffCanvasLabel">
                为角色分配权限
                <a href="JavaScript:" class="icon-link icon-link-hover text-decoration-none" @click="onSave">
                    <i class="fas fa-save"></i>
                    保存
                </a>                
            </h5>
            <button type="button" class="btn-close" data-bs-dismiss="offcanvas" aria-label="Close"></button>
          </div>
          <div class="offcanvas-body">
            <div class="form-check" v-for="(permission,index) in permissionList" :key="permission.id">
              <input class="form-check-input" type="checkbox" v-model="permission.checked" :id="permission.permissionCode">
              <label class="form-check-label" :for="permission.permissionCode">
                {{permission.remark}}
              </label> 
            </div>
          </div>
        </div>
    `,
    props: {
        roleInfo: {type: Object, required: true},
    },
    data() {
        return {
            // 所有的权限列表
            permissionList: ref([]),
            // 角色权限列表
            rolePermissionIds: ref([]),
            modalInstance: ref(null)
        };
    },
    methods: {
        /* 获取权限列表 */
        async getPermissionList() {
            const {data} = await axiosInstance.get("/permission/all");
            this.permissionList = data;
        },

        /* 保存分配角色权限 */
        async onSave() {
            // 过滤出已经被选择的权限
            const checkedPermissionList = this.permissionList
                .filter(role => role.checked)
                .map(role => role.id);

            // 分配的数据内容
            const data = {
                roleId: this.roleInfo.id,
                permissionIds: checkedPermissionList,
            }

            // 为角色分配权限
            const {code, message} = await axiosInstance.post("/role-permission/assign-permission", data)
            if (code === 200) {
                this.modalInstance.hide();
                this.rolePermissionIds = [];
                antd.message.success(message);
            }
        },
    },
    watch: {
        /* 监视角色信息 */
        async roleInfo(value) {
            const {id} = value;
            // 如果没有id直接返回
            if (!id) return;
            
            // 获取权限列表
            await this.getPermissionList();

            // 获取角色拥有的权限
            const {data} = await axiosInstance.get("/role-permission/permissions", {params: {userId: id}});
            // 提取角色拥有的权限ID数组
            const rolePermissionIds = data.map(permission => permission.permissionId + "");

            // 遍历所有权限，检查角色是否拥有该权限
            this.permissionList.forEach(role => {
                const hasRole = rolePermissionIds.includes(role.id);
                if (hasRole) role.checked = true;
            })
        }
    },
    mounted() {
        // 初始化模态框实例
        const modalEl = this.$refs.assignPermissionOffCanvasRef;
        this.modalInstance = new bootstrap.Offcanvas(modalEl);
    }
});