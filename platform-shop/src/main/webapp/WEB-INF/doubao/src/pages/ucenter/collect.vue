<template>
 	<div class="collect-list">
 		<!--公用头部-->
  		<!--<headbar :headFont = "headFont"></headbar>-->
  	<div >
  		<!--v-if="collectList"-->
	    <router-link :to="'/pages/goods/goods?id='+item.value_id" class="item" v-for="item in collectList" >
	      <img class="img" :src="item.list_pic_url"/>
	      <div class="info">
	        <div class="name">{{item.name}}</div>
	        <!--<div class="subtitle">{{item.goods_brief}}</div>-->
	        <div class="price">￥{{item.market_price}}</div>
	      </div>
	    </router-link>
    </div>
    <!--<div v-else class="noData" :data-c="collectList">
	   	没有更多数据了
    </div>-->
  </div>
</template>

<script>
	import { MessageBox } from 'mint-ui';
//	import headbar from '@/components/headbar.vue';
		
export default {
  name: 'coupon',
//  components:{headbar},
  data () {
    return {
//  	headFont:'我的收藏',
    	collectList:[]
    }
  },
  mounted(){
  	var that = this;    
    	that.$http({
	        method: 'post',
	        url:that.$url+ 'collect/list.options',
	        data:{typeId:0}
    	}).then(function (res) {
    			that.collectList = res.data.data;
		  })
  },
  methods:{
  	
  }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
.noData{
	font-size:.29rem;
	text-align: center;
}
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
