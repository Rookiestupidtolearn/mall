<template>
 	<div class="container">
 		<div class="add-address">
		    <!--<div class="add-form">-->
		        <!--<div class="form-item">
		            <input class="input" bindinput="bindinputName" placeholder="姓名" value="{{address.userName}}" auto-focus/>
		        </div>
		        <div class="form-item">
		            <input class="input" bindinput="bindinputMobile" value="{{address.telNumber}}" maxlength="15" type="number" placeholder="手机号码"/>
		        </div>
		        <div class="form-item">
		            <input class="input" value="{{address.full_region}}" disabled="true" bindtap="chooseRegion" placeholder="省份、城市、区县"/>
		        </div>
		        <div class="form-item">
		            <input class="input" bindinput="bindinputAddress" value="{{address.detailInfo}}" placeholder="详细地址, 如街道、楼盘号等"/>
		        </div>
		        <div class="form-default">
		            <text bindtap="bindIsDefault" class="default-input {{address.is_default == 1 ? 'selected' : ''}}">设为默认地址</text>
		        </div>
		    </div>-->
		
		    <!--<div class="btns">
		        <button class="cannel" bindtap="cancelAddress">取消</button>
		        <button class="save" bindtap="saveAddress">保存</button>
		    </div>-->
		
		    <!--<div class="region-select" wx:if="{{openSelectRegion}}">
		      <div class="hd">
		        <div class="region-selected">
		          <div class="item {{item.id == 0 ? 'disabled' : ''}} {{(regionType -1) === index ? 'selected' : ''}}" bindtap="selectRegionType" data-region-type-index="{{index}}" wx:for="{{selectRegionList}}" wx:key="{{item.third_code}}">{{item.name}}</div>
		        </div>
		        <div class="done {{selectRegionDone ? '' : 'disabled'}}" bindtap="doneSelectRegion">确定</div>
		      </div>
		      <div class="bd">
		        <scroll-div scroll-y="true" class="region-list">
		          <div class="item {{item.selected ? 'selected' : ''}}" bindtap="selectRegion" data-region-index="{{index}}" wx:for="{{regionList}}" wx:key="{{item.third_code}}">{{item.name}}</div>
		        </scroll-div>
		      </div>
		    </div>-->
		</div>
 	</div>
</template>

<script>
	import { Toast } from 'mint-ui';
	import { MessageBox } from 'mint-ui';
		
	export default {
	  name: 'addressAdd',
	  data () {
	    return {
//	    	address: {
//		      id:0,
//		      province_id: 0,
//		      city_id: 0,
//		      district_id: 0,
//		      town_id:0,
//		      address: '',
//		      full_region: '',
//		      userName: '',
//		      telNumber: '',
//		      is_default: 0
//		    },
//		    addressId: 0,
//		    openSelectRegion: false,
//		    selectRegionList: [
//		      { third_code: 0, name: '省份', parent_id: 1, type: 1 },
//		      { third_code: 0, name: '城市', parent_id: 1, type: 2 },
//		      { third_code: 0, name: '区县', parent_id: 1, type: 3 },
//		      { third_code: 0, name: '乡镇', parent_id: 1, type: 4 }
//		    ],
//		    regionType: 1,
//		    regionList: [],
//		    selectRegionDone: false
	    }
	  },
	  mounted(){
	    	
		 },
		 methods:{
		 	
		 }
	}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<!--v-html  的标签 加样式 用 >>>-->
<style scoped>
.add-address .add-form{
    background: #fff;
    width: 100%;
    height: auto;
    overflow: hidden;
}

.add-address .form-item{
    height: 1.16rem;
    padding-left: .3125rem;
    border-bottom: 1px solid #d9d9d9;
    display: flex;
    align-items: center;
    padding-right: .3125rem;
}

.add-address .input{
    flex: 1;
    height: .44rem;
    line-height: .44rem;
    overflow: hidden;
}

.add-address .form-default{
    border-bottom: 1px solid #d9d9d9;
    height: .96rem;
    background: #fafafa;
    padding-top: .28rem;
    font-size: .28rem;
}

.default-input{
    margin: 0 auto;
    display: block;
    width: 2.40rem;
    height: .40rem;
    padding-left: .50rem;
    line-height: .40rem;
    background: url(http://yanxuan.nosdn.127.net/hxm/yanxuan-wap/p/20161201/style/img/sprites/checkbox-sed825af9d3-a6b8540d42.png) .001rem -4.48rem no-repeat;
    background-size: .38rem 4.86rem;
    font-size: .28rem;
}

.default-input.selected{
    background: url(http://yanxuan.nosdn.127.net/hxm/yanxuan-wap/p/20161201/style/img/sprites/checkbox-sed825af9d3-a6b8540d42.png) 0 -1.92rem no-repeat;
    background-size: .38rem 4.86rem;
}

.add-address .btns{
    position: fixed;
    bottom: 0;
    left: 0;
    overflow: hidden;
    display: flex;
    height: 100rem;
    width: 100%;
}

.add-address .cannel,.add-address .save{
    flex: 1;
    height: 1.00rem;
    text-align: center;
    line-height: 1.00rem;
    font-size: .28rem;
    color: #fff;
    border:none;
    border-radius: 0;
}

.add-address .cannel{
    background: #333;
}

.add-address .save{
    background: #b4282d;
}


.region-select{
  width: 100%;
  height: 6.00rem;
  background: #fff;
  position: fixed;
  z-index: 10;
  left:0;
  bottom: 0;
}

.region-select .hd{
  height: 1.08rem;
  width: 100%;
  border-bottom: 1px solid #f4f4f4;
  padding: .46rem .30rem 0 .30rem;
}

.region-select .region-selected{
  float: left;
  height: .60rem;
  display: flex;
}

.region-select .region-selected .item{
  max-width: 1.40rem;
  margin-right: .30rem;
  text-align: left;
  line-height: .60rem;
  height: 100%;
  color: #333;
  font-size: .28rem;
  overflow: hidden;
      text-overflow: ellipsis;
    white-space: nowrap;
}

.region-select .region-selected .item.disabled{
  color: #999;
}

.region-select .region-selected .item.selected{
  color: #b4282d;
}

.region-select .done{
  float: right;
  height: .60rem;
  width: .60rem;
  border: none;
  background: #fff;
  line-height: .60rem;
  text-align: center;
  color: #333;
  font-size: .28rem;
}

.region-select .done.disabled{
  color: #999;
}



.region-select .bd{
  height: 4.92rem;
  width: 100%;
  padding: 0 .30rem;
}

.region-select {
  height: auto;
  overflow: scroll;

}

.region-list{
  width: 100%;
  height: 100%;
  line-height: 1.04rem;
  text-align: left;
  color: #333;
  font-size: .28rem;
}

.region-select .item{
  width: 100%;
  height: 1.04rem;
  line-height: 1.04rem;
  text-align: left;
  color: #333;
  font-size: .28rem;
}

.region-select .region-list .item.selected{
  color: #b4282d;
}

.bg-mask{
  height: 100%;
  width: 100%;
  background: rgba(0, 0, 0, 0.4);
  position: fixed;
  top:0;
  left:0;
  z-index: 8;
}

</style>
