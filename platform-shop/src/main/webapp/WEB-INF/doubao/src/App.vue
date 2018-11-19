<template>
  <div id="app">
    <router-view/>
  </div>
</template>

<script>
export default {
  name: 'App',
}
</script>

<style>
#app {
  font-family: 'Avenir', Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
}
body{
	margin:0;
	padding-bottom: 1.2rem;
}
.mint-msgbox-cancel{
	box-sizing: border-box;
	-webkit-box-sizing: border-box;
}
.mint-tabbar{
	position: fixed;
}
.mint-indicator-wrapper{
	padding: .2rem .5rem !important;
}
.mt88{
	margin-top:.88rem !important;
}
</style>
