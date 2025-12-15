const {defineComponent} = Vue;

const HeaderNavs = defineComponent({
    name: 'HeaderNavs',
    template: `
        <ul class="nav nav-tabs justify-content-center my-1">
            <li class="nav-item" v-for="(nav,index) in navList" :key="index">
                <a class="nav-link" aria-current="page" :class="{active:activeItem(nav)}"
                    :href="nav.href">{{nav.title}}</a>
            </li>
        </ul>
    `,
    data() {
        return {
            navList: ref([
                {href: "/login", title: "登录页面"},
                {href: "/user", title: "用户页面"},
                {href: "/role", title: "角色页面"},
                {href: "/permission", title: "权限页面"},
            ])
        };
    },
    methods: {
        activeItem(nav) {
            const pathname = window.location.pathname;
            return pathname === nav.href;
        },
    },
})