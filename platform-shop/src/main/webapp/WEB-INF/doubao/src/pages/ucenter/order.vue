<template>
	<div class="hello">
		<!--公用头部-->
		<!--<headbar :headFont = "headFont"></headbar>-->
		<showTan :showTn="showTn" :unsells="unsells"></showTan>
		<div class="showList mt9" v-show="appShow">
			<ul class="nav_list">
				<router-link v-for="(item,index) in items" @click="selectStyle (item, index)" tag="li" :to="item.to">
					<span :class="activeClass == index ? 'list_choice' : '' ">{{item.value}}</span>
				</router-link>
			</ul>
		</div>
		<div class="showList" v-if="orderList.length>0">
			<ul class="" v-infinite-scroll="loadMore" infinite-scroll-disabled="isMoreLoading" infinite-scroll-distance="10" class="loadm">
				<li v-for="(item,index) in orderList">
					<div class="order"  @click="orderDetail('/pages/ucenter/orderDetail?id='+item.id)">
						<div class="h">
							<div class="l">订单编号：{{item.order_sn}}</div>
							<div class="r">{{item.order_status_text}}</div>
						</div>
						<div class="b">
							<div class="l">实付：￥{{item.actual_price}}</div>
							<div class="r">
								<!--9 已完成   0 待付款   300,201待收货   101,103已取消-->
								<!--<div v-if="item.order_status == 9" >
				            	 				<div class="btn active" @click.prevent="tipsShow">退货申请</div>
				                </div>-->
								<div v-if="item.order_status == 300 || item.order_status == 201 || item.order_status == 200">
									<div class="btn active" @click.stop="confirmOrder(item.id)">确认收货</div>
									<div class="btn" @click.stop="hrefwul('/pages/ucenter/logistics?id='+item.id)">查看物流</div>
								</div>
								<div v-else-if="item.order_status == 0">
									<div class="btn active" @click.stop="payOrder(item.id)">去付款</div>
								</div>
							</div>
						</div>
					</div>
				</li>
			</ul>
			<p class="loading" v-if="isLoading"><img src="../../../static/images/timg.gif" class="timg" /><span class="lon">加载中...</span></p>
			<p class="loading" v-else><span class="lon">没有更多数据了</span></p>
		</div>
		<div class="noData" v-show="show">没有更多数据了</div>
		<returnHome :scrollshow = "scrollshow"></returnHome>
	</div>
</template>

