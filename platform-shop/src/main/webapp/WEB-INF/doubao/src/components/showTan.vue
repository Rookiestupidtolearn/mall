<template>
	<div class="sellOut">
  		<!--商品已下架的弹窗-->
  		<div class="bg" v-show="showTn" ></div>
  		<div class="bgShow" v-show="showTn">
  			<div class="font">订单中商品均已<span class="red">已售罄</span>，这些商品太火爆了，您晚了一步哦；</div>
  			<div class="itemtotal">
	  			<div class="item" v-for="(item,index) in unsells" >
	            <div class="cart-goods">
	              <div class="info">
	                	<img class="img"  :src="item.list_pic_url"/>
	                  <div class="t">
	                    <span class="name">{{item.goods_name}}</span>
	                     <span class="num">x{{item.number}}</span>
	                  </div>
	                  <span class="price">￥{{item.market_price}}</span>
	              </div>
	            </div>
	          </div>
	      	</div>
  			<ul class="btngroupT" @click="cancelShop">知道了，去重新下单</ul>
  		</div>
	</div>
</template>

<script>
	
	export default {
	  name: 'showTan',
	  data () {
	    return {
	    	showTan:this.showTn,
	    	showUnsells:this.unsells
	    }
	  },
	   props:{
	   	showTn:Boolean,
	   	unsells:''
	   },
	   methods:{
	   	cancelShop(){    
	   		var appHref = window.location.href;
			if(appHref.indexOf('device')>-1){
				var device = appHref.substring(appHref.indexOf('device')).split('=')[1].split('&')[0];
			}
	   		if(device == 'android'){
	    			window.android.toShopCart(); //调起andriod交互方法(由app发起。浏览器会报错正常)
	    			return false;
	    	}else if(device == 'ios'){
	    			var message = {'url':'toShopCart'}
					window.webkit.messageHandlers.webViewApp.postMessage(message);
					return false;
	    	}else{
	    		this.$router.push('/pages/shoppingcar');
	    	}
	   	}
	   }
	}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only  active -->
<style>
	.bg{
		position:fixed;
		left:0px;
		top:0px;
	    background:rgba(0, 0, 0, 0.6);
	    width:100%;  
	    height:100%;
	    filter:alpha(opacity=60);  
	    opacity:0.6;  
	    z-Index:999;  
	}
	.bgShow{
		position:fixed;
	    width:6rem;
	    top:2.3rem;
	    left:50%;
	    background-color:#fff;
		margin-left: -3rem;
	    z-Index:9999;  
	    border-radius:.08rem;
	    -webkit-border-radius:.08rem;
	    padding-top:.3rem;
	}
	.itemtotal{
		max-height: 5rem;
    	overflow-y: scroll;
    	padding-bottom:.5rem;
	}
	.bgShow .font{
		font-size: .25rem;
	    color: #333;
	    padding: 0 .3rem;
	    text-align: left;
	    margin-top: .3rem;
	    padding-bottom:.5rem;
	}
	.bgShow .btngroupT{
		width:100%;
		overflow: hidden;
		font-size:.29rem;
		padding:.2rem 0;
		border-top:1px solid #CBCBCB;
		color: #26A2FF;
	}
	.bgShow .font .red{
		color: red;
	}
	.bgShow .item{
		overflow: hidden;
	}
	.bgShow img{
		float:left;
		width:1.05rem;
		margin:0 .2rem;
	}
	.bgShow .item .name{
	    font-size: .25rem;
	    color: #333;
	    overflow: hidden;
	    text-overflow: ellipsis;
	    white-space: nowrap;
	    width: 4rem;
	    display: inline-block;
	    margin-top: .1rem;
	}
	.bgShow .item .t{
		float:left;
		margin: .08rem 0;
		text-align: left;
	    font-size: .25rem;
	    color: #333;
	    overflow: hidden;
	}
	.bgShow .item .num{
		vertical-align: super;
		margin: .08rem 0;
	}
	.bgShow .item .price{
		  float: left;
	    font-size: .29rem;
	    color: #333;
	}
</style>
