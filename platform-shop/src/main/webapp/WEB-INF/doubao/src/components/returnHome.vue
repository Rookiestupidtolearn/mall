<template>
	<div class="container">
		<div class="returnHome" v-show="scrolldown">
			<span @click="returnHome">返回首页</span><img src="../../static/images/rehome/rightBtn.png"/>
		</div>
		<div class="returnHome" @click="show" v-show="!scrolldown">
			<img src="../../static/images/rehome/leftBtn.png"/>
		</div>
	</div>
</template>

<script>
	
	export default {
	  name: 'returnHome',
	  data () {
	    return {
	    	scrolldown:this.scrollshow
	    }
	  },
	   props:{
	   	scrollshow:Boolean
	   },
	   mounted(){
	   	var startY,moveEndY,Y;
	   	let that = this;
	   	 document.addEventListener('touchstart',function(e){
　　　 startY = e.changedTouches[0].pageY;
	   	 })
	   	 document.addEventListener('touchmove',function(e){
　　　 moveEndY = e.changedTouches[0].pageY;
　　　 Y = moveEndY - startY;//上下
　　　if ( Y < 0) {
				console.log('用户上拉了');
				that.scrolldown = false;
　　　}
	   	 })
	   },
	   methods:{
	   	returnHome(){
	   		console.log('点击返回首页');
	   		var appHref = window.location.href;
			if(appHref.indexOf('device')>-1){
				var device = appHref.substring(appHref.indexOf('device')).split('=')[1].split('&')[0];
			}
	    	if(device == 'android'){
	    			window.android.goHome(); //调起andriod交互方法(由app发起。浏览器会报错正常)
	    			return false;
	    	}else if(device == 'ios'){
	    			var message = {'url': 'goHome'}
					window.webkit.messageHandlers.webViewApp.postMessage(message);
					return false;
	    	}else{
	    		this.$router.push({ path :'/'});
	    	}
	   	},
	   	show(){
	   		this.scrolldown = true;
	   	}
	   }
	}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only  active -->
<style>
	.returnHome img{
		width:.2rem;
		margin-left: .1rem;
		margin-top: .75rem;
	}
	.returnHome span{
		background-color:#cd9ffd;
		font-size:.24rem;
		padding:.05rem;
		border-radius: 50%;
		color: #fff;
		width: .7rem;
    	display: inline-block;
    	margin-top:.55rem;
	}
	.returnHome{
		position:fixed;
		right:0;
		top:60%;
	    background:rgba(188, 188, 188, 0.3);
	    padding: 0 0.1rem;
	    height:1.9rem;
	    z-Index:99;  
	    display: table-cell;
	    vertical-align: middle;
	}
</style>
