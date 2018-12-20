<template>
  <div class="classification">
  	<!--头部返回-->
  	<!--<headbar :headFont = "headFont"></headbar>-->
  	
  	<!--主体内容-->
  	<div class="searchTop" @click="searchRoute">
  				<mt-search v-model="value"  cancel-text="取消"  placeholder="商品搜索" class="wusearch"></mt-search>
  	</div>
  	<div class="content">
	  	<div class="catalog" >
	  		<p  v-for ="item in categoryList"  class = "item" :class="[item.id == currentCategory.id ?  'active'  :  '' ]"  @click="switchCate(item.id)">{{item.name}}</p>
	  	</div>
	  	<div class="rightca">
	  		<a href=""><img class="topb" :src="currentCategory.wap_banner_url" alt="" /></a>
	  		<p class="front_name">{{currentCategory.front_name}}</p>
	  		<div class="hd">
            <span class="line"></span>
            <span class="txt">{{currentCategory.name}}分类</span>
            <span class="line"></span>
        </div>
        <div class="bd">
        	<a v-for="item in currentCategory.subCategoryList" @click="andriod('/pages/category/category?id='+item.id)">
		        <img :src="item.wap_banner_url"/>
		       	<p>{{item.name}}</p>
	        </a>
        </div>
	  	</div>
  	</div>
  	<!--公用底部导航-->
  	<tabbar :selected="selected" :tabs='tabs' :style="{'display':[showAN ? 'none' : 'block']}"></tabbar> 
  </div>
</template>

<script>
	import tabbar from '@/components/tabbar.vue'
	import { Indicator } from 'mint-ui';
//	import headbar from '@/components/headbar'
	
