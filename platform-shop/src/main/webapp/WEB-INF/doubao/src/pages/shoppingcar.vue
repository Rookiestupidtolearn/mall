<template>
  <div class="shoppingcar">
  	<!--公用头部-->
  	<!--<headbar :headFont = "headFont"></headbar>-->
  	
  	<!--<div class="service-policy ">
  		<span class="item">30天无忧退货</span>
  		<span class="item">48小时快速退款</span>
  		<span class="item">免邮费</span>
  	</div>-->
  	<!--商品已下架的弹窗-->
  		<div class="bg" v-show="showTn" ></div>
  		<div class="bgShow" v-show="showTn" >
  			<div class="font">这位客官~你看中的以下宝贝实在太抢手，已经脱销啦。<br/>您再看看别的呗？斗宝俱乐部应有尽有哦~</div>
  			<div class="itemtotal">
	  			<div class="item" v-for="(item,index) in unsells" >
	            <div class="cart-goods">
	              <div class="info">
	                	<img class="img"  :src="item.list_pic_url"/>
	                  <div class="t">
	                    <span class="name">{{item.name}}</span>
	                     <span class="num">x{{item.cart_num}}</span>
	                  </div>
	                  <span class="price">￥{{item.market_price}}</span>
	              </div>
	            </div>
	          </div>
	      	</div>
  			<ul class="btngroup" v-if="isSell==0">
  				<li class="cancel" @click="cancel">再看看</li>
  				<li class="confirm" @click="goOrder"><i>继续结算</i><br/><span>(将去除列表商品)</span></li>
  			</ul>
  			<ul class="btngroupT" @click="cancelShop" v-if="isSell==1">知道了，去重新下单</ul>
  		</div>
  		<!--其它主体内容-->
  	<div class="no-cart" v-if="cartGoods.length <= 0">
    <div class="c">
      <div class="title-box">购物车空空如也～</div>
      <router-link class="to-index-btn" to="/">去逛逛</router-link>
    </div>
  </div>
  	<div class="cart-div" v-if="cartGoods.length > 0">
    	<div class="list">
      	<div class="group-item">
	        <div class="goods">
	          <div class="item" :class="isEditCart ? 'edit' : ''" v-for="(item,index) in cartGoods" >
	            <div class="checkbox" :class="item.checked ? 'checked' : ''" @click="checkedItem(index)" data-itemIndex="index"></div>
	            <div class="cart-goods">
	              <div class="info">
	                <router-link class="midHover" :to="item.good_url">
	                	<img class="img"  :src="item.list_pic_url"/>
	                  <div class="t">
	                    <span class="name">{{item.goods_name}}</span>
	                  </div>
	                  <div class="attr">{{ isEditCart ? '已选择:' : ''}}{{item.goods_specifition_name_value||''}}</div>
	                  <span class="price">￥{{item.market_price}}</span>
	                </router-link>
	               	<div class="tc" v-show="!isEditCart">
	                  <span class="num">x{{item.number}}</span>
	                </div>
	                <div class="b">
	                  <div class="selnum">
	                    <div class="cut" @click="cutNumber(index)" data-itemIndex="index">-</div>
	                    <input :value="item.number" class="number"  readonly type="number" />
	                    <div class="add" @click="addNumber(index)" data-itemIndex="index">+</div>
	                  </div>
	                </div>
	              </div>
	            </div>
	          </div>
	        </div>
	      </div>
	    </div>
	    <div class="cart-bottom">
      <div class="checkbox" :class="checkedAllStatus ? 'checked' : ''" @click="checkedAll">全选({{cartTotal.checkedGoodsCount}}) </div>
      <div class="total">{{!isEditCart ? '￥'+cartTotal.checkedGoodsAmount : ''}}</div>
      <div class="delete" @click="editCart">{{!isEditCart ? '编辑' : '完成'}}</div>
      <div class="checkout" @click="deleteCart" v-if="isEditCart">删除所选</div>
      <div class="checkout" @click="checkoutOrder" v-if="!isEditCart">去结算</div>
    </div>
   </div>

<!--公用底部导航-->
  	<tabbar :selected="selected" :tabs='tabs'></tabbar> 
  	
  </div>
</template>

<script>
import tabbar from '@/components/tabbar.vue'
//	import headbar from '@/components/headbar'
import { Toast } from 'mint-ui'
	
