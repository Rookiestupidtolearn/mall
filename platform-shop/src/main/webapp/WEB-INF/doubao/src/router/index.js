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
import addressList from '@/pages/ucenter/addressList'  //个人中心-地址管理
import accountSecurity from '@/pages/ucenter/accountSecurity'  //个人中心-账户安全
import helpCenter from '@/pages/ucenter/helpCenter'  //个人中心-帮助中心
import feedback from '@/pages/ucenter/feedback'  //个人中心-问题反馈
import mobile from '@/pages/ucenter/mobile'  //个人中心-问题反馈
import categoryPages from '@/pages/category/category'  //首页-渠道分类
import goods from '@/pages/category/goods'  //商品详情
import brandDetail from '@/pages/category/brandDetail'  //制造商详情
import orderDetail from '@/pages/ucenter/orderDetail'  //订单详情
import checkout from '@/pages/category/checkout'  //下单页面
import addressAdd from '@/pages/category/addressAdd'  //选择地址页面
import payResult from '@/pages/category/payResult'  //去付款页面
import selCoupon from '@/pages/category/selCoupon'  //选择优惠券页面
import addressList2 from '@/pages/category/addressList'  //购物车选择地址页面
import addressAdd2 from '@/pages/ucenter/addressAdd'  //个人中心选择地址页面
import amountMoney from '@/pages/ucenter/amountMoney'  //资金明细
import pay from '@/pages/category/pay'  //支付订单
import register from '@/pages/register/register'  //注册页面
import ptfwxy from '@/pages/xieyi/ptfwxy'  //平台服务协议
import namecard from '@/pages/ucenter/namecard'  //认证
import account from '@/pages/ucenter/account'  //账户中心
import userservice from '@/pages/ucenter/userservice'  //客户服务
import successPay from '@/pages/payment/successPay'  //支付成功
import failPay from '@/pages/payment/failPay'  //支付失败
import logistics from '@/pages/ucenter/logistics'  //物流页面
import lookLogistics from '@/pages/ucenter/lookLogistics'  //物流页面

Vue.use(Router)

//当页面路径是 /的时候 加载 component 为 home 的 vue 文件

const Home = resolve => require(['@/pages/home'], resolve)
export default new Router({
  routes: [
    {
      path: '/',
      name: 'home',
      component: home,
      meta: { 
				keepAlive: true,// 需要缓存
			}
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
      component: search,
      meta: { 
				keepAlive: true,// 需要缓存
			}
    },{
      path: '/pages/category/category',
      name: 'categoryPages',
      component: categoryPages
    },{
      path: '/pages/goods/goods',
      name: 'goods',
      component: goods
    },{
      path: '/pages/category/brandDetail',
      name: 'brandDetail',
      component: brandDetail
    },{
      path: '/pages/ucenter/orderDetail',
      name: 'orderDetail',
      component: orderDetail
    },{
      path: '/pages/category/checkout',
      name: 'checkout',
      component: checkout
    },{
      path: '/pages/category/addressAdd',
      name: 'addressAdd',
      component: addressAdd
    },{
      path: '/pages/category/payResult',
      name: 'payResult',
      component: payResult
    },{
      path: '/pages/category/selCoupon',
      name: 'selCoupon',
      component: selCoupon
    },{
      path: '/pages/category/addressList',
      name: 'addressList2',
      component: addressList2
    },{
      path: '/pages/ucenter/addressAdd',
      name: 'addressAdd2',
      component: addressAdd2
    },{
      path: '/pages/ucenter/amountMoney',
      name: 'amountMoney',
      component: amountMoney
    },{
      path: '/pages/category/pay',
      name: 'pay',
      component: pay
    },{
      path: '/pages/register/register',
      name: 'register',
      component: register
    },{
      path: '/pages/xieyi/ptfwxy',
      name: 'ptfwxy',
      component: ptfwxy
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