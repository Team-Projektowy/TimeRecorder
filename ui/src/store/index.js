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
  user: state => JSON.parse(state.user),
};

const actions = {
  login({ commit }, params) {
    commit(types.LOGIN);
    localStorage.setItem('token', params.token);
    localStorage.setItem('user', JSON.stringify(params.user));
    router.push({ path: '/' });
  },

  logout({ commit }) {
    localStorage.removeItem('token');
    localStorage.removeItem('user');
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
