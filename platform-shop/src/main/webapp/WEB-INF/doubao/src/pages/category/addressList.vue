<template>
 	<div class="container">
 		<!--公用头部-->
  		<!--<headbar :headFont = "headFont"></headbar>-->
  		
	    <div class="address-list"  v-if="addressList.length > 0" >
	        <div class="item" v-for="item in addressList" @click="selectAddress(item.id)">
	            <div class="l">
	                <div class="name">{{item.userName}}</div>
	                <div class="default" v-if="item.isDefault">默认</div>
	            </div>
	            <div class="c">
	                <div class="mobile">{{item.telNumber}}</div>
	                <div class="address">{{item.full_region+item.detailInfo}}</div>
	            </div>
	            <div class="r" >
	                <img class="del" @click.stop="addressAddOrUpdate(item.id)"  src="http://yanxuan.nosdn.127.net/hxm/yanxuan-wap/p/20161201/style/img/icon-normal/address-edit-7fee7b0d63.png"/>
	            </div>
	        </div>
	    </div>
	    <div class="empty-view" v-if="addressList.length <= 0">
	      <img class="icon" src="http://yanxuan.nosdn.127.net/hxm/yanxuan-wap/p/20161201/style/img/icon-normal/noAddress-26d570cefa.png"/>
	      <p class="text">收货地址在哪里</p>
	    </div>
	    <div class="add-address" @click="addressAddOrUpdate('0')" data-address-id="0">新建</div>
	</div>
</template>

<script>
import { MessageBox } from 'mint-ui';
//import headbar from '@/components/headbar.vue'
		
export default {
	name: 'addressList',
//	components:{headbar},
  data () {
    return {
//  	headFont:'地址列表',
    	addressList:[],
    	deleteList:[]
    }
  },
  created(){
  	 this.showaddressList();
  },
  methods:{
		showaddressList:function(){
	  		var that = this;    
	    	that.$http({
		        method: 'post',
		        url:that.$url+ 'address/list',
	    	}).then(function (response) {
	    			that.addressList = response.data.data;
	    			console.log(response.data.data)
			  })
	  	},
		addressAddOrUpdate:function(setId){
		     this.$router.push('/pages/category/addressList?id=' + setId);
		},
		selectAddress(setId){
		    //选择该收货地址
//		    var _day = 60 * 60 * 24 *1;
  			this.$cookie.setCookie('addressId',setId); //缓存地址id
		    this.$router.push('/pages/category/checkout');
		},
		 addressAddOrUpdate (setId) {
			this.$router.push('/pages/category/addressAdd?id=' + setId);
		  },
		deleteAddress:function(deleteId){
			MessageBox({
			  title: ' ',
			  message: '确定要删除地址? ',
			  showCancelButton: true
			}).then(action => {
				if(action == 'confirm'){
					var that = this;
					that.$http({
				        method: 'post',
				        url:that.$url+ 'address/delete',
				        params:{id:deleteId}
			    	}).then(function (res) {
			    		res = {"errno":0,"data":"","errmsg":"执行成功"};
			    		console.log(res.data);
			    		if( res.errno = '0'){
			    			that.showaddressList();
			    			console.log('用户点击确定');
			    		}
			        });
				}
			})
		}
	}
}
</script>

 <!--Add "scoped" attribute to limit CSS to this component only--> 
<style scoped>
.address-list{
    padding-left: .3125rem;
    background: #fff url(http://yanxuan.nosdn.127.net/hxm/yanxuan-wap/p/20161201/style/img/icon-normal/address-bg-bd30f2bfeb.png) 0 0 repeat-x;
    background-size: auto .105rem;
    margin-bottom: .90rem;
}

.address-list .item{
    height: 1.5655rem;
    border-bottom: 1px solid #DCD9D9;
}

.address-list .l{
    width: 1.55rem;
    height: .80rem;
    overflow: hidden;
    text-align: left;
    float:left;
    margin-top: .45rem;
}

.address-list .name{
    width: 1.55rem;
    height: .43rem;
    font-size: .29rem;
    color: #333;
    text-overflow: ellipsis;
    white-space: nowrap;
    overflow: hidden;
}

.address-list .default{
    width: .625rem;
    height: .33rem;
    line-height:.28rem;
    text-align: center;
    font-size: .20rem;
    color: #b4282d;
    border: 1px solid #b4282d;
    visibility: visible;
}
.address-list .c{
    height: auto;
    overflow: hidden;
    float:left;
    margin-top: .45rem;
    text-align: left;
}
.address-list .mobile{

    height: .29rem;
    font-size: .29rem;
    line-height: .29rem;
    overflow: hidden;
    color: #333;
    margin-bottom: .0625rem;
}
.address-list .address{
    height: .37rem;
    font-size: .25rem;
    line-height: .37rem;
    overflow: hidden;
    color: #666;
}
.address-list .r{
    width: .52rem;
    height: auto;
    overflow: hidden;
    margin-right: .165rem;
    float: right;
    margin-top: 0.5rem;
}
.address-list .del{
    display: block;
    width: .52rem;
    height: .52rem;
}
.add-address{
    background: #b4282d;
    text-align: center;
    width: 100%;
    height: .99rem;
    line-height: .99rem;
    position: fixed;
    border-radius: 0;
    border: none;
    color: #fff;
    font-size: .29rem;
    bottom: 0;
    left:0;
}
.empty-view{
  height: 100%;
  width: 100%;
  font-size: 0;
}
.empty-view .icon{
  height: 2.48rem;
  width: 2.58rem;
  margin-bottom: .10rem;
  margin-top: 3.5rem;
}
.empty-view .text{
  width: auto;
  font-size: .28rem;
  line-height: .35rem;
  color: #999;
}
</style>
