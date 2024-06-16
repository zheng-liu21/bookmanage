<!--<template>-->
<!--&lt;!&ndash;  <nav>&ndash;&gt;-->
<!--&lt;!&ndash;&lt;!&ndash;    <router-link to="/">Home</router-link> |&ndash;&gt;&ndash;&gt;-->
<!--&lt;!&ndash;&lt;!&ndash;    <router-link to="/about">About</router-link>&ndash;&gt;&ndash;&gt;-->
<!--&lt;!&ndash;  </nav>&ndash;&gt;-->
<!--  <router-view/>-->
<!--</template>-->

<!--<style>-->
<!--#app {-->
<!--  font-family: Avenir, Helvetica, Arial, sans-serif;-->
<!--  -webkit-font-smoothing: antialiased;-->
<!--  -moz-osx-font-smoothing: grayscale;-->
<!--  text-align: center;-->
<!--  color: #2c3e50;-->
<!--}-->

<!--nav {-->
<!--  padding: 30px;-->
<!--}-->

<!--nav a {-->
<!--  font-weight: bold;-->
<!--  color: #2c3e50;-->
<!--}-->

<!--nav a.router-link-exact-active {-->
<!--  color: #42b983;-->
<!--}-->
<!--</style>-->


<template>
  <el-config-provider :locale="locale">
    <div id="app">
      <router-view/>
    </div>
  </el-config-provider>
</template>
<script>
import zhCn from 'element-plus/lib/locale/lang/zh-cn'
export default {
  data() {
    return {
      locale: zhCn,
    }
  },
}
</script>
<style>
#app {
  width: 100vw;
  height: 100vh;
  overflow: hidden;
}

</style>

