<template>
  <div class="hello"  id="home" ref="viewBox">
  	<div class="searchTop" @click="searchRoute">
  				<input type="text" placeholder="商品搜索" class="wusearch"  disabled="disabled"/>
  	</div>
    <mt-swipe :auto="3000" class="swiper" >
		  <mt-swipe-item v-for="item in banner">
		  	<img :src="item.image_url" @click="mtlb(item.link)"/>
		  </mt-swipe-item>
		</mt-swipe>
	<div class="m-menu" >
			<div @click="qdjh(item.url)" class="item" v-for="item in channel">
				<img :src="item.icon_url"/>
				<p>{{item.name}}</p>
			</div>
		</div>
		<div class="h">
			<p class="txt">人气推荐</p>
			<div class="itemhot" v-for="item in hotGoods"  @click="andriod('/pages/goods/goods?id='+item.id)">
				<img :src="item.list_pic_url"/>
				<div class="right">
					<p class="name">{{item.name}}</p>
					<!--<p class="goods_brief">{{item.goods_brief}}</p>-->
					<p class="market_price">￥{{item.market_price}}</p>
				</div>
			</div>
	</div>
	<div class="icp">
			<p>公司ICP备案号：蜀ICP备18036938号-1</p>
			<p>增值电信业务许可证号：川B2-20190201</p>
	</div>
		<!--公用底部导航-->
  	<tabbar :selected="selected" :tabs='tabs' :style="{'display':[showAN ? 'none' : 'block']}"></tabbar>
  </div>
</template>

<script>
	import tabbar from '@/components/tabbar';
	import returnHome from '@/components/returnHome';
	import { Indicator } from 'mint-ui';
	
