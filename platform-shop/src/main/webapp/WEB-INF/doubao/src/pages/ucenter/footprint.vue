<template>
 	<div class="footprint">
 			<!--公用头部-->
  		<headbar :headFont = "headFont"></headbar>
  	
  	<div class="mt88">
	    <div class="day-item" v-for="item in footprintList" >
	      <div class="day-hd">{{item[0].add_time}}</div>
	      <div class="day-list">
	        <router-link :to="'/pages/category/goods?id='+iitem.id" class="item"  v-for = "iitem in item" >
	          <img class="img" :src="iitem.list_pic_url"/>
	          <div class="info">
	            <div class="name">{{iitem.name}}</div>
	            <!--<div class="subtitle">{{iitem.goods_brief}}</div>-->
	            <div class="price">￥{{iitem.market_price}}</div>
	          </div>
	        </router-link>
	      </div>
	    </div>
    </div>
  </div>
</template>

<script>
import { MessageBox } from 'mint-ui';
import headbar from '@/components/headbar.vue';
		
export default {
name: 'footprint',
components:{headbar},
  data () {
    return {
    	headFont:'我的足迹',
    	footprintList:[]
    }
  },
  mounted(){
  	var that = this;    
    	that.$http({
	        method: 'post',
	        url:that.$url+ 'footprint/list',
    	}).then(function (response) {
      		  	response = {"errno":0,"data":{"data":[[{"id":2120,"user_id":28,"goods_id":1181023,"add_time":"2018-11-07","referrer":0,"name":"sjmBB测试勿动002修改过名字","list_pic_url":"http://aoss.huaqianyueshang.com/wall/20181026/162648787edb46.png","goods_brief":null,"retail_price":120.00,"market_price":90.00,"product_market_price":90.00,"nickname":null,"avatar":null}],[{"id":2114,"user_id":28,"goods_id":1181025,"add_time":"2018-10-29","referrer":0,"name":"测试使用001","list_pic_url":"http://aoss.huaqianyueshang.com/wall/20181026/1400011555eb77.jpg","goods_brief":"这是一款为测试而建的产品，但它不仅仅为测试而生","retail_price":1000.00,"market_price":1000.00,"product_market_price":1000.00,"nickname":null,"avatar":null}],[{"id":2117,"user_id":28,"goods_id":1181022,"add_time":"2018-10-26","referrer":0,"name":"sjmBB测试勿动001","list_pic_url":"http://aoss.huaqianyueshang.com/wall/20181023/10323836475fd3.png","goods_brief":"sjm介绍","retail_price":20.00,"market_price":9.00,"product_market_price":9.00,"nickname":null,"avatar":null},{"id":1845,"user_id":28,"goods_id":1019006,"add_time":"2018-10-26","referrer":0,"name":"植物填充护颈夜交藤枕","list_pic_url":"http://yanxuan.nosdn.127.net/60c3707837c97a21715ecc3986a744ce.png","goods_brief":"健康保护枕","retail_price":99.00,"market_price":108.90,"product_market_price":0,"nickname":null,"avatar":null},{"id":1837,"user_id":28,"goods_id":1135000,"add_time":"2018-10-26","referrer":0,"name":"几何印花AB面全棉四件套","list_pic_url":"http://yanxuan.nosdn.127.net/53d0309471b570a7e12a3f01ba694491.png","goods_brief":"几何线条，舒适的北欧线条风","retail_price":359.00,"market_price":394.90,"product_market_price":0,"nickname":null,"avatar":null},{"id":1734,"user_id":28,"goods_id":1009012,"add_time":"2018-10-26","referrer":0,"name":"可水洗舒柔丝羽绒枕","list_pic_url":"http://yanxuan.nosdn.127.net/a196b367f23ccfd8205b6da647c62b84.png","goods_brief":"超细纤维，蓬松轻盈回弹","retail_price":59.00,"market_price":64.90,"product_market_price":0,"nickname":null,"avatar":null}],[{"id":1858,"user_id":28,"goods_id":1127052,"add_time":"2018-10-25","referrer":0,"name":"纯棉水洗色织格夏凉被","list_pic_url":"http://yanxuan.nosdn.127.net/4f483526cfe3b953f403ae02049df5b9.png","goods_brief":"100%棉填充，透气排汗，双面可用","retail_price":169.00,"market_price":185.90,"product_market_price":0,"nickname":null,"avatar":null},{"id":1521,"user_id":28,"goods_id":1147045,"add_time":"2018-10-25","referrer":0,"name":"清新趣粉系列居家地毯 灰黄条纹","list_pic_url":"http://yanxuan.nosdn.127.net/5cda4a0c4c4ff9728d03186bd053c9ca.png","goods_brief":"条纹色块拼接 软糯温馨","retail_price":599.00,"market_price":658.90,"product_market_price":0,"nickname":null,"avatar":null}],[{"id":2007,"user_id":28,"goods_id":1181021,"add_time":"2018-10-24","referrer":0,"name":"sjmA测试勿动002","list_pic_url":"http://aoss.huaqianyueshang.com/wall/20181023/102534294ab017.png","goods_brief":"sjm介绍下","retail_price":15.00,"market_price":10.00,"product_market_price":10.00,"nickname":null,"avatar":null},{"id":1289,"user_id":28,"goods_id":1147046,"add_time":"2018-10-24","referrer":0,"name":"清新趣粉系列居家地毯 条纹间粉","list_pic_url":"http://yanxuan.nosdn.127.net/655d718df8107f8e7fd1dc6140e29039.png","goods_brief":"多色拼接 舒柔静音","retail_price":599.00,"market_price":658.90,"product_market_price":0,"nickname":null,"avatar":null}],[{"id":2019,"user_id":28,"goods_id":1181019,"add_time":"2018-10-22","referrer":0,"name":"sjmA勿动001","list_pic_url":"http://aoss.huaqianyueshang.com/wall/20181022/11093713949cc7.png","goods_brief":"啊啊啊啊啊啊啊啊","retail_price":5.00,"market_price":3.00,"product_market_price":3.00,"nickname":null,"avatar":null},{"id":1593,"user_id":28,"goods_id":1116032,"add_time":"2018-10-22","referrer":0,"name":"怀抱休闲椅组合","list_pic_url":"http://yanxuan.nosdn.127.net/45176a783387751fc07a07f5031dd62c.png","goods_brief":"敦厚包裹感 葛优躺神器","retail_price":3499.00,"market_price":3848.90,"product_market_price":0,"nickname":null,"avatar":null},{"id":1518,"user_id":28,"goods_id":1151013,"add_time":"2018-10-22","referrer":0,"name":"简约知性全棉四件套 胭脂粉","list_pic_url":"http://yanxuan.nosdn.127.net/73a8692048f58f15e823b636d7c3bb74.png","goods_brief":"清新灵动粉 谱写浪漫意趣","retail_price":359.00,"market_price":394.90,"product_market_price":0,"nickname":null,"avatar":null},{"id":1347,"user_id":28,"goods_id":1152009,"add_time":"2018-10-22","referrer":0,"name":"魔兽世界 联盟 护腕 一只","list_pic_url":"http://yanxuan.nosdn.127.net/ae6d41117717387b82dcaf1dfce0cd97.png","goods_brief":"吸汗、舒适、弹性、防护、耐用","retail_price":29.00,"market_price":31.90,"product_market_price":0,"nickname":null,"avatar":null},{"id":1290,"user_id":28,"goods_id":1147047,"add_time":"2018-10-22","referrer":0,"name":"简约知性系列居家地毯 蓝粉拼接","list_pic_url":"http://yanxuan.nosdn.127.net/bda805b0a2464b6ec33c18981565e50e.png","goods_brief":"三角几何拼接 超细绒感","retail_price":559.00,"market_price":614.90,"product_market_price":0,"nickname":null,"avatar":null},{"id":941,"user_id":28,"goods_id":1143019,"add_time":"2018-10-22","referrer":0,"name":"粽夏冰晶端午粽礼盒 480克","list_pic_url":"http://yanxuan.nosdn.127.net/277b07c1e5e6fb57cf9ca47fcd3903d5.png","goods_brief":"五味冰粽，清凉一夏","retail_price":98.00,"market_price":107.80,"product_market_price":0,"nickname":null,"avatar":null},{"id":940,"user_id":28,"goods_id":1070000,"add_time":"2018-10-22","referrer":0,"name":"星云酥 180克/3颗","list_pic_url":"http://yanxuan.nosdn.127.net/8392725765cdd57fdae3f173877f4bda.png","goods_brief":"酥饼界的小仙女","retail_price":26.00,"market_price":28.60,"product_market_price":0,"nickname":null,"avatar":null},{"id":924,"user_id":28,"goods_id":1093001,"add_time":"2018-10-22","referrer":0,"name":"月光曲·马蹄莲","list_pic_url":"http://yanxuan.nosdn.127.net/71fede861c3641d570a89a65ccf4525f.png","goods_brief":"仿真PU，定格典雅","retail_price":29.00,"market_price":31.90,"product_market_price":0,"nickname":null,"avatar":null},{"id":895,"user_id":28,"goods_id":1138001,"add_time":"2018-10-22","referrer":0,"name":"北欧简约山形纹绣花抱枕","list_pic_url":"http://yanxuan.nosdn.127.net/dbc5b25b824c3b3d7ff43b56ca35eee9.png","goods_brief":"精细刺绣，舒适立体","retail_price":79.00,"market_price":86.90,"product_market_price":0,"nickname":null,"avatar":null}],[{"id":2111,"user_id":28,"goods_id":1181017,"add_time":"2018-10-19","referrer":0,"name":"sjmB-001设置规格测试使用","list_pic_url":"http://aoss.huaqianyueshang.com/wall/20181018/1747296661c50a.png","goods_brief":null,"retail_price":12.89,"market_price":66.00,"product_market_price":66.00,"nickname":null,"avatar":null},{"id":2015,"user_id":28,"goods_id":1153006,"add_time":"2018-10-19","referrer":0,"name":"魔兽世界 纪念版 麻将套装","list_pic_url":"http://yanxuan.nosdn.127.net/2743921b945a6c71fcdc3c5282a03413.png","goods_brief":"十年经典，纪念麻将套装","retail_price":1288.00,"market_price":1416.80,"product_market_price":0,"nickname":null,"avatar":null},{"id":1594,"user_id":28,"goods_id":1143015,"add_time":"2018-10-19","referrer":0,"name":"婴儿床蔺草席","list_pic_url":"http://yanxuan.nosdn.127.net/50e197854e0ada79c37b7215a1574450.png","goods_brief":"天然龙眉草，婴童可用","retail_price":79.00,"market_price":86.90,"product_market_price":0,"nickname":null,"avatar":null},{"id":632,"user_id":28,"goods_id":1022000,"add_time":"2018-10-19","referrer":0,"name":"意式毛线绣球四件套","list_pic_url":"http://yanxuan.nosdn.127.net/5350e35e6f22165f38928f3c2c52ac57.png","goods_brief":"浪漫毛线绣球，简约而不简单","retail_price":299.00,"market_price":328.90,"product_market_price":0,"nickname":null,"avatar":null}],[{"id":2016,"user_id":28,"goods_id":1134036,"add_time":"2018-10-18","referrer":0,"name":"凤梨酥 360克","list_pic_url":"http://yanxuan.nosdn.127.net/9356cc27b22bd47ad43913d13226555f.png","goods_brief":"特别添加10%凤梨果肉","retail_price":38.00,"market_price":41.80,"product_market_price":0,"nickname":null,"avatar":null},{"id":1999,"user_id":28,"goods_id":1181015,"add_time":"2018-10-18","referrer":0,"name":"sjmB重名测试","list_pic_url":"http://aoss.huaqianyueshang.com/wall/20181018/14355729853155.png","goods_brief":null,"retail_price":6.22,"market_price":5.00,"product_market_price":5.00,"nickname":null,"avatar":null},{"id":1735,"user_id":28,"goods_id":1181014,"add_time":"2018-10-18","referrer":0,"name":"sjmB公司饼干 ","list_pic_url":"http://aoss.huaqianyueshang.com/wall/20181017/182211534388dd.png","goods_brief":null,"retail_price":9.99,"market_price":9.99,"product_market_price":9.99,"nickname":null,"avatar":null},{"id":1701,"user_id":28,"goods_id":1181016,"add_time":"2018-10-18","referrer":0,"name":"sjmB重名测试","list_pic_url":"http://aoss.huaqianyueshang.com/wall/20181018/14373654485a3e.png","goods_brief":null,"retail_price":8.11,"market_price":7.00,"product_market_price":7.00,"nickname":null,"avatar":null},{"id":1492,"user_id":28,"goods_id":1037011,"add_time":"2018-10-18","referrer":0,"name":"安睡慢回弹记忆绵床垫","list_pic_url":"http://yanxuan.nosdn.127.net/a03ea6f4509439acdafcb7ceba1debe0.png","goods_brief":"5cm记忆绵的亲密包裹","retail_price":599.00,"market_price":658.90,"product_market_price":0,"nickname":null,"avatar":null},{"id":1337,"user_id":28,"goods_id":1152008,"add_time":"2018-10-18","referrer":0,"name":"魔兽世界 部落 护腕 一只","list_pic_url":"http://yanxuan.nosdn.127.net/203cb83d93606865e3ddde57b69b9e9a.png","goods_brief":"吸汗、舒适、弹性、防护、耐用","retail_price":29.00,"market_price":31.90,"product_market_price":0,"nickname":null,"avatar":null},{"id":544,"user_id":28,"goods_id":1181011,"add_time":"2018-10-18","referrer":0,"name":"嗯嗯嗯","list_pic_url":"","goods_brief":null,"retail_price":15.36,"market_price":null,"product_market_price":0,"nickname":null,"avatar":null},{"id":470,"user_id":28,"goods_id":1134030,"add_time":"2018-10-18","referrer":0,"name":"简约知性记忆棉坐垫","list_pic_url":"http://yanxuan.nosdn.127.net/aa49dfe878becf768eddc4c1636643a6.png","goods_brief":"慢回弹海绵，时尚设计。","retail_price":46.00,"market_price":50.60,"product_market_price":0,"nickname":null,"avatar":null},{"id":448,"user_id":28,"goods_id":1181012,"add_time":"2018-10-18","referrer":0,"name":"sjm饼干002,我修改商品信息，要下架操作","list_pic_url":"http://aoss.huaqianyueshang.com/wall/20181015/17550447908b26.png","goods_brief":"sjm","retail_price":19.66,"market_price":13.00,"product_market_price":0,"nickname":null,"avatar":null}],[{"id":1801,"user_id":28,"goods_id":1181013,"add_time":"2018-10-17","referrer":0,"name":"sjm新建商品，未申请上架","list_pic_url":"","goods_brief":null,"retail_price":12.77,"market_price":130.00,"product_market_price":130.00,"nickname":null,"avatar":null}],[{"id":2002,"user_id":28,"goods_id":1181010,"add_time":"2018-10-15","referrer":0,"name":"sjm饼干","list_pic_url":"http://aoss.huaqianyueshang.com/wall/20181015/1309491392339c.png","goods_brief":"women是简明介绍","retail_price":15.22,"market_price":10.00,"product_market_price":10.00,"nickname":null,"avatar":null},{"id":172,"user_id":28,"goods_id":1134032,"add_time":"2018-10-15","referrer":0,"name":"趣味粉彩系列记忆棉坐垫","list_pic_url":"http://yanxuan.nosdn.127.net/8b30eeb17c831eba08b97bdcb4c46a8e.png","goods_brief":"慢回弹海绵的呵护，萌趣添彩。","retail_price":49.00,"market_price":53.90,"product_market_price":0,"nickname":null,"avatar":null}],[{"id":189,"user_id":28,"goods_id":1181009,"add_time":"2018-10-13","referrer":0,"name":"妙芙饼干001","list_pic_url":"http://aoss.huaqianyueshang.com/wall/20181012/1523498698a37e.png","goods_brief":null,"retail_price":10.22,"market_price":9.70,"product_market_price":9.70,"nickname":null,"avatar":null}],[{"id":2115,"user_id":28,"goods_id":1181003,"add_time":"2018-10-12","referrer":0,"name":"手机","list_pic_url":"http://aoss.huaqianyueshang.com/wall/20181011/175250871aede0.jpg","goods_brief":"这是一个好的手机--吴明龙","retail_price":1998.00,"market_price":100.00,"product_market_price":100.00,"nickname":null,"avatar":null},{"id":2041,"user_id":28,"goods_id":1155015,"add_time":"2018-10-12","referrer":0,"name":"绿豆糕 80克（4枚入）","list_pic_url":"http://yanxuan.nosdn.127.net/66b9f1638c0517d179262f14ed1345f9.png","goods_brief":"细腻松软，入口绵柔","retail_price":12.90,"market_price":14.19,"product_market_price":0,"nickname":null,"avatar":null},{"id":1648,"user_id":28,"goods_id":1152031,"add_time":"2018-10-12","referrer":0,"name":"魔兽世界-伊利丹颈枕眼罩套装","list_pic_url":"http://yanxuan.nosdn.127.net/fd6e78a397bd9e9804116a36f0270b0a.png","goods_brief":"差旅好伴侣","retail_price":99.00,"market_price":108.90,"product_market_price":0,"nickname":null,"avatar":null},{"id":1490,"user_id":28,"goods_id":1084003,"add_time":"2018-10-12","referrer":0,"name":"纯棉美式绞花针织盖毯","list_pic_url":"http://yanxuan.nosdn.127.net/cf40c167e7054fe184d49f19121f63c7.png","goods_brief":"美式提花，温暖舒适","retail_price":199.00,"market_price":218.90,"product_market_price":0,"nickname":null,"avatar":null},{"id":553,"user_id":28,"goods_id":1181008,"add_time":"2018-10-12","referrer":0,"name":"的广发华福","list_pic_url":"","goods_brief":null,"retail_price":1000.00,"market_price":85.00,"product_market_price":85.00,"nickname":null,"avatar":null},{"id":469,"user_id":28,"goods_id":1135073,"add_time":"2018-10-12","referrer":0,"name":"海魂纹哈衣水手服（婴童）","list_pic_url":"http://yanxuan.nosdn.127.net/53052b04ae001d289c040e09ea92231c.png","goods_brief":"经典海魂纹自由海军领","retail_price":69.00,"market_price":75.90,"product_market_price":0,"nickname":null,"avatar":null},{"id":437,"user_id":28,"goods_id":1181002,"add_time":"2018-10-12","referrer":0,"name":"sjm002测试商品","list_pic_url":"http://aoss.huaqianyueshang.com/wall/20181011/1739116787d32a.png","goods_brief":null,"retail_price":500.00,"market_price":600.00,"product_market_price":600.00,"nickname":null,"avatar":null},{"id":156,"user_id":28,"goods_id":1181004,"add_time":"2018-10-12","referrer":0,"name":"23123","list_pic_url":"http://aoss.huaqianyueshang.com/wall/20181011/18021118f1a01.jpg","goods_brief":null,"retail_price":45234.00,"market_price":444.00,"product_market_price":444.00,"nickname":null,"avatar":null},{"id":127,"user_id":28,"goods_id":1092001,"add_time":"2018-10-12","referrer":0,"name":"星空原色水晶笔","list_pic_url":"http://yanxuan.nosdn.127.net/8e35b003ce7895c39eeb073b1f61b1d7.png","goods_brief":"140颗原色水晶，璀璨如星","retail_price":29.00,"market_price":31.90,"product_market_price":0,"nickname":null,"avatar":null},{"id":121,"user_id":28,"goods_id":1181006,"add_time":"2018-10-12","referrer":0,"name":"bb","list_pic_url":"http://aoss.huaqianyueshang.com/wall/20181012/115549586a3e8d.jpg","goods_brief":null,"retail_price":null,"market_price":null,"product_market_price":0,"nickname":null,"avatar":null},{"id":119,"user_id":28,"goods_id":1130056,"add_time":"2018-10-12","referrer":0,"name":"奢华植鞣头层水牛皮席三件套","list_pic_url":"http://yanxuan.nosdn.127.net/56e72b84a9bb66687c003ecdaba73816.png","goods_brief":"三峡水牛头层皮，高端夏凉必备","retail_price":2299.00,"market_price":2528.90,"product_market_price":0,"nickname":null,"avatar":null},{"id":118,"user_id":28,"goods_id":1131017,"add_time":"2018-10-12","referrer":0,"name":"平滑细篾头层青碳化竹凉席","list_pic_url":"http://yanxuan.nosdn.127.net/2b6e2268ed712f1a336283f013abb7a1.png","goods_brief":"细篾整密，凉滑不夹肉","retail_price":259.00,"market_price":284.90,"product_market_price":0,"nickname":null,"avatar":null},{"id":112,"user_id":28,"goods_id":1110017,"add_time":"2018-10-12","referrer":0,"name":"耐用材料猫咪护理清洁套装","list_pic_url":"http://yanxuan.nosdn.127.net/534231583f82572398ec84bad425cdaf.png","goods_brief":"精致钢材，美容清洁","retail_price":79.00,"market_price":86.90,"product_market_price":0,"nickname":null,"avatar":null},{"id":103,"user_id":28,"goods_id":1181007,"add_time":"2018-10-12","referrer":0,"name":"aaaaaaa","list_pic_url":"http://aoss.huaqianyueshang.com/wall/20181012/150412269e27de.jpg","goods_brief":null,"retail_price":34.00,"market_price":34.00,"product_market_price":0,"nickname":null,"avatar":null}],[{"id":1489,"user_id":28,"goods_id":1116011,"add_time":"2018-10-10","referrer":0,"name":"蔓越莓曲奇 200克","list_pic_url":"http://yanxuan.nosdn.127.net/767b370d07f3973500db54900bcbd2a7.png","goods_brief":"酥脆奶香，甜酸回味","retail_price":36.00,"market_price":39.60,"product_market_price":0,"nickname":null,"avatar":null},{"id":54,"user_id":28,"goods_id":1143016,"add_time":"2018-10-10","referrer":0,"name":"112升 纯PC拉链斜纹拉杆箱","list_pic_url":"http://yanxuan.nosdn.127.net/e56c6239ee4a641ce2a4565c6babb43e.png","goods_brief":"容量升级，全家一箱搞定","retail_price":319.00,"market_price":350.90,"product_market_price":0,"nickname":null,"avatar":null}],[{"id":2122,"user_id":28,"goods_id":1181000,"add_time":"2018-10-09","referrer":0,"name":"母亲节礼物-舒适安睡组合","list_pic_url":"http://yanxuan.nosdn.127.net/1f67b1970ee20fd572b7202da0ff705d.png","goods_brief":"安心舒适是最好的礼物","retail_price":2598.00,"market_price":2857.80,"product_market_price":0,"nickname":null,"avatar":null},{"id":2043,"user_id":28,"goods_id":1155000,"add_time":"2018-10-09","referrer":0,"name":"清新趣粉全棉四件套 条纹绿格","list_pic_url":"http://yanxuan.nosdn.127.net/d7d6ef1f1865991077384761b4521dce.png","goods_brief":"清新趣粉全棉四件套 青粉拼接","retail_price":399.00,"market_price":438.90,"product_market_price":0,"nickname":null,"avatar":null},{"id":1520,"user_id":28,"goods_id":1147048,"add_time":"2018-10-09","referrer":0,"name":"简约知性系列居家地毯 蓝灰格","list_pic_url":"http://yanxuan.nosdn.127.net/fd7920a2eadd10fa10c0c03959a2abe0.png","goods_brief":"沉稳双拼色 居家温柔伴护","retail_price":559.00,"market_price":614.90,"product_market_price":0,"nickname":null,"avatar":null},{"id":1517,"user_id":28,"goods_id":1152161,"add_time":"2018-10-09","referrer":0,"name":"竹语丝麻印花四件套","list_pic_url":"http://yanxuan.nosdn.127.net/977401e75113f7c8334c4fb5b4bf6215.png","goods_brief":"3重透气，清爽柔滑","retail_price":459.00,"market_price":504.90,"product_market_price":0,"nickname":null,"avatar":null},{"id":1494,"user_id":28,"goods_id":1181001,"add_time":"2018-10-09","referrer":0,"name":"测试商品sjm001","list_pic_url":"","goods_brief":"小本商品介绍信息","retail_price":101.11,"market_price":900.00,"product_market_price":900.00,"nickname":null,"avatar":null},{"id":894,"user_id":28,"goods_id":1138000,"add_time":"2018-10-09","referrer":0,"name":"日式蓬软太鼓抱枕","list_pic_url":"http://yanxuan.nosdn.127.net/ad953e16ad8c33b714e7af941ce8cd56.png","goods_brief":"萌趣太鼓造型 软糯轻柔回弹","retail_price":29.00,"market_price":31.90,"product_market_price":0,"nickname":null,"avatar":null},{"id":526,"user_id":28,"goods_id":1166008,"add_time":"2018-10-09","referrer":0,"name":"Carat钻石 不粘厨具组合","list_pic_url":"http://yanxuan.nosdn.127.net/615a16e899e01efb780c488df4233f48.png","goods_brief":"钻石涂层，不粘锅锅具组","retail_price":459.00,"market_price":533.00,"product_market_price":533.00,"nickname":null,"avatar":null},{"id":40,"user_id":28,"goods_id":1135072,"add_time":"2018-10-09","referrer":0,"name":"经典海魂纹水手裙（婴童）","list_pic_url":"http://yanxuan.nosdn.127.net/43e57d4208cdc78ac9c088f9b3e798f5.png","goods_brief":"自由海军领探索未来梦","retail_price":69.00,"market_price":75.90,"product_market_price":0,"nickname":null,"avatar":null},{"id":30,"user_id":28,"goods_id":1092024,"add_time":"2018-10-09","referrer":0,"name":"双宫茧纱布亲肤手工蚕丝被","list_pic_url":"http://yanxuan.nosdn.127.net/f245a86dcb9f455217241e437b203926.png","goods_brief":"手工匠心织造","retail_price":1599.00,"market_price":1758.90,"product_market_price":0,"nickname":null,"avatar":null}]],"numsPerPage":10,"count":0,"totalPages":0,"currentPage":1},"errmsg":"执行成功"};
    		if(response.data.errno == '401' || response.data.errno == '请先登录'){
    			MessageBox({
					  title: ' ',
					  message: '请先登录 ',
					  showCancelButton: true
					});
    			return false;
    		}else{
    			console.log(response)
    			that.footprintList = response.data.data;
    		}
		  })
 	 }
}
</script>

 <!--Add "scoped" attribute to limit CSS to this component only--> 
<style scoped>
.footprint{
   height: auto;
  overflow: hidden;
  width: 100%;
  border-top: 1px solid #e1e1e1;
}

.day-item{
  height: auto;
  overflow: hidden;
  text-align: left;
  width: 100%;
  margin-bottom: .20rem;
}

.day-hd{
  height: .94rem;
  width: 100%;
  line-height: .94rem;
  background: #fff;
  padding-left: .30rem;
  color: #333;
  font-size: .28rem;
}

.day-list{
  width: 100%;
  height: auto;
  overflow: hidden;
  background: #fff;
  padding-left: .30rem;
  border-top: 1px solid #e1e1e1;
}

.item{
  display: block;
  height: 1.6rem;
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
  float: left;
  width: 5.20rem;
  height: 1.50rem;
  padding-left: .20rem;
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
