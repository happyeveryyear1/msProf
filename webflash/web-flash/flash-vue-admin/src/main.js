import Vue from 'vue'

import 'normalize.css/normalize.css' // A modern alternative to CSS resets

import ElementUI from 'element-ui'
import 'element-ui/lib/theme-chalk/index.css'

import ECharts from 'vue-echarts/components/ECharts.vue'
import '@/styles/index.scss' // global css

import App from './App'
import store from './store'
import router from './router'
import i18n from './lang' // Internationalization
import '@/icons' // icon
import '@/permission' // permission control
import crypto from 'crypto'
import VueResource from 'vue-resource'
import axios from 'axios'
import echarts from 'echarts'

/**
 * If you don't want to use mock-server
 * you want to use MockJs for mock api
 * you can execute: mockXHR()
 *
 * Currently MockJs will be used in the production environment,
 * please remove it before going online! ! !
 */

// set ElementUI lang to EN
Vue.use(ElementUI, { i18n: (key, value) => i18n.t(key, value) })

Vue.component('v-chart', ECharts)
Vue.config.productionTip = false
Vue.prototype.$crypto = crypto
Vue.use(VueResource)
Vue.prototype.$echarts = echarts

axios.defaults.withCredentials = true

new Vue({
    el: '#app',
    router,
    store,
    i18n,
    render: h => h(App)
})