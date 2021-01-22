import Vue from 'vue'
import Vuex from 'vuex'
import router from '../router'

Vue.use(Vuex);

const types = {
  LOGIN: 'LOGIN',
  LOGOUT: 'LOGOUT'
};

const state = {
  logged: localStorage.getItem('token'),
  user: localStorage.getItem('user'),
};

const getters = {
  isLogged: state => state.logged,
  user: state => state.user,
};

const actions = {
  login({ commit }, user) {
    commit(types.LOGIN);
    localStorage.setItem('user', user);
    router.push({ path: '/' });
  },

  logout({ commit }) {
    localStorage.removeItem('token');
    commit(types.LOGOUT);
  }
};

const mutations = {
  [types.LOGIN](state) {
    state.logged = 1;
  },
  [types.LOGOUT](state) {
    state.logged = 0;
  }
};

export default new Vuex.Store({
  state,
  getters,
  actions,
  mutations
})
