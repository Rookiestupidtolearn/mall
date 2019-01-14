<template>
 	<div class="container">
 		<!--公用头部-->
  		<!--<headbar :headFont = "headFont"></headbar>-->
  		
 		<div class="cate-over ">
	 		<div class="cate-nav" ref="box">
	            <div v-for="item in brotherCategory"  :class="idm == item.id ? 'active' : ''" class="item" @click="switchCate(item.id)">
	                <div class="name">{{item.name}}</div>
	            </div>
		    </div>
	    </div>
	   <div class="cate-item" >
	        <!--<div class="h">
	            <p class="name">{{currentCategory.name}}</p>
	            <p class="desc">{{currentCategory.front_name}}</p>
	        </div>-->
	        <div class="b">
	            <a  class="item [(iindex + 1) % 2 == 0 ? 'item-b' : '']"   v-for="(iitem,index) in goodsList"  @click="andriod('/pages/goods/goods?id='+iitem.id)">
	                <img class="img" :src="iitem.list_pic_url" background-size="cover"/>
	                <p class="name">{{iitem.name}}</p>
	                <p class="price">￥{{iitem.market_price}}</p>
	            </a>
	        </div>
	    </div>
  </div>
</template>

<script>
	import { MessageBox } from 'mint-ui';
//	import headbar from '@/components/headbar.vue'
	import { Toast } from 'mint-ui';
		
	export default {
	  name: 'coupon',
//	  components:{headbar},
	  data () {
	    return {
//	    	headFont:'产品分类',
	    	brotherCategory:[],
	    	goodsList:[],
	    	currentCategory:'',
	    	idm:'',
	    	indexId:'',
	    }
	  },
	  mounted(){
	  		this.idm = this.$route.query.id;
		  	this.categoryShow();
	    	this.listShow();
	  },
	  watch:{
	  	brotherCategory:function(){
	  		this.$nextTick(function(){
	  			var activeIndex = document.getElementsByClassName('active')[0];
	  			var activeIndexLeft = activeIndex.offsetLeft;
	  			this.$refs.box.scrollLeft = activeIndexLeft;
	  		})
	  	}
	  },
	  methods:{
	  	andriod(e){   //与andriod和ios交互
				var hrefD = window.location.href;
//				var hrefD= 'http://192.168.124.29:8081/#/pages/goods/goods?id=18013&device=android';
				var delDevice = hrefD.split('#')[0];
				var comHref =delDevice.substring(delDevice.length-1,0);  //android和ios公用链接头
				if(hrefD.indexOf('device')>-1){
		    		var device = hrefD.split('&')[1].split('=')[1];
		    	}
	//  	http://192.168.124.29:8081/#/?device=andriod;
	    	if(device == 'android'){
	    			window.android.productDetail(comHref +'/#' + e ); //调起andriod交互方法(由app发起。浏览器会报错正常)
	    			return false;
	    	}else if(device == 'ios'){
	    		
	    			var message = {'url':comHref +'/#' + e}
						window.webkit.messageHandlers.webViewApp.postMessage(message);
						return false;
	    	}else{
	    		this.$router.push(e);
	    	}
	 	},
	  	switchCate(idItem){
	  		this.idm = idItem;
	  		this.listShow();
	  	},
	  	listShow(){
	  		let that = this;    
	  		that.$http({
		        method: 'post',
		        url:that.$url+ 'goods/list',
		        params:{categoryId:that.idm,page:1,size:20},
	    	}).then(function (response) {
				that.goodsList = response.data.data.goodsList;
				if(that.goodsList.length < 1){
					Toast({message: '内容为空',duration:2000});
				}
			 })
	  	},
	  	categoryShow(){
	  		let that = this;    
	    	that.$http({
		        method: 'post',
		        url:that.$url+ 'goods/category',
		        params:{id:that.idm}
	    	}).then(function (response) {
				that.brotherCategory = response.data.data.brotherCategory;
				that.currentCategory = response.data.data.currentCategory;
			 })
	  	}
	  }
	}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
.cate-over{
	position: fixed;
    top: 0;
    width:100%;
}
.cate-nav{
   background-color: #fff;
   display: -webkit-box;
    width: 100%;
    overflow-x: scroll;
    overflow-y: hidden;
    -webkit-overflow-scrolling: touch;
}
.cate-nav-body{
    height: .84rem;
    white-space: nowrap;   
    background: #fff;
    border-top: 1px solid rgba(0,0,0,.15);
    overflow: hidden;
}

.cate-nav .item{
    height: .84rem;
}

.cate-nav .item .name{
    display: block;
    height: .84rem;
    padding: 0 .36rem;
    line-height: .84rem;
    color: #333;
    font-size: .30rem;
    width: auto;
}

.cate-nav .item.active .name{
    color: #ab2b2b;
    border-bottom: 2px solid #ab2b2b;
    box-sizing: border-box;
    -webkit-box-sizing: border-box;
}

.cate-item{
    margin-top: 1.05rem;
    height: auto;
    overflow: hidden;
}

.cate-item .h{
    height: 1.45rem;
    width: 7.50rem;
}

.cate-item .h .name{
    display: block;
    height: .35rem;
    margin-bottom: .18rem;
    font-size: .30rem;
    color: #333;
}

.cate-item .h .desc{
    display: block;
    height: .24rem;
    font-size: .24rem;
    color: #999;
}

.cate-item .b{
  width: 7.50rem;
  padding: 0 .0625rem;
  height: auto;
  overflow: hidden;
}

.cate-item .b .item{
 float: left;
background: #fff;
width: 3.65rem;
margin-bottom: .06rem;
padding-bottom: .3rem;
height: auto;
overflow: hidden;
text-align: center;
margin-right: .06rem;
}

.cate-item .b .item-b{
  margin-left: .0625rem;
}

.cate-item .item .img{
  width: 3.02rem;
  height: 3.02rem;
}

.cate-item .item .name{
  display: block;
  width: 3.3rem;
  margin: .115rem 0 .22rem 0;
  text-align: center;
  overflow: hidden;
  text-overflow: ellipsis;
white-space: nowrap;
  padding: 0 .20rem;
  font-size: .30rem;
  color: #333;
}

.cate-item .item .price{
  display: block;
  width: 3.65rem;
  height: .30rem;
  text-align: center;
  font-size: .30rem;
  color: #b4282d;
}

.loadmore {
  height: 1.00rem;
  width: 100%;
  line-height: .80rem;
  text-align: center;
  margin-top: 0rem;
}

.loadmore text {
  color: #999;
}

</style>
