package com.platform.api;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.shiro.util.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.util.StringUtil;
import com.platform.annotation.IgnoreAuth;
import com.platform.annotation.LoginUser;
import com.platform.dao.ApiBrandMapper;
import com.platform.dao.ApiCategoryMapper;
import com.platform.dao.ApiGoodsGalleryMapper;
import com.platform.dao.ApiGoodsMapper;
import com.platform.dao.ApiProductMapper;
import com.platform.dao.JdGoodsImgPathMapper;
import com.platform.dao.JdGoodsMapper;
import com.platform.entity.BrandVo;
import com.platform.entity.CategoryVo;
import com.platform.entity.GoodsGalleryVo;
import com.platform.entity.GoodsVo;
import com.platform.entity.ProductVo;
import com.platform.entity.UserVo;
import com.platform.service.ApiUserService;
import com.platform.util.ApiBaseAction;
import com.platform.utils.R;
import com.platform.youle.constant.Constants.Urls;
import com.platform.youle.entity.GoodsImagePathVo;
import com.platform.youle.entity.JdGoodsVo;
import com.platform.youle.entity.RequestBaseEntity;
import com.platform.youle.entity.RequestChildsEntity;
import com.platform.youle.entity.RequestSkuDetailEntity;
import com.platform.youle.service.AbsApiRootCateService;
import com.platform.youle.util.HttpUtil;
import com.platform.youle.util.TokenUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * API测试接口
 *
 * @author lipengjun
 * @email 939961241@qq.com
 * @date 2017-03-23 15:47
 */
@Api(tags = "测试接口")
@RestController
@RequestMapping("/api/test")
public class ApiTestController extends ApiBaseAction{

    @Autowired
    private ApiUserService userService;
//    @Autowired
//    private AbsApiGoodsService absApiGoodsService;
    
    @Autowired
    private AbsApiRootCateService absApiRootCateService;
    @Autowired
    private ApiCategoryMapper apiCategoryMapper;
    @Autowired
    private ApiBrandMapper apiBrandMapper;
    @Autowired
    private ApiGoodsMapper apiGoodsMapper;
    @Autowired
    private ApiGoodsGalleryMapper apiGoodsGalleryMapper;
    @Autowired
    private JdGoodsMapper jdGoodsMapper;
    @Autowired
    private JdGoodsImgPathMapper goodsImagePathMapper;
    @Autowired
    private ApiProductMapper apiProductMapper;
    /**
     * 获取用户信息
     */
    @PostMapping("userInfo")
    public R userInfo(@LoginUser UserVo user) {
        return R.ok().put("user", user);
    }

    /**
     * 忽略Token验证测试
     */
    @IgnoreAuth
    @PostMapping("notToken")
    public R notToken() {
        return R.ok().put("msg", "无需token也能访问。。。");
    }


    /**
     * 测试
     */
    @IgnoreAuth
    @PostMapping(value = "propter")
    public Object test() {
    	
        return R.ok().put("propter", "可以了");
    }
    
