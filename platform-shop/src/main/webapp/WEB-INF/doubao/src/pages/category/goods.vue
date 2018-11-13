<template>
 	<div class="container">
	 	<div v-if="!openAttr" >
		   <mt-swipe :auto="3000" class="swiper" >
			  <mt-swipe-item v-for="item in banner">
			  	<img :src="item.img_url"/>
			  </mt-swipe-item>
			</mt-swipe>
			<div class="service-policy">
		  		<span class="item">30天无忧退货</span>
		  		<span class="item">48小时快速退款</span>
		  		<span class="item">免邮费</span>
		  	</div>
		  	<div class="goods-info">
		      <div class="c">
		        <p class="name">{{goods.name}}</p>
		        <p class="desc">{{goods.goods_brief}}</p>
		        <p class="price">￥{{goods.market_price}}</p>
		        <div class="brand" v-if="brand.name">
		          <router-link :to="'../category/brandDetail?id='+brand.id">
		            <p>{{brand.name}}</p>
		          </router-link>
		        </div>
		      </div>
		    </div>
			<div class="section-nav section-attr" @click="switchAttrPop">
			      <div class="t">请选择规格数量</div>
			      <img class="i" src="../../../static/images/address_right.png" background-size="cover"/>
			</div>
			<div class="goods-attr">
		      <div class="t">商品参数</div>
		      <div class="l">
		        <div class="item" v-for="item in attribute" >
		          <p class="left">{{item.name}}</p>
		          <p class="right">{{item.value}}</p>
		        </div>
		      </div>
		    </div>
		    <!--商品详情-->
		    <div class="detail">
		     <div v-html="goods_desc"></div>
		    </div>
		    <!--常见问题-->
		   <div class="common-problem">
		      <div class="h">
		        <div class="line"></div>
		        <span class="title">常见问题</span>
		      </div>
		       <div class="b">
		        <div class="item" v-for="item in issueList">
		          <div class="question-box">
		            <p class="spot"></p>
		            <p class="question">{{item.question}}</p>
		          </div>
		          <div class="answer">
		            {{item.answer}}
		          </div>
		        </div>
		      </div>
		    </div>
		    <!--大家都在看-->
		    <div class="related-goods">
		      <div class="h">
		        <div class="line"></div>
		        <span class="title">大家都在看</span>
		      </div>
		      <div class="b">
		        <div class="item" v-for="item in relatedGoods" >
		          <router-link :to="'/pages/goods/goods?id='+item.id">
		            <img class="img" :src="item.list_pic_url" background-size="cover"/>
		            <p class="name">{{item.name}}</p>
		            <p class="price">￥{{item.market_price}}</p>
		          </router-link>
		        </div>
		      </div>
		    </div>
	    </div>
    	<!--商品规格-->
    	 <div v-if="openAttr" class="attr-pop">
		    <div class="img-info">
		      <img class="img" :src="goods.list_pic_url"/>
		      <div class="info">
		        <div class="c">
		          <p class="p">价格：￥{{market_price}}</p>
		          <p class="a" v-if="productList.length>0">已选择：{{checkedSpecText}}</p>
		        </div>
		      </div>
		    </div>
		    <!--<div class="spec-con">
		      <div class="spec-item" wx:for="{{specificationList}}" wx:key="{{item.specification_id}}">
		        <div class="name">{{item.name}}</div>
		        <div class="values">
		          <div class="value {{vitem.checked ? 'selected' : ''}}" bindtap="clickSkuValue" wx:for="{{item.valueList}}" wx:for-item="vitem" wx:key="{{vitem.id}}" data-value-id="{{vitem.id}}" data-name-id="{{vitem.specification_id}}">{{vitem.value}}</div>
		        </div>
		      </div>
		      <div class="number-item">
		        <div class="name">数量</div>
		        <div class="selnum">
		          <div class="cut" bindtap="cutNumber">-</div>
		          <input value="{{number}}" class="number" disabled="true" type="number" />
		          <div class="add" bindtap="addNumber">+</div>
		        </div>
		      </div>
		    </div>-->
		  </div>
		 <!--加入购物车底栏-->
    	<div class="bottom-btn">
			  <div class="l l-collect" :class="openAttr ? 'back' : ''" @click="closeAttrOrCollect">
			    <img class="icon" :src="collectBackImage "/>
			  </div>
			  <div class="l l-cart" bindtap="openCartPage">
			    <div class="box">
			      <span class="cart-count">{{cartGoodsCount}}</span>
			      <img class="icon" src="../../../static/images/ic_menu_shoping_nor.png" />
			    </div>
			  </div>
			  <!-- <div class="c" bindtap='buyGoods'>立即购买</div> -->
			  <button :class="undercarriage ? 'disabled' : 'r'" v-if="undercarriage">{{undercarriName}}</button>
			  <button :class="undercarriage ? 'disabled' : 'r'" v-else @click="addToCart">{{undercarriName}}</button>
			</div>
  </div>
