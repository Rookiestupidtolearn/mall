var util = require('../../../utils/util.js');
var api = require('../../../config/api.js');
var app = getApp();
Page({
  data: {
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
      isDefault: 0
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
  },
  bindinputMobile(event) {
    let address = this.data.address;
    address.telNumber = event.detail.value;
    this.setData({
      address: address
    });
  },
  bindinputName(event) {
    let address = this.data.address;
    address.userName = event.detail.value;
    this.setData({
      address: address
    });
  },
  bindinputAddress (event){
    let address = this.data.address;
    address.detailInfo = event.detail.value;
    this.setData({
      address: address
    });
  },
  bindIsDefault(){
    let address = this.data.address;
    address.isDefault = !address.isDefault;
    this.setData({
      address: address
    });
  },
  getAddressDetail() {
    let that = this;
    util.request(api.AddressDetail, { id: that.data.addressId }).then(function (res) {
      if (res.errno === 0) {
        if(res.data){
            that.setData({
                address: res.data
            });
        }
      }
    });
  },
  setRegionDoneStatus() {
    let that = this;
    let doneStatus = that.data.selectRegionList.every(item => {
      return item.third_code != 0;
    });

    that.setData({
      selectRegionDone: doneStatus
    })

  },
  chooseRegion() {
    let that = this;
    this.setData({
      openSelectRegion: !this.data.openSelectRegion
    });
    //设置区域选择数据
    let address = this.data.address;
    if (address.province_id > 0 && address.city_id > 0 && address.district_id > 0) {
      let selectRegionList = this.data.selectRegionList;
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
      this.setData({
        selectRegionList: selectRegionList,
        regionType: 3
      });
      this.getRegionList(address.city_id);
    } else {
      this.setData({
        selectRegionList: [
          { third_code: 0, name: '省份', parent_id: 1, type: 1 },
          { third_code: 0, name: '城市', parent_id: 1, type: 2 },
          { third_code: 0, name: '区县', parent_id: 1, type: 3 },
          { third_code: 0, name: '乡镇', parent_id: 1, type: 4 }
        ],
        regionType: 1
      })
      this.getRegionList(0);
    }

    this.setRegionDoneStatus();

  },
  onLoad: function (options) {
    // 页面初始化 options为页面跳转所带来的参数
    console.log(options)
    if (options.id) {
      this.setData({
        addressId: options.id
      });
      this.getAddressDetail();
    }

    this.getRegionList(0);

  },
  onReady: function () {

  },
  selectRegionType(event) {
    let that = this;
    let regionTypeIndex = event.target.dataset.regionTypeIndex;
    let selectRegionList = that.data.selectRegionList;
    console.log("regionTypeIndex:"+regionTypeIndex);
    
    //判断是否可点击
    if (selectRegionList[regionTypeIndex].third_code == 0){
      return false;
    }

    this.setData({
      regionType: regionTypeIndex + 1
    })
    
    let selectRegionItem = selectRegionList[regionTypeIndex];

    this.getRegionList(selectRegionItem.parent_id);

    this.setRegionDoneStatus();

  },
  selectRegion(event) {
    let that = this;
    let regionIndex = event.target.dataset.regionIndex;
    let regionItem = this.data.regionList[regionIndex];
    let regionType = regionItem.type;
    let selectRegionList = this.data.selectRegionList;
    selectRegionList[regionType - 1] = regionItem;


    if (regionType != 4) {
      this.setData({
        selectRegionList: selectRegionList,
        regionType: regionType + 1
      })
      this.getRegionList(regionItem.third_code);
    } else {
      this.setData({
        selectRegionList: selectRegionList
      })
    }

    //重置下级区域为空
    selectRegionList.map((item, index) => {
    
      if (index > regionType - 1) {
        item.third_code = 0;
        // item.name = index == 1 ? '城市' : '区县';
        if(index == 1){
        	item.name ='城市';
        }
        if(index == 2){
        	item.name ='区县';
        }
        if(index == 3){
        	item.name ='乡镇';
        }
        item.parent_id = 0;
      }
      return item;
    });

    this.setData({
      selectRegionList: selectRegionList
    })


    that.setData({
      regionList: that.data.regionList.map(item => {

        //标记已选择的
        if (that.data.regionType == item.type && that.data.selectRegionList[that.data.regionType - 1].third_code == item.third_code) {
          item.selected = true;
        } else {
          item.selected = false;
        }

        return item;
      })
    });

    this.setRegionDoneStatus();

  },
  doneSelectRegion() {
    if (this.data.selectRegionDone === false) {
      return false;
    }

    let address = this.data.address;
    let selectRegionList = this.data.selectRegionList;
    address.province_id = selectRegionList[0].third_code;
    address.city_id = selectRegionList[1].third_code;
    address.district_id = selectRegionList[2].third_code;
    if(selectRegionList[3] != null){
    	address.town_id = selectRegionList[3].third_code;
    	address.town_name = selectRegionList[3].name;
    }
    address.province_name = selectRegionList[0].name;
    address.city_name = selectRegionList[1].name;
    address.district_name = selectRegionList[2].name;
    address.full_region = selectRegionList.map(item => {
      return item.name;
    }).join('');

    this.setData({
      address: address,
      openSelectRegion: false
    });

  },
  cancelSelectRegion() {
    this.setData({
      openSelectRegion: false,
      regionType: this.data.regionDoneStatus ? 3 : 1
    });

  },
  getRegionList(regionId) {
    let that = this;
    let regionType = that.data.regionType;
    let selectRegionList = that.data.selectRegionList;
    util.request(api.RegionList, { parentId: regionId }).then(function (res) {
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
    wx.navigateBack({
      url: '/pages/ucenter/address/address',
    })
  },
  saveAddress(){
    console.log(this.data.address)
    let address = this.data.address;

    if (address.userName == '') {
      util.showErrorToast('请输入姓名');

      return false;
    }

    if (address.telNumber == '') {
      util.showErrorToast('请输入手机号码');
      return false;
    }

    if (address.district_id == 0) {
      util.showErrorToast('请输入省市区');
      return false;
    }

    if (address.detailInfo == '') {
      util.showErrorToast('请输入详细地址');
      return false;
    }

    let that = this;
    util.request(api.AddressSave, { 
      id: address.id,
      userName: address.userName,
      telNumber: address.telNumber,
      province_id: address.province_id,
      city_id: address.city_id,
      district_id: address.district_id,
      is_default: address.isDefault,
      provinceName: address.province_name,
      cityName: address.city_name,
      countyName: address.district_name,
      townName:address.town_name,
      province:address.province_id,
      city:address.city_id,
      district:address.district_id,
      town:address.town_id,
      detailInfo: address.detailInfo,
    },'post','application/json').then(function (res) {
      if (res.errno === 0) {
        wx.navigateBack({
          url: '/pages/ucenter/address/address',
        })
      }
    });

  },
  onShow: function () {
    // 页面显示

  },
  onHide: function () {
    // 页面隐藏

  },
  onUnload: function () {
    // 页面关闭

  }
})