package com.platform.api;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.platform.annotation.IgnoreAuth;
import com.platform.annotation.LoginUser;
import com.platform.cache.J2CacheUtils;
import com.platform.dao.ApiCategoryMapper;
import com.platform.dao.ApiGoodsMapper;
import com.platform.entity.AttributeVo;
import com.platform.entity.BrandVo;
import com.platform.entity.CartVo;
import com.platform.entity.CategoryVo;
import com.platform.entity.CommentPictureVo;
import com.platform.entity.CommentVo;
import com.platform.entity.CouponVo;
import com.platform.entity.DoubaoSearchGoods;
import com.platform.entity.FootprintVo;
import com.platform.entity.GoodsGalleryVo;
import com.platform.entity.GoodsIssueVo;
import com.platform.entity.GoodsSpecificationVo;
import com.platform.entity.GoodsVo;
import com.platform.entity.ProductVo;
import com.platform.entity.RelatedGoodsVo;
import com.platform.entity.SearchHistoryVo;
import com.platform.entity.UserCouponVo;
import com.platform.entity.UserVo;
import com.platform.service.ApiAttributeService;
import com.platform.service.ApiBrandService;
import com.platform.service.ApiCartService;
import com.platform.service.ApiCategoryService;
import com.platform.service.ApiCollectService;
import com.platform.service.ApiCommentPictureService;
import com.platform.service.ApiCommentService;
import com.platform.service.ApiCouponService;
import com.platform.service.ApiFootprintService;
import com.platform.service.ApiGoodsGalleryService;
import com.platform.service.ApiGoodsIssueService;
import com.platform.service.ApiGoodsService;
import com.platform.service.ApiGoodsSpecificationService;
import com.platform.service.ApiProductService;
import com.platform.service.ApiRelatedGoodsService;
import com.platform.service.ApiSearchHistoryService;
import com.platform.service.ApiUserCouponService;
import com.platform.service.ApiUserService;
import com.platform.service.DoubaoSearchGoodsService;
import com.platform.util.ApiBaseAction;
import com.platform.util.ApiPageUtils;
import com.platform.utils.Base64;
import com.platform.utils.CharUtil;
import com.platform.utils.DateUtils;
import com.platform.utils.Query;
import com.qiniu.util.StringUtils;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

/**
 * 作者: @author Harmon <br>
 * 时间: 2017-08-11 08:32<br>
 * 描述: ApiIndexController <br>
 */
@Api(tags = "商品管理")
@RestController
@RequestMapping("/api/goods")
public class ApiGoodsController extends ApiBaseAction {
    @Autowired
    private ApiGoodsService goodsService;
    @Autowired
    private ApiGoodsSpecificationService goodsSpecificationService;
    @Autowired
    private ApiProductService productService;
    @Autowired
    private ApiGoodsGalleryService goodsGalleryService;
    @Autowired
    private ApiGoodsIssueService goodsIssueService;
    @Autowired
    private ApiAttributeService attributeService;
    @Autowired
    private ApiBrandService brandService;
    @Autowired
    private ApiCommentService commentService;
    @Autowired
    private ApiUserService userService;
    @Autowired
    private ApiCommentPictureService commentPictureService;
    @Autowired
    private ApiCollectService collectService;
    @Autowired
    private ApiFootprintService footprintService;
    @Autowired
    private ApiCategoryService categoryService;
    @Autowired
    private ApiSearchHistoryService searchHistoryService;
    @Autowired
    private ApiRelatedGoodsService relatedGoodsService;
    @Autowired
    private ApiCouponService apiCouponService;
    @Autowired
    private ApiUserCouponService apiUserCouponService;
    @Autowired
    private ApiCartService cartService;
    @Autowired
    private ApiCategoryMapper apiCategoryMapper;
    @Autowired
    private ApiGoodsMapper apiGoodsMapper;
    @Autowired
    private DoubaoSearchGoodsService doubaoSearchGoodsService;
    

    /**
     */
    @ApiOperation(value = "商品首页")
    @IgnoreAuth
    @PostMapping(value = "index")
    public Object index() {
        //
        Map param = new HashMap();
        param.put("is_delete", 0);
        param.put("is_on_sale", 1);
        List<GoodsVo> goodsList = goodsService.queryList(param);
        //
        return toResponsSuccess(goodsList);
    }

    /**
     * 获取 商品规格信息，用于购物车编辑时选择规格
     */
    @ApiOperation(value = " 获取商品规格信息")
    @ApiImplicitParams({@ApiImplicitParam(name = "id", value = "商品id", paramType = "path", required = true)})
    @IgnoreAuth
    @PostMapping(value = "sku")
    public Object sku(Integer id) {
        Map<String, Object> resultObj = new HashMap();
        //
        Map param = new HashMap();
        param.put("goods_id", id);
        List<GoodsSpecificationVo> goodsSpecificationEntityList = goodsSpecificationService.queryList(param);
        //
        List<ProductVo> productEntityList = productService.queryList(param);
        //
        resultObj.put("specificationList", goodsSpecificationEntityList);
        resultObj.put("productList", productEntityList);
        return toResponsSuccess(resultObj);
    }