</template>

<script>
	import { Toast } from 'mint-ui';
		
	export default {
	  name: 'goods',
	  data () {
	    return {
	    	market_price:'',
	    	idm:'',
	    	banner:[],
	    	undercarriage:'',
	    	undercarriName:'加入购物车',
	    	goods:'',
	    	brand:'',
	    	attribute:[],
	    	issueList:[],
	    	relatedGoods:[],
	    	goods_desc:[],
	    	checkedSpecText: '请选择规格数量',
	    	cartGoodsCount:0,
	    	userHasCollect: 0,
	    	openAttr: false,
	    	noCollectImage: "../../static/images/icon_collect.png",
		    hasCollectImage: "../../static/images/icon_collect_checked.png",
		    collectBackImage: "../../static/images/icon_collect.png"
	    }
	  },
	  mounted(){
	  		var that = this;
	  		this.idm = this.$route.query.id;
	  		
	  		//商品详情
	    	that.$http({
		        method: 'post',
		        url:that.$url+ 'goods/detail',
		        params:{id:that.idm}
	    	}).then(function (response) {
	    		response = {"errno":0,"data":{"specificationList":[{"specification_id":1,"valueList":[{"id":42,"goods_id":1181025,"specification_id":1,"value":"红色","name":"颜色","pic_url":"http://aoss.huaqianyueshang.com/wall/20181026/155058750d0feb.jpg"},{"id":43,"goods_id":1181025,"specification_id":1,"value":"白色","name":"颜色","pic_url":"http://aoss.huaqianyueshang.com/wall/20181026/1604191376b61b.jpg"},{"id":44,"goods_id":1181025,"specification_id":1,"value":"黑色","name":"颜色","pic_url":"http://aoss.huaqianyueshang.com/wall/20181026/161237735c397c.png"}],"name":"颜色"}],"issue":[{"id":4,"goods_id":null,"question":"如何开具发票？","answer":"1.如需开具普通发票，请在下单时选择“我要开发票”并填写相关信息（APP仅限2.4.0及以"},{"id":3,"goods_id":null,"question":"如何申请退货？","answer":"1.自收到商品之日起30日内，顾客可申请无忧退货，退款将原路返还，不同的银行处理时间不同，"},{"id":2,"goods_id":null,"question":"使用什么快递发货？","answer":"严选默认使用顺丰快递发货（个别商品使用其他快递），配送范围覆盖全国大部分地区（港澳台地区除"},{"id":1,"goods_id":null,"question":"购买运费如何收取？","answer":"免邮费"}],"userHasCollect":0,"comment":{"data":{},"count":0},"attribute":[],"minPriceList":[{"id":307,"goods_id":1181025,"product_id":null,"goods_specification_ids":"43","goods_sn":"001","goods_number":1000,"market_price":1000.00,"retail_price":1000.00,"goods_name":"测试使用001","list_pic_url":"http://aoss.huaqianyueshang.com/wall/20181026/1400011555eb77.jpg"}],"brand":{"id":1046000,"name":"Police制造商","list_pic_url":"http://aoss.huaqianyueshang.com/wall/20181019/14420363377ff7.png","simple_desc":"严选团队选定Police品牌制造商合作，\n有11年眼镜生产资质，兼顾品质与品味，\n为你带来专业时尚的墨镜。","pic_url":"http://aoss.huaqianyueshang.com/wall/20181019/144207896fe0c0.png","sort_order":6,"is_show":1,"floor_price":109.00,"app_list_pic_url":"http://aoss.huaqianyueshang.com/wall/20181019/14421084551149.png","is_new":0,"new_pic_url":"","new_sort_order":10},"gallery":[{"id":840,"goods_id":1181025,"img_url":"http://aoss.huaqianyueshang.com/wall/20181026/140031570e022c.jpg","img_desc":null,"sort_order":null},{"id":839,"goods_id":1181025,"img_url":"http://aoss.huaqianyueshang.com/wall/20181026/140025785bfebf.jpg","img_desc":null,"sort_order":null},{"id":838,"goods_id":1181025,"img_url":"http://aoss.huaqianyueshang.com/wall/20181026/140020619f0657.jpg","img_desc":null,"sort_order":null},{"id":837,"goods_id":1181025,"img_url":"http://aoss.huaqianyueshang.com/wall/20181026/140016652fa9b.jpg","img_desc":null,"sort_order":null},{"id":836,"goods_id":1181025,"img_url":"http://aoss.huaqianyueshang.com/wall/20181026/1400102169b9d3.jpg","img_desc":null,"sort_order":null}],"productList":[{"id":310,"goods_id":1181025,"product_id":null,"goods_specification_ids":"42","goods_sn":"001","goods_number":1000,"market_price":1000.00,"retail_price":1000.00,"goods_name":"测试使用001","list_pic_url":"http://aoss.huaqianyueshang.com/wall/20181026/1400011555eb77.jpg"},{"id":307,"goods_id":1181025,"product_id":null,"goods_specification_ids":"43","goods_sn":"001","goods_number":1000,"market_price":1000.00,"retail_price":1000.00,"goods_name":"测试使用001","list_pic_url":"http://aoss.huaqianyueshang.com/wall/20181026/1400011555eb77.jpg"}],"info":{"id":1181025,"category_id":1036015,"goods_sn":"001","name":"测试使用001","brand_id":1046000,"goods_number":1000,"keywords":"测试","goods_brief":"这是一款为测试而建的产品，但它不仅仅为测试而生","goods_desc":"<p><img class=\"fr-fin\" data-fr-image-preview=\"false\" alt=\"Image title\" src=\"http://aoss.huaqianyueshang.com/wall/20181026/151631151c6a4b.png\" width=\"300\"></p><p><span class=\"f-video-editor fr-fvn\">https://pan.baidu.com/s/1jXKvSRYyghJG5Uv1qYaVtw</span><br></p>","is_on_sale":0,"add_time":1540533998000,"sort_order":null,"is_delete":0,"attribute_category":1036004,"counter_price":null,"extra_price":null,"is_new":1,"goods_unit":null,"primary_pic_url":"http://aoss.huaqianyueshang.com/wall/20181026/1620338004250f.jpg","list_pic_url":"http://aoss.huaqianyueshang.com/wall/20181026/1400011555eb77.jpg","market_price":1000.00,"retail_price":1000.00,"sell_volume":null,"primary_product_id":307,"unit_price":null,"promotion_desc":"这是一款为测试而建的产品","promotion_tag":null,"app_exclusive_price":null,"is_app_exclusive":0,"is_limited":0,"is_hot":1,"cart_num":0,"product_id":null,"product_market_price":1000.00}},"errmsg":"执行成功"};
	    		that.banner = response.data.gallery;
	    		that.goods = response.data.info;
	    		that.brand = response.data.brand;
	    		that.attribute = response.data.attribute;
	    		that.issueList = response.data.issue;
	    		that.goods_desc = response.data.info.goods_desc;
	    		that.undercarriage = response.data.info.is_on_sale; //上下架按钮显示状态
//				that.banner = response.data.data.gallery
			 })
	    	
	    	//关联
	    	that.$http({
	    		method: 'post',
		        url:that.$url+ 'goods/related',
		        params:{id:that.idm}
	    	}).then(function (response) {
		          that.relatedGoods = response.data.data.goodsList;
		    });
	    	
	  },
	  methods:{
	  	addToCart(){
	  		console.log('1111')
	  	},
	  	switchAttrPop(){
	  		if (this.openAttr == false) {
		        this.openAttr = !this.openAttr,
		        this.collectBackImage =  "../../../static/images/detail_back.png";
		    }
	  	},
	  	closeAttrOrCollect(){
	  		let that = this;
		    if (this.openAttr) {
		    	this.openAttr = !this.openAttr;
		      if (this.userHasCollect == 1) {
		          	this.collectBackImage =  this.hasCollectImage;
		      } else {
		      		this.collectBackImage =  this.noCollectImage;
		      }
		    } else {
		      //添加或是取消收藏
		      //关联
	    	that.$http({
	    		method: 'post',
		        url:that.$url+ 'collect/addordelete',
		        params:{ typeId: 0, valueId: this.idm}
	    	}).then(function (response) {
		          let _res = response;
		          if (_res.errno == 0) {
			            if ( _res.data.type == 'add') {
			                this.collectBackImage =  this.hasCollectImage;
			            } else {
			              	this.collectBackImage =  this.noCollectImage;
			            }
		          } else {
		          		Toast(_res.errmsg);
		          }
		        });
		    }
	  	}
	  }
	}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<!--v-html  的标签 加样式 用 >>>-->
