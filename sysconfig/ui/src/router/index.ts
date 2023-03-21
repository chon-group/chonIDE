import {createRouter, createWebHistory, RouteRecordRaw} from 'vue-router'
import Login from "@/views/Login.vue";
import Connect from "@/views/Connect.vue";
import Domain from "@/views/Domain.vue";
import Project from "@/views/Project/Project.vue";
import Home from "@/views/Home.vue";

export class Routes {
    public static LOGIN = "/login";
    public static CONNECT = "/connect";
    public static DOMAIN = "/domain";
    public static HOME = "/home";
    public static PROJECT = "/projects/";
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
        path: Routes.HOME,
        name: 'Home',
        component: Home
    },
    {
        path: Routes.PROJECT + ":id",
        name: 'Project',
        component: Project
    }
]

const router = createRouter({
    history: createWebHistory(process.env.BASE_URL),
    routes
});

export default router