export default {
	components: {tabbar },
  name: 'shoppingcar',
  data () {
    return {
//  	headFont:'购物车',
			showTn:false, //已售罄/下架弹窗显示
			unsells:[],
			isSell:'', //是否可售
    	cartGoods: [],
	    cartTotal: {
	      "goodsCount": 0,
	      "goodsAmount": 0.00,
	      "checkedGoodsCount": 0,
	      "checkedGoodsAmount": 0.00
	    },
	    isEditCart: false,
	    checkedAllStatus: true,
    	editCartList:[],
    	selected:"shoppingcar",
   	 tabs:[require("../../static/images/ic_menu_choice_nor.png"),require("../../static/images/ic_menu_sort_nor.png"),
    	require("../../static/images/ic_menu_shoping_pressed.png"),require("../../static/images/ic_menu_me_nor.png")],
    }
  },
  mounted(){
		this.getCartList();
  },
  methods:{
  		cancel(){
  			this.showTn = false;
  		},
  		cancelShop(){
	   		window.location.reload();
	   },
	  	deleteCart(){
	  			//获取已选择的商品
			    let that = this;
			
			    let productIds = this.cartGoods.filter(function (element, index, array) {
			      if (element.checked == true) {
			        return true;
			      } else {
			        return false;
			      }
			    });
			
			    if (productIds.length <= 0) {
			      return false;
			    }
			
			    productIds = productIds.map(function (element, index, array) {
			      if (element.checked == true) {
			        return element.product_id;
			      }
			    });
			
		  that.$http({
	        method: 'post',
	        url: that.$url+'cart/delete.options',
	        data:{productIds: productIds.join(',')}
        }).then(function (res) {
        		var res = res.data;
			      if (res.errno === 0) {
//			        console.log(res.data);
			        let cartList = res.data.cartList.map(v => {
			          console.log(v);
			          v.checked = false;
			          return v;
			        });
			
			         that.cartGoods = cartList;
			         that.cartTotal =  res.data.cartTotal
			      }else{
			        Toast(res.data.errmsg);
			      }
			
			      that.checkedAllStatus = that.isCheckedAll()
			    });
	  	},
	  	checkoutOrder(){
	  		//获取已选择的商品
			    let that = this;
			    var checkedGoods = this.cartGoods.filter(function (element, index, array) {
			      if (element.checked == true) {
			        return true;
			      } else {
			        return false;
			      }
			    });
			    if (checkedGoods.length <= 0) {
			      return false;
			    }
			    /*三方状态下架或无库存*/
		      var goodIds = checkedGoods.map(function (v) {
		        return v.goods_id+'_'+v.number;
		      }); 
		     that.$http({
		        method: 'post',
		        url: that.$url+'cart/payBeforeCheck.options',
		        data:{ goodIds: goodIds.join(','), }
		     }).then(function (res) {
			     	var res = res.data;
			     	if(res.errno == 0){
			     		that.$router.push('/pages/category/checkout?isBuy=false');
			     	}else{
			     		that.showTn = true;
			     		that.unsells = res.unsells;
			     		that.isSell = res.isSell;
			     	}
		      });
			      
	  	},
	  	goOrder(){  //弹窗的继续结算
	  		this.$router.push('/pages/category/checkout?isBuy=false');
	  	},
	  	checkedAll(){
	  			let that = this;
			    if (!this.isEditCart) {
			      var productIds = this.cartGoods.map(function (v) {
			        return v.product_id;
			      });
			     that.$http({
			        method: 'post',
			        url: that.$url+'cart/checked.options',
			        data:{ productIds: productIds.join(','), isChecked: that.isCheckedAll() ? 0 : 1 }
			     }).then(function (res) {
			     	var res = res.data;
			        if (res.errno === 0) {
			            that.cartGoods = res.data.cartList,
			            that.cartTotal = res.data.cartTotal
			        }else{
			          Toast(res.data.errmsg);
			        }
			
			          that.checkedAllStatus = that.isCheckedAll()
			      });
			    } else {
			      //编辑状态
			      let checkedAllStatus = that.isCheckedAll();
			      let tmpCartData = that.cartGoods.map(function (v) {
			        v.checked = !checkedAllStatus;
			        return v;
			      });
			
			        that.cartGoods = tmpCartData;
			        that.checkedAllStatus = that.isCheckedAll();
			       that.cartTotal.checkedGoodsCount =  that.getCheckedGoodsCount()
			    }
	  	},
		  checkedItem(itemIndex){
			    let that = this;
			    if (!this.isEditCart) {
			      that.$http({
			        method: 'post',
			        url: that.$url+'cart/checked.options',
			        data:{ 
				        	productIds: that.cartGoods[itemIndex].product_id, 
				        	isChecked: that.cartGoods[itemIndex].checked ? 0 : 1 
			      	 }
		        }).then(function (res) {
		        		var res = res.data;
				        if (res.errno === 0) {
//				          console.log(res.data);
				            that.cartGoods = res.data.cartList;
				            that.cartTotal = res.data.cartTotal
				        }
				          that.checkedAllStatus = that.isCheckedAll();
				      });
			    } else {
			      //编辑状态
			      let tmpCartData = this.cartGoods.map(function (element, index, array) {
			        if (index == itemIndex){
			          element.checked = !element.checked;
			        }
			        return element;
			      });
			
			        that.cartGoods = tmpCartData;
			        that.checkedAllStatus =  that.isCheckedAll();
			        that.cartTotal.checkedGoodsCount = that.getCheckedGoodsCount()
			    }
  	},
  	editCart(){
	  		var that = this;
		    if (this.isEditCart) {
		      this.getCartList();
		       this.isEditCart =  !this.isEditCart;
		    } else {
		      //编辑状态
		      let tmpCartList = this.cartGoods.map(function (v) {
		        v.checked = false;
		        return v;
		      });
		      this.editCartList =  this.cartGoods;
		      this.cartGoods =  tmpCartList;
		      this.isEditCart = !this.isEditCart;
		      this.checkedAllStatus = that.isCheckedAll();
		      this.cartTotal.checkedGoodsCount = that.getCheckedGoodsCount();
		    }
  	},
  	getCartList(){
  		let that = this;
	    that.$http({
	    	 method: 'post',
    		url: that.$url+'cart/index',
	    }).then(function (res) {
	    	var res = res.data;
	      	if (res.errno === 0) {
		        that.cartGoods = res.data.cartList;
		        that.cartTotal = res.data.cartTotal;
	      	}else{
	      		Toast(res.errmsg);
	      	}
	        that.checkedAllStatus = that.isCheckedAll();
	    });
  	},
  	isCheckedAll(){
	  	return this.cartGoods.every(function (element, index, array) {
		    if (element.checked == true) {
		        return true;
		    } else {
		        return false;
		    }
	    });
  	},
  	getCheckedGoodsCount(){
	  		let checkedGoodsCount = 0;
		    this.cartGoods.forEach(function (v) {
		      if (v.checked === true) {
		        checkedGoodsCount += v.number;
		      }
		    });
		    return checkedGoodsCount;
  	},
  	updateCart(productId, goodsId, number, id){
	  		let that = this;
		    that.$http({
		    	 method: 'post',
        	url: that.$url+'cart/update.options',
		      data:{
		      	productId: productId,
			      goodsId: goodsId,
			      number: number,
			      id: id}
			    }).then(function (res) {
			      if (res.errno === 0) {
			        console.log(res.data);
			      }
		        that.checkedAllStatus = that.isCheckedAll();
		    });
  	},
  	cutNumber(itemIndex){
		    let cartItem = this.cartGoods[itemIndex];
		    let number = (cartItem.number - 1 > 1) ? cartItem.number - 1 : 1;
		    cartItem.number = number;
		    this.cartGoods =  this.cartGoods;
		    this.updateCart(cartItem.product_id, cartItem.goods_id, number, cartItem.id);
  	},
  	addNumber(itemIndex){
		    let cartItem = this.cartGoods[itemIndex];
		    let number = cartItem.number + 1;
		    cartItem.number = number;
		    this.cartGoods =  this.cartGoods;
		    this.updateCart(cartItem.product_id, cartItem.goods_id, number, cartItem.id);
  	}
  }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only  active -->
<style scoped>
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
	}
	.bgShow .font{
		font-size: .25rem;
    color: #333;
    padding: 0 .3rem;
    text-align: left;
    margin-top: .3rem;
    padding-bottom:.5rem;
	}
	.bgShow .btngroup{
		width:100%;
		overflow: hidden;
		font-size:.29rem;
		border-top:1px solid #CBCBCB;
	}
	.bgShow .btngroup li.cancel{
		border-right:1px solid #CBCBCB;
		-webkit-box-sizing: border-box;
		line-height:.9rem;
	}
	.bgShow .btngroup li.confirm{
		color: #26A2FF;
		line-height:.3rem;
	}
	.bgShow .btngroup li.confirm i{
		display: inline-block;
		margin-top:.2rem;
	}
	.bgShow .btngroup li.confirm span{
		font-size:.22rem;
	}
	.bgShow .btngroup li{
		float:left;
		width:50%;
		height:.9rem;
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
.midHover {
		float:left;
		height:1.25rem;
}
.cart-div .item .img {
		float:left;
		height:1.25rem;
		width:1.25rem;
		background:#f4f4f4;
		margin:0 .18rem 0 0;
}
.cart-div .item .price {
	float:left;
	font-size: .29rem;
	color:#333;
}
.cart-div .item .b {
		height:.28rem;
		line-height:.28rem;
		font-size:.25rem;
		color:#333;
		overflow:hidden;
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

.no-cart{
    width: 100%;
    height: auto;
    margin: 0 auto;
}

.no-cart .c{
    width: 100%;
    height: auto;
    margin-top: 2.00rem;
}

.no-cart .c image{
    margin: 0 auto;
    display: block;
    text-align: center;
    width: 2.58rem;
    height: 2.58rem;
}

.no-cart .c text{
    margin: 0 auto;
    display: block;
    width: 2.58rem;
    height: .29rem;
    line-height: .29rem;
    text-align: center;
    font-size: .29rem;
    color: #999;
}
.title-box{
    width: 100%;
    padding-top: 3.30rem;
    text-align: center;
    font-size:.28rem;
    color:#999;
    background: url(https://platform-wxmall.oss-cn-beijing.aliyuncs.com/upload/20180727/15015039793f4e.png) no-repeat center 205rem;
    background-size: 1.00rem auto;
    margin-bottom: .50rem;
}
.to-index-btn{
    color:#fff;
    background:#e64340;
    border-radius:.06rem;
    width:3.00rem;
    height: auto;
    line-height: .70rem;
    text-align: center;
    font-size:.28rem;
    margin: 0 auto;
    display: block;
}

.cart-div{
    width: 100%;
    height: auto;
    overflow: hidden;
    
}

.cart-div .list{
    height: auto;
    width: 100%;
    overflow: hidden;
    margin-bottom: 1.20rem;
}

.cart-div .group-item{
    height: auto;
    width: 100%;
    background: #fff;
    margin-bottom: .18rem;
}

.cart-div .item{
    height: 1.64rem;
    width: 100%;
    overflow: hidden;
}
.cart-div .item .checkbox{
    float: left;
    padding: .80rem .18rem .80rem .56rem;
    background: url(http://nos.netease.com/mailpub/hxm/yanxuan-wap/p/20150730/style/img/icon-normal/checkbox-0e09baa37e.png) no-repeat center center;
    background-size: .34rem;
}

.cart-div .item .checkbox.checked{
    background: url(http://nos.netease.com/mailpub/hxm/yanxuan-wap/p/20150730/style/img/icon-normal/checkbox-checked-822e54472a.png) no-repeat center center;
    background-size: .34rem;
}

.cart-div .item .cart-goods{
    float: right;
    height: 1.64rem;
    width: 6.7rem;
    border-bottom: 1px solid #f4f4f4;
    position:relative;
}

.midHover{
  float:left;
  height:1.25rem;
}

.cart-div .item .info{
    float: left;
    height:1.25rem;
    width: 6.53rem;
    margin: .195rem .26rem .195rem 0;
}

.cart-div .item .t{
    margin: .08rem 0;
    font-size: .25rem;
    color: #333;
    overflow: hidden;
    float: left;
}

.cart-div .item .name{
    font-size: .25rem;
    color: #333;
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
    width: 4.2rem;
    text-align: left;
    display: inline-block;
}

.cart-div .item .num{
    height: .28rem;
    line-height: .28rem;
    float: right;
    margin-right: .15rem;
}

.cart-div .item .attr{
    margin-bottom: .06rem;
    height: .24rem;
    line-height: .24rem;
    font-size: .22rem;
    color: #666;
    overflow: hidden;
}

.cart-div .item .b{
    height: .28rem;
    line-height: .28rem;
    font-size: .25rem;
    color: #333;
    overflow: hidden;
}

.cart-div .item .price{
    float: left;
    margin-top: .25rem;
}

.cart-div .item .open{
    height: .28rem;
    width: 1.50rem;
    display: block;
    float: right;
    background: url(http://nos.netease.com/mailpub/hxm/yanxuan-wap/p/20150730/style/img/icon-normal/arrowDown-d48093db25.png) right center no-repeat;
    background-size: .25rem;
    font-size: .25rem;
    color: #333;
}

.cart-div .item .tc{
    margin: .08rem 0;
    font-size: .25rem;
    color: #333;
    overflow: hidden;
}

.cart-div .item .attr.tc{
    display: none;
}

.cart-div .item.edit .attr{
    position:absolute;
    right:.20rem;
    background-size: .12rem .20rem;
    margin: .08rem 0;
    height: .39rem;
    line-height: .39rem;
    font-size: .24rem;
    color: #999;
    overflow: hidden;
}

.cart-div .item.edit .b{
    position:absolute;
    right:.20rem;
    bottom:.25rem;
    height: .52rem;
    overflow: hidden;
}

.cart-div .item.edit .price{
    line-height: .52rem;
    height: .52rem;
    margin-top: .25rem;
    clear: both;
    position: absolute;
    top: .65rem;
    left: 50%;
    margin-left: -1.9rem;
}

.cart-div .item .selnum{
    display: none;
}

.cart-div .item.edit .selnum{
    width: 2.35rem;
    height: .52rem;
    border: 1px solid #ccc;
    display: flex;
		box-sizing: border-box;    
		-webkit-box-sizing: border-box;    
}

.selnum .cut{
    width: .70rem;
    height: 100%;
    text-align: center;
    line-height: .50rem;
    display:inline-block;
}

.selnum .number{
    flex: 1;
    height: 100%;
    text-align: center;
    line-height: .6875rem;
    border-left: 1px solid #ccc;
    border-right: 1px solid #ccc;
    float: left;
    display:inline-block;
		width: .7rem;
}

.selnum .add{
    width: .80rem;
    height: 100%;
    text-align: center;
    line-height: .50rem;
    display:inline-block;
}


.cart-div .group-item .header{
    width: 100%;
    height: .94rem;
    line-height: .94rem;
    padding: 0 .26rem;
    border-bottom: 1px solid #f4f4f4;
}

.cart-div .promotion .icon{
    display: inline-block;
    height: .24rem;
    width: .15rem;
}

.cart-div .promotion{
    margin-top: .255rem;
    float: left;
    height: .43rem;
    width: 4.80rem;
    /*margin-right: 84rem;*/
    line-height: .43rem;
    font-size: 0;
}

.cart-div .promotion .tag{
    border: 1px solid #f48f18;
    height: .37rem;
    line-height: .31rem;
    padding: 0 .09rem;
    margin-right: .10rem;
    color: #f48f18;
    font-size: .245rem;
}

.cart-div .promotion .txt{
    height: .43rem;
    line-height: .43rem;
    padding-right: .10rem;
    color: #333;
    font-size: .29rem;
    overflow: hidden;
}

.cart-div .get{
    margin-top: .18rem;
    float: right;
    height: .58rem;
    padding-left: .14rem;
    border-left: 1px solid #d9d9d9;
    line-height: .58rem;
    font-size: .29rem;
    color: #333;
}

.cart-bottom{
    position: fixed;
    bottom:1.2rem;
    left:0;
    height: 1.00rem;
    width: 100%;
    background: #fff;
    display: flex;
}

.cart-bottom .checkbox{
    height: .34rem;

    padding-left: .60rem;
    line-height: .34rem;
    margin: .33rem .18rem .33rem .26rem;
    background: url(http://nos.netease.com/mailpub/hxm/yanxuan-wap/p/20150730/style/img/icon-normal/checkbox-0e09baa37e.png) no-repeat;
    background-size: .34rem;
    font-size: .29rem;
}

.cart-bottom .checkbox.checked{
    background: url(http://nos.netease.com/mailpub/hxm/yanxuan-wap/p/20150730/style/img/icon-normal/checkbox-checked-822e54472a.png) no-repeat;
    background-size: .34rem;
}

.cart-bottom .total{
    height: .34rem;
    flex: 1;
    margin: .33rem .10rem;
    font-size: .29rem;
    text-align: left;
}


.cart-bottom .delete{
    height: .34rem;
    width: auto;
    margin: .33rem .18rem;
    font-size: .29rem;
}

.cart-bottom .checkout{
    height: 1.00rem;
    width: 2.10rem;
    text-align: center;
    line-height: 1.00rem;
    font-size: .29rem;
    background: #b4282d;
    color: #fff;
}
.bgShow .btngroupT{
		width:100%;
		overflow: hidden;
		font-size:.29rem;
		padding:.2rem 0;
		border-top:1px solid #CBCBCB;
		color: #26A2FF;
	}
</style>
