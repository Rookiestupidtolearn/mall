<template>
 	<div class="container" ref="input1">
 		<!--公用头部-->
  		<!--<headbar :headFont = "headFont"></headbar>-->
  	
	 	<div v-if="!openAttr" >
		   <mt-swipe :auto="3000" class="swiper" >
			  <mt-swipe-item v-for="item in banner">
			  	<img :src="item.img_url"/>
			  </mt-swipe-item>
			</mt-swipe>
			<!--<div class="service-policy">
		  		<span class="item">30天无忧退货</span>
		  		<span class="item">48小时快速退款</span>
		  		<span class="item">免邮费</span>
		  	</div>-->
		  	<div class="goods-info">
		     <div class="c">
		        <p class="name">{{goods.name}}</p>
		        <!--<p class="desc">{{goods.goods_brief}}</p>-->
		        <p class="price">￥{{goods.market_price}}</p>
		        <div class="brand" v-if="brand">
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
		        <div class="item" v-for="item in relatedGoods" @click="detailHref('/pages/goods/goods?id='+item.id)">
		            <img class="img" :src="item.list_pic_url" background-size="cover"/>
		            <p class="name">{{item.name}}</p>
		            <p class="price">￥{{item.market_price}}</p>
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
			<returnhome :scrollshow = "scrollshow"></returnhome>
  </div>
</template>

<script>
	import { Toast } from 'mint-ui';
	import { Indicator } from 'mint-ui';
	import returnhome from '@/components/returnHome';