<style scoped>
	.attr-pop{
    height: 100%;
    padding: .3125rem;
    background: #fff;
}

.attr-pop .img-info{
    width: 6.875rem;
    height: 1.77rem;
    overflow: hidden;
    margin-bottom: .415rem;
}

.attr-pop .img{
    float: left;
    height: 1.77rem;
    width: 1.77rem;
    background: #f4f4f4;
    margin-right: .3125rem;
}

.attr-pop .info{
    float: left;
    height: 1.77rem;
    display: flex;
    align-items: center;
}

.attr-pop .p{
    font-size: .33rem;
    color: #333;
    margin-bottom: .10rem;
}

.attr-pop .a{
    font-size: .29rem;
    color: #333;
    height: .40rem;
    line-height: .40rem;
}

.spec-con{
    width: 100%;
    height: auto;
    overflow: hidden;
}

.spec-con .name{
    height: .32rem;
    margin-bottom: .22rem;
    font-size: .29rem;
    color: #333;
}

.spec-con .values{
    height: auto;
    margin-bottom: .3125rem;
    font-size: 0;
}

.spec-con .value{
    display: inline-block;
    height: .62rem;
    padding: 0 .35rem;
    line-height: .56rem;
    text-align: center;
    margin-right: .25rem;
    margin-bottom: .165rem;
    border: 1px solid #333;
    font-size: .25rem;
    color: #333;
}

