import Vue from 'vue'
import BootstrapVue from 'bootstrap-vue/dist/bootstrap-vue.esm';
import 'bootstrap-vue/dist/bootstrap-vue.css';
import 'bootstrap/dist/css/bootstrap.css';

import router from './router';
import App from './App.vue';

Vue.use(BootstrapVue);

new Vue({
  el: '#app',
  router,
  render: h => h(App)
})
