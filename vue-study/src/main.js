import Vue from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import axios from "../src/api";
import Element from 'element-ui'
import 'element-ui/lib/theme-chalk/index.css'
import './assets/css/global.css'
import './assets/fonts/iconfont.css'
import './utils/directive'


Vue.config.productionTip = false
Vue.use(Element)
Vue.prototype.$axios = axios

new Vue({
  router,
  store,
  render: h => h(App)
}).$mount('#app')