.spec-con .value.disable{
    border: 1px solid #ccc;
    color: #ccc;
}

.spec-con .value.selected{
    border: 1px solid #b4282d;
    color: #b4282d;
}

.number-item .selnum{
    width: 3.22rem;
    height: .71rem;
    border: 1px solid #ccc;
    display: flex;
}

.number-item .cut{
    width: .9375rem;
    height: 100%;
    text-align: center;
    line-height: .65rem;
}

.number-item .number{
    flex: 1;
    height: 100%;
    text-align: center;
    line-height: .6875rem;
    border-left: 1px solid #ccc;
    border-right: 1px solid #ccc;
    float: left;
}

.number-item .add{
    width: .9375rem;
    height: 100%;
    text-align: center;
    line-height: .65rem;
}

	.bottom-btn{
    position: fixed;
    left:0;
    bottom:0;
    z-index: 10;
    width: 7.50rem;
    height: 1.00rem;
    display: flex;
    background: #fff;
}
.bottom-btn .l{
    float: left;
    height: 1.00rem;
    width: 1.62rem;
    border: 1px solid #f4f4f4;
    display: flex;
    align-items: center;
    justify-content: center;
    
}
.bottom-btn .l.l-collect{
    border-right: none;
    border-left: none;
    text-align: center;
}
.bottom-btn .l.l-cart .box{
    position: relative;
    height: .60rem;
    width: .60rem;
}
.bottom-btn .l.l-cart .cart-count{
    height: .28rem;
    width: .28rem;
    z-index: 10;
    position: absolute;
    top: 0;
    right:0;
    background: #b4282d;
    text-align: center;
    font-size: .18rem;
    color: #fff;
    line-height: .28rem;
    border-radius: 50%;
}
.bottom-btn .l.l-cart .icon{
    position: absolute;
    top: .10rem;
    left:0;
}
.bottom-btn .l .icon{
    display: block;
    height: .44rem;
    width: .44rem;
}
.bottom-btn .c{
    float: left;
    height: 1.00rem;
    line-height: .96rem;
    flex: 1;
    text-align: center;
    color: #333;
    border-top: 1px solid #f4f4f4;
    border-bottom: 1px solid #f4f4f4;
}
.bottom-btn .r{
    border:1px solid #b4282d;
    background: #b4282d;
    float: left;
    height: 1.00rem;
    line-height: .96rem;
    flex: 1;
    text-align: center;
    color: #fff;
    border-radius:0;
    font-size: .34rem;
}
.bottom-btn .disabled{
    border:none;
    background: #959191;
    float: left;
    height: 1.00rem;
    line-height: .96rem;
    flex: 1;
    text-align: center;
    color: #fff;
    border-radius:0;
    font-size: .34rem;
}

