<template>
 	<div class="collect-list">
 		<!--公用头部-->
  		<headbar :headFont = "headFont"></headbar>
  		
    <router-link :to="'/pages/category/goods?id='+item.id" class="item mt88" v-for="item in collectList" >
      <img class="img" :src="item.list_pic_url"/>
      <div class="info">
        <div class="name">{{item.name}}</div>
        <!--<div class="subtitle">{{item.goods_brief}}</div>-->
        <div class="price">￥{{item.market_price}}</div>
      </div>
    </router-link>
  </div>
</template>

<script>
	import { MessageBox } from 'mint-ui';
	import headbar from '@/components/headbar.vue';
		
export default {
  name: 'coupon',
    components:{headbar},
  data () {
    return {
    	headFont:'我的收藏',
    	collectList:[]
    }
  },
  mounted(){
  	var that = this;    
    	that.$http({
	        method: 'post',
	        url:that.$url+ 'collect/list',
	        params:{typeId:0}
    	}).then(function (response) {
      		  		response = {"errno":0,"data":[{"id":99,"user_id":28,"value_id":1181000,"add_time":1540456127,"is_attention":0,"type_id":0,"name":"母亲节礼物-舒适安睡组合","list_pic_url":"http://yanxuan.nosdn.127.net/1f67b1970ee20fd572b7202da0ff705d.png","goods_brief":"安心舒适是最好的礼物","retail_price":"2598.00","market_price":2857.80,"product_market_price":0},{"id":99,"user_id":28,"value_id":1181000,"add_time":1540456127,"is_attention":0,"type_id":0,"name":"母亲节礼物-舒适安睡组合","list_pic_url":"http://yanxuan.nosdn.127.net/1f67b1970ee20fd572b7202da0ff705d.png","goods_brief":"安心舒适是最好的礼物","retail_price":"2598.00","market_price":2857.80,"product_market_price":0}],"errmsg":"执行成功"};
    		if(response.data.errno == '401' || response.data.errno == '请先登录'){
    			MessageBox({
					  title: ' ',
					  message: '请先登录 ',
					  showCancelButton: true
					});
    			return false;
    		}else{
    			console.log(response)
    			that.collectList = response.data;
    		}
		  })
  },
  methods:{
  	
  }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
.collect-list{
  width: 100%;
  height: auto;
  overflow: hidden;
  background: #fff;
  padding-left: .30rem;
  border-top: 1px solid #e1e1e1;
}

.item{
  display: block;
  height: 1.4rem;
  width: 7.20rem;
  background: #fff;
  padding: .30rem .30rem .30rem 0;
  border-bottom: 1px solid #e1e1e1;
}

.item:last-child{
  border-bottom: 1px solid #fff;
}

.item .img{
  float: left;
  width: 1.50rem;
  height: 1.50rem;
}

.item .info{
  float: right;
  width: 5.40rem;
  height: 1.50rem;
  padding-left: .20rem;
  text-align: left;
}

.item .info .name{
  font-size: .28rem;
  color: #333;
  line-height: .40rem;
}


.item .info .subtitle{
  margin-top: .08rem;
  font-size: .24rem;
  color: #888;
  line-height: .40rem;
}

.item .info .price{
  margin-top: .08rem;
  font-size: .28rem;
  color: #333;
  line-height: .40rem;
}

</style>
