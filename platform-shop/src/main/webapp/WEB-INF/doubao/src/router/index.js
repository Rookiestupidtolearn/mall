import Vue from 'vue'
import Router from 'vue-router'

import home from '@/pages/home'  //首页
import classification from '@/pages/classification'  //分类
import shoppingcar from '@/pages/shoppingcar'  //购物车
import ucenter from '@/pages/ucenter'  //个人中心
import search from '@/pages/search/search'  //分类-搜索页面
import order from '@/pages/ucenter/order'  //个人中心-我的订单-全部
import order1 from '@/pages/ucenter/order1'  //个人中心-我的订单-待付款
import order2 from '@/pages/ucenter/order2'  //个人中心-我的订单-待收货
import order3 from '@/pages/ucenter/order3'  //个人中心-我的订单-已完成
import order4 from '@/pages/ucenter/order4'  //个人中心-我的订单-已取消
import coupon from '@/pages/ucenter/coupon'  //个人中心-我的优惠券
import collect from '@/pages/ucenter/collect'  //个人中心-我的收藏
import footprint from '@/pages/ucenter/footprint'  //个人中心-我的足迹
import accountSecurity from '@/pages/ucenter/accountSecurity'  //个人中心-账户安全
import helpCenter from '@/pages/ucenter/helpCenter'  //个人中心-帮助中心
import feedback from '@/pages/ucenter/feedback'  //个人中心-问题反馈
import mobile from '@/pages/ucenter/mobile'  //账户安全-绑定手机号
import categoryPages from '@/pages/category/category'  //首页-渠道分类
import goods from '@/pages/category/goods'  //商品详情
import orderDetail from '@/pages/ucenter/orderDetail'  //订单详情
import checkout from '@/pages/category/checkout'  //下单页面
import selCoupon from '@/pages/category/selCoupon'  //选择优惠券页面
import amountMoney from '@/pages/ucenter/amountMoney'  //资金明细
import register from '@/pages/register/register'  //注册页面
import klrule from '@/pages/xieyi/klrule'  //斗宝俱乐部“克拉”使用规则
import yhzcxy from '@/pages/xieyi/yhzcxy'  //斗宝俱乐部用户注册协议
import ysqxy from '@/pages/xieyi/ysqxy'  //隐私权协议
import namecard from '@/pages/ucenter/namecard'  //账户安全-姓名,身份认证
import account from '@/pages/ucenter/account'  //账户中心
import userservice from '@/pages/ucenter/userservice'  //客户服务
import successPay from '@/pages/payment/successPay'  //支付成功
import failPay from '@/pages/payment/failPay'  //支付失败
import logistics from '@/pages/ucenter/logistics'  //物流列表
import lookLogistics from '@/pages/ucenter/lookLogistics'  //物流详情
import addressList from '@/pages/ucenter/addressList'  //个人中心-地址管理
import addressAdd from '@/pages/ucenter/addressAdd'  //个人中心-添加地址
import addresscateList from '@/pages/category/addresscateList'  //购物车-地址管理
import addresscateAdd from '@/pages/category/addresscateAdd'  //购物车-添加地址
import bodylook from '@/pages/category/bodylook'  //商品详情-大家都在看
/*活动*/
import kelarule from '@/pages/activity/kelarule'  //克拉使用规则

Vue.use(Router)

//当页面路径是 /的时候 加载 component 为 home 的 vue 文件

export default new Router({
  routes: [
    {
      path: '/',
      name: 'home',
      component: home,
    },{
      path: '/pages/xieyi/klrule',
      name: 'klrule',
      component: klrule,
    },{
      path: '/pages/xieyi/yhzcxy',
      name: 'yhzcxy',
      component: yhzcxy,
    },{
      path: '/pages/xieyi/ysqxy',
      name: 'ysqxy',
      component: ysqxy,
    },{
      path: '/pages/category/bodylook',
      name: 'bodylook',
      component: bodylook
    },{
      path: '/pages/activity/kelarule',
      name: 'kelarule',
      component: kelarule
    },{
      path: '/pages/classification',
      name: 'classification',
      component: classification
    },{
      path: '/pages/shoppingcar',
      name: 'shoppingcar',
      component: shoppingcar
    },{
      path: '/pages/ucenter',
      name: 'ucenter',
      component: ucenter
    },{
      path: '/pages/ucenter/order',
      name: 'order',
      component: order
    },{
      path: '/pages/ucenter/coupon',
      name: 'coupon',
      component: coupon
    },{
      path: '/pages/ucenter/collect',
      name: 'collect',
      component: collect
    },{
      path: '/pages/ucenter/footprint',
      name: 'footprint',
      component: footprint
    },{
      path: '/pages/ucenter/addressList',
      name: 'addressList',
      component: addressList
    },{
      path: '/pages/ucenter/accountSecurity',
      name: 'accountSecurity',
      component: accountSecurity
    },{
      path: '/pages/ucenter/helpCenter',
      name: 'helpCenter',
      component: helpCenter
    },{
      path: '/pages/ucenter/feedback',
      name: 'feedback',
      component: feedback
    },{
      path: '/pages/ucenter/mobile',
      name: 'mobile',
      component: mobile
    },{
      path: '/pages/ucenter/search',
      name: 'search',
      component: search
    },{
      path: '/pages/category/category',
      name: 'categoryPages',
      component: categoryPages
    },{
      path: '/pages/goods/goods',
      name: 'goods',
      component: goods
    },{
      path: '/pages/ucenter/orderDetail',
      name: 'orderDetail',
      component: orderDetail
    },{
      path: '/pages/category/checkout',
      name: 'checkout',
      component: checkout
    },{
      path: '/pages/category/addresscateAdd',
      name: 'addresscateAdd',
      component: addresscateAdd
    },{
      path: '/pages/category/selCoupon',
      name: 'selCoupon',
      component: selCoupon
    },{
      path: '/pages/category/addresscateList',
      name: 'addresscateList',
      component: addresscateList
    },{
      path: '/pages/ucenter/addressAdd',
      name: 'addressAdd',
      component: addressAdd
    },{
      path: '/pages/ucenter/amountMoney',
      name: 'amountMoney',
      component: amountMoney
    },{
      path: '/pages/register/register',
      name: 'register',
      component: register
    },{
      path: '/pages/ucenter/namecard',
      name: 'namecard',
      component: namecard
    },{
      path: '/pages/ucenter/account',
      name: 'account',
      component: account
    },{
      path: '/pages/ucenter/userservice',
      name: 'userservice',
      component: userservice
    },{
      path: '/pages/payment/successPay',
      name: 'successPay',
      component: successPay
    },{
      path: '/pages/payment/failPay',
      name: 'failPay',
      component: failPay  
    },{
      path: '/pages/ucenter/logistics',
      name: 'logistics',
      component: logistics
    },{
      path: '/pages/ucenter/lookLogistics',
      name: 'lookLogistics',
      component: lookLogistics
    },{
      path: '/pages/ucenter/order1',
      name: 'order1',
      component: order1
    },{
      path: '/pages/ucenter/order2',
      name: 'order2',
      component: order2
    },{
      path: '/pages/ucenter/order3',
      name: 'order3',
      component: order3
    },{
      path: '/pages/ucenter/order4',
      name: 'order4',
      component: order4
    }
  ]

  
})