.detail >>>  .fr-fin{
	width: 100%;
}

.detail >>> p{
	font-size:0;
}

.detail >>> .fr-fvn{
	font-size:.28rem;
}

.related-goods{
width: 7.50rem;
height: auto;
overflow: hidden;
}
.related-goods .h{
    position: relative;
    height: .5rem;
    width: 7.50rem;
    padding: .5625rem 0;
    background: #fff;
    text-align: center;
    border-bottom: 1px solid #f4f4f4;
}
.related-goods .h .line{
    display: inline-block;
    position: absolute;
    top: .72rem;
    left: 0;
    z-index: 2;
    height: 1px;
    margin-left: 2.25rem;
    width: 3.00rem;
    background: #ccc;
}

.related-goods .h .title{
    display: inline-block;
    position: absolute;
    top: .56125rem;
    left: 0;
    z-index: 3;
    height: .33rem;
    margin-left: 2.85rem;
    width: 1.80rem;
    background: #fff;
    font-size: .28rem;
}

.related-goods .b{
  width: 7.50rem;
  height: auto;
  overflow: hidden;
}

.related-goods .b .item{
  float: left;
  background: #fff;
  width: 3.75rem;
  height: auto;
  overflow: hidden;
  text-align: center;
  padding: .15rem .3125rem;
  border-right: 1px solid #f4f4f4;
  border-bottom: 1px solid #f4f4f4;
  box-sizing: border-box;
   -webkit-box-sizing: border-box;
}

.related-goods .item .img{
  width: 3.1145rem;
  height: 3.1145rem;
}

.related-goods .item .name{
  display: block;
  width: 3.1145rem;
  height: .35rem;
  margin: .115rem 0 .15rem 0;
  text-align: center;
  overflow: hidden;
  font-size: .30rem;
  color: #333;
}

.related-goods .item .price{
  display: block;
  width: 3.1145rem;
  height: .30rem;
  text-align: center;
  font-size:.30rem;
  color: #b4282d;
}

.common-problem{
    width: 7.50rem;
    height: auto;
    overflow: hidden;
}

.common-problem .h{
    position: relative;
    height: 1.455rem;
    width: 7.50rem;
    background: #fff;
    text-align: center;
}

.common-problem .h .line{
    display: inline-block;
    position: absolute;
    top: .72rem;
    left: 0;
    z-index: 2;
    height: 1px;
    margin-left: 2.25rem;
    width: 3.00rem;
    background: #ccc;
}

.common-problem .h .title{
    display: inline-block;
    position: absolute;
    top: .56rem;
    left: 0;
    z-index: 3;
    height: .33rem;
    margin-left: 2.85rem;
    width: 1.80rem;
    background: #fff;
    font-size: .28rem;
}

.common-problem .b{
  width: 7.50rem;
  height: auto;
  overflow: hidden;
  padding: 0rem .30rem;
  background: #fff;

}

.common-problem .item{
  height: auto;
  overflow: hidden;
  padding-bottom: .25rem;
  width: 6.8rem;
}

.common-problem .question-box .spot{
  float: left;
  display: block;
  height: .08rem;
  width: .08rem;
  background: #b4282d;
  border-radius: 50%;
  margin-top: .11rem;
}

.common-problem .question-box .question{
  text-align: left;
  line-height: .30rem;
  padding-left: .08rem;
  display: block;
  font-size: .26rem;
  padding-bottom: .15rem;
  color: #303030;
  padding-left: .2rem;
}

.common-problem .answer{
  line-height: .36rem;
  padding-left: .16rem;
  font-size: .26rem;
  color: #787878;
  text-align: left;
}

	.goods-attr{
    height: auto;
    overflow: hidden;
    padding: 0 .3125rem .25rem .3125rem;
    background: #fff;
}

