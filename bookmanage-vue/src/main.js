import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import ElementPlus from 'element-plus'
import * as Elicons from '@element-plus/icons-vue'
import 'element-plus/dist/index.css'
const app=createApp(App)
for (const  name in Elicons){
    app.component(name,Elicons[name]);
}

app.use(router).use(ElementPlus).mount('#app')
