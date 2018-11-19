<template>
		<div class="container">
		  	<mt-header :title="headFont" fixed>
			  <router-link to="/" slot="left">
			    <mt-button icon="back" @click="handleClose">返回</mt-button>
			  </router-link>
			</mt-header>
		</div>
</template>

<script>
	import { Header } from 'mint-ui';
	
	export default {
	  name: 'tabbar',
	  data () {
	    return {
	    	
	    }
	  },
	   props:{
	   			headFont: String,
//	   			headpush:String
//	   			showHead:String
	   },
	   methods:{
	   		handleClose(){
	   			console.log('返回');
	   			this.$router.go(-1);
	   		}
	   }
	}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only  active -->
<style>
	.mint-header{
		height:.88rem;
	}
	header.mint-header.is-fixed{
		z-index:1111
	}
</style>
