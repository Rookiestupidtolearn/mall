<template>
  <div class="hello">
  	<div class="order">
   		<router-link class="myorder" to="/pages/ucenter/helpCenter" tag="div">
   			<div class="left">帮助中心</div>
   			<div class="right">></div>
		 </router-link>
		 <div class="myorder" @click="locationHref" >
   			<div class="left">在线客服</div>
   			<div class="right">></div>
		 </div>
   		<router-link to="/pages/ucenter/feedback" class="myorder" tag="div">
			<div class="left">信息反馈</div>
			<div class="right"  to="/pages/ucenter/order">></div>
		 </router-link>
   </div>
  </div>
</template>
<script>
	export default {
	name: 'userservice',
	//components:{headbar},
	  data () {
	    return {
	    }
	  },
	  methods:{
	  	locationHref(){
	  		window.location.href='https://www.sobot.com/chat/pc/index.html?sysNum=e5ef8967b4114644a4c290bf0729f959';
	  	}
	  }
	}
</script>
<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
	.order{
			font-size:.29rem;
			background: #fff;
		}
		.ordermt{
			margin-top:.16rem;
		}
		.order .myorder .left{
			float:left;
		}
		.order .myorder .right{
			float:right;
			color:#a1a1a1
		}
		.order .myorder {
			padding:.24rem .24rem .24rem 0rem;
			overflow: hidden;
			margin-left: .24rem;
			border-bottom: 1px solid #eeeeee;
		}
</style>
