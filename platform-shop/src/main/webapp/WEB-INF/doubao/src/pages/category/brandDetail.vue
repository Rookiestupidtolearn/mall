<template>
 	<div class="container">
	    <div class="brand-info">
	        <div class="name">
	            <img class="img"  :src="brand.app_list_pic_url" background-size="cover"/>
	            <div class="info-box">
	                <div class="info">
	                    <span class="txt">{{brand.name}}</span>
	                    <span class="line"></span>
	                </div>
	            </div>
	        </div>
	        <div class="desc">
	            {{brand.simple_desc}}
	        </div>
	    </div>
	    
	   <div class="cate-item">
	        <div class="b">
	            <router-link v-for="(iitem,iindex) in goodsList"   class="item" :class="iindex % 2 == 0 ? 'item-b' : ' ' " :to="'../goods/goods?id='+iitem.id">
	                <img class="img" :src="iitem.list_pic_url" background-size="cover"/>
	                <p class="name">{{iitem.name}}</p>
	                <p class="price">￥{{iitem.market_price}}</p>
	            </router-link>
	        </div>
	    </div>
	    
	</div>
</template>

<script>
	import { MessageBox } from 'mint-ui';
	import { Toast } from 'mint-ui';
		
	export default {
	  name: 'brandDetail',
	  data () {
	    return {
	    	idm:'',
	    	brand:'',
	    	goodsList:[],
	    }
	  },
	  mounted(){
	  		let that = this;
	  		that.idm = that.$route.query.id;
	  		//品牌详情
	    	that.$http({
		        method: 'post',
		        url:that.$url+ 'brand/detail',
		        params:{id:that.idm}
	    	}).then(function (response) {
	    		that.brand = response.data.data.brand;
			 })
	    	
	    	//商品列表
	    	that.$http({
		        method: 'post',
		        url:that.$url+ 'goods/list',
		        params:{brandId:that.idm,page:1,size:1000}
	    	}).then(function (response) {
	    		that.goodsList = response.data.data.goodsList;
	    		
			 })
	  },
	  methods:{
	  	
	  }
	}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
	page{
    background: #f4f4f4;
}
.brand-info .name{
    width: 100%;
    height: 2.90rem;
    position: relative;
}

.brand-info .img{
    position: absolute;
    top:0;
    left:0;
    width: 100%;
    height: 2.90rem;
}

.brand-info .info-box{
    position: absolute;
    top:0;
    left:0;
    width: 100%;
    height: 2.90rem;
    text-align: center;
    display: flex;
    justify-content: center;
    align-items: center;
}

.brand-info .info{
    display: block;
}

.brand-info .txt{
    display: block;
    height: .375rem;
    font-size: .375rem;
    color: #fff;
}

.brand-info .line{
    margin: 0 auto;
    margin-top: .16rem;
    display: block;
    height: .02rem;
    width: 1.45rem;
    background: #fff;
}

.brand-info .desc{
    background: #fff;
    height: auto;
    overflow: hidden;
    padding: .415rem .3125rem;
    font-size: .30rem;
    color: #666;
    line-height: .415rem;
    text-align: center;
    width: 6.85rem;
}

.cate-item .b{
  width: 7.50rem;
  height: auto;
  overflow: hidden;
  border-top: 1px solid #f4f4f4;
  margin-top: .20rem;
}

.cate-item .b .item{
  float: left;
  background: #fff;
  width: 3.75rem;
  padding-bottom: .33rem;
  border-bottom: 1px solid #f4f4f4;
  height: auto;
  overflow: hidden;
  text-align: center;
  box-sizing: border-box;
  -webkit-box-sizing: border-box;
}

.cate-item .b .item-b{
 border-right: 1px solid #f4f4f4;
}

.cate-item .item .img{
    margin-top: .10rem;
  width: 3.02rem;
  height: 3.02rem;
}

.cate-item .item .name{
  display: block;
  height: .35rem;
  padding: 0 .20rem;
  overflow: hidden;
  margin: .115rem 0 .22rem 0;
  text-align: center;
  font-size: .30rem;
  color: #333;
}

.cate-item .item .price{
  display: block;
  height: .30rem;
  text-align: center;
  font-size: .30rem;
  color: #b4282d;
}
</style>
