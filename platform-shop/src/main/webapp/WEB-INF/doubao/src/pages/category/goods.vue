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
		        <!--<div class="brand" v-if="!brandShow"></div> v-else-->
		        <div class="brand" v-if="brand.name">
		          <!--<router-link :to="'../category/brandDetail?id='+brand.id">-->
		            <p>{{brand.name}}</p>
		          <!--</router-link>-->
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
	    		response = {"errno":0,"data":{"specificationList":[],"issue":[{"id":5,"goods_id":null,"question":"如何开具发票？","answer":"1.如需开具普通发票，请在下单时选择“我要开发票”并填写相关信息（APP仅限2.4.0及以"}],"userHasCollect":1,"comment":{"data":{},"count":0},"attribute":[],"minPriceList":[{"id":1125,"goods_id":1183710,"product_id":null,"goods_specification_ids":"","goods_sn":"JD300263","goods_number":100,"market_price":158.00,"retail_price":140.80,"goods_name":null,"list_pic_url":null,"normal_matching":0.0,"activity_matching":0.0}],"brand":{"id":1046275,"name":"天地粮人","list_pic_url":null,"simple_desc":null,"pic_url":null,"sort_order":null,"is_show":null,"floor_price":null,"app_list_pic_url":null,"is_new":null,"new_pic_url":null,"new_sort_order":null},"gallery":[{"id":15381,"goods_id":1183710,"img_url":"http://img13.360buyimg.com/n0/jfs/t3100/181/1901796131/394165/8edfa003/57d64610N0e136d51.jpg","img_desc":null,"sort_order":0},{"id":15380,"goods_id":1183710,"img_url":"http://img13.360buyimg.com/n0/jfs/t2404/206/2223514441/412363/55ecc5f9/56a9871aNd3168331.jpg","img_desc":null,"sort_order":1},{"id":15379,"goods_id":1183710,"img_url":"http://img13.360buyimg.com/n0/jfs/t2104/346/1448093428/357503/57d99f18/56a98725N6e2a72ce.jpg","img_desc":null,"sort_order":2},{"id":15378,"goods_id":1183710,"img_url":"http://img13.360buyimg.com/n0/jfs/t1897/205/2144045910/303099/6f69c870/56a9872aN9588500b.jpg","img_desc":null,"sort_order":3},{"id":15377,"goods_id":1183710,"img_url":"http://img13.360buyimg.com/n0/jfs/t2344/55/1441246672/310008/c4edfe8b/56a9a01fN0369a03f.jpg","img_desc":null,"sort_order":4}],"productList":[{"id":1125,"goods_id":1183710,"product_id":null,"goods_specification_ids":"","goods_sn":"JD300263","goods_number":100,"market_price":158.00,"retail_price":140.80,"goods_name":null,"list_pic_url":null,"normal_matching":0.0,"activity_matching":0.0}],"info":{"id":1183710,"category_id":1,"goods_sn":"JD300263","name":"天地粮人之喜从天降 有机 十二种 杂粮礼盒 3.96kg（礼品 节日礼物 礼包 年货 公司福利 团购）","brand_id":1046275,"goods_number":100,"keywords":null,"goods_brief":"<table cellpadding=\"0\" cellspacing=\"1\" width=\"100%\" border=\"0\" class=\"Ptable\"><tr><th class=\"tdTitle\" colspan=\"2\">主体</th><tr><tr><td class=\"tdTitle\">品牌</td><td>天地粮人</td></tr><tr><td class=\"tdTitle\">配料</td><td>有机（黑豆 绿豆  黑米 黄豆 荞麦米 玉米碴 糙米 大麦米 燕麦米 糯米 高粱米 大黄米）</td></tr><tr><td class=\"tdTitle\">生产许可证</td><td>详见礼盒内商品包装</td></tr><tr><td class=\"tdTitle\">产品标准号</td><td>详见礼盒内商品包装</td></tr><tr><td class=\"tdTitle\">保质期</td><td>540天</td></tr><tr><td class=\"tdTitle\">类型</td><td>杂粮</td></tr><tr><td class=\"tdTitle\">储存方法</td><td>置阴凉干燥处</td></tr><tr><td class=\"tdTitle\">是否转基因</td><td>非转基因食品</td></tr></table>","goods_desc":"<div cssurl='//sku-market-gw.jd.com/css/pc/1112775.css?t=1508226196627'></div><div id='zbViewModulesH'  value='12878'></div><input id='zbViewModulesHeight' type='hidden' value='12878'/><div skudesign=\"100010\"></div><div class=\"ssd-module-wrap\" >\n    <div class=\"ssd-module M15082093579031 animate-M15082093579031\">\n        <div class=\"ssd-widget-pic W15082093579031I0\" >\n    <img    src=\"https://img30.360buyimg.com/sku/jfs/t5866/33/9843376207/24138/ae36947b/598ac838Nb38c9642.jpg\"    alt=\"【京东超市】天地粮人之喜从天降 有机 十二种 杂粮礼盒 3.96kg（礼品 节日礼物 礼包 年货 公司福利 团购）-京东\"  />\n        </div>\n<div class=\"ssd-widget-text zb-W15022675308574-1502267894000 W15082093579031I1\">\n    Souvenir&nbsp;Conference&nbsp;gift\n    </div>\n<div class=\"ssd-widget-text zb-W15023512324703-1502351296000 W15082093579031I2\">\n    Organic&nbsp;gift\n    </div>\n<div class=\"ssd-widget-text zb-W150235310449912-1502353124000 W15082093579031I3\">\n    Boutique&nbsp;gift\n    </div>\n<div class=\"ssd-widget-text zb-W15027017212077I4-1502854641000 W15082093579031I4\">\n    &#20276;&#25163;&#31036;&nbsp;&#20250;&#35758;&#31036;&#21697;\n    </div>\n<div class=\"ssd-widget-text zb-W15023513059734-1502351329000 W15082093579031I5\">\n    &#26377;&nbsp;&#26426;&nbsp;&#31036;&nbsp;&#30418;\n    </div>\n<div class=\"ssd-widget-text zb-W150235313264913-1502353147000 W15082093579031I6\">\n    &#31934;&nbsp;&#21697;&nbsp;&#31036;&nbsp;&#30418;\n    </div>\n<div class=\"ssd-widget-line W15082093579031I7\">\n        </div>\n<div class=\"ssd-widget-line W15082093579031I8\">\n        </div>\n<div class=\"ssd-widget-line W15082093579031I9\">\n        </div>\n<div class=\"ssd-widget-line W15082093579031I10\">\n        </div>\n<div class=\"ssd-widget-line W15082093579031I11\">\n        </div>\n<div class=\"ssd-widget-line W15082093579031I12\">\n        </div>\n<div class=\"ssd-widget-pic W15082093579031I13\" >\n    <img    src=\"https://img30.360buyimg.com/sku/jfs/t6943/124/2272811016/61945/87efb65e/598ad23fN5caafa53.jpg\"    alt=\"【京东超市】天地粮人之喜从天降 有机 十二种 杂粮礼盒 3.96kg（礼品 节日礼物 礼包 年货 公司福利 团购）-京东\"  />\n     <a class=\"ssd-widget-link\" title=\"{【京东超市】天地粮人之喜从天降 有机 十二种 杂粮礼盒 3.96kg（礼品 节日礼物 礼包 年货 公司福利 团购）} -京东\"   ></a>     </div>\n<div class=\"ssd-widget-pic W15082093579031I14\" >\n    <img    src=\"https://img30.360buyimg.com/sku/jfs/t7153/225/1527803137/64010/3fa31e8e/598c0e69N11fcb026.jpg\"    alt=\"【京东超市】天地粮人之喜从天降 有机 十二种 杂粮礼盒 3.96kg（礼品 节日礼物 礼包 年货 公司福利 团购）-京东\"  />\n     <a class=\"ssd-widget-link\" title=\"{【京东超市】天地粮人之喜从天降 有机 十二种 杂粮礼盒 3.96kg（礼品 节日礼物 礼包 年货 公司福利 团购）} -京东\"   ></a>     </div>\n<div class=\"ssd-widget-pic W15082093579031I15\" >\n    <img    src=\"https://img30.360buyimg.com/sku/jfs/t7117/32/1546248541/64383/dfbcc437/598c0fa4Ndbecb006.jpg\"    alt=\"【京东超市】天地粮人之喜从天降 有机 十二种 杂粮礼盒 3.96kg（礼品 节日礼物 礼包 年货 公司福利 团购）-京东\"  />\n     <a class=\"ssd-widget-link\" title=\"{【京东超市】天地粮人之喜从天降 有机 十二种 杂粮礼盒 3.96kg（礼品 节日礼物 礼包 年货 公司福利 团购）} -京东\"   ></a>     </div>\n<div class=\"ssd-widget-pic W15082093579031I16\" >\n    <img    src=\"https://img30.360buyimg.com/sku/jfs/t6055/206/8642431287/78948/271a574a/598c1591N288514ca.jpg\"    alt=\"【京东超市】天地粮人之喜从天降 有机 十二种 杂粮礼盒 3.96kg（礼品 节日礼物 礼包 年货 公司福利 团购）-京东\"  />\n     <a class=\"ssd-widget-link\" title=\"{【京东超市】天地粮人之喜从天降 有机 十二种 杂粮礼盒 3.96kg（礼品 节日礼物 礼包 年货 公司福利 团购）} -京东\"   ></a>     </div>\n<div class=\"ssd-widget-pic W15082093579031I17\" >\n    <img    src=\"https://img30.360buyimg.com/sku/jfs/t6949/335/2428793252/73765/cfd8e3a1/598c15d1Nd9025eca.jpg\"    alt=\"【京东超市】天地粮人之喜从天降 有机 十二种 杂粮礼盒 3.96kg（礼品 节日礼物 礼包 年货 公司福利 团购）-京东\"  />\n     <a class=\"ssd-widget-link\" title=\"{【京东超市】天地粮人之喜从天降 有机 十二种 杂粮礼盒 3.96kg（礼品 节日礼物 礼包 年货 公司福利 团购）} -京东\"   ></a>     </div>\n<div class=\"ssd-widget-pic W15082093579031I18\" >\n    <img    src=\"https://img30.360buyimg.com/sku/jfs/t7114/124/1524642521/78234/7b96fec2/598c1619N58b02332.jpg\"    alt=\"【京东超市】天地粮人之喜从天降 有机 十二种 杂粮礼盒 3.96kg（礼品 节日礼物 礼包 年货 公司福利 团购）-京东\"  />\n     <a class=\"ssd-widget-link\" title=\"{【京东超市】天地粮人之喜从天降 有机 十二种 杂粮礼盒 3.96kg（礼品 节日礼物 礼包 年货 公司福利 团购）} -京东\"   ></a>     </div>\n<div class=\"ssd-widget-pic W15082093579031I19\" >\n    <img    src=\"https://img30.360buyimg.com/sku/jfs/t7129/31/1538518777/63855/172fb8e7/598c1653Ndc8a1f7a.jpg\"    alt=\"【京东超市】天地粮人之喜从天降 有机 十二种 杂粮礼盒 3.96kg（礼品 节日礼物 礼包 年货 公司福利 团购）-京东\"  />\n     <a class=\"ssd-widget-link\" title=\"{【京东超市】天地粮人之喜从天降 有机 十二种 杂粮礼盒 3.96kg（礼品 节日礼物 礼包 年货 公司福利 团购）} -京东\"   ></a>     </div>\n<div class=\"ssd-widget-pic W15082093579031I20\" >\n    <img    src=\"https://img30.360buyimg.com/sku/jfs/t6775/36/2451721876/79535/a53e8911/598c16aaNb18dc303.jpg\"    alt=\"【京东超市】天地粮人之喜从天降 有机 十二种 杂粮礼盒 3.96kg（礼品 节日礼物 礼包 年货 公司福利 团购）-京东\"  />\n     <a class=\"ssd-widget-link\" title=\"{【京东超市】天地粮人之喜从天降 有机 十二种 杂粮礼盒 3.96kg（礼品 节日礼物 礼包 年货 公司福利 团购）} -京东\"   ></a>     </div>\n<div class=\"ssd-widget-pic W15082093579031I21\" >\n    <img    src=\"https://img30.360buyimg.com/sku/jfs/t7042/44/2416316762/85816/22532e22/598c1820Nf66f49f7.jpg\"    alt=\"【京东超市】天地粮人之喜从天降 有机 十二种 杂粮礼盒 3.96kg（礼品 节日礼物 礼包 年货 公司福利 团购）-京东\"  />\n     <a class=\"ssd-widget-link\" title=\"{【京东超市】天地粮人之喜从天降 有机 十二种 杂粮礼盒 3.96kg（礼品 节日礼物 礼包 年货 公司福利 团购）} -京东\"   ></a>     </div>\n<div class=\"ssd-widget-pic W15082093579031I22\" >\n    <img    src=\"https://img30.360buyimg.com/sku/jfs/t6088/144/8671948698/72400/5652f65f/598c194aNaa21e0d0.jpg\"    alt=\"【京东超市】天地粮人之喜从天降 有机 十二种 杂粮礼盒 3.96kg（礼品 节日礼物 礼包 年货 公司福利 团购）-京东\"  />\n     <a class=\"ssd-widget-link\" title=\"{【京东超市】天地粮人之喜从天降 有机 十二种 杂粮礼盒 3.96kg（礼品 节日礼物 礼包 年货 公司福利 团购）} -京东\"   ></a>     </div>\n<div class=\"ssd-widget-pic W15082093579031I23\" >\n    <img    src=\"https://img30.360buyimg.com/sku/jfs/t7180/85/1542569699/94340/567e089/598c1a80N82e4f215.jpg\"    alt=\"【京东超市】天地粮人之喜从天降 有机 十二种 杂粮礼盒 3.96kg（礼品 节日礼物 礼包 年货 公司福利 团购）-京东\"  />\n     <a class=\"ssd-widget-link\" title=\"{【京东超市】天地粮人之喜从天降 有机 十二种 杂粮礼盒 3.96kg（礼品 节日礼物 礼包 年货 公司福利 团购）} -京东\"   ></a>     </div>\n<div class=\"ssd-widget-pic W15082093579031I24\" >\n    <img    src=\"https://img30.360buyimg.com/sku/jfs/t6097/11/8762005289/85302/bda0cde7/598c1b6dNe70264fe.jpg\"    alt=\"【京东超市】天地粮人之喜从天降 有机 十二种 杂粮礼盒 3.96kg（礼品 节日礼物 礼包 年货 公司福利 团购）-京东\"  />\n     <a class=\"ssd-widget-link\" title=\"{【京东超市】天地粮人之喜从天降 有机 十二种 杂粮礼盒 3.96kg（礼品 节日礼物 礼包 年货 公司福利 团购）} -京东\"   ></a>     </div>\n<div class=\"ssd-widget-pic W15082093579031I25\" >\n    <img    src=\"https://img30.360buyimg.com/sku/jfs/t6973/106/2481984882/94584/b138636b/598c1c8bNa2b85a03.jpg\"    alt=\"【京东超市】天地粮人之喜从天降 有机 十二种 杂粮礼盒 3.96kg（礼品 节日礼物 礼包 年货 公司福利 团购）-京东\"  />\n     <a class=\"ssd-widget-link\" title=\"{【京东超市】天地粮人之喜从天降 有机 十二种 杂粮礼盒 3.96kg（礼品 节日礼物 礼包 年货 公司福利 团购）} -京东\"   ></a>     </div>\n<div class=\"ssd-widget-pic W15082093579031I26\" >\n    <img    src=\"https://img30.360buyimg.com/sku/jfs/t6937/5/2436906689/79551/1e7d7f95/598c1d3cN0b8fc404.jpg\"    alt=\"【京东超市】天地粮人之喜从天降 有机 十二种 杂粮礼盒 3.96kg（礼品 节日礼物 礼包 年货 公司福利 团购）-京东\"  />\n     <a class=\"ssd-widget-link\" title=\"{【京东超市】天地粮人之喜从天降 有机 十二种 杂粮礼盒 3.96kg（礼品 节日礼物 礼包 年货 公司福利 团购）} -京东\"   ></a>     </div>\n<div class=\"ssd-widget-pic W15082093579031I27\" >\n    <img    src=\"https://img30.360buyimg.com/sku/jfs/t9016/249/1276677677/49957/a9922899/59b781ceNe9cafe94.jpg\"    alt=\"【京东超市】天地粮人之喜从天降 有机 十二种 杂粮礼盒 3.96kg（礼品 节日礼物 礼包 年货 公司福利 团购）-京东\"  />\n     <a class=\"ssd-widget-link\" title=\"{【京东超市】天地粮人之喜从天降 有机 十二种 杂粮礼盒 3.96kg（礼品 节日礼物 礼包 年货 公司福利 团购）} -京东\"   ></a>     </div>\n<div class=\"ssd-widget-pic W15082093579031I28\" >\n    <img    src=\"https://img30.360buyimg.com/sku/jfs/t7417/325/3008225937/56297/a444c37f/59b781ceNc65750f9.jpg\"    alt=\"【京东超市】天地粮人之喜从天降 有机 十二种 杂粮礼盒 3.96kg（礼品 节日礼物 礼包 年货 公司福利 团购）-京东\"  />\n     <a class=\"ssd-widget-link\" title=\"{【京东超市】天地粮人之喜从天降 有机 十二种 杂粮礼盒 3.96kg（礼品 节日礼物 礼包 年货 公司福利 团购）} -京东\"   ></a>     </div>\n</div>\n<div class=\"ssd-module M14808581731392 animate-M14808581731392\">\n        <div class=\"ssd-widget-pic W14808581731392I0\" >\n    <img    src=\"https://img30.360buyimg.com/sku/jfs/t6808/2/2173788868/36743/8c183cb8/59897f72N23c5d426.jpg\"    alt=\"【京东超市】天地粮人之喜从天降 有机 十二种 杂粮礼盒 3.96kg（礼品 节日礼物 礼包 年货 公司福利 团购）-京东\"  />\n        </div>\n<div class=\"ssd-widget-pic W14808581731392I1\" >\n    <img    src=\"https://img30.360buyimg.com/sku/jfs/t6778/67/2122151590/70302/b751d75f/598980fcN712cf6ea.jpg\"    alt=\"【京东超市】天地粮人之喜从天降 有机 十二种 杂粮礼盒 3.96kg（礼品 节日礼物 礼包 年货 公司福利 团购）-京东\"  />\n        </div>\n</div>\n<div class=\"ssd-module ssd-module-heading M14808581776303\" id=\"detail-tag-id-879\"  name=\"detail-tag-id-879\" text=\"产品信息\">\r\n<div class=\"ssd-module-heading-layout\"><span class=\"ssd-widget-heading-ch W14808581776304\" >产品信息</span><span class=\"ssd-widget-heading-en W14808581776305\" >Product Description</span></div>\r\n</div><div class=\"ssd-module M14808581942896 animate-M14808581942896\">\n        <div class=\"ssd-widget-pic W14808581942896I0\" >\n    <img    src=\"//img30.360buyimg.com/sku/jfs/t3427/61/2173386183/400647/f95003ba/58428e52N82eb8875.jpg\"    alt=\"【京东超市】天地粮人之喜从天降 有机 十二种 杂粮礼盒 3.96kg（礼品 节日礼物 礼包 年货 公司福利 团购）-京东\"  />\n        </div>\n<div class=\"ssd-widget-text zb-W14808581942896I1-1505288963000 W14808581942896I1\">\n    &#12304;&#20135;&#21697;&#21697;&#29260;&#12305;&#22825;&#22320;&#31918;&#20154;\n    </div>\n<div class=\"ssd-widget-text zb-W14808581942896I2-1505288970000 W14808581942896I2\">\n    &#12304;&#20135;&#21697;&#21697;&#21517;&#12305;&#22825;&#22320;&#31918;&#20154;&#20043;&#21916;&#20174;&#22825;&#38477;&nbsp;&nbsp;&#21313;&#20108;&#31181;&nbsp;&#26377;&#26426;&nbsp;&#26434;&#31918;&#31036;&#30418;\n    </div>\n<div class=\"ssd-widget-text zb-W14808581942896I3-1505288976000 W14808581942896I3\">\n    &#12304;&#20869;&#35013;&#20135;&#21697;&#12305;&#26377;&#26426;&#40644;&#35910;&#12289;&#26377;&#26426;&#40657;&#35910;&#12289;&#26377;&#26426;&#32511;&#35910;&#12289;&#26377;&#26426;&#33630;&#40614;&#31859;&#12289;&#26377;&#26426;&#29577;&#31859;&#30900;&#12289;&#26377;&#26426;&#31961;&#31859;\n    </div>\n<div class=\"ssd-widget-text zb-W14808581942896I4-1505288984000 W14808581942896I4\">\n    &#26377;&#26426;&#22823;&#40614;&#31859;&#12289;&#26377;&#26426;&#29141;&#40614;&#31859;&#12289;&#26377;&#26426;&#31983;&#31859;&#12289;&#26377;&#26426;&#39640;&#31921;&#31859;&#12289;&#26377;&#26426;&#22823;&#40644;&#31859;&#12289;&#26377;&#26426;&#40657;&#31859;\n    </div>\n<div class=\"ssd-widget-text zb-W14808581942896I5-1505288999000 W14808581942896I5\">\n    &#12304;&#20928;&nbsp;&#21547;&nbsp;&#37327;&#12305;3.96kg&nbsp;\n    </div>\n<div class=\"ssd-widget-text zb-W14808581942896I6-1505289006000 W14808581942896I6\">\n    &#12304;&#20445;&nbsp;&#36136;&nbsp;&#26399;&#12305;18&#20010;&#26376;\n    </div>\n<div class=\"ssd-widget-text zb-W14808581942896I7-1505288992000 W14808581942896I7\">\n    &#12304;&#20135;&#21697;&#20135;&#22320;&#12305;&#36797;&#23425;&#26397;&#38451;\n    </div>\n<div class=\"ssd-widget-text zb-W14808581942896I8-1505289012000 W14808581942896I8\">\n    &#12304;&#36136;&#37327;&#31561;&#32423;&#12305;&#19968;&#32423;\n    </div>\n<div class=\"ssd-widget-text zb-W14808581942896I9-1505289055000 W14808581942896I9\">\n    &#12304;&#23458;&#26381;&#28909;&#32447;&#12305;\n    </div>\n<div class=\"ssd-widget-text zb-W15052890177632-1505289098000 W15052890177632\">\n    &#22825;&#22320;&#31918;&#20154;&#20840;&#22269;&#23458;&#26381;&#28909;&#32447;&nbsp;\n    </div>\n<div class=\"ssd-widget-text zb-W15052890625313-1505289126000 W15052890625313\">\n    &#65288;&#20225;&#19994;&#22242;&#36141;&#19982;&#20135;&#21697;&#21672;&#35810;&#65289;\n    </div>\n<div class=\"ssd-widget-text zb-W14834364213431-1505289605000 W14834364213431\">\n    &#65288;&#37197;&#36865;&#21672;&#35810;&#19982;&#35746;&#21333;&#21040;&#36135;&#26597;&#35810;&#65289;\n    </div>\n<div class=\"ssd-widget-text zb-W15052895552174-1505289571000 W15052895552174\">\n    &#20140;&#19996;&#20840;&#22269;&#23458;&#26381;&#28909;&#32447;\n    </div>\n<div class=\"ssd-widget-text zb-W14834364534622-1505289114000 W14834364534622\">\n    400-0670-550\n    </div>\n<div class=\"ssd-widget-text zb-W14834364996003-1505289584000 W14834364996003\">\n    400-606-5500\n    </div>\n</div>\n<div class=\"ssd-module ssd-module-heading M14808586058837\" id=\"detail-tag-id-485\"  name=\"detail-tag-id-485\" text=\"尺码信息\">\r\n<div class=\"ssd-module-heading-layout\"><span class=\"ssd-widget-heading-ch W14808586058848\" >尺码信息</span><span class=\"ssd-widget-heading-en W14808586058849\" >Size Description</span></div>\r\n</div><div class=\"ssd-module M148085861755110 animate-M148085861755110\">\n        <div class=\"ssd-widget-text W148085861755110I0\">\n    &#31036;&#30418;&#23610;&#23544;\n    </div>\n<div class=\"ssd-widget-text W148085861755110I1\">\n    218mm\n    </div>\n<div class=\"ssd-widget-text W148085861755110I2\">\n    395mm\n    </div>\n<div class=\"ssd-widget-text W148085861755110I3\">\n    105mm\n    </div>\n</div>\n<div class=\"ssd-module ssd-module-heading M148085864497011\" id=\"detail-tag-id-477\"  name=\"detail-tag-id-477\" text=\"产品特色\">\r\n<div class=\"ssd-module-heading-layout\"><span class=\"ssd-widget-heading-ch W148085864497012\" >产品特色</span><span class=\"ssd-widget-heading-en W148085864497013\" >Product Feature</span></div>\r\n</div><div class=\"ssd-module M148085866739114 animate-M148085866739114\">\n        <div class=\"ssd-widget-text W148085866739114I0\">\n    01\n    </div>\n<div class=\"ssd-widget-text W148085866739114I1\">\n    02\n    </div>\n<div class=\"ssd-widget-text W148085866739114I2\">\n    03\n    </div>\n<div class=\"ssd-widget-text W148085866739114I3\">\n    04\n    </div>\n<div class=\"ssd-widget-text W148085866739114I4\">\n    &#36865;&#31036;&#36865;&#21697;&#36136;&#21487;&#38752;&#30340;&#26434;&#31918;&#20135;&#21697;&#65292;&#26426;&#36873;&#21152;&#20154;&#24037;&#25163;&#36873;&#31561;&#22810;&#36947;&#24037;&#24207;&#23436;&#25104;&#65292;&#36865;&#21435;&#30340;&#26356;&#26159;&#19968;&#20221;&#20581;&#24247;&#30340;&#31069;&#31119;&#12290;\n    </div>\n<div class=\"ssd-widget-text W148085866739114I5\">\n    Reliable&nbsp;product&nbsp;quality\n    </div>\n<div class=\"ssd-widget-text W148085866739114I6\">\n    &#31036;&#30418;&#20869;&#32622;&#21313;&#20108;&#31181;&#36739;&#21463;&#27426;&#36814;&#30340;&#19981;&#21516;&#31181;&#31867;&#26377;&#26426;&#26434;&#31918;&#20135;&#21697;&#65292;&#25645;&#37197;&#21512;&#29702;&#12289;&#31185;&#23398;&#37197;&#27604;&#12290;<br/>\n    </div>\n<div class=\"ssd-widget-text W148085866739114I7\">\n    Rich&nbsp;product\n    </div>\n<div class=\"ssd-widget-text W148085866739114I8\">\n    &#20013;&#22269;&#33410;&#26085;&#32418;&#30340;&#20027;&#33394;&#35843;&#65292;&#20984;&#26174;&#33410;&#26085;&#27668;&#27675;&#65307;&#31036;&#30418;&#23610;&#23544;&#22823;&#26041;&#24471;&#20307;&#65292;&#19981;&#22840;&#24352;&#19988;&#26377;&#20869;&#28085;&#65307;&#20250;&#35753;&#24744;&#35273;&#24471;&#36865;&#31036;&#21363;&#26377;&#38754;&#23376;&#21448;&#26377;&#37324;&#23376;&#65281;\n    </div>\n<div class=\"ssd-widget-text W148085866739114I9\">\n    exquisite&nbsp;packaging\n    </div>\n<div class=\"ssd-widget-text W148085866739114I10\">\n    &ldquo;&#21916;&#20174;&#22825;&#38477;&rdquo;&#26159;&#20195;&#34920;&#24744;&#30340;&#28145;&#28145;&#31069;&#31119;&#65292;&#36890;&#36807;&#20135;&#21697;&#30340;&#25991;&#23383;&#24456;&#22909;&#30340;&#19988;&#26377;&#25928;&#30340;&#25226;&#24744;&#24819;&#34920;&#36798;&#30340;&#24515;&#24773;&#21644;&#31069;&#31119;&#20256;&#36798;&#32473;&#20102;&#23545;&#26041;&#65292;&#20250;&#20351;&#23545;&#26041;&#24863;&#21040;&#30495;&#30340;&#24456;&#36148;&#24515;&#65281;\n    </div>\n<div class=\"ssd-widget-text W148085866739114I11\">\n    Meaning&nbsp;auspicious\n    </div>\n</div>\n<div class=\"ssd-module ssd-module-heading M148085869634515\" id=\"detail-tag-id-481\"  name=\"detail-tag-id-481\" text=\"产品优势\">\r\n<div class=\"ssd-module-heading-layout\"><span class=\"ssd-widget-heading-ch W148085869634516\" >产品优势</span><span class=\"ssd-widget-heading-en W148085869634517\" >Product Advantage</span></div>\r\n</div><div class=\"ssd-module M148085870768218 animate-M148085870768218\">\n        </div>\n<div class=\"ssd-module M15012253574621 animate-M15012253574621\">\n        </div>\n<div class=\"ssd-module ssd-module-heading M148085872591019\" id=\"detail-tag-id-479\"  name=\"detail-tag-id-479\" text=\"产品细节\">\r\n<div class=\"ssd-module-heading-layout\"><span class=\"ssd-widget-heading-ch W148085872591020\" >产品细节</span><span class=\"ssd-widget-heading-en W148085872591021\" >Product Details</span></div>\r\n</div><div class=\"ssd-module M148085875594822 animate-M148085875594822\">\n        <div class=\"ssd-widget-pic W148085875594822I0\" >\n    <img    src=\"//img30.360buyimg.com/sku/jfs/t3799/303/2146593649/210476/469a4391/58428fe5Nfd7d1524.png\"    alt=\"天地粮人 有机 荞麦米330g（无添加 真空装 杂粮 大米伴侣）-京东\"  />\n     <a class=\"ssd-widget-link\" title=\"{【京东超市】天地粮人之喜从天降 有机 十二种 杂粮礼盒 3.96kg（礼品 节日礼物 礼包 年货 公司福利 团购）} -京东\"   ></a>     </div>\n<div class=\"ssd-widget-pic W148085875594822I1\" >\n    <img    src=\"//img30.360buyimg.com/sku/jfs/t3430/186/1008211406/188312/d5006dc3/581aa6ecN4fbbeb3b.png\"    alt=\"天地粮人 有机 黄豆330g（无添加 真空装 杂粮 可发芽打豆浆）-京东\"  />\n     <a class=\"ssd-widget-link\" title=\"{【京东超市】天地粮人之喜从天降 有机 十二种 杂粮礼盒 3.96kg（礼品 节日礼物 礼包 年货 公司福利 团购）} -京东\"   ></a>     </div>\n<div class=\"ssd-widget-pic W148085875594822I2\" >\n    <img    src=\"//img30.360buyimg.com/sku/jfs/t3442/150/1043168495/198354/1113d5e5/581aa6eaNa11585be.png\"    alt=\"天地粮人 有机 糙米330g（无添加 胚芽大米 真空装 杂粮）-京东\"  />\n     <a class=\"ssd-widget-link\" title=\"{【京东超市】天地粮人之喜从天降 有机 十二种 杂粮礼盒 3.96kg（礼品 节日礼物 礼包 年货 公司福利 团购）} -京东\"   ></a>     </div>\n<div class=\"ssd-widget-pic W148085875594822I3\" >\n    <img    src=\"//img30.360buyimg.com/sku/jfs/t4000/125/202439211/193198/7bc594aa/58428fe5Nfc0822ac.png\"    alt=\"天地粮人 有机 玉米碴330g（无添加 真空装 杂粮 大米伴侣）-京东\"  />\n     <a class=\"ssd-widget-link\" title=\"{【京东超市】天地粮人之喜从天降 有机 十二种 杂粮礼盒 3.96kg（礼品 节日礼物 礼包 年货 公司福利 团购）} -京东\"   ></a>     </div>\n<div class=\"ssd-widget-pic W148085875594822I4\" >\n    <img    src=\"//img30.360buyimg.com/sku/jfs/t3139/298/4377676603/203453/7365799/58428fe5Nf67a64ce.png\"    alt=\"天地粮人 有机 大黄米330g（无添加 黄黏米 粽子米 大米伴侣 真空装 杂粮）-京东\"  />\n     <a class=\"ssd-widget-link\" title=\"{【京东超市】天地粮人之喜从天降 有机 十二种 杂粮礼盒 3.96kg（礼品 节日礼物 礼包 年货 公司福利 团购）} -京东\"   ></a>     </div>\n<div class=\"ssd-widget-pic W148085875594822I5\" >\n    <img    src=\"//img30.360buyimg.com/sku/jfs/t3766/128/1029988563/191645/bfe5cd38/581aa6efNf689b2b2.png\"    alt=\"天地粮人 有机 黑米330g（无添加 黑大米 真空装 杂粮）-京东\"  />\n     <a class=\"ssd-widget-link\" title=\"{【京东超市】天地粮人之喜从天降 有机 十二种 杂粮礼盒 3.96kg（礼品 节日礼物 礼包 年货 公司福利 团购）} -京东\"   ></a>     </div>\n<div class=\"ssd-widget-pic W148085875594822I6\" >\n    <img    src=\"//img30.360buyimg.com/sku/jfs/t3763/41/2165362587/181617/170eaee/58429098Ncb99728f.png\"    alt=\"天地粮人 有机 高粱米330g（无添加 真空装 杂粮 大米伴侣）-京东\"  />\n     <a class=\"ssd-widget-link\" title=\"{【京东超市】天地粮人之喜从天降 有机 十二种 杂粮礼盒 3.96kg（礼品 节日礼物 礼包 年货 公司福利 团购）} -京东\"   ></a>     </div>\n<div class=\"ssd-widget-pic W148085875594822I7\" >\n    <img    src=\"//img30.360buyimg.com/sku/jfs/t3715/222/1005478946/175007/1ac781a4/581aa6f0N83b95b49.png\"    alt=\"天地粮人 有机 糯米330g（无添加 江米 黏米 粽子米 糯大米 真空装 杂粮）-京东\"  />\n     <a class=\"ssd-widget-link\" title=\"{【京东超市】天地粮人之喜从天降 有机 十二种 杂粮礼盒 3.96kg（礼品 节日礼物 礼包 年货 公司福利 团购）} -京东\"   ></a>     </div>\n<div class=\"ssd-widget-pic W148085875594822I8\" >\n    <img    src=\"//img30.360buyimg.com/sku/jfs/t3316/10/998311323/200185/826e16f9/581abd65Na6774d52.png\"    alt=\"天地粮人 有机 燕麦米330g（无添加 真空装 杂粮 大米伴侣）-京东\"  />\n     <a class=\"ssd-widget-link\" title=\"{【京东超市】天地粮人之喜从天降 有机 十二种 杂粮礼盒 3.96kg（礼品 节日礼物 礼包 年货 公司福利 团购）} -京东\"   ></a>     </div>\n<div class=\"ssd-widget-pic W148085875594822I9\" >\n    <img    src=\"//img30.360buyimg.com/sku/jfs/t3235/283/4421769671/217163/e44b12b0/58428fe5Nb93dbb06.png\"    alt=\"天地粮人 有机 大麦米330g（无添加 大麦仁 真空装 杂粮 大米伴侣）-京东\"  />\n     <a class=\"ssd-widget-link\" title=\"{【京东超市】天地粮人之喜从天降 有机 十二种 杂粮礼盒 3.96kg（礼品 节日礼物 礼包 年货 公司福利 团购）} -京东\"   ></a>     </div>\n<div class=\"ssd-widget-text zb-W148085875594822I10-1505375265000 W148085875594822I10\">\n    &#40644;&#35910;<br/>huang&nbsp;dou\n    </div>\n<div class=\"ssd-widget-text zb-W148085875594822I11-1505375302000 W148085875594822I11\">\n    &#33630;&#40614;&#31859;<br/>qiao&nbsp;mai&nbsp;mi\n    </div>\n<div class=\"ssd-widget-text zb-W148085875594822I12-1505375313000 W148085875594822I12\">\n    &#29577;&#31859;&#30900;<br/>Yu&nbsp;mi&nbsp;cha\n    </div>\n<div class=\"ssd-widget-text zb-W148085875594822I13-1505375327000 W148085875594822I13\">\n    &#31961;&#31859;<br/>cao&nbsp;mi\n    </div>\n<div class=\"ssd-widget-text zb-W148085875594822I14-1505375341000 W148085875594822I14\">\n    &#22823;&#40614;&#31859;<br/>Da&nbsp;mai&nbsp;mi\n    </div>\n<div class=\"ssd-widget-text zb-W148085875594822I15-1505375351000 W148085875594822I15\">\n    &#29141;&#40614;&#31859;<br/>yan&nbsp;mai&nbsp;mi\n    </div>\n<div class=\"ssd-widget-text zb-W148085875594822I16-1505375359000 W148085875594822I16\">\n    &#31983;&#31859;<br/>nuo&nbsp;mi\n    </div>\n<div class=\"ssd-widget-text zb-W148085875594822I17-1505375370000 W148085875594822I17\">\n    &#39640;&#31921;&#31859;<br/>Gao&nbsp;liang&nbsp;mi\n    </div>\n<div class=\"ssd-widget-text zb-W148085875594822I18-1505375382000 W148085875594822I18\">\n    &#22823;&#40644;&#31859;<br/>Da&nbsp;huang&nbsp;mi\n    </div>\n<div class=\"ssd-widget-text zb-W148085875594822I19-1505375391000 W148085875594822I19\">\n    &#40657;&#31859;<br/>hei&nbsp;mi\n    </div>\n<div class=\"ssd-widget-pic W148085895050223\" >\n    <img    src=\"//img30.360buyimg.com/sku/jfs/t3610/162/995134876/172502/be36253/581aa6eaNc987057c.png\"    alt=\"天地粮人 有机 黑豆330g（无添加 真空装 杂粮 大米伴侣）-京东\"  />\n     <a class=\"ssd-widget-link\" title=\"{【京东超市】天地粮人之喜从天降 有机 十二种 杂粮礼盒 3.96kg（礼品 节日礼物 礼包 年货 公司福利 团购）} -京东\"   ></a>     </div>\n<div class=\"ssd-widget-pic W148085895585424\" >\n    <img    src=\"//img30.360buyimg.com/sku/jfs/t3370/260/1649969902/189416/e1c0bfa8/582d7adfNb4610ada.png\"    alt=\"天地粮人 有机 绿豆330g（无添加 真空装 杂粮 大米伴侣）-京东\"  />\n     <a class=\"ssd-widget-link\" title=\"{【京东超市】天地粮人之喜从天降 有机 十二种 杂粮礼盒 3.96kg（礼品 节日礼物 礼包 年货 公司福利 团购）} -京东\"   ></a>     </div>\n<div class=\"ssd-widget-text zb-W148085903966725-1505375279000 W148085903966725\">\n    &#40657;&#35910;<br/>hei&nbsp;dou\n    </div>\n<div class=\"ssd-widget-text zb-W148085908773026-1505375289000 W148085908773026\">\n    &#32511;&#35910;<br/>l&uuml;&nbsp;dou\n    </div>\n</div>\n<div class=\"ssd-module ssd-module-heading M148085917580027\" id=\"detail-tag-id-467\"  name=\"detail-tag-id-467\" text=\"产地介绍\">\r\n<div class=\"ssd-module-heading-layout\"><span class=\"ssd-widget-heading-ch W148085917580128\" >产地介绍</span><span class=\"ssd-widget-heading-en W148085917580129\" >Origin Description</span></div>\r\n</div><div class=\"ssd-module M148085918898630 animate-M148085918898630\">\n        <div class=\"ssd-widget-pic W15052910135945\" >\n    <img    src=\"https://img30.360buyimg.com/sku/jfs/t7567/342/305484085/69321/3c2eddd9/59916405Nf7ec01d0.jpg\"    alt=\"【京东超市】天地粮人之喜从天降 有机 十二种 杂粮礼盒 3.96kg（礼品 节日礼物 礼包 年货 公司福利 团购）-京东\"  />\n        </div>\n</div>\n<div class=\"ssd-module ssd-module-heading M148085919276231\" id=\"detail-tag-id-880\"  name=\"detail-tag-id-880\" text=\"特色食谱\">\r\n<div class=\"ssd-module-heading-layout\"><span class=\"ssd-widget-heading-ch W148085919276232\" >特色食谱</span><span class=\"ssd-widget-heading-en W148085919276233\" >Special Recipe</span></div>\r\n</div><div class=\"ssd-module M148085921099734 animate-M148085921099734\">\n        <div class=\"ssd-widget-text zb-W148085921099734I0-1505292203000 W148085921099734I0\">\n    &#40657;&#31859;&#12289;&#29980;&#37202;&#26354;\n    </div>\n<div class=\"ssd-widget-text zb-W148085921099734I1-1505292207000 W148085921099734I1\">\n    1.&#40657;&#31859;&#29992;&#27700;&#28120;&#27927;&#21518;&#21152;&#19968;&#30871;&#27700;&#28024;&#27873;4&#20010;&#23567;&#26102;<br/>2.&#25226;&#28024;&#27873;&#22909;&#30340;&#40657;&#31859;&#25918;&#20837;&#39640;&#21387;&#38149;&#65292;&#21152;&#20837;&#36866;&#37327;&#30340;&#27700;<br/>3.&#22823;&#28779;&#29038;&#21040;&#39640;&#21387;&#38149;&#21457;&#20986;&#25169;&#21735;&#22768;&#21518;&#25913;&#23567;&#28779;&#29038;20&#20998;&#38047;<br/>4.&#25226;&#40657;&#31859;&#39277;&#30427;&#20986;&#65292;&#25918;&#63865;&#21518;&#25746;&#20837;&#29980;&#37202;&#26354;&#65292;&#25292;&#21248;<br/>5.&#25163;&#27838;&#28857;&#20937;&#24320;&#27700;&#25226;&#31859;&#39277;&#21387;&#23454;&#19968;&#19979;<br/>6.&#22312;&#31859;&#39277;&#20013;&#38388;&#25665;&#19968;&#20010;&#27934;<br/>7.&#30422;&#19978;&#30422;&#23376;&#65292;&#22799;&#22825;&#19968;&#33324;2&#22825;&#21363;&#21487;<br/>8.&#21507;&#30340;&#26102;&#20505;&#21487;&#20197;&#20877;&#25918;&#28857;&#20937;&#24320;&#27700;&#19981;&#35753;&#20854;&#32487;&#32493;&#21457;&#37237;\n    </div>\n<div class=\"ssd-widget-text zb-W148085921099734I2-1505292212000 W148085921099734I2\">\n    &#40644;&#35910;&#65292;&#28023;&#24102;&#65292;&#29482;&#39592;&#65292;&#23004;&#29255;&#65292;&#30416;\n    </div>\n<div class=\"ssd-widget-text zb-W148085921099734I3-1505292220000 W148085921099734I3\">\n    1.&#40644;&#35910;&#27927;&#20928;&#27873;&#21457;&#19968;&#22812;<br/>2.&#28023;&#24102;&#27873;&#21457;&#21518;&#27927;&#20928;&#65292;&#20999;&#23567;&#27573;<br/>3.&#29482;&#39592;&#21057;&#22823;&#22359;&#65292;&#27927;&#20928;&#65292;&#28118;&#21435;&#34880;&#27700;<br/>4.&#25226;&#29482;&#39592;&#21644;&#23004;&#29255;&#19968;&#21516;&#25918;&#20837;&#27748;&#38149;&#20013;<br/>5.&#22823;&#28779;&#28903;&#24320;&#21435;&#38500;&#34920;&#38754;&#28014;&#27819;&#33267;&#27748;&#27700;&#28165;&#28552;<br/>6.&#20877;&#21152;&#20837;&#40644;&#35910;&#21644;&#28023;&#24102;<br/>7.&#39135;&#29992;&#21069;&#21152;&#30416;&#35843;&#21619;\n    </div>\n<div class=\"ssd-widget-text zb-W148085921099734I4-1505292232000 W148085921099734I4\">\n    &#31983;&#31859;&#12289;&#26928;&#27713;&#12289;&#35910;&#27801;\n    </div>\n<div class=\"ssd-widget-text zb-W148085921099734I5-1505292241000 W148085921099734I5\">\n    1.&#31983;&#31859;&#27927;&#20928;&#21518;&#65292;&#27813;&#24178;&#27700;&#20998;&#21518;&#20498;&#20837;180&#20811;&#26928;&#27974;<br/>2.&#35843;&#20837;&#30416;&#12289;&#32454;&#30722;&#31958;&#65292;&#25292;&#21248;&#21518;&#35206;&#19978;&#20445;&#40092;&#33180;&#65292;&#25918;&#20837;&#20912;&#31665;&#20919;&#34255;24&#23567;&#26102;<br/>4.&#23558;&#20919;&#34255;&#36807;&#30340;&#31983;&#31859;&#65292;&#25918;&#20837;&#33976;&#38149;&#37324;&#33976;&#29087;<br/>5.&#23558;&#26928;&#27974;&#21152;&#28909;&#21518;&#65292;&#20498;&#20837;&#33976;&#22909;&#30340;&#31983;&#31859;&#39277;&#37324;&#65292;&#25605;&#25292;&#22343;&#21248;&#65292;&#30452;&#33267;&#31983;&#31859;&#21560;&#25910;&#23436;&#26928;&#27974;<br/>6.&#21462;&#36866;&#37327;&#31983;&#31859;&#39277;&#22242;&#25104;&#22242;&#21518;&#21387;&#25153;&#65292;&#25918;&#20837;&#36866;&#37327;&#35910;&#27801;&#39301;&#65292;&#20877;&#22242;&#25104;&#22242;&#65292;&#20570;&#22909;&#30340;&#31983;&#31859;&#22242;&#25918;&#20837;&#20912;&#31665;&#20919;&#34255;&#21518;&#39135;&#29992;&#65292;&#39118;&#21619;&#26356;&#20339;\n    </div>\n<div class=\"ssd-widget-pic W15052910955156\" >\n    <img    src=\"https://img30.360buyimg.com/sku/jfs/t7873/159/288400982/26592/562b8159/59916557Ndf6e95a9.jpg\"    alt=\"【京东超市】天地粮人之喜从天降 有机 十二种 杂粮礼盒 3.96kg（礼品 节日礼物 礼包 年货 公司福利 团购）-京东\"  />\n        </div>\n<div class=\"ssd-widget-pic W15052911149757\" >\n    <img    src=\"https://img30.360buyimg.com/sku/jfs/t5797/224/9576056401/63360/f3c23e67/599bf17fN5ba5e9c0.png\"    alt=\"【京东超市】天地粮人之喜从天降 有机 十二种 杂粮礼盒 3.96kg（礼品 节日礼物 礼包 年货 公司福利 团购）-京东\"  />\n        </div>\n<div class=\"ssd-widget-pic W15052911377078\" >\n    <img    src=\"https://img30.360buyimg.com/sku/jfs/t6013/315/9421860993/78834/a19c1284/59941636N04005420.png\"    alt=\"【京东超市】天地粮人之喜从天降 有机 十二种 杂粮礼盒 3.96kg（礼品 节日礼物 礼包 年货 公司福利 团购）-京东\"  />\n        </div>\n<div class=\"ssd-widget-pic W15052911556959\" >\n    <img    src=\"https://img30.360buyimg.com/sku/jfs/t7390/319/1480439581/71957/f58d3b54/599d3887N80646726.png\"    alt=\"【京东超市】天地粮人之喜从天降 有机 十二种 杂粮礼盒 3.96kg（礼品 节日礼物 礼包 年货 公司福利 团购）-京东\"  />\n        </div>\n<div class=\"ssd-widget-text zb-W150529117620210-1505291247000 W150529117620210\">\n    &#40657;&#31859;&#29980;&#37202;&#37247;\n    </div>\n<div class=\"ssd-widget-text zb-W150529225890214-1505292270000 W150529225890214\">\n    &#21046;&#20316;&#39135;&#26448;\n    </div>\n<div class=\"ssd-widget-text zb-W150529225890214-1505292270000 W150529228213316\">\n    &#21046;&#20316;&#39135;&#26448;\n    </div>\n<div class=\"ssd-widget-text zb-W150529225890214-1505292270000 W150529232106618\">\n    &#21046;&#20316;&#39135;&#26448;\n    </div>\n<div class=\"ssd-widget-text zb-W150529227366315-1505292279000 W150529227366315\">\n    &#21046;&#20316;&#27969;&#31243;\n    </div>\n<div class=\"ssd-widget-text zb-W150529227366315-1505292279000 W150529228802117\">\n    &#21046;&#20316;&#27969;&#31243;\n    </div>\n<div class=\"ssd-widget-text zb-W150529227366315-1505292279000 W150529232503519\">\n    &#21046;&#20316;&#27969;&#31243;\n    </div>\n<div class=\"ssd-widget-text zb-W150529125157511-1505291279000 W150529125157511\">\n    &#40644;&#35910;&#28023;&#24102;&#22823;&#39592;&#27748;\n    </div>\n<div class=\"ssd-widget-text zb-W150529128472612-1505291298000 W150529128472612\">\n    &#26928;&#27974;&#31983;&#31859;&#22242;\n    </div>\n</div>\n\n</div><!-- 2017-10-17 11:06:14 --> \n<link rel='stylesheet' type='text/css' href='//sku-market-gw.jd.com/css/pc/1112775.css?t=1520296182643' media='all' />","is_on_sale":1,"add_time":1542690950000,"update_time":1542945292000,"sort_order":null,"is_delete":0,"attribute_category":null,"counter_price":null,"extra_price":null,"is_new":null,"goods_unit":null,"primary_pic_url":null,"list_pic_url":"http://img13.360buyimg.com/n0/jfs/t3100/181/1901796131/394165/8edfa003/57d64610N0e136d51.jpg","market_price":158.00,"retail_price":140.80,"sell_volume":null,"primary_product_id":null,"unit_price":null,"promotion_desc":null,"promotion_tag":null,"app_exclusive_price":null,"is_app_exclusive":null,"is_limited":null,"is_hot":1,"cart_num":0,"product_id":null,"product_market_price":158.00,"is_7_ToReturn":null,"source":"JD"}},"errmsg":"执行成功"};
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
	div{
		font-size:.29rem;
	}
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
    padding: .02rem .15rem;
    border: 1px solid #f48f18;
    font-size: .25rem;
    color: #f48f18;
    border-radius: .04rem;
    /*background: url(http://nos.netease.com/mailpub/hxm/yanxuan-wap/p/20150730/style/img/icon-normal/detailTagArrow-18bee52dab.png) 95% center no-repeat;
    background-size: .1075rem .1875rem;*/
}
</style>
