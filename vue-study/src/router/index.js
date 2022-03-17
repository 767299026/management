import Vue from 'vue'
import VueRouter from 'vue-router'
import Login from '../views/Login.vue'
import Home from "../views/Home.vue";
import welcome from "../components/welcome";
import user from "../components/user";

Vue.use(VueRouter)

const routes = [
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
        }
    ]
  }
]

const router = new VueRouter({
  routes
})

export default router