//	import headbar from '@/components/headbar.vue'
		
	export default {
	  name: 'bodylook',
//	  components:{headbar},
		components:{returnhome},
	  data () {
	    return {
//	    	headFont:'商品详情',
			scrollshow:true,
	    	market_price:'',
	    	idm:'',
	    	banner:[],
	    	undercarriage:'',
	    	undercarriName:'加入购物车',
	    	goods:'',
	    	brand:'',
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
	    	noCollectImage: require("../../../static/images/icon_collect.png"),
		    hasCollectImage: require("../../../static/images/icon_collect_checked.png"),
		    collectBackImage: require("../../../static/images/icon_collect.png")
	    }
	  },
	  mounted(){
	  		var that = this;
	  		this.idm = this.$route.query.id;
	  		
	  		//获取购物车数量
	  		that.$http({
	    		method: 'post',
		        url:that.$url+ 'cart/goodscount',
	    	}).then(function (response) {
	    		if(response.data.errno==0){
	    			let _res = response.data;
	    			var goodsCount = _res.data.cartTotal.goodsCount; //购物车数量
	    			if (goodsCount ==""  || goodsCount == undefined){
	    				that.cartGoodsCount = 0;
	    			}else{
	    				that.cartGoodsCount = goodsCount;
	    			}
	    		}
		    });
		    //大家都在看
	  		this.relaed();
	    	//商品详情
	  		this.Detail();
	  		
	  },
	destroyed(){
		//删除上次智齿遗留DOM
 		document.body.removeChild(document.getElementById('zhichiBtnBox'));
 		document.body.removeChild(document.getElementById('ZCPanel'));
 		document.body.removeChild(document.getElementById('bubbleMsg'));
	},
//	watch:{
//		$route(to,from){
//			this.relateds();
//		}
//	},
	methods:{
//		relateds(){
//			if(parseInt(this.$route.query.id) && this.$route.query.id !== this.idm){
//				this.idm=this.$route.query.id;
//			    this.Detail();
//			    this.relaed();
//			}
//		},
		relaed(){
			let that = this;
			Indicator.open();
			//关联
	    	that.$http({
	    		method: 'post',
		        url:that.$url+ 'goods/related',
		        params:{id:that.idm}
	    	}).then(function (response) {
    			Indicator.close();
		        that.relatedGoods = response.data.data.goodsList;
		    });
		},
		Detail(){
			let that = this;
			//商品详情
	    	that.$http({
		        method: 'post',
		        url:that.$url+ 'goods/detail',
		        params:{id:that.idm}
	    	}).then(function (response) {
	    		var res = response.data;
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

				that.zhichi(); //智齿客服
				
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
		        if (res.data.userHasCollect == 1) {
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
		        if(specValueList.length != 0){
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
		        }
		        that.market_price = that.minPriceList[0].market_price;
		        that.checkedSpecText =  showArraySpec.join('　');
		        that.specificationList = _specificationList;
		        
	 		 })
		},
		zhichi(){
			let that = this;
			//			获取userId
			if(document.getElementById('zhichiBtnBox') == null){
				var userId = that.$cookie.getCookie('userId');
				//	  	<!--该链接可在智齿客服工作台=>设置=>接入渠道中找到--> this指向script了
				let para=document.createElement("script");
		        para.src = "https://www.sobot.com/chat/frame/js/entrance.js?sysNum=e5ef8967b4114644a4c290bf0729f959";
		        para.setAttribute("id", "zhichiScript");
	        	para.setAttribute("class", "zhiCustomBtn");
	        	para.setAttribute("data-args", "partnerId="+userId);   //ios记录用户标识
		       	this.$refs.input1.appendChild(para);
	
				para.onload=function(){
					//	初始化智齿咨询组件实例
					var zhiManager = (getzhiSDKInstance());
					//再调用load方法
					zhiManager.on("load", function() {
					    zhiManager.initBtnDOM();
					});
					
					zhiManager.set('title_info',that.goods.name);   //商品信息的标题（必传）
					zhiManager.set('url_info',window.location.href);  //商品信息的商品链接地址（必传）
					zhiManager.set('abstract_info',that.goods.name);  //商品信息的简述内容（选传） 无描述用的标题
					zhiManager.set('label_info',that.market_price);	  //商品标签例：价格（选传）
					zhiManager.set('thumbnail_info',that.banner[0].img_url);  //商品的缩略图（选传）
					zhiManager.set('invite', 0); //关闭开启自动邀请
	
				}
			}else{
				document.getElementById('zhichiBtnBox').style.display= 'block'; //默认隐藏智齿
			}
			
		},
		detailHref(e){
			var appHref = window.location.href;
			var device = '';
			var comHref = window.location.origin;
			if(appHref.indexOf('device')>-1){
				device = appHref.split('&')[1].split('=')[1];
			}
	    	if(device == 'android'){
	    			window.android.productDetail(comHref +'/#'+e); //调起andriod交互方法(由app发起。浏览器会报错正常)
	    			return false;
	    	}else if(device == 'ios'){
	    			var message = {'url':comHref +'/#'+ e}
						window.webkit.messageHandlers.webViewApp.postMessage(message);
						return false;
	    	}else{
	    		this.$router.push(e);
	    	}
		},
	  	cutNumber(){
      		this.number = (this.number - 1 > 1) ? this.number - 1 : 1
	  	},
	  	addNumber () {
	     	 this.number = this.number + 1
	  	},
	  	openCartPage(){
	  		/*android与ios交互*/
	  		var hrefD = window.location.href;
				if(hrefD.indexOf('device')>-1){
	    		var device = hrefD.split('&')[1].split('=')[1];
	    	}
	//  	http://192.168.124.29:8081/#/?device=andriod;
//				alert(device);
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
  		    let that = this;
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
			        url:that.$url+ 'cart/add.options',
			        data:{ goodsId:that.goods.id, number:that.number, productId: checkedProduct[0].id }
		    	}).then(function (response) {
			          let _res = response;
			          if (_res.data.errno == 0) {
			            Toast({message:'添加成功',duration: 1500});
			              that.openAttr = !that.openAttr,
			              that.cartGoodsCount = _res.data.data.cartTotal.goodsCount
			            if (that.userHasCollect == 1) {
			                that.collectBackImage = that.hasCollectImage
			            } else {
			               that.collectBackImage = that.noCollectImage
			            }
			          } else {
			          	that.$toast({message:_res.data.errmsg,duration:3000});
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
		        url:that.$url+ 'collect/addordelete.options',
		        data:{ typeId: 0, valueId: this.idm}
	    	}).then(function (response) {
		          let _res = response.data;
		          if (_res.errno == 0) {
			            if ( _res.data.type == 'add') {
			                that.collectBackImage =  that.hasCollectImage;
			            } else {
			              	that.collectBackImage = that.noCollectImage;
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
/*其它*/
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
    z-index: 30;
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
	font-size:.29rem;
	background-color:#fff
}
.detail >>>  img{
	vertical-align: top;
    margin-top: -.001rem;
    height: auto !important;
}
.detail >>> table{
	width:7.5rem !important;
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
  margin: .115rem 0 .15rem 0;
  text-align: center;
  overflow: hidden;
  font-size: .30rem;
  color: #333;
  overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
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
.container{
	padding-bottom: .6rem;
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