    /**
     * 商品详情页数据
     */
    @ApiOperation(value = " 商品详情页数据")
    @ApiImplicitParams({@ApiImplicitParam(name = "id", value = "商品id", paramType = "path", required = true),
            @ApiImplicitParam(name = "referrer", value = "商品referrer", paramType = "path", required = false)})
    @IgnoreAuth
    @PostMapping(value = "detail")
    public Object detail(Integer id, Long referrer) {
        Map<String, Object> resultObj = new HashMap<>();
        //
        Long userId = getUserId();
        GoodsVo info = goodsService.queryObject(id);
        if(info.getProduct_market_price().compareTo(BigDecimal.ZERO) > 0){
        	info.setMarket_price(info.getProduct_market_price());
        }
        Map<String,Object> param = new HashMap<>();
        param.put("goods_id", id);
        //
        Map<String, Object> specificationParam = new HashMap<>();
        specificationParam.put("fields", "gs.*, s.name");
        specificationParam.put("goods_id", id);
        specificationParam.put("specification", true);
        specificationParam.put("sidx", "s.id");
        specificationParam.put("order", "asc");
        List<GoodsSpecificationVo> goodsSpecificationEntityList = goodsSpecificationService.queryList(specificationParam);

        List<Map> specificationList = new ArrayList<>();
        
        //按规格名称分组
        for (int i = 0; i < goodsSpecificationEntityList.size(); i++) {
        	
            GoodsSpecificationVo specItem = goodsSpecificationEntityList.get(i);
            //
            List<GoodsSpecificationVo> tempList = null;
            for (int j = 0; j < specificationList.size(); j++) {
                if (specificationList.get(j).get("specification_id").equals(specItem.getSpecification_id())) {
                    tempList = (List<GoodsSpecificationVo>) specificationList.get(j).get("valueList");
                    break;
                }
            }
            //
            if (null == tempList) {
                Map temp = new HashMap();
                temp.put("specification_id", specItem.getSpecification_id());
                temp.put("name", specItem.getName());
                tempList = new ArrayList();
                tempList.add(specItem);
                temp.put("valueList", tempList);
                specificationList.add(temp);
            } else {
                for (int j = 0; j < specificationList.size(); j++) {
                    if (specificationList.get(j).get("specification_id").equals(specItem.getSpecification_id())) {
                        tempList = (List<GoodsSpecificationVo>) specificationList.get(j).get("valueList");
                        tempList.add(specItem);
                        break;
                    }
                }
            }
        }
        //
        List<ProductVo> productEntityList = productService.queryList(param);
        //
        List<GoodsGalleryVo> gallery = goodsGalleryService.queryList(param);
        Map ngaParam = new HashMap();
        ngaParam.put("fields", "nga.value, na.name");
        ngaParam.put("sidx", "nga.id");
        ngaParam.put("order", "asc");
        ngaParam.put("goods_id", id);
        List<AttributeVo> attribute = attributeService.queryList(ngaParam);
        //
        Map issueParam = new HashMap();
//        issueParam.put("goods_id", id);
        List<GoodsIssueVo> issue = goodsIssueService.queryList(issueParam);
        //
        BrandVo brand = brandService.queryObject(info.getBrand_id());
        //
        param.put("value_id", id);
        param.put("type_id", 0);
        Integer commentCount = commentService.queryTotal(param);
        List<CommentVo> hotComment = commentService.queryList(param);
        Map commentInfo = new HashMap();
        if (null != hotComment && hotComment.size() > 0) {
            UserVo commentUser = userService.queryObject(hotComment.get(0).getUser_id());
            commentInfo.put("content", Base64.decode(hotComment.get(0).getContent()));
            commentInfo.put("add_time", DateUtils.timeToStr(hotComment.get(0).getAdd_time(), DateUtils.DATE_PATTERN));
            commentInfo.put("nickname", commentUser.getNickname());
            commentInfo.put("avatar", commentUser.getAvatar());
            Map paramPicture = new HashMap();
            paramPicture.put("comment_id", hotComment.get(0).getId());
            List<CommentPictureVo> commentPictureEntities = commentPictureService.queryList(paramPicture);
            commentInfo.put("pic_list", commentPictureEntities);
        }
        Map comment = new HashMap();
        comment.put("count", commentCount);
        comment.put("data", commentInfo);
        //当前用户是否收藏
        Integer userHasCollect=0;
        if(null!=userId && 0!=userId){//用户登录才查
            Map collectParam = new HashMap();
            collectParam.put("user_id", getUserId());
            collectParam.put("value_id", id);
            collectParam.put("type_id", 0);
            userHasCollect = collectService.queryTotal(collectParam);
            if (userHasCollect > 0) {
                userHasCollect = 1;
            }
        }

        
        if(null != userId){
        	//记录用户的足迹
            FootprintVo footprintEntity = new FootprintVo();
            footprintEntity.setAdd_time(System.currentTimeMillis() / 1000);
            footprintEntity.setGoods_brief(info.getGoods_brief());
            footprintEntity.setList_pic_url(info.getList_pic_url());
            footprintEntity.setGoods_id(info.getId());
            footprintEntity.setName(info.getName());
            footprintEntity.setRetail_price(info.getRetail_price());
            footprintEntity.setUser_id(userId);
            if (null != referrer) {
                footprintEntity.setReferrer(referrer);
            } else {
                footprintEntity.setReferrer(0L);
            }
            footprintService.save(footprintEntity);
        }
        
        //根据商品id查询对应最低价位的规格（前端默认选中此规格）
        Map paramMap = new HashMap();
        paramMap.put("goods_id",id);
        paramMap.put("sidx","a.market_price");
        paramMap.put("order","asc");
        paramMap.put("offset",0);
        paramMap.put("limit",1);
        List<ProductVo> minPriceList =  productService.queryList(paramMap);
        
        //
        resultObj.put("info", info);
        resultObj.put("gallery", gallery);
        resultObj.put("attribute", attribute);
        resultObj.put("userHasCollect", userHasCollect);
        resultObj.put("issue", issue);
        resultObj.put("comment", comment);
        resultObj.put("brand", brand);
        resultObj.put("specificationList", specificationList);
        resultObj.put("productList", productEntityList);
        resultObj.put("minPriceList", minPriceList);
        // 记录推荐人是否可以领取红包，用户登录时校验
        try {
            // 是否已经有可用的转发红包
            Map params = new HashMap();
            params.put("user_id", userId);
            params.put("send_type", 2);
            params.put("unUsed", true);
            List<CouponVo> enabledCouponVos = apiCouponService.queryUserCoupons(params);
            if ((null == enabledCouponVos || enabledCouponVos.size() == 0)
                    && null != referrer && null != userId) {
                // 获取优惠信息提示
                Map couponParam = new HashMap();
                couponParam.put("enabled", true);
                Integer[] send_types = new Integer[]{2};
                couponParam.put("send_types", send_types);
                List<CouponVo> couponVos = apiCouponService.queryList(couponParam);
                if (null != couponVos && couponVos.size() > 0) {
                    CouponVo couponVo = couponVos.get(0);
                    Map footprintParam = new HashMap();
                    footprintParam.put("goods_id", id);
                    footprintParam.put("referrer", referrer);
                    Integer footprintNum = footprintService.queryTotal(footprintParam);
                    if (null != footprintNum && null != couponVo.getMin_transmit_num()
                            && footprintNum > couponVo.getMin_transmit_num()) {
                        UserCouponVo userCouponVo = new UserCouponVo();
                        userCouponVo.setAdd_time(new Date());
                        userCouponVo.setCoupon_id(couponVo.getId());
                        userCouponVo.setCoupon_number(CharUtil.getRandomString(12));
                        userCouponVo.setUser_id(getUserId());
                        apiUserCouponService.save(userCouponVo);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return toResponsSuccess(resultObj);
    }

    /**
     * 　获取分类下的商品
     */
    @ApiOperation(value = " 获取分类下的商品")
    @ApiImplicitParams({@ApiImplicitParam(name = "id", value = "分类id", paramType = "path", required = true)})
    @IgnoreAuth
    @PostMapping(value = "category")
    public Object category(Integer id,String type) {  
    	 Map<String, Object> resultObj = new HashMap();
    	 List<CategoryVo> newSubCategorys = new ArrayList<>();
         //查询二级分类
         CategoryVo parentCategory = categoryService.queryObject(id);
         Map params = new HashMap();
         if("parent".equals(type)){
        	 params.put("parent_id", parentCategory.getId());
         }else if("sub".equals(type)){
        	 params.put("parent_id", parentCategory.getParent_id());
         }
         List<CategoryVo> newBrotherCategory = new ArrayList<>();
//         List<Integer> subCategorys = categoryService.queryListOfGoodsNotNull(params);
        List<Integer> subCategorys = categoryService.queryListOfGoodsNotNullAndSortByGoodsNum(params);//按照分类下面的商品数排序
         if(!CollectionUtils.isEmpty(subCategorys)){
         	for(Integer categoryId : subCategorys){
         		CategoryVo vo = categoryService.queryObject(categoryId);
         		if(vo != null){
         			newSubCategorys.add(vo);
         		}
         	}
         }
         if(!CollectionUtils.isEmpty(newSubCategorys)){
         	for(CategoryVo sub : newSubCategorys){
         		if(sub.getWap_banner_url() != null){
         			newBrotherCategory.add(sub);
         		}
         	}
         }
         resultObj.put("currentCategory", newBrotherCategory.size() == 0 ? null : newBrotherCategory.get(0));
         resultObj.put("parentCategory", parentCategory);
         resultObj.put("brotherCategory", newBrotherCategory);
         return toResponsSuccess(resultObj);
    }

    /**
     　　获取商品列表
     */
    @ApiOperation(value = "获取商品列表")
    @ApiImplicitParams({@ApiImplicitParam(name = "categoryId", value = "分类id", paramType = "path", required = true),
            @ApiImplicitParam(name = "brandId", value = "品牌Id", paramType = "path", required = true),
            @ApiImplicitParam(name = "isNew", value = "新商品", paramType = "path", required = true),
            @ApiImplicitParam(name = "isHot", value = "热卖商品", paramType = "path", required = true)})
    @IgnoreAuth
    @PostMapping(value = "list")
    public Object list(@LoginUser UserVo loginUser, Integer categoryId,
                       Integer brandId, String keyword, Integer isNew, Integer isHot,
                       @RequestParam(value = "page", defaultValue = "1") Integer page, @RequestParam(value = "size", defaultValue = "10") Integer size,
                       String sort, String order,String type) {
        Map<String,Object> params = new HashMap<>();
        String REDIS_CATEGORY_LOCK = "cagetoryLock";
        params.put("is_delete", 0);
        params.put("is_on_sale", 1);
        params.put("brand_id", brandId);
        params.put("keyword", keyword);
        params.put("is_new", isNew);
        params.put("is_hot", isHot);
        params.put("page", page);
        params.put("limit", size);
        //
        if (null != sort && sort.equals("price")) {
            params.put("sidx", "market_price");
            params.put("order", order);
        }
        //添加到搜索历史
        if (!StringUtils.isNullOrEmpty(keyword)) {
        	Long userId = getUserId();
        	loginUser = userService.queryObject(userId);
            SearchHistoryVo searchHistoryVo = new SearchHistoryVo();
            searchHistoryVo.setAdd_time(System.currentTimeMillis() / 1000);
            searchHistoryVo.setKeyword(keyword);
            searchHistoryVo.setUser_id(null != loginUser ? loginUser.getUserId().toString() : "");
            searchHistoryVo.setFrom("");
            searchHistoryService.save(searchHistoryVo);
        }
        //筛选的分类
        List<CategoryVo> filterCategory = new ArrayList();
        CategoryVo rootCategory = new CategoryVo();
        rootCategory.setId(0);
        rootCategory.setName("全部");
        rootCategory.setChecked(false);
        filterCategory.add(rootCategory);
        //
        params.put("fields", "category_id,nideshop_goods.id,nideshop_goods.market_price");
        List<GoodsVo> categoryEntityList = new ArrayList<>();
        Object goodsObj = J2CacheUtils.get(J2CacheUtils.SHOP_CATEGORY_GOODS,REDIS_CATEGORY_LOCK);
        if(goodsObj != null){
        	categoryEntityList =(List<GoodsVo>)goodsObj;
        }else{
        	categoryEntityList = goodsService.queryList(params);
        	J2CacheUtils.put(J2CacheUtils.SHOP_CATEGORY_GOODS, REDIS_CATEGORY_LOCK, categoryEntityList);
        }
        params.remove("fields");
        params.put("group", "id");
        if (null != categoryEntityList && categoryEntityList.size() > 0) {
            List<Integer> categoryIds = new ArrayList();
            for (GoodsVo goodsVo : categoryEntityList) {
            	if(goodsVo!=null){
            		if(!categoryIds.contains(goodsVo.getCategory_id())){
            			categoryIds.add(goodsVo.getCategory_id());
            		}
            	}
            }
            //查找二级分类的parent_id
            Map categoryParam = new HashMap();
            if(!CollectionUtils.isEmpty(categoryIds)){
            	categoryParam.put("ids", categoryIds);
            }
            categoryParam.put("fields", "parent_id");
            List<CategoryVo> parentCategoryList = categoryService.queryList(categoryParam);
            //
            List<Integer> parentIds = new ArrayList();
            for (CategoryVo categoryEntity : parentCategoryList) {
                parentIds.add(categoryEntity.getParent_id());
            }
            //一级分类
            categoryParam = new HashMap();
            categoryParam.put("fields", "id,name");
            categoryParam.put("order", "asc");
            categoryParam.put("sidx", "sort_order");
            categoryParam.put("ids", parentIds);
            List<CategoryVo> parentCategory = categoryService.queryList(categoryParam);
            if (null != parentCategory) {
                filterCategory.addAll(parentCategory);
            }
        }
        //加入分类条件
        if (null != categoryId && categoryId > 0) {
        	//查询二级分类
            CategoryVo subCategorys = apiCategoryMapper.queryObject(categoryId);
            //查询父节点
            CategoryVo parentCategorys = apiCategoryMapper.queryObject(subCategorys.getParent_id());
            if("热销".equals(subCategorys.getName()) && "其他".equals(parentCategorys.getName())){
            	List<Integer> categoryIds  = categoryService.quertOtherIds();
        		params.put("categoryIds", categoryIds);
            }else{
            	List<Integer> categoryIds = new ArrayList();
            	
        		Map categoryParam = new HashMap();
        		if("parent".equals(type)){
        			categoryParam.put("parent_id", categoryId);
        		}else if("sub".equals(type)){
        			categoryParam.put("parent_id", subCategorys.getParent_id());
        		}
        		categoryParam.put("fields", "id");
//        		List<CategoryVo> childIds = categoryService.queryList(categoryParam);
        		List<CategoryVo> childIds = getBrotherCategorys(categoryId, type);
        		if("parent".equals(type)){
        			if(!CollectionUtils.isEmpty(childIds)){
        				categoryIds.add(childIds.get(0).getId());
        			}else{
        				categoryIds.add(categoryId);
        			}
        		}else if("sub".equals(type)){
        			categoryIds.add(categoryId);
        		}
        		if(!CollectionUtils.isEmpty(categoryIds)){
        			params.put("categoryIds", categoryIds);
        		}
            }
        }
        //查询列表数据
        params.put("fields", "nideshop_goods.id as id,nideshop_goods.name as name, nideshop_goods.list_pic_url as list_pic_url, nideshop_goods.market_price as market_price, nideshop_goods.retail_price, nideshop_goods.goods_brief,case when min(nideshop_product.market_price) != '' then min(nideshop_product.market_price) else 0 end product_market_price");
        if (null != sort && sort.equals("price")) {
            params.put("sidx", "market_price");
            params.put("order", order);
        }else{
        	params.put("sidx", "id");
            params.put("order", "desc");
        }
        Query query = new Query(params);
        PageHelper.startPage(query.getPage(), query.getLimit());
        List<GoodsVo> goodsList = goodsService.queryList(query);
        for(GoodsVo goodsVo : goodsList){
        	//如果商品有规格，则展示最低规格价
    		if(goodsVo.getProduct_market_price().compareTo(BigDecimal.ZERO) > 0 ){
    			goodsVo.setMarket_price(goodsVo.getProduct_market_price());
    		}
        }
        ApiPageUtils goodsData = new ApiPageUtils(new PageInfo(goodsList));
        //搜索到的商品
        for (CategoryVo categoryEntity : filterCategory) {
            if (null != categoryId && categoryEntity.getId() == 0 || categoryEntity.getId() == categoryId) {
                categoryEntity.setChecked(true);
            } else {
                categoryEntity.setChecked(false);
            }
        }
        goodsData.setFilterCategory(filterCategory);
        goodsData.setGoodsList(goodsList);
        return toResponsSuccess(goodsData);
    }
    
    /**
     * 查询三级分类
     * @param id
     * @param type
     * @return
     */
    public List<CategoryVo> getBrotherCategorys(Integer id,String type){
    	List<CategoryVo> newBrotherCategory = new ArrayList<>();
    	 Map<String, Object> resultObj = new HashMap();
    	 List<CategoryVo> newSubCategorys = new ArrayList<>();
    	 CategoryVo parentCategory = categoryService.queryObject(id);
         Map params = new HashMap();
         if("parent".equals(type)){
        	 params.put("parent_id", parentCategory.getId());
         }else if("sub".equals(type)){
        	 params.put("parent_id", parentCategory.getParent_id());
         }
        List<Integer> subCategorys = categoryService.queryListOfGoodsNotNullAndSortByGoodsNum(params);//查找三级分类不为空并且按分类下的商品总数排序
        if(!CollectionUtils.isEmpty(subCategorys)){
        	for(Integer categoryId : subCategorys){
        		CategoryVo vo = categoryService.queryObject(categoryId);
        		if(vo != null){
        			newSubCategorys.add(vo);
        		}
        	}
        }
        if(!CollectionUtils.isEmpty(newSubCategorys)){
        	for(CategoryVo sub : newSubCategorys){
        		if(sub.getWap_banner_url() != null){
        			newBrotherCategory.add(sub);
        		}
        	}
        }
        return newBrotherCategory;
    }
    
    
    
    /**
     * 搜索商品
     * @param loginUser
     * @param categoryId
     * @param brandId
     * @param keyword
     * @param isNew
     * @param isHot
     * @param page
     * @param size
     * @param sort
     * @param order
     * @return
     */
    @ApiOperation(value = "获取商品列表")
    @ApiImplicitParams({@ApiImplicitParam(name = "categoryId", value = "分类id", paramType = "path", required = true),
            @ApiImplicitParam(name = "brandId", value = "品牌Id", paramType = "path", required = true),
            @ApiImplicitParam(name = "isNew", value = "新商品", paramType = "path", required = true),
            @ApiImplicitParam(name = "isHot", value = "热卖商品", paramType = "path", required = true)})
    @IgnoreAuth
    @PostMapping(value = "searchGoodsList")
    public Object searchGoodsList(@LoginUser UserVo loginUser, Integer categoryId,Integer brandId, String keyword, Integer isNew, Integer isHot,
            @RequestParam(value = "page", defaultValue = "1") Integer page, @RequestParam(value = "size", defaultValue = "10") Integer size,
            String sort, String order) {
    	//添加到搜索历史
    	 if (!StringUtils.isNullOrEmpty(keyword)) {
         	Long userId = getUserId();
         	loginUser = userService.queryObject(userId);
             SearchHistoryVo searchHistoryVo = new SearchHistoryVo();
             searchHistoryVo.setAdd_time(System.currentTimeMillis() / 1000);
             searchHistoryVo.setKeyword(keyword);
             searchHistoryVo.setUser_id(null != loginUser ? loginUser.getUserId().toString() : "");
             searchHistoryVo.setFrom("");
             searchHistoryService.save(searchHistoryVo);
         }
    	
    	//1、通过商品名检索临时表,获取所有的商品id和分类id    
    	 List<DoubaoSearchGoods> doubaoSearchGoodsList =  doubaoSearchGoodsService.queryDoubaoSearchGoodsList(keyword);
    	
    	//2.1 通过分类id查询分类表(查询对应的所有的一级分类) 2.2   如果参数有分类id(关键字+分类id检索a表)  
    	 List<Integer> categoryIds = new ArrayList<Integer>();
    	 List<Integer> goodsIds = new ArrayList<Integer>();
    	 for(DoubaoSearchGoods doubaoSearchGoods : doubaoSearchGoodsList){
    		 categoryIds.add(doubaoSearchGoods.getCateGoryId());
    		 goodsIds.add(doubaoSearchGoods.getId());
    	 }
    	 //查询一级分类
    	 List<Integer>  parentIds =  apiCategoryMapper.queryParentIdsByCategoryId(categoryIds);
    	 if(CollectionUtils.isEmpty(parentIds)){
    		 logger.error("【商品搜索】根据关键字["+keyword+"]未查询到一级分类id");
    	 }
    	 //取出通过关键字搜索的分类
    	 List<CategoryVo> filterCategory = new ArrayList();
         CategoryVo rootCategory = new CategoryVo();
         rootCategory.setId(0);
         rootCategory.setName("全部");
         rootCategory.setChecked(false);
         filterCategory.add(rootCategory);
         List<CategoryVo> categoryList = apiCategoryMapper.queryCategoryList(parentIds);
         filterCategory.addAll(categoryList);
         for (CategoryVo categoryEntity : filterCategory) {
             if (categoryEntity.getId().intValue() == 0 || categoryEntity.getId().intValue() == categoryId.intValue()) {
                 categoryEntity.setChecked(true);
             } else {
                 categoryEntity.setChecked(false);
             }
         }
         
         
         //查询商品
         List<Integer> categoryListParam = new ArrayList<Integer>();
         Map<String,Object> params = new HashMap<>();
         if(categoryId != null && categoryId != 0){
        	 //查询一级分类下的二级分类id
        	 Map categoryParam = new HashMap();
      		categoryParam.put("parent_id", categoryId);
      		categoryParam.put("fields", "id");
      		List<CategoryVo> childIds = categoryService.queryList(categoryParam);
      		for (CategoryVo categoryEntity : childIds) {
      			categoryListParam.add(categoryEntity.getId());
      		}
      		categoryListParam.add(categoryId);
      		if(!CollectionUtils.isEmpty(categoryListParam)){
      			params.put("categoryIds", categoryListParam);
      			params.put("keyword", keyword);
      		}
      		//获取商品id
      		List<Integer> goodsIdsList = doubaoSearchGoodsService.qureyGoodsIdByparam(params);
      		params.remove("categoryIds");
      		params.remove("keyword");
      		params.put("goods_ids", goodsIdsList);
         }else{
        	params.put("goods_ids", goodsIds);
         }
         params.put("is_delete", 0);
         params.put("is_on_sale", 1);
         params.put("brand_id", brandId);
         params.put("is_new", isNew);
         params.put("is_hot", isHot);
         params.put("page", page);
         params.put("limit", size);
         params.put("fields", "nideshop_goods.id as id,nideshop_goods.name as name, nideshop_goods.list_pic_url as list_pic_url, nideshop_goods.market_price as market_price, nideshop_goods.retail_price, nideshop_goods.goods_brief,case when min(nideshop_product.market_price) != '' then min(nideshop_product.market_price) else 0 end product_market_price");
         if (null != sort && sort.equals("price")) {
             params.put("sidx", "market_price");
             params.put("order", order);
         }else{
        	params.put("sidx", "id");
            params.put("order", "desc");
         }
         Query query = new Query(params);
         PageHelper.startPage(query.getPage(), query.getLimit());
         List<GoodsVo> goodsList = goodsService.queryList(query);
         logger.info("===========================通过关键字"+keyword+"进行检索的结果=======================");
         logger.info("===========================商品个数:"+goodsList.size());
         logger.info("===========================分类个数:"+filterCategory.size());
    	
        ApiPageUtils goodsData = new ApiPageUtils(new PageInfo(goodsList));
        goodsData.setFilterCategory(filterCategory);
        goodsData.setGoodsList(goodsList);
        return toResponsSuccess(goodsData);
    }
    
    
    
    
    
    
    
    
    /**
     * 　　商品列表筛选的分类列表
     */
    @ApiOperation(value = "商品列表筛选的分类列表")
    @IgnoreAuth
    @PostMapping(value = "filter")
    public Object filter(Integer categoryId,
                         String keyword, Integer isNew, Integer isHot) {
        Map params = new HashMap();
        params.put("is_delete", 0);
        params.put("is_on_sale", 1);
        params.put("categoryId", categoryId);
        params.put("keyword", keyword);
        params.put("isNew", isNew);
        params.put("isHot", isHot);
        if (null != categoryId) {
            Map categoryParams = new HashMap();
            categoryParams.put("categoryId", categoryId);
            List<CategoryVo> categoryEntityList = categoryService.queryList(categoryParams);
            List<Integer> category_ids = new ArrayList();
            for (CategoryVo categoryEntity : categoryEntityList) {
                category_ids.add(categoryEntity.getId());
            }
            params.put("category_id", category_ids);
        }
        //筛选的分类
        List<CategoryVo> filterCategory = new ArrayList();
        CategoryVo rootCategory = new CategoryVo();
        rootCategory.setId(0);
        rootCategory.setName("全部");
        // 二级分类id
        List<GoodsVo> goodsEntityList = goodsService.queryList(params);
        if (null != goodsEntityList && goodsEntityList.size() > 0) {
            List<Integer> categoryIds = new ArrayList();
            for (GoodsVo goodsEntity : goodsEntityList) {
                categoryIds.add(goodsEntity.getCategory_id());
            }
            //查找二级分类的parent_id
            Map categoryParam = new HashMap();
            categoryParam.put("categoryIds", categoryIds);
            List<CategoryVo> parentCategoryList = categoryService.queryList(categoryParam);
            //
            List<Integer> parentIds = new ArrayList();
            for (CategoryVo categoryEntity : parentCategoryList) {
                parentIds.add(categoryEntity.getId());
            }
            //一级分类
            categoryParam.put("categoryIds", parentIds);
            List<CategoryVo> parentCategory = categoryService.queryList(categoryParam);
            if (null != parentCategory) {
                filterCategory.addAll(parentCategory);
            }
        }
        return toResponsSuccess(filterCategory);
    }

    /**
     * 　　新品首发
     */
    @ApiOperation(value = "新品首发")
    @IgnoreAuth
    @PostMapping(value = "new")
    public Object newAction() {
        Map<String, Object> resultObj = new HashMap();
        Map bannerInfo = new HashMap();
        bannerInfo.put("url", "");
        bannerInfo.put("name", "坚持初心，为你寻觅世间好物");
        bannerInfo.put("img_url", "https://platform-wxmall.oss-cn-beijing.aliyuncs.com/upload/20180727/1504208321fef4.png");
        resultObj.put("bannerInfo", bannerInfo);
        return toResponsSuccess(resultObj);
    }

    /**
     * 　　人气推荐
     */
    @ApiOperation(value = "人气推荐")
    @IgnoreAuth
    @PostMapping(value = "hot")
    public Object hot() {
        Map<String, Object> resultObj = new HashMap();
        Map bannerInfo = new HashMap();
        bannerInfo.put("url", "");
        bannerInfo.put("name", "大家都在买的严选好物");
        bannerInfo.put("img_url", "https://platform-wxmall.oss-cn-beijing.aliyuncs.com/upload/20180727/1504208321fef4.png");
        resultObj.put("bannerInfo", bannerInfo);
        return toResponsSuccess(resultObj);
    }

    /**
     * 　　商品详情页的大家都在看的商品
     */
    @ApiOperation(value = "商品详情页")
    @IgnoreAuth
    @PostMapping(value = "related")
    public Object related(Integer id) {
        Map<String, Object> resultObj = new HashMap();
        Map param = new HashMap();
        param.put("goods_id", id);
        param.put("fields", "related_goods_id");
        List<RelatedGoodsVo> relatedGoodsEntityList = relatedGoodsService.queryList(param);

        List<Integer> relatedGoodsIds = new ArrayList();
        for (RelatedGoodsVo relatedGoodsEntity : relatedGoodsEntityList) {
            relatedGoodsIds.add(relatedGoodsEntity.getRelated_goods_id());
        }

        List<GoodsVo> relatedGoods = new ArrayList<GoodsVo>();

        if (null == relatedGoodsIds || relatedGoods.size() < 1) {
            //查找同分类下的商品
            GoodsVo goodsCategory = goodsService.queryObject(id);
            Map paramRelated = new HashMap();
            paramRelated.put("fields", "nideshop_goods.id, name, list_pic_url, nideshop_goods.retail_price,nideshop_goods.market_price,case when min(nideshop_product.market_price) != '' then min(nideshop_product.market_price) else 0 end product_market_price");
            paramRelated.put("category_id", goodsCategory.getCategory_id());
            relatedGoods = goodsService.queryList(paramRelated);
        } else {
            Map paramRelated = new HashMap();
            paramRelated.put("goods_ids", relatedGoodsIds);
            paramRelated.put("fields", "nideshop_goods.id, name, list_pic_url, nideshop_goods.retail_price,nideshop_goods.market_price,case when min(nideshop_product.market_price) != '' then min(nideshop_product.market_price) else 0 end product_market_price");
            relatedGoods = goodsService.queryList(paramRelated);
        }
        //设置默认展示最低价格的商品规格
        if(CollectionUtils.isNotEmpty(relatedGoods)){
        	for(GoodsVo GoodsVo : relatedGoods){
        		if(GoodsVo.getProduct_market_price().compareTo(BigDecimal.ZERO) > 0){
        			GoodsVo.setMarket_price(GoodsVo.getProduct_market_price());
                }
            }
        }
        resultObj.put("goodsList", relatedGoods);
        return toResponsSuccess(resultObj);
    }

    /**
     * 　　在售的商品总数
     */
    @ApiOperation(value = "在售的商品总数")
    @IgnoreAuth
    @PostMapping(value = "count")
    public Object count() {
        Map<String, Object> resultObj = new HashMap();
        Map param = new HashMap();
        param.put("is_delete", 0);
        param.put("is_on_sale", 1);
        Integer goodsCount = goodsService.queryTotal(param);
        resultObj.put("goodsCount", goodsCount);
        return toResponsSuccess(resultObj);
    }

    /**
     * 　　获取商品列表
     */
    @ApiOperation(value = "获取商品列表")
    @IgnoreAuth
    @PostMapping(value = "productlist")
    public Object productlist(Integer categoryId,
                              Integer isNew, Integer discount,
                              @RequestParam(value = "page", defaultValue = "1") Integer page, @RequestParam(value = "size", defaultValue = "10") Integer size,
                              String sort, String order) {
        Map params = new HashMap();
        params.put("is_new", isNew);
        params.put("page", page);
        params.put("limit", size);
        params.put("order", sort);
        params.put("sidx", order);
        //
        if (null != sort && sort.equals("price")) {
            params.put("sidx", "retail_price");
            params.put("order", order);
        } else if (null != sort && sort.equals("sell")) {
            params.put("sidx", "orderNum");
            params.put("order", order);
        } else {
            params.put("sidx", "id");
            params.put("order", "desc");
        }
        // 0不限 1特价 2团购
        if (null != discount && discount == 1) {
            params.put("is_hot", 1);
        } else if (null != discount && discount == 2) {
            params.put("is_group", true);
        }
        //加入分类条件
        if (null != categoryId && categoryId > 0) {
            List<Integer> categoryIds = new ArrayList();
            Map categoryParam = new HashMap();
            categoryParam.put("parent_id", categoryId);
            categoryParam.put("fields", "id");
            List<CategoryVo> childIds = categoryService.queryList(categoryParam);
            for (CategoryVo categoryEntity : childIds) {
                categoryIds.add(categoryEntity.getId());
            }
            categoryIds.add(categoryId);
            params.put("categoryIds", categoryIds);
        }
        //查询列表数据
        Query query = new Query(params);
        List<GoodsVo> goodsList = goodsService.queryCatalogProductList(query);
        int total = goodsService.queryTotal(query);

        // 当前购物车中
        List<CartVo> cartList = new ArrayList();
        if (null != getUserId()) {
            //查询列表数据
            Map cartParam = new HashMap();
            cartParam.put("user_id", getUserId());
            cartList = cartService.queryList(cartParam);
        }
        if (null != cartList && cartList.size() > 0 && null != goodsList && goodsList.size() > 0) {
            for (GoodsVo goodsVo : goodsList) {
                for (CartVo cartVo : cartList) {
                    if (goodsVo.getId().equals(cartVo.getGoods_id())) {
                        goodsVo.setCart_num(cartVo.getNumber());
                    }
                }
            }
        }
        ApiPageUtils goodsData = new ApiPageUtils(goodsList, total, query.getLimit(), query.getPage());
        goodsData.setGoodsList(goodsData.getData());
        return toResponsSuccess(goodsData);
    }
}