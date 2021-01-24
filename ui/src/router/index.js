import Vue from 'vue'
import VueRouter from 'vue-router'
import Home from '../views/Home/Index'
import Login from "../views/Auth/Login";
import Register from "../views/Users/Register";
import store from '../store/index';
import Error from "../views/Error/Error";
import TimeReport from "../views/TimeReport";
import TasksAll from "../views/Tasks/TasksAll";
import TaskEdit from "../views/Tasks/TaskEdit";
import TaskCreate from "../views/Tasks/TaskCreate";
import UsersAll from "../views/Users/UsersAll";
import UserEdit from "../views/Users/UserEdit";
import ChangePassword from "../views/Auth/ChangePassword";

Vue.use(VueRouter)

const ifAuthenticated = (to, from, next) => {
  if (store.getters.isLogged) {
    next();
  } else {
    next('/login');
  }
}

const ifAdmin = (to, from, next) => {
  if (store.getters.isLogged && store.getters.user.admin) {
    next();
  } else {
    next("/error/403");
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
    path: '/change-password',
    name: 'ChangePassword',
    component: ChangePassword,
    beforeEnter: ifAuthenticated,
  },
  {
    path: '/users',
    name: 'UsersAll',
    component: UsersAll,
    beforeEnter: ifAdmin
  },
  {
    path: '/users/create',
    name: 'Register',
    component: Register,
    beforeEnter: ifAdmin
  },
  {
    path: '/users/:userId/edit',
    name: 'UsersEdit',
    component: UserEdit,
    beforeEnter: ifAdmin
  },
  {
    path: '/time-report',
    name: 'TimeReport',
    component: TimeReport,
    beforeEnter: ifAdmin
  },
  {
    path: '/tasks',
    name: 'TasksAll',
    component: TasksAll,
    beforeEnter: ifAdmin
  },
  {
    path: '/tasks/create',
    name: 'TaskCreate',
    component: TaskCreate,
    beforeEnter: ifAdmin
  },
  {
    path: '/tasks/:taskId/edit',
    name: 'TaskEdit',
    component: TaskEdit,
    beforeEnter: ifAdmin
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