export default {
	components: { tabbar },
  name: 'home',
  data () {
    return {
    	showAN:'',
    	value:'',
      banner:[],
      channel:[],
      hotGoods:[],
      category:[],
      selected:"home",
   	 tabs:[require("../../static/images/ic_menu_choice_pressed.png"),require("../../static/images/ic_menu_sort_nor.png"),
          require("../../static/images/ic_menu_shoping_nor.png"),require("../../static/images/ic_menu_me_nor.png")],

    }
  },
  mounted(){
  		let that = this;    
  	//banner
  		that.$http({
        method: 'post',
        url: that.$url+'index/banner'
    	}).then(function (response) {
		    that.banner = response.data.data.banner
		  })
    //channel
  		that.$http({
        method: 'post',
        url:that.$url+ 'index/channel'
    	}).then(function (response) {
		    that.channel = response.data.data.channel
		  })
    	//hotgoods
    	Indicator.open();
	    that.$http({
	        method: 'post',
	        url:that.$url+ 'index/hotGoods'
	    	}).then(function (response) {
	    		Indicator.close();
			    that.hotGoods = response.data.data.hotGoodsList;
			  })
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
	beforeRouteLeave(to, from, next) { 
			 let position = window.scrollY; //记录离开页面的位置 
			 if (position == null) position = 0 ;
			 if(this.$route.name == 'home'){
				 		this.$cookie.setCookie('scrollHome',window.scrollY);
		 		}
			 next();
	 }, 
 watch:{
   		hotGoods:function(){
   				this.$nextTick(function(){
   						if(this.$cookie.getCookie('scrollHome') == '' || this.$cookie.getCookie('scrollHome') == 0  || this.$cookie  .getCookie('scrollHome') == -1){
   					}else{
   						window.scrollTo(0,this.$cookie.getCookie('scrollHome'));
   					}
   				})
   		}
 },
 methods:{
	 	mtlb(e){   //与轮播andriod和ios交互
				var hrefD = window.location.href;
				if(hrefD.indexOf('device')>-1){
	    		var device = hrefD.split('?')[1].split('=')[1];
	    	}
	    	if(device == 'android'){
	    			window.android.productDetail(e); //调起andriod交互方法(由app发起。浏览器会报错正常)
	    			return false;
	    	}else if(device == 'ios'){
	    			var message = {'url': e}
						window.webkit.messageHandlers.webViewApp.postMessage(message);
						return false;
	    	}else{
	    		window.location.href =e;
	    	}
	 	},
	 	qdjh(e){   //与渠道分类andriod和ios交互
				var hrefD = window.location.href;
				var delDevice = hrefD.split('#')[0];
				var comHref =delDevice .substring(delDevice.length-1,0);  //android和ios公用链接头
				if(hrefD.indexOf('device')>-1){
	    		var device = hrefD.split('?')[1].split('=')[1];
	    	}
	//  	http://192.168.124.29:8081/#/?device=andriod;
	    	if(device == 'android'){
	    			window.android.toSecondary(comHref +'/#/'+ e); //调起andriod交互方法(由app发起。浏览器会报错正常)
	    			return false;
	    	}else if(device == 'ios'){
	    			var message = {'url':comHref+'/#/' + e}
						window.webkit.messageHandlers.webViewApp.postMessage(message);
						return false;
	    	}else{
	    		this.$router.push(e);
	    	}
	 	},
	 	andriod(e){   
			this.$cookie.interactive(e);  //商品详情与andriod和ios交互
	 	},
 		searchRoute(){
 			var hrefD = window.location.href;
				var delDevice = hrefD.split('#')[0];
				var comHref =delDevice .substring(delDevice.length-1,0);  //android和ios公用链接头
				if(hrefD.indexOf('device')>-1){
	    		var device = hrefD.split('?')[1].split('=')[1];
	    	}
	//  http://192.168.124.29:8080/#/?device=andriod
//				alert(device);
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
 }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
	#home{
		padding-bottom: 1.2rem;
	}
	.mint-tabbar{
		position: fixed;
	}
	.more-a{
		height:4.21rem;
	}
		.more-a .name {
	margin-top:1.3rem !important
		}
		.listAmount a img.icon{
		margin:.60rem auto 0 auto;
		width:.70rem;
		height:.70rem;
	}
	.listAmount{
		overflow: hidden;
		width:7.4rem;
		margin:0 auto;
	}
	.listAmount a{
		display: block;
		float:left;
		width:49.5%;
		background-color: #fff;
		margin-bottom:.05rem;
		margin-right: .05rem;
		padding-bottom: .15rem;
	}
		.listAmount a:nth-of-type(even){
			margin-right: 0;
		}
	.listAmount a img{
		margin-top:.20rem;
		width:3.02rem;
		height:3.02rem;
	}
	.listAmount a .name{
		text-align: center;
    font-size: .26rem;
    color: #333;
    padding: 0 .2rem;
    overflow: hidden;
    white-space: nowrap;
    margin: 0 auto;
    text-overflow: ellipsis;
	}
	.listAmount a .price{
		text-align:center;
font-size:.30rem;
color:#b4282d;

	}
	.category .instr{
		font-size:.29rem;
		color:#333;
		margin: .4rem 0;
	}
	.itemhot .name{
		color:#333;
		line-height:.50rem;
		font-size:.30rem;
		margin-top:.35rem;
	}
	.itemhot .goods_brief{
		color:#999;
		line-height:.50rem;
		font-size:.25rem;
	}
	.itemhot .market_price{
		color:#b4282d;
		line-height:.50rem;
		font-size:.33rem;
	}
.itemhot{
	display: block;
		overflow: hidden;
		background-color: #fff;
		border-top:1px solid #d9d9d9;
		padding:0 .20rem;
		/*height:2.64rem;*/
		width:7.10rem;
	}
	.itemhot img{
		margin-top:.12rem;
		margin-right:.12rem;
		float:left;
		width:2.40rem;
		height:2.40rem;
	}
		.itemhot .right{
		float: left;
		width:4.5rem;
		text-align: left;
	}
.swiper img{
	width:100%;
	height:4.17rem;
}
.swiper{
	width:7.5rem;
	height:4.17rem;
	margin-top:.95rem;
}
.m-menu {
  overflow: hidden;
  width: 7.5rem;
  background-color: #fff;
  margin:.15rem 0;
  display: flex;
  display: -webkit-flex;
  justify-content: space-between;
}
.m-menu .item {
float:left;
padding:.2rem 0;
font-size: 0;
width: 20%;
flex: 1;
}
.search{
		vertical-align: top;
}
.m-menu .item img{
	width:.6rem;
	height:.6rem;
}
.m-menu .item p{
	font-size:.24rem;
	color:#333;
	margin-top:.05rem
}
 .h {
background-color: #fff;
}
.h .txt{
	padding-right:.3rem;
	background-size:.16rem .27rem;
	display:inline-block;
	height:.36rem;
	font-size:.33rem;
	line-height:.36rem;
}
.wusearch{
    width: 7.5rem;
    padding: 0 0 0 .35rem;
    font-size: .28rem;
    height: .95rem;
    border: .12rem solid #d9d9d9;
    background: url(http://yanxuan.nosdn.127.net/hxm/yanxuan-wap/p/20161201/style/img/icon-normal/search2-2fb94833aa.png) no-repeat .1rem .25rem;
    background-size: .2rem;
    background-color: #fff;
    box-sizing: border-box;
}
.wusearch::-webkit-input-placeholder {
		font-size: .24rem;
		color:#555;
 }
 .icp{
 	padding:.35rem 0 .5rem 0;
 }
 .icp p{
 	font-size:.25rem;
 	color:#959191;
 	margin-top: .15rem;
 }
</style>