<script>
	import { InfiniteScroll } from 'mint-ui';
	import { MessageBox } from 'mint-ui';
	import { Indicator } from 'mint-ui';

	import showTan from '@/components/showTan.vue';
	import returnHome from '@/components/returnHome.vue';
	//import headbar from '@/components/headbar.vue';

	export default {
		name: 'order',
		//components:{headbar},
		components:{showTan,returnHome},
		data() {
			return {
				//  	headFont:'订单列表',
				scrollshow:true,
				showTn: false, //是否显示弹窗
				unsells:[],
				appShow: "",
				orderList: [],
				activeClass: 0,
				totalPages: '',
				page: 1,
				show: false,
				isMoreLoading: true,
				isLoading: true,
				size: 10,
				items: [　　{
						value: '全部',
						to: '/pages/ucenter/order'
					}, 　　 {
						value: '待付款',
						to: '/pages/ucenter/order1'
					}, 　　 {
						value: '待收货',
						to: '/pages/ucenter/order2'
					}, 　　 {
						value: '已完成',
						to: '/pages/ucenter/order3'
					},
					{
						value: '已取消',
						to: '/pages/ucenter/order4'
					}, 　
				]
			}
		},
		destroyed() {
			Indicator.close();
		},
		mounted() {
			this.getProjectInfo();
			var hrefD = window.location.href;
			var delDevice = hrefD.split('?')[0];
			var comHref = delDevice.substring(delDevice.length - 1, 0); //android和ios公用链接头
			if(hrefD.indexOf('device') > -1) {
				var device = appHref.split('?')[1].split('=')[1].split('&')[0];
			}
			if(device == 'android') {
				this.appShow = false;
				return false;
			} else if(device == 'ios') {
				this.appShow = false;
				return false;
			} else {
				this.appShow = true;
			}
		},
		methods: {
			hrefwul(e){
				this.$cookie.interactive(e);  //与android和ios交互
		  	},
			orderDetail(e){
				this.$cookie.interactive(e); //与android和ios交互
			},
			cancelShop() {
				this.$router.push('/pages/shoppingcar')
			},
			confirmOrder(id) {
				var that = this;
				that.$http({
					method: 'post',
					url: that.$url + 'order/confirmOrder.options',
					data: {
						orderId: id,
					}
				}).then(function(res) {
					var res = res.data;
					if(res.errno == 0) {
						MessageBox({
							title: ' ',
							message: '已确认收到商品 ',
							showCancelButton: true
						}, function(params) {
							if(params == 'confirm') {
								window.location.reload();
							}
						});
					} else {
						that.$toast(res.errmsg);
					}

				})
			},
			selectStyle(item, index) {
				this.activeClass = index;　
			},
			payOrder(orderIndex) {
				var that = this;
				that.$http({
					method: 'post',
					url: that.$url + 'pay/toPayOrder.options',
					data: {
						orderId: orderIndex
					}
				}).then(function(res) {
					var res = res.data;
					console.log(res);
					if(res.errno == 0) {
						window.location.href = res.payurl;
					}else if(res.errno == 1) {
						that.showTn = true;
						that.unsells = res.unsells;
					}else{
						that.$toast(res.errmsg);
					}
				})
			},
			loadMore() {
				var that = this;
				this.page = that.page + 1; // 增加分页
				this.isMoreLoading = true; // 设置加载更多中
				this.isLoading = true; // 设置加载更多中
				if(this.page > this.totalPages) { // 超过了分页
					this.isLoading = false; // 显示没有更多了
					this.isMoreLoading = false; // 关闭加载中
					return false
				}
				// 做个缓冲
				setTimeout(() => {
					this.getProjectInfo('loadMore')
				}, 500)

			},
			getProjectInfo(type) {
				if(type != "loadMore") {
					Indicator.open();
				}
				var that = this;
				that.$http({
					method: 'post',
					url: that.$url + 'order/queryUnPayments.options',
					data: {
						orderStatus: 'all',
						page: that.page,
						size: that.size
					}
				}).then(function(response) {
					var response = response.data;
					if(response.errno == 0) {
						if(type == 'loadMore') {
							that.orderList = that.orderList.concat(response.data.data);
						} else {
							Indicator.close();
							that.orderList = response.data.data;
							that.totalPages = response.data.totalPages;
							if(that.orderList.length == 0) {
								that.show = true;
							}
						}
					} else {
						that.$toast(response.errmsg);
					}
					that.isMoreLoading = false;
				})
			}

		}
	}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
	.order .btn {
		display: inline-block;
		height: auto;
		padding: .09rem .19rem;
		font-size: .26rem;
		color: #666666;
		-webkit-border-radius: 2rem;
		background-color: initial;
		border: 1px solid #d8d8d8;
		margin-left: .2rem;
	}
	
	.order .btn.active {
		color: #ef7c2c;
		border: 1px solid #ef7c2c;
	}
	
	.timg {
		width: .5rem;
		margin-right: .2rem;
	}
	
	.loading {
		font-size: .28rem;
		text-align: center;
		margin-top: .4rem;
	}
	
	.loading .lon {
		position: relative;
		top: -.12rem;
	}
	
	.resetbtn {
		display: inline-block !important;
		height: auto !important;
		padding: .09rem .19rem !important;
		font-size: .26rem !important;
		color: #ef7c2c !important;
		-webkit-border-radius: 2rem !important;
		background-color: initial !important;
		border: 1px solid #ef7c2c !important;
	}
	
	.nav_list {
		font-size: .29rem;
		background-color: #fff;
		overflow: hidden;
		padding: .2rem 0;
		position: fixed;
		width: 7.5rem;
		top: 0;
		z-index: 2;
	}
	
	.nav_list li {
		float: left;
		width: 20%;
		font-size: .3rem;
		color: #666666;
	}
	
	.nav_list li span {
		padding: .15rem 0;
	}
	
	.nav_list li .list_choice {
		border-bottom: .04rem solid #ef7c2c;
		color: #ef7c2c;
	}
	
	.order .b {
		height: .81rem;
		margin-left: .3125rem;
		padding-right: .3125rem;
		padding-top: .27rem;
		border-top: 1px solid #f4f4f4;
		font-size: .26rem;
		color: #3b3c3c;
	}
	
	.order .b .l {
		float: left;
	}
	
	.order .b .r {
		float: right;
		font-size: .26rem !important;
		color: #ef7c2c !important;
	}
	
	.order {
		display: block;
		margin-top: .20rem;
		background: #fff;
	}
	
	.order .h {
		height: .833rem;
		line-height: .833rem;
		margin-left: .3125rem;
		padding-right: .3125rem;
		border-bottom: 1px solid #f4f4f4;
		font-size: .30rem;
		color: #333;
	}
	
	.order .h .l {
		float: left;
		font-size: .26rem;
	}
	
	.order .h .r {
		float: right;
		color: #ef7c2c;
		font-size: .26rem;
	}
	
	.noData {
		font-size: .29rem;
		margin-top: 1.4rem;
	}
	
	.mt9 {
		margin-bottom: .9rem;
	}
</style>