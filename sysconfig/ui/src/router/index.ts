import {createRouter, createWebHistory, RouteRecordRaw} from 'vue-router'
import Login from "@/views/Login.vue";
import Connect from "@/views/Connect.vue";
import Manager from "@/views/Manager.vue";
import Domain from "@/views/Domain.vue";
import Coder from "@/views/Coder.vue";


const routes: Array<RouteRecordRaw> = [
    {
        path: '/',
        name: 'Login',
        component: Login
    },
    {
        path: '/connect',
        name: 'Connect',
        component: Connect
    },
    {
        path: '/manager',
        name: 'Manager',
        component: Manager
    },
    {
        path: '/domain',
        name: 'Domain',
        component: Domain
    },
    {
        path: '/coder',
        name: 'Coder',
        component: Coder
    }
]

const router = createRouter({
    history: createWebHistory(process.env.BASE_URL),
    routes
})

export default router