    /**
     * 根据手机号查询用户信息接口测试方法
     *
     * @param mobile
     * @return
     */
    @IgnoreAuth
    @PostMapping("userListByMobile")
    public R userList(String mobile) {
        UserVo userEntity = userService.queryByMobile(mobile);
        return R.ok().put("dto", userEntity);
    }
    /**
     * 5.1大类  入库
     * @return
     */
    @ApiOperation(value = "保存一级分类")
    @PostMapping("rootCate")
    public Object rootCate() {
    	String result = "";
    	RequestBaseEntity entity = new RequestBaseEntity();
	    initRequestParam(entity);
    	try {
    		result = HttpUtil.post(Urls.base_test_url+Urls.rootCate, objectToMap(entity));
    		if(!StringUtil.isEmpty(result)){
    			JSONObject dateObj = JSONObject.parseObject(result);
    			String resultObj = dateObj.get("RESULT_DATA").toString();
    			if(!StringUtil.isEmpty(resultObj)){
    				JSONArray dateAttr = JSONArray.parseArray(resultObj);
    				if(!CollectionUtils.isEmpty(dateAttr)){
    					for(int i = 0;i<dateAttr.size();i++){
    						JSONObject obj = JSONObject.parseObject(dateAttr.get(i).toString());
    						CategoryVo vo = new CategoryVo();
    						vo.setId(Integer.parseInt(obj.get("code").toString()));
    						vo.setName(obj.get("name").toString());
    						vo.setIs_show(1);
    						vo.setParent_id(obj.get("parentId") == null ? 0 : Integer.parseInt(obj.get("parentId").toString()));
    						vo.setLevel(obj.get("level").toString());
    						apiCategoryMapper.save(vo);
    					}
    				}
    			}
    		}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
    
    /**
     * 二级分流入库
     * @return
     */
    @ApiOperation(value = "保存二级分类")
    @PostMapping("childs")
    public Object childs() {
    	
    	List<CategoryVo> categorys = apiCategoryMapper.queryAllCategorys();
    	if(!CollectionUtils.isEmpty(categorys)){
    		for(CategoryVo vo : categorys){
    			RequestChildsEntity entity = new RequestChildsEntity();
    		    initRequestParam(entity);
    		    entity.setParentCate(vo.getId());
    			try {
					String result = HttpUtil.post(Urls.base_test_url+Urls.childs, objectToMap(entity));
					if(!StringUtil.isEmpty(result)){
		    			JSONObject dateObj = JSONObject.parseObject(result);
		    			String resultObj = dateObj.get("RESULT_DATA").toString();
		    			if(!StringUtil.isEmpty(resultObj)){
		    				JSONArray dateAttr = JSONArray.parseArray(resultObj);
		    				if(!CollectionUtils.isEmpty(dateAttr)){
		    					for(int i = 0;i<dateAttr.size();i++){
		    						JSONObject obj = JSONObject.parseObject(dateAttr.get(i).toString());
		    						CategoryVo vos = new CategoryVo();
		    						System.out.println(dateAttr.get(i).toString());
		    						List<GoodsVo> goods = apiGoodsMapper.quertGoodsByCategory(obj.get("code").toString());
		    						if(!CollectionUtils.isEmpty(goods)){
		    							for(GoodsVo good : goods){
		    								if(good.getList_pic_url() !=  null){
		    									vos.setWap_banner_url(good.getList_pic_url());
		    								}
		    								break;
		    							}
		    						}
		    						vos.setId(Integer.parseInt(obj.get("code").toString()));
		    						vos.setName(obj.get("name").toString());
		    						vos.setIs_show(1);
		    						vos.setParent_id(obj.get("parentId") == null ? 0 : Integer.parseInt(obj.get("parentId").toString()));
		    						vos.setLevel(obj.get("level").toString());
		    						apiCategoryMapper.save(vos);
		    					}
		    				}
		    			}
		    		}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
    		}
    	}
    	return null;
    }
    
    
    /*
     * 保存商品
     */

    public String queryAllProduects(){
    	String resultObj = "";
         RequestBaseEntity entity = new RequestBaseEntity();
         initRequestParam(entity);
    	 try {
			String result = HttpUtil.post(Urls.base_test_url+Urls.getAllProductIdsUrl, objectToMap(entity));
			if(!StringUtil.isEmpty(result)){
				JSONObject dateObj = JSONObject.parseObject(result);
    			resultObj = dateObj.get("RESULT_DATA").toString();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	 return resultObj;
    }
    
    @ApiOperation(value = "查询所有产品id")
    @PostMapping("saveGoods")
    @Transactional
    public Object saveGoods(){
    	String porducts = this.queryAllProduects();
//    	JSONArray proAttr = JSONArray.parseArray(porducts);
//    	if(!CollectionUtils.isEmpty(proAttr)){
//    		for(int i = 0;i<proAttr.size();i++){
    			JSONObject obj = JSONObject.parseObject(porducts);
    			String res = obj.get("1").toString();
    			String newStr = (String) res.subSequence(1, res.length()-1);
    			String[] attr = newStr.split(",");
    			for(int j = 0;j<attr.length;j++){
			        RequestSkuDetailEntity entity = new RequestSkuDetailEntity();
			        initRequestParam(entity);
			        entity.setPid(Long.parseLong(attr[j].toString()));
			        try {
						String result = HttpUtil.post(Urls.base_test_url+Urls.detial, objectToMap(entity));
						if(!StringUtils.isEmpty(result)){
							if(!StringUtil.isEmpty(result)){
								JSONObject dateObj = JSONObject.parseObject(result);
				    			String resultObj = dateObj.get("RESULT_DATA").toString();
				    			if(!StringUtil.isEmpty(resultObj)){
				    				GoodsVo vo = new GoodsVo();
				    				JSONObject resultDate = JSONObject.parseObject(resultObj);
				    				String productDate = resultDate.get("PRODUCT_DATA").toString();
				    				if(!StringUtils.isEmpty(productDate)){
				    					JSONObject productObj = JSONObject.parseObject(productDate);
				    					vo.setGoods_sn(productObj.get("productId") == null ? null : "JD".concat(productObj.get("productId").toString()));
				    					vo.setName(productObj.get("name") == null ? "" : productObj.get("name").toString());
				    					BrandVo brand = new BrandVo();
				    					if(productObj.get("brand") != null){
				    						List <BrandVo> brands = apiBrandMapper.quertBrandByNames(productObj.get("brand").toString());
				    						if(CollectionUtils.isEmpty(brands)){
				    							brand.setName(productObj.get("brand").toString());
				    							apiBrandMapper.save(brand);
				    							vo.setBrand_id(brand.getId());
				    						}
				    					}
				    					vo.setIs_delete(0);
				    					vo.setList_pic_url(productObj.get("thumbnailImage") == null ? "" : productObj.get("thumbnailImage").toString());
				    					vo.setCategory_id(Integer.parseInt(productObj.get("productCate").toString()));
				    					if(productObj.get("status") != null){
				    						vo.setIs_on_sale(2);
				    					}
				    					vo.setMarket_price(productObj.get("marketPrice") == null ? BigDecimal.ZERO : new BigDecimal(productObj.get("marketPrice").toString()));
				    					vo.setRetail_price(productObj.get("retailPrice") == null ? BigDecimal.ZERO : new BigDecimal(productObj.get("retailPrice").toString()));
				    					vo.setGoods_brief(productObj.get("features") == null ? "" : productObj.get("features").toString());
				    					if(productObj.get("hot") != null){
				    						if("true".equals(productObj.get("hot").toString())){
				    							vo.setIs_hot(1);
				    						}
				    						if("false".equals(productObj.get("hot").toString())){
				    							vo.setIs_hot(0);
				    						}
				    					}
				    					vo.setGoods_desc(resultDate.get("PRODUCT_DESCRIPTION") == null ? null : resultDate.get("PRODUCT_DESCRIPTION").toString());
				    					vo.setAdd_time(new Date());
				    					vo.setSource("JD");
				    					apiGoodsMapper.save(vo);
				    					ProductVo product = new ProductVo();
				    					product.setGoods_id(vo.getId());
				    					product.setGoods_name(vo.getName());
				    					product.setGoods_number(vo.getGoods_number());
				    					product.setGoods_sn(vo.getGoods_sn());
				    					product.setGoods_specification_ids("");
				    					product.setList_pic_url(vo.getList_pic_url());
				    					product.setMarket_price(vo.getMarket_price());
				    					product.setRetail_price(vo.getRetail_price());
				    					apiProductMapper.save(product);
				    				}
				    				
				    				if(resultDate.get("PRODUCT_IMAGE") != null){
				    					JSONArray imgattr = JSONArray.parseArray(resultDate.get("PRODUCT_IMAGE").toString());
				    					if(!CollectionUtils.isEmpty(imgattr)){
				    						for(int t = 0;t<imgattr.size();t++){ 
				    							GoodsGalleryVo  galleryVo = new GoodsGalleryVo();
				    							JSONObject imgObj = JSONObject.parseObject(imgattr.get(t).toString());
				    							galleryVo.setImg_url(imgObj.get("imageUrl").toString());
				    							galleryVo.setGoods_id(vo.getId());
				    							galleryVo.setSort_order(Integer.parseInt(imgObj.get("orderSort").toString()));
				    							apiGoodsGalleryMapper.save(galleryVo);
				    						}
				    					}
				    				}
				    			}
							}
						}
					} catch (Exception e) {
						logger.error("导入商品异常，商品productid:" + attr[j]);
						e.printStackTrace();
						continue;
					}
    			}
//    		}
//    	}
    	return null;
    }
    
    
    
    
    
    
    
    
    public void initRequestParam(RequestBaseEntity  entity){
	      entity.setWid(TokenUtil.wid);
	      entity.setTimestamp(TokenUtil.currentTime.toString());
	      entity.setToken(TokenUtil.token);
	  }
    public Map<String,Object>  objectToMap(RequestBaseEntity entity) throws Exception{
        String str = JSON.toJSONString(entity);
        Map<String,Object> map = (Map<String,Object>) JSON.parse(str);
        return map;
    }

    @ApiOperation(value = "查询所有产品id")
    @PostMapping("saveJdGoods")
    @Transactional
    public Object saveJdGoods(){
    	String porducts = this.queryAllProduects();
    			JSONObject obj = JSONObject.parseObject(porducts);
    			String res = obj.get("1").toString();
    			String newStr = (String) res.subSequence(1, res.length()-1);
    			String[] attr = newStr.split(",");
    			for(int j = 0;j<attr.length;j++){
			        RequestSkuDetailEntity entity = new RequestSkuDetailEntity();
			        initRequestParam(entity);
			        entity.setPid(Long.parseLong(attr[j].toString()));
			        try {
						String result = HttpUtil.post(Urls.base_test_url+Urls.detial, objectToMap(entity));
						if(!StringUtils.isEmpty(result)){
							if(!StringUtil.isEmpty(result)){
								try {
									JSONObject dateObj = JSONObject.parseObject(result);
									String resultObj = dateObj.get("RESULT_DATA").toString();
									if(!StringUtils.isEmpty(resultObj)){
										JSONObject resultDate = JSONObject.parseObject(resultObj);
										JdGoodsVo good = new JdGoodsVo();
										
										if(resultDate.getString("PRODUCT_IMAGE") != null){
											JSONArray imgArrays = JSONArray.parseArray(resultDate.getString("PRODUCT_IMAGE").toString());
											if(!CollectionUtils.isEmpty(imgArrays)){
												for(int i = 0;i<imgArrays.size();i++){
													JSONObject obj2 = JSONObject.parseObject(imgArrays.get(i).toString());
													GoodsImagePathVo imagePath = new GoodsImagePathVo();
													imagePath.setImageUrl(obj2.get("imageUrl").toString());
													imagePath.setOrderSort(Integer.parseInt(obj2.get("orderSort").toString()));
													imagePath.setCreateTime(new Date());
													imagePath.setUpdateTime(new Date());
													imagePath.setProductId(Long.parseLong(attr[j]));
													goodsImagePathMapper.save(imagePath);
													good.setGoodsImagePathId(imagePath.getId());
												}
											}
										}
										
										good.setMobileProductDecription(resultDate.get("MOBILE_PRODUCT_DESCRIPTION") == null ? "" : resultDate.get("MOBILE_PRODUCT_DESCRIPTION").toString());
										good.setProductDecription(resultDate.get("PRODUCT_DESCRIPTION") == null ? "" : resultDate.get("PRODUCT_DESCRIPTION").toString());
										
										if(resultDate.getString("PRODUCT_DATA") != null){
											String productDate = resultDate.getString("PRODUCT_DATA").toString();
											JSONObject productObj = JSONObject.parseObject(productDate);
											good.setProductId(productObj.get("productId") == null ? null : Long.parseLong(productObj.get("productId").toString()));
											good.setName(productObj.get("name") == null ? "" : productObj.get("name").toString());
											good.setType(productObj.get("type") == null ? "" : productObj.get("type").toString());
											good.setThumbnailImage(productObj.get("thumbnailImage") == null ? "" : productObj.get("thumbnailImage").toString());
											good.setBrand(productObj.get("brand") == null ? "" : productObj.get("brand").toString());
											good.setProductCate(productObj.get("productCate") == null ? null : Integer.parseInt(productObj.get("productCate").toString()));
											good.setProductCode(productObj.get("productCode") == null ? "" : productObj.get("productCode").toString());
											good.setStatus(productObj.get("status") == null ? "" : productObj.get("status").toString());
											good.setMarketPrice(productObj.get("marketPrice") == null ? null : new BigDecimal(productObj.get("marketPrice").toString()));
											good.setRetailPrice(productObj.get("retailPrice") == null ? null : new BigDecimal(productObj.get("retailPrice").toString()));
											good.setProductPlace(productObj.get("productPlace") == null ? "" : productObj.get("productPlace").toString());
											good.setFeatures(productObj.get("features") == null ? "" : productObj.get("features").toString());
											if(productObj.get("hot") != null){
												good.setHot("true".equals(productObj.get("hot").toString()) ? 1: 0);
											}
											if(productObj.get("is7ToReturn") != null){
												good.setIs7Toreturn("true".equals(productObj.get("is7ToReturn").toString()) ? 1: 0);
											}
											
											good.setCreateTime(new Date());
											good.setId(Long.parseLong(attr[j]));
										}
										jdGoodsMapper.save(good);
									}
								} catch (Exception e) {
									logger.error("[商品入库]异常，商品productid" + attr[j]);
								}
				    			}
							}
						} catch (Exception e) {
							e.printStackTrace();
							continue;
						}
    			}
    	return null;
    }
}