.goods-attr .t{
    width: 6.875rem;
    height: 1.04rem;
    line-height: 1.04rem;
    font-size: .385rem;
    text-align: left;
}

.goods-attr .item{
    width: 6.875rem;
    height: .68rem;
    padding: .11rem .20rem;
    margin-bottom: .11rem;
    background: #f7f7f7;
    font-size: .385rem;
}

.goods-attr .left{
    float: left;
    font-size: .25rem;
    width: 1.34rem;
    height: .45rem;
    line-height: .45rem;
    overflow: hidden;
    color: #999;
}

.goods-attr .right{
    float: left;
    font-size: .365rem;
    margin-left: .20rem;
    width: 4.80rem;
    height: .45rem;
    line-height: .45rem;
    overflow: hidden;
    color: #333;
}

	.section-nav{
    width: 7.50rem;
    height: 1.08rem;
    background: #fff;
    margin-bottom: .20rem;
}

.section-nav .t{
    float: left;
    width: 6.00rem;
    height: 1.08rem;
    line-height: 1.08rem;
    font-size: .29rem;
    color: #333;
    margin-left: .3125rem;
    text-align: left;
}

.section-nav .i{
    float: right;
    width: .52rem;
    height: .52rem;
    margin-right: .16rem;
    margin-top: .28rem;
}

.section-act .t{
    float: left;
    display: flex;
    align-items: center;
    width: 6.00rem;
    height: 1.08rem;
    overflow: hidden;
    line-height: 1.08rem;
    font-size: .29rem;
    color: #999;
    margin-left: .3125rem;
}

.section-act .label{
    color: #999;
}

.section-act .tag{
    display: flex;
    align-items: center;
    padding:0 .10rem;
    border-radius: .03rem;
    height: .37rem;
    width: auto;
    color: #f48f18;
    overflow: hidden;
    border: 1px solid #f48f18;
    font-size: .25rem;
    margin:0 .10rem;
}

.section-act .text{
    display: flex;
    align-items: center;
    height: .37rem;
    width: auto;
    overflow: hidden;
    color: #f48f18;
    font-size: .29rem;
}

	.swiper{
		height:7.5rem;
	}
	.swiper img{
		height:7.5rem;
		width:7.5rem;
	}
	.service-policy {
		width:7rem;
		height:.73rem;
		background:#f4f4f4;
		display: flex;
		justify-content: space-between;
		margin:0 auto;
	}
	.service-policy .item {
		background:url(http://nos.netease.com/mailpub/hxm/yanxuan-wap/p/20150730/style/img/icon-normal/servicePolicyRed-518d32d74b.png) 0 center no-repeat;
		background-size:.10rem;
		padding-left:.15rem;
		font-size:.25rem;
		color:#666;
		display: flex;
		align-items: center;
	}
	.goods-info{
    width: 7.50rem;
    height: 3.06rem;
    overflow: hidden;
    background: #fff;
}

.goods-info .c{
    display: block;
    width: 7.1rem;
    margin-left: .31rem;
    padding: .38rem 31.25rem .38rem 0;
    border-bottom: 1px solid #f4f4f4;
}

.goods-info .c text{
    display: block;
    width: 6.875rem;
    text-align: center;
}

.goods-info .name{
    height: .41rem;
    margin-bottom: .05rem;
    font-size: .41rem;
    line-height: .41rem;
}

.goods-info .desc{
    height: .43rem;
    margin-bottom: .41rem;
    font-size: .24rem;
    line-height: .36rem;
    color: #999;
}


.goods-info .price{
    height: .35rem;
    font-size: .35rem;
    line-height: .35rem;
    color: #b4282d;
}

.goods-info .brand{
    margin-top: .23rem;
    min-height: .40rem;
    text-align: center;
    font-size:0;
}

.goods-info .brand p{
    display: inline-block;
    width: auto;
    padding: .02px .30rem .02px .105rem;
    line-height: .355rem;
    border: 1px solid #f48f18;
    font-size: .25rem;
    color: #f48f18;
    border-radius: .04rem;
    background: url(http://nos.netease.com/mailpub/hxm/yanxuan-wap/p/20150730/style/img/icon-normal/detailTagArrow-18bee52dab.png) 95% center no-repeat;
    background-size: .1075rem .1875rem;
}
</style>
