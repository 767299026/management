import Vue from 'vue'
import VueRouter from 'vue-router'
import Login from '../views/Login.vue'
import Home from "../views/Home.vue";
import welcome from "../components/home/welcome";
import user from "../components/user/user";
import rights from "../components/rights/rights";
import roles from "../components/rights/roles";
import cate from "../components/goods/cate";
import params from "../components/goods/params";
import order from "../components/order/order";
import report from "@/components/report/report";

Vue.use(VueRouter)

const routes = [
    {
        path: '/',
        redirect: '/login'
    },
    {
        name: Login,
        path: '/login',
        component: Login
    },
    {
        name: Home,
        path: '/home',
        component: Home,
        redirect: '/welcome',
        children: [
        {
            name: welcome,
            path: '/welcome',
            component: welcome
        },
        {
            name: user,
            path: '/users',
            component: user
        },
        {
            name: rights,
            path: '/rights',
            component: rights
        },
            {
                name: roles,
                path: '/roles',
                component: roles
            },
            {
                name: cate,
                path: '/categories',
                component: cate
            },
            {
                name: params,
                path: '/params',
                component: params
            },
            {
                name:order,
                path: '/orders',
                component: order
            },
            {
                name:report,
                path: '/reports',
                component: report
            }
        ]
  }
]

const router = new VueRouter({
  routes
})

export default router
