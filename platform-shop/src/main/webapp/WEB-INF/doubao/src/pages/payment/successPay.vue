<template>
  <div class="failPay">
  	<h4>支付成功</h4>
  	<div class="buttongroup">
  		<span @click="look">查看订单</span>
  		<span @click="success">完成</span>
  	</div>
  	<returnhome :scrollshow = "scrollshow"></returnhome>
  </div>
</template>

<script>
	import returnhome from '@/components/returnHome';
	
export default {
  name: 'successPay',
  components:{returnhome},
  data () {
    return {
    	scrollshow:true
    }
  },
  mounted(){
  		
	 },
	 methods:{
	 	look(){
	 		var appHref = window.location.href;
			var comHref = window.location.origin;
			if(appHref.indexOf('device')>-1){
				var device = appHref.substring(appHref.indexOf('device')).split('=')[1].split('&')[0];
			}
	    	if(device == 'android'){
	    			window.android.viewOrder(comHref +'/#/pages/ucenter/order'); //调起andriod交互方法(由app发起。浏览器会报错正常)
	    			return false;
	    	}else if(device == 'ios'){
	    			var message = {'url':comHref +'/#/pages/ucenter/order'}
						window.webkit.messageHandlers.webViewApp.postMessage(message);
						return false;
	    	}else{
	    		this.$router.push('/pages/ucenter/order');
	    	}
	 	},
	 	success(){
	 		var appHref = window.location.href;
			var comHref = window.location.origin;
			if(appHref.indexOf('device')>-1){
				var device = appHref.substring(appHref.indexOf('device')).split('=')[1].split('&')[0];
			}
	    	if(device == 'android'){
	    			window.android.payFinish(); //调起andriod交互方法(由app发起。浏览器会报错正常)
	    			return false;
	    	}else if(device == 'ios'){
	    			var message = {'url':'payFinish'}
						window.webkit.messageHandlers.webViewApp.postMessage(message);
						return false;
	    	}else{
	    		this.$router.push('/pages/shoppingcar');
	    	}
	 	}
 	}
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
	h4{
		font-size:.38rem;
		color:#a3585a;
		margin:.8rem 0 .5rem 0;
		font-weight: normal;
	}
	p{
		text-align: left;
		font-size:.28rem;
		color:#2C3E50;
		width:6rem;
		margin:0 auto;
		margin-top:.15rem;
	}
	.buttongroup{
		margin-top:.6rem;
	}
	.buttongroup span{
		display: inline-block;
		width:1.8rem;
		height:.65rem;
		line-height:.65rem;
		border:1px solid #838383;
		font-size:.28rem;
	}
</style>