export default {
	components: {tabbar },
  name: 'classification',
  data () {
    return {
    	showAN:'',
    	value:'',
			categoryList:[],
			currentCategory:[],
//			headFont:'分类',
		selected:"classification",
   	 tabs:[require("../../static/images/ic_menu_choice_nor.png"),require("../../static/images/ic_menu_sort_pressed.png"),
          require("../../static/images/ic_menu_shoping_nor.png"),require("../../static/images/ic_menu_me_nor.png")],
    }
  },
  mounted(){
  	var that = this;   
    //记录上次用户登录时查看的分类
  	let eventId =  that.$cookie.getCookie('eventId');
  	if( eventId != ''){
  		Indicator.open();
  		/*产品右侧分类*/
	  		that.$http({
	        method: 'post',
	        url: that.$url+'catalog/current',
					params:{ id : eventId  }
	    	}).then(function (response) {
			    that.currentCategory = response.data.data.currentCategory
			  })
	    	/*展示左侧分类*/
	    	that.$http({
	        method: 'post',
	        url: that.$url+'catalog/index',
	    	}).then(function (response) {
	    		Indicator.close();
			    that.categoryList = response.data.data.categoryList
			  })
  	}else{
  			Indicator.open();
	  		that.$http({
	        method: 'post',
	        url: that.$url+'catalog/index',
	    	}).then(function (response) {
	    		Indicator.close();
			    that.categoryList = response.data.data.categoryList
			    that.currentCategory = response.data.data.currentCategory
			  })
  	}
  	
  	/*android和ios对接特殊处理*/
    	var hrefD = window.location.href;
				if(hrefD.indexOf('device')>-1){
	    		var device = hrefD.split('?')[1].split('=')[1];
	    	}
	//  	http://192.168.124.29:8081/#/?device=andriod;
//				alert(device);
	    	if(device == 'android'){
	    			this.showAN = true;   //是否显示公用底部
	    	}else if(device == 'ios'){
	    			this.showAN = true;
	    	}else{
	    		this.showAN = false;
	    	}
  	
  },
  methods:{
  	andriod(e){   //与andriod和ios交互
				var hrefD = window.location.href;
				var delDevice = hrefD.split('#')[0];
				var comHref =delDevice .substring(delDevice.length-1,0);  //android和ios公用链接头
				if(hrefD.indexOf('device')>-1){
	    		var device = hrefD.split('?')[1].split('=')[1];
	    	}
	//  	http://192.168.124.29:8081/#/?device=andriod;
	    	if(device == 'android'){
	    			window.android.toSecondary(comHref +'/#' + e); //调起andriod交互方法(由app发起。浏览器会报错正常)
	    			return false;
	    	}else if(device == 'ios'){
	    			var message = {'url':comHref +'/#' + e}
						window.webkit.messageHandlers.webViewApp.postMessage(message);
						return false;
	    	}else{
	    		this.$router.push(e);
	    	}
	 	},
  	searchRoute(){
 			var hrefD = window.location.href;
				var delDevice = hrefD.split('#')[0];
				var comHref =delDevice .substring(delDevice.length-1,0);  //android和ios公用链接头
				if(hrefD.indexOf('device')>-1){
	    		var device = hrefD.split('?')[1].split('=')[1];
	    	}
	//  	http://192.168.124.29:8081/#/?device=andriod;
	    	if(device == 'android'){
	    			window.android.toSearch(comHref + '/#/pages/ucenter/search'); //调起andriod交互方法(由app发起。浏览器会报错正常)
	    			return false;
	    	}else if(device == 'ios'){
	    			var message = {'url':comHref + '/#/pages/ucenter/search'}
						window.webkit.messageHandlers.webViewApp.postMessage(message);
						return false;
	    	}else{
		  		this.$router.push('/pages/ucenter/search');
		  		/*清除搜索记录缓存*/
		  		this.$cookie.delCookie('search');
					this.$cookie.delCookie('searchKey');
			}
  	},
  	switchCate(eventId){
  		Indicator.open();
	    var that = this;
	    that.$cookie.setCookie('eventId',eventId);
	    that.$http({
        method: 'post',
        url: that.$url+'catalog/current',
				params:{ id : eventId  }
    	}).then(function (response) {
    		Indicator.close();
		    that.currentCategory = response.data.data.currentCategory
		  })
  	}
  }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only  active -->
<style scoped>
	.classification{
		background-color:#fff !important;
	}
	.front_name {
		position:absolute;
		top:0rem;
		text-align:center;
		color:#fff;
		font-size:.28rem;
		left:0;
		height:1.92rem;
		line-height:1.92rem;
		width:100%;
		overflow: hidden;
}

	.catalog 	.active{
		border-left:.06rem solid #ab2b2b;
		color:#ab2b2b;
		font-size:.28rem;
	}
	.bd {
	 	overflow: hidden;
	 	margin-top: .3rem;
	 	width: 5.5rem;
	 	padding-bottom:.5rem
	 }
 .bd a {
		display:block;
		float:left;
		height:2.16rem;
		width:1.44rem;
		margin-right:.34rem;
    margin-top: .06rem;
}
.bd a img {
	width:1.44rem;
	height:1.44rem;
}
.bd a p{
	text-align:center;
	font-size:.24rem;
	color:#333;
	height:.72rem;
	width:1.44rem;
	overflow: hidden;
 text-overflow: ellipsis;
 white-space: nowrap;
}
	.topb{
		height: 1.92rem;
		width: 100%;
	}
	.hd .line {
		display: inline-block;
	width:.40rem;
	height:1px;
	background:#d9d9d9;
}
.hd{
	font-size: 0.24rem;
}
.hd .txt {
font-size:.24rem;
text-align:center;
color:#333;
padding:0 .10rem;
width:auto;
	vertical-align: -webkit-baseline-middle;
}
	.rightca{
		float:left;
		-webkit-overflow-scrolling: touch; 
		border-left:1px solid #fafafa;
		height: 10.5rem;
		padding:0 .3rem 0 .3rem;
		width: 5.2rem;
		margin-top: 0.3rem;
		position: relative;
		overflow-y: scroll;
	}
	.item{
		text-align:center;
		line-height:.90rem;
		width:1.62rem;
		height:.90rem;
		color:#333;
		font-size:.28rem;
		overflow: hidden;
	}
		.wusearch{
			font-size:.3rem !important;
			height:100%;
		}
		.content{
			overflow: hidden;
   	 	padding-top: 1.2rem;
   	 	height:10.5rem;
		}
		.content .catalog{
			/*解决ios卡顿问题*/
			-webkit-overflow-scrolling: touch;   
			float:left;
			height:10.5rem;
			width:1.62rem;
			overflow-y:scroll ;
			overflow-x:hidden ;
		}
</style>
