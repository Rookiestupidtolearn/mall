<template>
 	<div class="container">
 		<!--公用头部-->
  		<!--<headbar :headFont = "headFont"></headbar>-->
  	
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
		        <!--<p class="desc">{{goods.goods_brief}}</p>-->
		        <p class="price">￥{{goods.market_price}}</p>
		        <div class="brand" v-if="!brandShow"></div>
		        <div class="brand" v-else>
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
    	 <div v-if="openAttr" class="attr-pop ">
		    <div class="img-info">
		      <img class="img" :src="goods.list_pic_url"/>
		      <div class="info">
		        <div class="c">
		          <p class="p">价格：￥{{market_price}}</p>
		          <p class="a" v-if="productList.length>0">已选择：{{checkedSpecText}}</p>
		        </div>
		      </div>
		    </div>
		    <div class="spec-con">
		      <div class="spec-item" v-for="item in specificationList" >
		        <div class="name">{{item.name}}</div>
		        <div class="values">
		          <div v-for="vitem in item.valueList" class="value" :class="vitem.checked ? 'selected' : '' " :data-c="vitem.id" :data-d="vitem.specification_id"  @click="clickSkuValue" >{{vitem.value}}</div>
		        </div>
		      </div>
		      <div class="number-item">
		        <div class="name">数量</div>
		        <div class="selnum">
		          <div class="cut" @click="cutNumber">-</div>
		          <input :value="number" class="number"  type="number" />
		          <div class="add" @click="addNumber">+</div>
		        </div>
		      </div>
		    </div>
		  </div>
		 <!--加入购物车底栏-->
    	<div class="bottom-btn">
			  <div class="l l-collect" :class="openAttr ? 'back' : ''" @click="closeAttrOrCollect">
			    <img class="icon" :src="collectBackImage "/>
			  </div>
			  <div class="l l-cart" @click="openCartPage">
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
//	import headbar from '@/components/headbar.vue'
		
	export default {
	  name: 'goods',
//	  components:{headbar},
	  data () {
	    return {
//	    	headFont:'商品详情',
	    	market_price:'',
	    	idm:'',
	    	banner:[],
	    	undercarriage:'',
	    	undercarriName:'加入购物车',
	    	goods:'',
	    	brand:'',
	    	brandShow:false,
	    	minPriceList:{},
	    	attribute:[],
	    	issueList:[],
	    	relatedGoods:[],
    	 	productList: [],
	    	goods_desc:[],
	    	number: 1,
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
	  		
	  		//关联
	    	that.$http({
	    		method: 'post',
		        url:that.$url+ 'goods/related',
		        params:{id:that.idm}
	    	}).then(function (response) {
		          that.relatedGoods = response.data.data.goodsList;
		    });
	    	
	  		//商品详情
	    	that.$http({
		        method: 'post',
		        url:that.$url+ 'goods/detail',
		        params:{id:that.idm}
	    	}).then(function (response) {
	    		response = {"errno":0,"data":{"specificationList":[],"issue":[{"id":4,"goods_id":null,"question":"如何开具发票？","answer":"1.如需开具普通发票，请在下单时选择“我要开发票”并填写相关信息（APP仅限2.4.0及以"},{"id":3,"goods_id":null,"question":"如何申请退货？","answer":"1.自收到商品之日起30日内，顾客可申请无忧退货，退款将原路返还，不同的银行处理时间不同，"},{"id":2,"goods_id":null,"question":"使用什么快递发货？","answer":"严选默认使用顺丰快递发货（个别商品使用其他快递），配送范围覆盖全国大部分地区（港澳台地区除"},{"id":1,"goods_id":null,"question":"购买运费如何收取？","answer":"免邮费"}],"userHasCollect":0,"comment":{"data":{},"count":0},"attribute":[],"minPriceList":[{"id":242,"goods_id":1155015,"product_id":null,"goods_specification_ids":"","goods_sn":"1155015","goods_number":100,"market_price":0.00,"retail_price":12.90,"goods_name":"绿豆糕 80克（4枚入）","list_pic_url":"http://yanxuan.nosdn.127.net/66b9f1638c0517d179262f14ed1345f9.png"}],"brand":null,"gallery":[],"productList":[{"id":242,"goods_id":1155015,"product_id":null,"goods_specification_ids":"","goods_sn":"1155015","goods_number":100,"market_price":0.00,"retail_price":12.90,"goods_name":"绿豆糕 80克（4枚入）","list_pic_url":"http://yanxuan.nosdn.127.net/66b9f1638c0517d179262f14ed1345f9.png"}],"info":{"id":1155015,"category_id":1008015,"goods_sn":"1155015","name":"绿豆糕 80克（4枚入）","brand_id":0,"goods_number":100,"keywords":"","goods_brief":"细腻松软，入口绵柔","goods_desc":"","is_on_sale":1,"add_time":1504064411000,"sort_order":6,"is_delete":0,"attribute_category":0,"counter_price":0.00,"extra_price":0.00,"is_new":1,"goods_unit":"件","primary_pic_url":"http://yanxuan.nosdn.127.net/67d52acd78bf685bb5982bcac47ca01a.jpg","list_pic_url":"http://yanxuan.nosdn.127.net/66b9f1638c0517d179262f14ed1345f9.png","market_price":14.19,"retail_price":12.90,"sell_volume":7353,"primary_product_id":1162102,"unit_price":0.00,"promotion_desc":"限时购","promotion_tag":"","app_exclusive_price":0.00,"is_app_exclusive":0,"is_limited":0,"is_hot":1,"cart_num":0,"product_id":null,"product_market_price":0}},"errmsg":"执行成功"};
//	    		response = {"errno":0,"data":{"specificationList":[{"specification_id":1,"valueList":[{"id":1,"goods_id":1181000,"specification_id":1,"value":"1.5m床垫*1+枕头*2","name":"颜色","pic_url":""},{"id":2,"goods_id":1181000,"specification_id":1,"value":"1.8m床垫*1+枕头*2","name":"颜色","pic_url":""}],"name":"颜色"},{"specification_id":2,"valueList":[{"id":4,"goods_id":1181000,"specification_id":2,"value":"玛瑙红","name":"规格","pic_url":"http://yanxuan.nosdn.127.net/29442127f431a1a1801c195905319427.png?quality=90&thumbnail=200x200&imageView"},{"id":5,"goods_id":1181000,"specification_id":2,"value":"烟白灰","name":"规格","pic_url":"http://yanxuan.nosdn.127.net/36f64a7161b67e7fb8ea45be32ecfa25.png?quality=90&thumbnail=200x200&imageView"},{"id":3,"goods_id":1181000,"specification_id":2,"value":"浅杏粉","name":"规格","pic_url":"http://yanxuan.nosdn.127.net/10022c73fa7aa75c2c0d736e96cc56d5.png?quality=90&thumbnail=200x200&imageView"}],"name":"规格"}],"issue":[{"id":4,"goods_id":null,"question":"如何开具发票？","answer":"1.如需开具普通发票，请在下单时选择“我要开发票”并填写相关信息（APP仅限2.4.0及以"},{"id":3,"goods_id":null,"question":"如何申请退货？","answer":"1.自收到商品之日起30日内，顾客可申请无忧退货，退款将原路返还，不同的银行处理时间不同，"},{"id":2,"goods_id":null,"question":"使用什么快递发货？","answer":"严选默认使用顺丰快递发货（个别商品使用其他快递），配送范围覆盖全国大部分地区（港澳台地区除"},{"id":1,"goods_id":null,"question":"购买运费如何收取？","answer":"免邮费"}],"userHasCollect":0,"comment":{"data":{},"count":0},"attribute":[{"id":null,"attribute_category_id":null,"name":"规格","input_type":null,"value":"组合一：AB面独立弹簧床垫 进口乳胶150*200cm*1+可水洗抗菌防螨丝羽绒枕*2。\n组合二：AB面独立弹簧床垫 进口乳胶180*200cm*1+可水洗抗菌防螨丝羽绒枕*2","sort_order":null}],"minPriceList":[{"id":2,"goods_id":1181000,"product_id":null,"goods_specification_ids":"1_4","goods_sn":"Y100400","goods_number":200,"market_price":0.00,"retail_price":1500.00,"goods_name":"母亲节礼物-舒适安睡组合","list_pic_url":"http://yanxuan.nosdn.127.net/1f67b1970ee20fd572b7202da0ff705d.png"}],"brand":{"id":1001020,"name":"Ralph Lauren制造商","list_pic_url":"http://yanxuan.nosdn.127.net/9df78eb751eae2546bd3ee7e61c9b854.png","simple_desc":"我们与Ralph Lauren Home的制造商成功接洽，掌握先进的生产设备，传承品牌工艺和工序。追求生活品质的你，值得拥有。","pic_url":"http://yanxuan.nosdn.127.net/089e4066f0c2bc6b062d17c6292735dc.png","sort_order":20,"is_show":1,"floor_price":29.00,"app_list_pic_url":"http://yanxuan.nosdn.127.net/9df78eb751eae2546bd3ee7e61c9b854.png","is_new":0,"new_pic_url":"","new_sort_order":10},"gallery":[{"id":735,"goods_id":1181000,"img_url":"http://yanxuan.nosdn.127.net/355efbcc32981aa3b7869ca07ee47dac.jpg","img_desc":"","sort_order":5},{"id":734,"goods_id":1181000,"img_url":"http://yanxuan.nosdn.127.net/43e283df216881037b70d8b34f8846d3.jpg","img_desc":"","sort_order":5},{"id":733,"goods_id":1181000,"img_url":"http://yanxuan.nosdn.127.net/12e41d7e5dabaf9150a8bb45c41cf422.jpg","img_desc":"","sort_order":5},{"id":732,"goods_id":1181000,"img_url":"http://yanxuan.nosdn.127.net/5c1d28e86ccb89980e6054a49571cdec.jpg","img_desc":"","sort_order":5}],"productList":[{"id":246,"goods_id":1181000,"product_id":null,"goods_specification_ids":"1_5","goods_sn":"1181000","goods_number":100,"market_price":3333.00,"retail_price":0.01,"goods_name":"母亲节礼物-舒适安睡组合","list_pic_url":"http://yanxuan.nosdn.127.net/1f67b1970ee20fd572b7202da0ff705d.png"},{"id":245,"goods_id":1181000,"product_id":null,"goods_specification_ids":"2_5","goods_sn":"1181000","goods_number":100,"market_price":3333.00,"retail_price":2578.00,"goods_name":"母亲节礼物-舒适安睡组合","list_pic_url":"http://yanxuan.nosdn.127.net/1f67b1970ee20fd572b7202da0ff705d.png"},{"id":5,"goods_id":1181000,"product_id":null,"goods_specification_ids":"2_4","goods_sn":"Y200400","goods_number":200,"market_price":0.00,"retail_price":2000.00,"goods_name":"母亲节礼物-舒适安睡组合","list_pic_url":"http://yanxuan.nosdn.127.net/1f67b1970ee20fd572b7202da0ff705d.png"},{"id":4,"goods_id":1181000,"product_id":null,"goods_specification_ids":"2_3","goods_sn":"Y200300","goods_number":400,"market_price":0.00,"retail_price":1001.00,"goods_name":"母亲节礼物-舒适安睡组合","list_pic_url":"http://yanxuan.nosdn.127.net/1f67b1970ee20fd572b7202da0ff705d.png"},{"id":2,"goods_id":1181000,"product_id":null,"goods_specification_ids":"1_4","goods_sn":"Y100400","goods_number":200,"market_price":0.00,"retail_price":1500.00,"goods_name":"母亲节礼物-舒适安睡组合","list_pic_url":"http://yanxuan.nosdn.127.net/1f67b1970ee20fd572b7202da0ff705d.png"},{"id":1,"goods_id":1181000,"product_id":null,"goods_specification_ids":"","goods_sn":"Y100300","goods_number":100,"market_price":0.00,"retail_price":999.00,"goods_name":"母亲节礼物-舒适安睡组合","list_pic_url":"http://yanxuan.nosdn.127.net/1f67b1970ee20fd572b7202da0ff705d.png"}],"info":{"id":1181000,"category_id":1008008,"goods_sn":"1181000","name":"母亲节礼物-舒适安睡组合","brand_id":1001020,"goods_number":100,"keywords":"","goods_brief":"安心舒适是最好的礼物","goods_desc":"<p><img src=\"http://yanxuan.nosdn.127.net/3ddfe10db43f7df33ba82ae7570ada80.jpg\" style=\"\" class=\"fr-fin\"></p><p><img src=\"http://yanxuan.nosdn.127.net/7682b7930b4776ce032f509c24a74a1e.jpg\" style=\"\" class=\"fr-fin\"></p><p><img src=\"http://yanxuan.nosdn.127.net/e0bb6a50e27681925c5bb26bceb67ef4.jpg\" style=\"\" class=\"fr-fin\"></p><p><img src=\"http://yanxuan.nosdn.127.net/ba63b244c74ce06fda82bb6a29cc0f71.jpg\" style=\"\" class=\"fr-fin\"></p><p><img src=\"http://yanxuan.nosdn.127.net/3c7808c3a4769bad5af4974782f08654.jpg\" style=\"\" class=\"fr-fin\"></p><p><img src=\"http://yanxuan.nosdn.127.net/71798aaac23a91fdab4d77e1b980a4df.jpg\" style=\"\" class=\"fr-fin\"></p><p><img src=\"http://yanxuan.nosdn.127.net/c88cbb2dd2310b732571f49050fe4059.jpg\" style=\"\" class=\"fr-fin\"></p><p><img src=\"http://yanxuan.nosdn.127.net/5dfdcd654e0f3076f7c05dd9c19c15ea.jpg\" style=\"\" class=\"fr-fin\"></p><p><img src=\"http://yanxuan.nosdn.127.net/bd55d6ef7af69422d8d76af10ee70156.jpg\" style=\"\" class=\"fr-fin\"></p><p><img src=\"http://yanxuan.nosdn.127.net/bae571b22954c521b35e446d652edc1d.jpg\" style=\"\" class=\"fr-fin\"></p><p><img src=\"http://yanxuan.nosdn.127.net/e709c4d9e46d602a4d2125e47110f6ae.jpg\" style=\"\" class=\"fr-fin\"></p><p><img src=\"http://yanxuan.nosdn.127.net/83e41915035c418db177af8b1eca385c.jpg\" style=\"\" class=\"fr-fin\"></p><p><img src=\"http://yanxuan.nosdn.127.net/f42c561e6935fe3e0e0873653da78670.jpg\" style=\"\" class=\"fr-fin\"></p><p><img src=\"http://yanxuan.nosdn.127.net/8317726fbae80b98764dc4c6233a913e.jpg\" style=\"\" class=\"fr-fin\"></p><p><img src=\"http://yanxuan.nosdn.127.net/ba904b7c948b8015db2171435325270f.jpg\" style=\"\" class=\"fr-fin\"></p><p><img src=\"http://yanxuan.nosdn.127.net/4969c73d0d8f29bffb69529c96ca4889.jpg\" style=\"\" class=\"fr-fin\"></p><p><img src=\"http://yanxuan.nosdn.127.net/d80b9b8c5c31031d1cd5357e48748632.jpg\" style=\"\" class=\"fr-fin\"></p><p><img src=\"http://yanxuan.nosdn.127.net/3fe79bdae40662a7b1feed3179d3dd1f.jpg\" style=\"\" class=\"fr-fin\"></p><p><img src=\"http://yanxuan.nosdn.127.net/79eef059963b12479f65e782d1dca128.jpg\" style=\"\" class=\"fr-fin\"></p><p><img src=\"http://yanxuan.nosdn.127.net/e5a8f64f4297ccc01b41df98b0f252c8.jpg\" style=\"\" class=\"fr-fin\"></p><p><img src=\"http://yanxuan.nosdn.127.net/a940b9e9525c4861407e4c3b07b02977.jpg\" style=\"\" class=\"fr-fin\"></p><p><img src=\"http://yanxuan.nosdn.127.net/224b8b81cbe12e4ad060a50f1e26601a.jpg\" style=\"\" class=\"fr-fin\"></p><p><img src=\"http://yanxuan.nosdn.127.net/85e151647452fad718effb7b1adc18e2.jpg\" style=\"\" class=\"fr-fin\"></p><p><img src=\"http://yanxuan.nosdn.127.net/d47444ff3bb9dc0aa4ab1f9b84d83768.jpg\" style=\"\" class=\"fr-fin\"></p><p><img src=\"http://yanxuan.nosdn.127.net/136262743f0c849cc0c55c8e7963dd7e.jpg\" style=\"\" class=\"fr-fin\"></p><p><img src=\"http://yanxuan.nosdn.127.net/331a97cbaff5b25a3b08ed7cdfe29df9.jpg\" style=\"\" class=\"fr-fin\"></p><p><img src=\"http://yanxuan.nosdn.127.net/89b450aa0a8afe1db566dcad926f1fe8.jpg\" style=\"\" class=\"fr-fin\"></p><p><img src=\"http://yanxuan.nosdn.127.net/c1cf94f13b7280a97e751cebe573fa78.jpg\" style=\"\" class=\"fr-fin\"></p><p><img src=\"http://yanxuan.nosdn.127.net/1822c23def83b77e7607c24237eeec74.jpg\" style=\"\" class=\"fr-fin\"></p><p><img src=\"http://yanxuan.nosdn.127.net/2af234312b3914d6d0bc316f92134614.jpg\" style=\"\" class=\"fr-fin\"></p><p><img src=\"http://yanxuan.nosdn.127.net/c4f8ab2b3813275d954a8bedcf902d26.jpg\" style=\"\" class=\"fr-fin\"></p><p><img src=\"http://yanxuan.nosdn.127.net/42f18842ff0c92ed849c4401ae47bb61.jpg\" style=\"\" class=\"fr-fin\"></p><p><img src=\"http://yanxuan.nosdn.127.net/a8ea64a35799e50ab31ecb65345fe8f4.jpg\" style=\"\" class=\"fr-fin\"></p><p><img src=\"http://yanxuan.nosdn.127.net/18759fa90cd153bdd744280807c3c155.jpg\" style=\"\" class=\"fr-fin\"></p><p><img src=\"http://yanxuan.nosdn.127.net/431f00d068a8e747959deb3b7bdd495a.jpg\" style=\"\" class=\"fr-fin\"></p><p><img src=\"http://yanxuan.nosdn.127.net/5bd3b44f1f4c627bfa39f7809e866ec6.jpg\" style=\"\" class=\"fr-fin\"></p><p><img src=\"http://yanxuan.nosdn.127.net/7fc36778fe2f6129b9c26e8298c5be7e.jpg\" style=\"\" class=\"fr-fin\"></p><p><img src=\"http://yanxuan.nosdn.127.net/c568432e3d5ab6786cd9dcae8008891b.jpg\" style=\"\" class=\"fr-fin\"></p><p><img src=\"http://yanxuan.nosdn.127.net/ec82ff5aecafa48807117da68cce2ce9.jpg\" style=\"\" class=\"fr-fin\"></p><p><img src=\"http://yanxuan.nosdn.127.net/b8eccbed570da595e6f8a71ed4abc42c.jpg\" style=\"\" class=\"fr-fin\"></p><p><img src=\"http://yanxuan.nosdn.127.net/9cae1fed6ecefcd61435fe6e2c700fd6.jpg\" style=\"\" class=\"fr-fin\"></p><p><img src=\"http://yanxuan.nosdn.127.net/e306a418f82777399f5e88b93cca22db.jpg\" style=\"\" class=\"fr-fin\"></p><p><img src=\"http://yanxuan.nosdn.127.net/a66d717084e23864ce079f936557709f.jpg\" style=\"\" class=\"fr-fin\"></p><p><img src=\"http://yanxuan.nosdn.127.net/6ae06c6505cdbf87a0210fe3b8727d5f.jpg\" style=\"\" class=\"fr-fin\"></p><p><img src=\"http://yanxuan.nosdn.127.net/58ac2086725b0ba2fe800195f274a0b4.jpg\" style=\"\" class=\"fr-fin\"></p><p><img src=\"http://yanxuan.nosdn.127.net/5e2e9d9eb099647fbe041ec6645ac034.jpg\" style=\"\" class=\"fr-fin\"></p><p><img src=\"http://yanxuan.nosdn.127.net/8154357c0fab82bd4e67cda9aaa128c0.jpg\" style=\"\" class=\"fr-fin\"></p><p><img src=\"http://yanxuan.nosdn.127.net/4325763b738ec3183ecf0d82b2b28e32.jpg\" style=\"\" class=\"fr-fin\"></p><p><img src=\"http://yanxuan.nosdn.127.net/06d8ea9d10035a00f26c5c52cc601ca7.jpg\" style=\"\" class=\"fr-fin\"></p><p><img src=\"http://yanxuan.nosdn.127.net/499f30b9e69b5dddf3db36f105756111.jpg\" style=\"\" class=\"fr-fin\"></p><p><img src=\"http://yanxuan.nosdn.127.net/ed7e1733d54e711a560edb3a76f1a60c.jpg\" style=\"\" class=\"fr-fin\"></p><p><img src=\"http://yanxuan.nosdn.127.net/b6474347eebdb917d2e827fd526dd01c.jpg\" style=\"\" class=\"fr-fin\"></p><p><img src=\"http://yanxuan.nosdn.127.net/b2c0691f9204c5ebc94b4ff678919ca7.jpg\" style=\"\" class=\"fr-fin\"></p><p><img src=\"http://yanxuan.nosdn.127.net/b4811e702a6884a9251d7cc9e3b06b6f.jpg\" style=\"\" class=\"fr-fin\"></p><p><img src=\"http://yanxuan.nosdn.127.net/d518783c054695acf329e81d597fdec3.jpg\" style=\"\" class=\"fr-fin\"></p><p><img src=\"http://yanxuan.nosdn.127.net/835ce09e785cca635c176008975053a1.jpg\" style=\"\" class=\"fr-fin\"></p><p><img src=\"http://yanxuan.nosdn.127.net/769af780de81a302c0a3b03ed8e6c528.jpg\" style=\"\" class=\"fr-fin\"></p><p><img src=\"http://yanxuan.nosdn.127.net/da34f99daf9141f0fe56a766461b8485.jpg\" style=\"\" class=\"fr-fin\"></p><p><img src=\"http://yanxuan.nosdn.127.net/d7c9cd8722a2f9a78e158ce02ec5d4f2.jpg\" style=\"\" class=\"fr-fin\"></p><p><img src=\"http://yanxuan.nosdn.127.net/09ea18953884b15227819e326103462b.jpg\" style=\"\" class=\"fr-fin\"></p><p><img src=\"http://yanxuan.nosdn.127.net/5ef728213983842edf1aec27b2c1f5b6.jpg\" style=\"\" class=\"fr-fin\"></p><p><img src=\"http://yanxuan.nosdn.127.net/95409f2a884dcfeaabfe5e61fcf9ec37.jpg\" style=\"\" class=\"fr-fin\"></p><p><img src=\"http://yanxuan.nosdn.127.net/6807836dc2a940ba56ea10c7a63b14c9.jpg\" style=\"\" class=\"fr-fin\"></p><p><img src=\"http://yanxuan.nosdn.127.net/e1d976d06853e7a0e6c9cc4ab484ac8a.jpg\" style=\"\" class=\"fr-fin\"></p><p><img src=\"http://yanxuan.nosdn.127.net/47f5673dec5005092f6d897d6335966c.jpg\" style=\"\" class=\"fr-fin\"></p><p><img src=\"http://yanxuan.nosdn.127.net/1b0109abd0e6a0d13fa2423a96c1167e.jpg\" style=\"\" class=\"fr-fin\"></p><p><img src=\"http://yanxuan.nosdn.127.net/916111a8f94cc0bd39375b3dcac14e35.jpg\" style=\"\" class=\"fr-fin\"></p><p><img src=\"http://yanxuan.nosdn.127.net/c1360df3d6b703c5df9b2f47a2a3d12e.jpg\" style=\"\" class=\"fr-fin\"></p><p><img src=\"http://yanxuan.nosdn.127.net/1d5a31eb93ef873a165993bd99f29df1.jpg\" style=\"\" class=\"fr-fin\"></p><p><img src=\"http://yanxuan.nosdn.127.net/997a48948b89dd7261ed5a59ba884f45.jpg\" style=\"\" class=\"fr-fin\"></p><p><img src=\"http://yanxuan.nosdn.127.net/eb96d9689735c9f4019ebf76da43b2b2.jpg\" style=\"\" class=\"fr-fin\"></p><p><img src=\"http://yanxuan.nosdn.127.net/a92cf2172e6cafe080e4511205568d79.jpg\" style=\"\" class=\"fr-fin\"></p><p><img src=\"http://yanxuan.nosdn.127.net/c9e94570428f197292bb3f43609963f5.jpg\" style=\"\" class=\"fr-fin\"></p><p><img src=\"http://yanxuan.nosdn.127.net/37145f06cce747311692ad7f276645db.jpg\" style=\"\" class=\"fr-fin\"></p><p><img src=\"http://yanxuan.nosdn.127.net/c9a698b71ed911364fc6f243006c241c.jpg\" style=\"\" class=\"fr-fin\"></p><p><img src=\"http://yanxuan.nosdn.127.net/e89db969711efaa441c43d6b90498a0c.jpg\" style=\"\" class=\"fr-fin\"></p><p><img src=\"http://yanxuan.nosdn.127.net/3803bb1a18229562f18943512b1d3524.jpg\" style=\"\" class=\"fr-fin\"></p><p><img src=\"http://yanxuan.nosdn.127.net/235cbb5be905ac2b87e7e8f7c8d90144.jpg\" style=\"\" class=\"fr-fin\"></p><p><img src=\"http://yanxuan.nosdn.127.net/3e38435b3fdbae4ee80b83995592901e.jpg\" style=\"\" class=\"fr-fin\"></p><p><img src=\"http://yanxuan.nosdn.127.net/8ceb7cd3231585da60a74dd2c1aa9015.jpg\" style=\"\" class=\"fr-fin\"></p><p><img src=\"http://yanxuan.nosdn.127.net/e151e225c2e30aab7ccf086094381577.jpg\" style=\"\" class=\"fr-fin\"></p><p><img src=\"http://yanxuan.nosdn.127.net/363c19306953daf10968f4aa86617997.jpg\" style=\"\" class=\"fr-fin\"></p><p><img src=\"http://yanxuan.nosdn.127.net/4237a392cf2e69b110ad4ecf35e44059.jpg\" style=\"\" class=\"fr-fin\"></p><p><img src=\"http://yanxuan.nosdn.127.net/da8ab35ada2dfe55006db01efa96e51a.jpg\" style=\"\" class=\"fr-fin\"></p><p><img src=\"http://yanxuan.nosdn.127.net/aa1d4fd00b7879db3f1051dc6d16aa87.jpg\" style=\"\" class=\"fr-fin\"></p><p><img src=\"http://yanxuan.nosdn.127.net/302a8f2d997ff22bedcd837672cdafc2.jpg\" style=\"\" class=\"fr-fin\"></p><p><img src=\"http://yanxuan.nosdn.127.net/a39ff68c00522aef0472402958a334d2.jpg\" style=\"\" class=\"fr-fin\"></p><p><img src=\"http://yanxuan.nosdn.127.net/86ccd0eb677c8b552398869d11a8917e.jpg\" style=\"\" class=\"fr-fin\"></p><p><img src=\"http://yanxuan.nosdn.127.net/a6d0ede352da947060d912d903646a5d.jpg\" style=\"\" class=\"fr-fin\"></p><p><img src=\"http://yanxuan.nosdn.127.net/e6a118bf95bdb61891409d25f193e9c4.jpg\" style=\"\" class=\"fr-fin\"></p><p><img src=\"http://yanxuan.nosdn.127.net/c214066e9bf65d60bcebd691b5b1cbc1.jpg\" style=\"\" class=\"fr-fin\"></p><p><img src=\"http://yanxuan.nosdn.127.net/c301559ba3ee280bcbf2fc4269bfa9ca.jpg\" style=\"\" class=\"fr-fin\"></p><p><img src=\"http://yanxuan.nosdn.127.net/573748f5c12ecb4515ba00a7b6e981dc.jpg\" style=\"\" class=\"fr-fin\"></p><p><img src=\"http://yanxuan.nosdn.127.net/27bcc8bf512a7e6f994a9683b3deea82.jpg\" style=\"\" class=\"fr-fin\"></p><p><img src=\"http://yanxuan.nosdn.127.net/e22a4507fd1e4b5ef859035e857ae419.jpg\" style=\"\" class=\"fr-fin\"></p><p><img src=\"http://yanxuan.nosdn.127.net/27b07b4ca709c33ad71b368f87781307.jpg\" style=\"\" class=\"fr-fin\"></p><p><img src=\"http://yanxuan.nosdn.127.net/ef31eb48bcb133728bffda7e1239b592.jpg\" style=\"\" class=\"fr-fin\"></p><p><img src=\"http://yanxuan.nosdn.127.net/5f49aaaca59c0733ec92f100d01bc0af.jpg\" style=\"\" class=\"fr-fin\"></p><p><img src=\"http://yanxuan.nosdn.127.net/818889261deb75044e1018ec53485d85.jpg\" style=\"\" class=\"fr-fin\"></p><p><img src=\"http://yanxuan.nosdn.127.net/200369f023243e2faeb18a2a0a352ef1.jpg\" style=\"\" class=\"fr-fin\"></p><p><br></p>","is_on_sale":1,"add_time":1504064411000,"sort_order":1,"is_delete":0,"attribute_category":1036000,"counter_price":0.00,"extra_price":0.00,"is_new":1,"goods_unit":"","primary_pic_url":"https://mp123.oss-cn-shenzhen.aliyuncs.com/upload/20170908/092741972ad4b4.jpg","list_pic_url":"http://yanxuan.nosdn.127.net/1f67b1970ee20fd572b7202da0ff705d.png","market_price":2857.80,"retail_price":2598.00,"sell_volume":1533,"primary_product_id":1194000,"unit_price":0.00,"promotion_desc":"限时购","promotion_tag":"","app_exclusive_price":0.00,"is_app_exclusive":0,"is_limited":0,"is_hot":1,"cart_num":0,"product_id":null,"product_market_price":0}},"errmsg":"执行成功"};
	    		let res = response;
	    		that.goods = res.data.info;
	    		that.banner = res.data.gallery;
	    		that.brand = res.data.brand;
	    		that.attribute = res.data.attribute;
	    		that.issueList = res.data.issue;
	    		that.productList = res.data.productList;
	    		that.goods_desc = res.data.info.goods_desc;
	    		that.undercarriage = res.data.info.is_on_sale; //上下架按钮显示状态
	    		that.market_price = res.data.info.market_price; //商品默认价格
	    		that.specificationList = res.data.specificationList;
	    		that.minPriceList = res.data.minPriceList;
//				that.banner = response.data.data.gallery

		    	//购物车下架至灰
		        if (that.undercarriage == '0' || that.undercarriage == '2'){
		            that.undercarriage = true,
		            that.undercarriName = '商品已下架';
		        }else{
		            that.undercarriage = false,
		            that.undercarriName = '加入购物车';
		        }
		        //设置默认值
		        that.setDefSpecInfo(that.specificationList);
		        if (res.userHasCollect == 1) {
		           that.collectBackImage = that.hasCollectImage;
		        } else {
		            that.collectBackImage = that.noCollectImage;
		        }
		        that.getGoodsRelated();
	        	 //默认显示最低价格组
		        let showArraySpec=[];
		        let _specificationList = that.specificationList;
		        let minGoodsSpec = (that.minPriceList[0].goods_specification_ids).split('_');
		        let specValueList = that.specificationList;
		        for (let i = 0; i < minGoodsSpec.length; i++){
		          for (let j = 0; j < specValueList[i].valueList.length; j++){
		            if (minGoodsSpec[i] == specValueList[i].valueList[j].id){
		              showArraySpec.push(specValueList[i].valueList[j].value);
		              if (_specificationList[i].valueList[j].checked) {
		                _specificationList[i].valueList[j].checked = false;
		              } else {
		                _specificationList[i].valueList[j].checked = true;
		              }
		            } else {
		              _specificationList[i].valueList[j].checked = false;
		            }
		          }
		        }
		        that.market_price = that.minPriceList[0].market_price;
		        that.checkedSpecText =  showArraySpec.join('　');
		        that.specificationList = _specificationList;
		        
//		        判断品牌制造商是否有
		        if(that.brand.name ==null || that.brand.name==undefined || that.brand.name == ''){
					that.brandShow = !that.brandShow;
				}else{
					that.brandShow = !that.brandShow;
				}
	 		 })
	  },
	  methods:{
	  	cutNumber(){
      		this.number = (this.number - 1 > 1) ? this.number - 1 : 1
	  	},
	  	addNumber () {
	     	 this.number = this.number + 1
	  	},
	  	openCartPage(){
	  		this.$router.push('/pages/shoppingcar');
	  	},
	  	getGoodsRelated() {
		    let that = this;
		    //关联
		    that.$http({
	    		method: 'post',
		        url:that.$url+ 'goods/related',
		        params:{id:that.idm}
	    	}).then(function (response) {
		          that.relatedGoods = response.data.data.goodsList;
		    });
		  },
	  	setDefSpecInfo(specificationList) {
	        //未考虑规格联动情况
	        let that = this;
	        if (!specificationList)return;
	        for (let i = 0; i < specificationList.length;i++){
	            let specification = specificationList[i];
	            let specNameId = specification.specification_id;
	            //规格只有一个时自动选择规格
	            if (specification.valueList && specification.valueList.length == 1){
	                let specValueId = specification.valueList[0].id;
	                that.clickSkuValue({ currentTarget: { dataset: { "nameId": specNameId, "valueId": specValueId } } });
	            }
	        }
	        specificationList.map(function(item){
	
	        });
	
	    },
	  	clickSkuValue(event){
		    let that = this;
		    let specNameId = event.target.dataset.d;
		    let specValueId = event.target.dataset.c;
		
		    //判断是否可以点击
		
		    //todo性能优化，可在v-for中添加index，可以直接获取点击的属性名和属性值，不用循环
		    let _specificationList = this.specificationList;
		    for (let i = 0; i < _specificationList.length; i++) {
		      if (_specificationList[i].specification_id == specNameId) {
		        for (let j = 0; j < _specificationList[i].valueList.length; j++) {
		          if (_specificationList[i].valueList[j].id == specValueId) {
		            //如果已经选中，则反选
		            if (_specificationList[i].valueList[j].checked) {
		              _specificationList[i].valueList[j].checked = false;
		            } else {
		              _specificationList[i].valueList[j].checked = true;
		            }
		          } else {
		            _specificationList[i].valueList[j].checked = false;
		          }
		        }
		      }
		    }
		     this.specificationList =  _specificationList;
		    //重新计算spec改变后的信息
		    this.changeSpecInfo();
		
		    //重新计算哪些值不可以点击
		  },
	  	//获取选中的规格信息
		  getCheckedSpecValue() {
		    let checkedValues = [];
		    let _specificationList =this.specificationList;
		    for (let i = 0; i < _specificationList.length; i++) {
		      let _checkedObj = {
		        nameId: _specificationList[i].specification_id,
		        valueId: 0,
		        valueText: ''
		      };
		      for (let j = 0; j < _specificationList[i].valueList.length; j++) {
		        if (_specificationList[i].valueList[j].checked) {
		          _checkedObj.valueId = _specificationList[i].valueList[j].id;
		          _checkedObj.valueText = _specificationList[i].valueList[j].value;
		        }
		      }
		      checkedValues.push(_checkedObj);
		    }
		
		    return checkedValues;
		
		  },
			  	//判断规格是否选择完整
		  isCheckedAllSpec () {
		    return !this.getCheckedSpecValue().some(function (v) {
		      if (v.valueId == 0) {
		        return true;
		      }
		    });
		  },
		  getCheckedSpecKey() {
		    let checkedValue = this.getCheckedSpecValue().map(function (v) {
		      return v.valueId;
		    });
		
		    return checkedValue.join('_');
		  },
		  changeSpecInfo() {
		    let checkedNameValue = this.getCheckedSpecValue();
		
		    //设置选择的信息
		    let checkedValue = checkedNameValue.filter(function (v) {
		      if (v.valueId != 0) {
		        return true;
		      } else {
		        return false;
		      }
		    }).map(function (v) {
		      return v.valueText;
		    });
		    if (checkedValue.length > 0) {
		        this.checkedSpecText = checkedValue.join('　');
		    } else {
		        this.checkedSpecText =  '请选择规格数量';
		    }
		    //提示选择完整规格
		    if (!this.isCheckedAllSpec()) {
		      return false;
		    }
		    //根据选中的规格，判断是否有对应的sku信息
		    let checkedProduct = this.getCheckedProductItem(this.getCheckedSpecKey());
		    if (!checkedProduct || checkedProduct.length <= 0) {
		      //找不到对应的product信息，提示没有库存
//		     	Toast({message:'商品无库存',duration: 1500});
		     	this.undercarriage = true,
		        this.undercarriName = '商品无库存';
		    	return false;
		    }
		
		    //验证库存
		    if (checkedProduct.goods_number <this.number) {
		      //找不到对应的product信息，提示没有库存
//			     Toast({message:'商品无库存',duration: 1500});
			     this.undercarriage = true,
		         this.undercarriName = '商品无库存';
			     return false;
		    }
		     this.market_price =  checkedProduct[0].market_price;
		
		  },
		  getCheckedProductItem: function (key) {
		    return this.productList.filter(function (v) {
		      if (v.goods_specification_ids == key) {
		        return true;
		      } else {
		        return false;
		      }
		    });
		  },
	  	addToCart(){
  		    var that = this;
  		    if (this.openAttr == false) {
		        this.openAttr = !this.openAttr,
		        this.collectBackImage =  "../../../static/images/detail_back.png";
		    } else {
		      //提示选择完整规格
		      if (!this.isCheckedAllSpec()) {
	            Toast({message:'请选择完整规格',duration: 1500});
		        return false;
		      }
		
		      //根据选中的规格，判断是否有对应的sku信息
		      let checkedProduct = this.getCheckedProductItem(this.getCheckedSpecKey());
		      if (!checkedProduct || checkedProduct.length <= 0) {
		        //找不到对应的product信息，提示没有库存
		         Toast({message:'商品无库存',duration: 1500});
		        return false;
		      }
		
		      //验证库存
		      if (checkedProduct.goods_number <this.number) {
		        //找不到对应的product信息，提示没有库存
		        Toast({message:'商品无库存',duration: 1500});
		        return false;
		      }
		
		      //添加到购物车
		      that.$http({
		    		method: 'post',
			        url:that.$url+ 'cart/add',
			        params:{ goodsId:that.goods.id, number:that.number, productId: checkedProduct[0].id }
		    	}).then(function (response) {
			          let _res = response;
			          if (_res.data.errno == 0) {
			            Toast({message:'添加成功',duration: 1500});
			            that.setData({
			              openAttr: !that.openAttr,
			              cartGoodsCount: _res.data.cartTotal.goodsCount
			            });
			            if (that.userHasCollect == 1) {
			              that.setData({
			                'collectBackImage': that.hasCollectImage
			              });
			            } else {
			              that.setData({
			                'collectBackImage': that.noCollectImage
			              });
			            }
			          } else {
			          	that.fontSize.goLogin();
			          }
			
			        });
		    }
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
		          		Toast({message: _res.data.errmsg,duration: 1500});
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
    position: fixed;
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
    text-align: left;
}

.attr-pop .p{
    font-size: .33rem;
    color: #333;
    margin-bottom: .10rem;
}

.attr-pop .a{
    font-size: .27rem;
    color: #333;
    height: .40rem;
    line-height: .40rem;
}

.spec-con{
    width: 100%;
    height: auto;
    overflow: hidden;
    text-align: left;
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
    font-size:.29rem;
}

.number-item .cut{
    width: .9rem;
    height: 100%;
    text-align: center;
    line-height: .65rem;
}

.number-item .number{
	width: .9rem;
    flex: 1;
    height: 100%;
    text-align: center;
    line-height: .6875rem;
    border-left: 1px solid #ccc;
    border-right: 1px solid #ccc;
    float: left;
    font-size: .3rem;
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
    margin-bottom: .05rem;
    font-size: .41rem;
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
    margin-top: .23rem;
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
