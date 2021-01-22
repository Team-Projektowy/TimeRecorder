import '@babel/polyfill'
import 'mutationobserver-shim'
import Vue from 'vue'
import './plugins/bootstrap-vue'
import App from './App.vue'
import router from './router/index'
import store from './store'
import VueResource from 'vue-resource';
import Vuelidate from 'vuelidate';

Vue.config.productionTip = false
Vue.prototype.$serverUrl = 'http://localhost:8080';
Vue.use(VueResource);
Vue.use(Vuelidate);

Vue.http.interceptors.push((request, next) => {
  if (localStorage.getItem('token')) {
    request.headers.set('Authorization', 'Bearer ' + localStorage.getItem('token'));
  }

  next(response => {
    const statusCode = response.status;
    if (statusCode === 401) {
      router.push({ name: "Login" });
      return;
    }
    const statusCodeGroup = Math.floor(statusCode / 100);
    if (statusCodeGroup === 4 || statusCodeGroup === 5) {
      router.push({ path: "/error/" + statusCode });
    }
  })
});

new Vue({
  router,
  store,
  render: h => h(App)
}).$mount('#app')
