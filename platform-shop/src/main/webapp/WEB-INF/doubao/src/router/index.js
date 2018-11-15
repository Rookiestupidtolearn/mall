import Vue from 'vue'
import Router from 'vue-router'
import tabbar from '@/components/tabbar'  //首页
import home from '@/components/home'  //首页
import classification from '@/components/classification'  //分类
import search from '@/pages/search/search'  //分类-搜索页面
import shoppingcar from '@/components/shoppingcar'  //购物车
import ucenter from '@/components/ucenter'  //个人中心
import order from '@/pages/ucenter/order'  //个人中心-我的订单
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

Vue.use(Router)

//当页面路径是 /的时候 加载 component 为 home 的 vue 文件
export default new Router({
  routes: [
    {
      path: '/',
      name: 'home',
      component: home
    },{
      path: '/tabbar',
      name: 'tabbar',
      component: tabbar
    },{
      path: '/classification',
      name: 'classification',
      component: classification
    },{
      path: '/shoppingcar',
      name: 'shoppingcar',
      component: shoppingcar
    },{
      path: '/ucenter',
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
      path: '/pages/category/goods',
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
    }
  ]
})