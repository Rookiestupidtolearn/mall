<template>
 	<div class="container">
 		<!--公用头部-->
  		<!--<headbar :headFont = "headFont"></headbar>-->
  		
	    <div class="address-list "  v-if="addressListC.length > 0" >
	        <div class="item" v-for="item in addressListC" @click="addressAddOr(item.id)" >
	            <div class="l">
	                <div class="name">{{item.userName}}</div>
	                <div class="default" v-if="item.isDefault">默认</div>
	            </div>
	            <div class="c">
	                <div class="mobile">{{item.telNumber}}</div>
	                <div class="address">{{item.full_region+item.detailInfo}}</div>
	            </div>
	            <div class="r" >
	                <img class="del" src="../../../static/images/del-address.png"  @click.stop="deleteAddress(item.id)"/>
	            </div>
	        </div>
	    </div>
	    <div class="empty-view " v-if="addressListC.length <= 0">
	      <img class="icon" src="http://yanxuan.nosdn.127.net/hxm/yanxuan-wap/p/20161201/style/img/icon-normal/noAddress-26d570cefa.png"/>
	      <p class="text">收货地址在哪里</p>
	    </div>
	    <div class="add-address" @click="addressAddOr('1')" data-address-id="0">新建</div>
	    <returnhome :scrollshow = "scrollshow"></returnhome>
	</div>
</template>

<script>
import { MessageBox } from 'mint-ui';
import returnhome from '@/components/returnHome'
//import headbar from '@/components/headbar.vue';
		
export default {
	name: 'addressList2',
	 components:{returnhome},
  data () {
    return {
//  	headFont:'地址管理',
		scrollshow:true,
    	addressListC:[],
    	deleteList:[]
    }
  },
  mounted(){
  	this.showaddressListC();
  },
  methods:{
  		showaddressListC(){
  			var that = this;    
			that.$http({
		        method: 'post',
		        url:that.$url+ 'address/list',
			}).then(function (response) {
				var response=response.data;
				if(response.errno != 401){
					that.addressListC = response.data;
				}
			  })
  	},
		addressAddOr(setId){
//			alert('个人中心-地址列表');
			this.$router.push({ path: 'addressAdd', query: { id: setId }})
		},
		deleteAddress(deleteId){
			MessageBox({
			  title: ' ',
			  message: '确定要删除地址? ',
			  showCancelButton: true
			}).then(action => {
				if(action == 'confirm'){
					var that = this;
					that.$cookie.delCookie('addressId');
					that.$http({
				        method: 'post',
				        url:that.$url+ 'address/delete.options',
				        data:{id:deleteId}
			    	}).then(function (res) {
			    		if( res.errno = '0'){
			    			that.showaddressListC();
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
    overflow: hidden;
    text-align: left;
    float:left;
}

.address-list .name{
    margin-top: .45rem;
    width: 1.55rem;
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
    width: 4.8rem;
    text-overflow: ellipsis;
    white-space: nowrap;
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
