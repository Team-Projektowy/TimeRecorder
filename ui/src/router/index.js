import Vue from 'vue'
import VueRouter from 'vue-router'
import Home from '../views/Home.vue'
import Login from "../views/Auth/Login";
import Register from "../views/Auth/Register";
import store from '../store/index';

Vue.use(VueRouter)

const ifAuthenticated = (to, from, next) => {
  if (store.getters.isLogged) {
    next();
  } else {
    next('/login');
  }
}

const routes = [
  {
    path: '/',
    name: 'Home',
    component: Home,
    beforeEnter: ifAuthenticated,
  },
  {
    path: '/login',
    name: 'Login',
    component: Login,
    beforeEnter: (to, from, next) => {
      if (!store.getters.isLogged) {
        next();
      } else {
        next(from);
      }
    }
  },
  {
    path: '/register',
    name: 'Register',
    component: Register,
    beforeEnter: ifAuthenticated
  },
  {
    path: "/error/:code",
    name: "Error",
    component: Error,
  },
  {
    path: "*",
    name: 'PageNotFound',
    component: Error,
    beforeEnter: (to, from, next) => {
      next("/error/404")
    }
  },
]

const router = new VueRouter({
  mode: 'history',
  routes,
})

export default router;
