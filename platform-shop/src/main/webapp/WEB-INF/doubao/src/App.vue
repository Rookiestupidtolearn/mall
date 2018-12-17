<template>
  <div id="app">
    <router-view/>
    <!--<keep-alive>
          <router-view v-if="$route.meta.keepAlive" class="router-view"></router-view>
      </keep-alive>
     <router-view v-if="!$route.meta.keepAlive" class="router-view" ></router-view>-->
  </div>
</template>

<script>
export default {
  name: 'App',
}
</script>

<style>
#app {
  font-family: Microsoft YaHei,Tahoma,Arial,sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
  max-width:750px;
  margin:0 auto;
}
</style>
