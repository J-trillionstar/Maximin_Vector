import App from './App'

import uView from '@/uni_modules/uview-ui'
Vue.use(uView)

// #ifndef VUE3
import Vue from 'vue'
// Vue.prototype.g_uid = ''
// Vue.prototype.g_openid = ''
// Vue.prototype.g_phone = ''
// Vue.prototype.g_username = ''
// Vue.prototype.g_imageurl = ''
// Vue.prototype.g_kind = -1
// Vue.prototype.g_password = false
// Vue.prototype.wx_img = ''
// Vue.prototype.urlhead = "http://localhost:8080"
Vue.prototype.urlhead = "https://www.trillionstar.top"
Vue.config.productionTip = false
App.mpType = 'app'
const app = new Vue({
    ...App
})
app.$mount()
// #endif

// #ifdef VUE3
import { createSSRApp } from 'vue'
export function createApp() {
  const app = createSSRApp(App)
  return {
    app
  }
}
// #endif