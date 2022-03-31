import Vue from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import axios from "../src/api";
/*import Element from 'element-ui'
import 'element-ui/lib/theme-chalk/index.css'*/
import ZkTable from 'vue-table-with-tree-grid'
import './assets/css/global.css'
import './assets/fonts/iconfont.css'
import './utils/directive'
import QuillEditor from 'vue-quill-editor';
import 'quill/dist/quill.core.css'
import 'quill/dist/quill.snow.css'
import 'quill/dist/quill.bubble.css'

Vue.config.productionTip = false
/*Vue.use(Element)*/
Vue.use(QuillEditor)
Vue.component('tree-table', ZkTable)
Vue.prototype.$axios = axios

new Vue({
  router,
  store,
  render: h => h(App)
}).$mount('#app')
