<template>
 	<div class="container">
 		<div class="add-address">
		   <div class="add-form">
		        <div class="form-item">
		            <input class="input"  placeholder="姓名" v-model="address.userName" />
		        </div>
		        <div class="form-item">
		            <input class="input"    v-model="address.telNumber" oninput="if(value.length > 11)value = value.slice(0, 11)" type="number" placeholder="手机号码"/>
		        </div>
		        <div class="form-item">
		            <input class="input" v-model="address.full_region" readonly @click="chooseRegion" placeholder="省份、城市、区县"/>
		        </div>
		        <div class="form-item">
		            <input class="input"  v-model="address.detailInfo" placeholder="详细地址, 如街道、楼盘号等"/>
		        </div>
		        <div class="form-default">
		            <span @click="bindIsDefault"  class="default-input" :class=" address.is_default == 1 ? 'selected' : ''">设为默认地址</span>
		        </div>
		    </div>
		
		    <div class="btns">
		        <button class="cannel" @click="cancelAddress">取消</button>
		        <button class="save" @click="saveAddress">保存</button>
		    </div>
		
		    <div class="region-select" v-if="openSelectRegion">
		      <div class="hd">
		        <div class="region-selected">
		          <div class="item" bindtap="selectRegionType" v-for="item in selectRegionList" >{{item.name}}</div>
		          <!--:class="item {{item.id == 0 ? 'disabled' : ''}} {{(regionType -1) === index ? 'selected' : ''}}"--> 
		        </div>
		        <div class="done"  :class="selectRegionDone ? '' : 'disabled'" bindtap="doneSelectRegion">确定</div>
		      </div>
		      <div class="bd">
		        <!--<scroll-div scroll-y="true" class="region-list">
		          <div class="item {{item.selected ? 'selected' : ''}}" bindtap="selectRegion" data-region-index="{{index}}" wx:for="{{regionList}}" wx:key="{{item.third_code}}">{{item.name}}</div>
		        </scroll-div>-->
		      </div>
		    </div>
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
	    	address: {
		      id:0,
		      province_id: 0,
		      city_id: 0,
		      district_id: 0,
		      town_id:0,
		      address: '',
		      full_region: '',
		      userName: '',
		      telNumber: '',
		      is_default: 0,
		    },
		    addressId: 0,
		    openSelectRegion: false,
		    selectRegionList: [
		      { third_code: 0, name: '省份', parent_id: 1, type: 1 },
		      { third_code: 0, name: '城市', parent_id: 1, type: 2 },
		      { third_code: 0, name: '区县', parent_id: 1, type: 3 },
		      { third_code: 0, name: '乡镇', parent_id: 1, type: 4 }
		    ],
		    regionType: 1,
		    regionList: [],
		    selectRegionDone: false
	    }
	  },
	  mounted(){
	  		let options = this.$route.query.id;
	  		console.log(options);
		    if (options) {
		      this.addressId = options;
		      this.getAddressDetail();
		    }
		
		    this.getRegionList(0);
	 },
	 methods:{
	 	bindIsDefault(){
	 		let address = this.address;
		    address.is_default = !address.is_default;
		    this.address = address;
	 	},
	 	chooseRegion(){
	 		let that = this;
		     that.openSelectRegion =  !this.data.openSelectRegion;
		    //设置区域选择数据
		    let address = this.address;
		    if (address.province_id > 0 && address.city_id > 0 && address.district_id > 0) {
		      let selectRegionList = this.selectRegionList;
		      selectRegionList[0].third_code = address.province_id;
		      selectRegionList[0].name = address.province_name;
		      selectRegionList[0].parent_id = 0;
		
		      selectRegionList[1].third_code = address.city_id;
		      selectRegionList[1].name = address.city_name;
		      selectRegionList[1].parent_id = address.province_id;
		
		      selectRegionList[2].third_code = address.district_id;
		      selectRegionList[2].name = address.district_name;
		      selectRegionList[2].parent_id = address.city_id;
		      if(selectRegionList[3] != null){
		    	  selectRegionList[3].third_code = address.town_id;
		          selectRegionList[3].name = address.town_name;
		          selectRegionList[3].parent_id = address.district_id;
		      }
		      this.selectRegionList = selectRegionList;
		      this.regionType =  3;
		      this.getRegionList(address.city_id);
		    } else {
		        this.selectRegionList = [
		          { third_code: 0, name: '省份', parent_id: 1, type: 1 },
		          { third_code: 0, name: '城市', parent_id: 1, type: 2 },
		          { third_code: 0, name: '区县', parent_id: 1, type: 3 },
		          { third_code: 0, name: '乡镇', parent_id: 1, type: 4 }
		        ];
		      this.regionType = 1;
		      this.getRegionList(0);
		    }
		
		    this.setRegionDoneStatus();

	 	},
	 	setRegionDoneStatus(){
	 		let that = this;
		    let doneStatus = that.selectRegionList.every(item => {
		      return item.third_code != 0;
		    });
		
		    that.selectRegionDone =  doneStatus;
	 	},
	 	getRegionList(regionId) {
		    let that = this;
		    let regionType = that.regionType;
		    let selectRegionList = that.selectRegionList;
		    that.$http({
		    	method:'post',
		    	url:that.$url + 'region/list',
		    	params:{parentId: regionId }
		    }).then(function (res) {
		      if (res.errno === 0) {
		    	 if(res.data != 0){
		         if (regionType == 1){
		           that.setData({
		             selectRegionList: [
		               { third_code: selectRegionList[0].third_code, name: selectRegionList[0].name, parent_id: selectRegionList[0].parent_id,      type: selectRegionList[0].type },
		               { third_code: 0, name: '城市', parent_id: 1, type: 2 },
		               { third_code: 0, name: '区县', parent_id: 1, type: 3 },
		               { third_code: 0, name: '乡镇', parent_id: 1, type: 4 }
		             ]
		           })
		         } else if (regionType == 2){
		           that.setData({
		             selectRegionList: [
		               { third_code: selectRegionList[0].third_code, name: selectRegionList[0].name, parent_id: selectRegionList[0].parent_id, type: selectRegionList[0].type },
		               { third_code: selectRegionList[1].third_code, name: selectRegionList[1].name, parent_id: selectRegionList[1].parent_id, type: selectRegionList[1].type },
		               { third_code: 0, name: '区县', parent_id: 1, type: 3 },
		               { third_code: 0, name: '乡镇', parent_id: 1, type: 4 }
		             ]
		           })
		         } else if (regionType == 3){
		           that.setData({
		             selectRegionList: [
		               { third_code: selectRegionList[0].third_code, name: selectRegionList[0].name, parent_id: selectRegionList[0].parent_id, type: selectRegionList[0].type },
		               { third_code: selectRegionList[1].third_code, name: selectRegionList[1].name, parent_id: selectRegionList[1].parent_id, type: selectRegionList[1].type },
		               { third_code: selectRegionList[2].third_code, name: selectRegionList[2].name, parent_id: selectRegionList[2].parent_id, type: selectRegionList[2].type },
		               { third_code: 0, name: '乡镇', parent_id: 1, type: 4 }
		             ]
		           })
		         }
		    		 that.setData({
		            regionList: res.data.map(item => {
		              //标记已选择的
		              if (regionType == item.type && that.data.selectRegionList[regionType - 1].third_code == item.third_code) {
		                item.selected = true;
		              } else {
		                item.selected = false;
		              }
		              return item;
		            })
		          });
		    	 }else{
		         if(that.data.regionType == 4){
		           that.setData({
		             selectRegionDone:true,
		             selectRegionList: [
		               { third_code: selectRegionList[0].third_code, name: selectRegionList[0].name, parent_id: selectRegionList[0].parent_id, type: selectRegionList[0].type },
		               { third_code: selectRegionList[1].third_code, name: selectRegionList[1].name, parent_id: selectRegionList[1].parent_id, type: selectRegionList[1].type },
		               { third_code: selectRegionList[2].third_code, name: selectRegionList[2].name, parent_id: selectRegionList[2].parent_id, type: selectRegionList[2].type }   
		             ]
		           })
		         }
		    	 }
		      }
		    });
		},
	 	cancelAddress(){
	 		 this.$router.push('/pages/category/addressList');
	 	},
	 	getAddressDetail() {
		    let that = this;
		    that.$http({
		    	method:'post',
		    	url:that.$url + 'address/detail',
		    	params:{
		    		id: that.addressId
		    	}
		    }).then(function (res) {
//		    	res = {"errno":0,"data":{"id":105,"userId":28,"userName":"xt","telNumber":"121212121212121","postalCode":null,"nationalCode":null,"province":"2","provinceName":"上海","city":"2815","cityName":"长宁区","county":"51975","countyName":"城区","town":"","townName":null,"detailInfo":"sdfsdfsdsdfsdfsd","isDefault":1,"createTime":"2018-11-15","isDelete":null,"full_region":"上海长宁区城区"},"errmsg":"执行成功"};
		      if (res.errno == 0) {
		        if(res.data){
		             that.address = res.data;
		        }
		      }
		    });
		},
	 	saveAddress(){
//	 		console.log(this.address);
		    let address = this.address;
		
		    if (address.userName == '') {
		      Toast('请输入姓名');
		      return false;
		    }
		
		    if (address.telNumber == '') {
		      Toast('请输入手机号码');
		      return false;
		    }
		
		    if (address.district_id == 0) {
		      Toast('请输入省市区');
		      return false;
		    }
		
		    if (address.detailInfo == '') {
		      Toast('请输入详细地址');
		      return false;
		    }
		
		    let that = this;
		    that.$http({
		    	method:'post',
		    	url:that.$url + 'address/save',
		    	params:{
				      id: address.id,
				      userName: address.userName,
				      telNumber: address.telNumber,
				      province_id: address.province_id,
				      city_id: address.city_id,
				      district_id: address.district_id,
				      is_default: address.is_default,
				      provinceName: address.province_name,
				      cityName: address.city_name,
				      countyName: address.district_name,
				      townName:address.town_name,
				      province:address.province_id,
				      city:address.city_id,
				      district:address.district_id,
				      town:address.town_id,
				      detailInfo: address.detailInfo
		    	}
		    }).then(function (res) {
		      if (res.errno === 0) {
		        this.$router.push('/pages/category/addressList');
		      }
		    });

	 	}
	 }
	 
	 
	}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
	input::-webkit-input-placeholder {
         color: #999;
        font-size: .29rem;
}
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
    height: .7rem;
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
    text-align: left;
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
    height: 1.00rem;
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
