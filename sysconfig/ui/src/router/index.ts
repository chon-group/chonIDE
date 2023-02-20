import {createRouter, createWebHistory, RouteRecordRaw} from 'vue-router'
import Login from "@/views/Login.vue";
import Connect from "@/views/Connect.vue";
import Domain from "@/views/Domain.vue";
import Coder from "@/views/Coder.vue";

export class Routes {
    public static LOGIN = "/login";
    public static CONNECT = "/connect";
    public static CODER = "/coder";
    public static DOMAIN = "/domain";
}

const routes: Array<RouteRecordRaw> = [
    {
        path: Routes.LOGIN,
        name: 'Login',
        component: Login
    },
    {
        path: Routes.CONNECT,
        name: 'Connect',
        component: Connect
    },
    {
        path: Routes.DOMAIN,
        name: 'Domain',
        component: Domain
    },
    {
        path: Routes.CODER,
        name: 'Coder',
        component: Coder
    }
]

const router = createRouter({
    history: createWebHistory(process.env.BASE_URL),
    routes
});

export default router
