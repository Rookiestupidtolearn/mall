package com.platform.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.shiro.util.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.platform.annotation.IgnoreAuth;
import com.platform.dao.ApiGoodsMapper;
import com.platform.entity.CategoryVo;
import com.platform.entity.GoodsVo;
import com.platform.service.ApiCategoryService;
import com.platform.util.ApiBaseAction;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

/**
 * 作者: @author Harmon <br>
 * 时间: 2017-08-11 08:32<br>
 * 描述: ApiIndexController <br>
 */
@Api(tags = "栏目")
@RestController
@RequestMapping("/api/catalog")
public class ApiCatalogController extends ApiBaseAction {
    @Autowired
    private ApiCategoryService categoryService;
    @Autowired
    private ApiGoodsMapper apiGoodsMapper;

    /**
     * 获取分类栏目数据
     */
    @ApiOperation(value = "获取分类栏目数据")
    @ApiImplicitParams({@ApiImplicitParam(name = "id", value = "id", paramType = "query", required = false),
            @ApiImplicitParam(name = "page", value = "page", paramType = "query", required = false),
            @ApiImplicitParam(name = "size", value = "size", paramType = "query", required = false)})
    @IgnoreAuth
    @PostMapping(value = "index")
    public Object index(Integer id,
                        @RequestParam(value = "page", defaultValue = "1") Integer page,
                        @RequestParam(value = "size", defaultValue = "20") Integer size) {
        Map<String, Object> resultObj = new HashMap();
        Map params = new HashMap();
        params.put("page", page);
        params.put("limit", size);
        params.put("sidx", "sort_order");
        params.put("order", "asc");
        params.put("parent_id", 0);
        //查询列表数据
        List<CategoryVo> newData = new ArrayList<>();
        List<CategoryVo> data = categoryService.queryList(params);
        List<CategoryVo> newCategorys = new ArrayList<>();
        if (!CollectionUtils.isEmpty(data)) {
            for (CategoryVo vo : data) {
                List<CategoryVo> subData = categoryService.quertSubCategorys(vo.getId());
                if (!CollectionUtils.isEmpty(subData)) {
                    for (CategoryVo sub : subData) {
//        				List<GoodsVo> goods = apiGoodsMapper.quertGoodsByCategory(sub.getId().toString());
//                		if(!CollectionUtils.isEmpty(goods)){
//                			newData.add(vo);
//                			break;
//                		}
                        if (sub.getWap_banner_url() != null) {
                            newData.add(vo);
                            break;
                        }
                        if ("热销".equals(sub.getName()) && "其他".equals(sub.getName())) {
                            newData.add(vo);
                        }
                    }
                }
            }
        }
        //
        CategoryVo currentCategory = null;
        if (null != id) {
            currentCategory = categoryService.queryObject(id);
        }
        if (null == currentCategory && null != newData && newData.size() != 0) {
            currentCategory = newData.get(0);
        } else {
            currentCategory = new CategoryVo();
        }

        //获取子分类数据
        if (null != currentCategory && null != currentCategory.getId()) {
            params.put("parent_id", currentCategory.getId());
            List<CategoryVo> subCategorys = categoryService.queryListOfGoodsNotNull(params);
            if (!CollectionUtils.isEmpty(subCategorys)) {
                for (CategoryVo vo : subCategorys) {
//            		List<GoodsVo> goods = apiGoodsMapper.quertGoodsByCategory(vo.getId().toString());
//            		if(!CollectionUtils.isEmpty(goods)){
//            			newCategorys.add(vo);
//            		}
                    if (vo.getWap_banner_url() != null) {
                        newCategorys.add(vo);
                    }
                }
            }
            currentCategory.setSubCategoryList(newCategorys);
        }

        resultObj.put("categoryList", newData);
        resultObj.put("currentCategory", currentCategory);
        return toResponsSuccess(resultObj);
    }

    /**
     *
     */
    @ApiOperation(value = "分类目录当前分类数据接口")
    @ApiImplicitParams({@ApiImplicitParam(name = "id", value = "id", paramType = "query", required = false)})
    @IgnoreAuth
    @PostMapping(value = "current")
    public Object current(Integer id) {
        Map<String, Object> resultObj = new HashMap();
        Map params = new HashMap();
        params.put("parent_id", 0);
        List<CategoryVo> newCategorys = new ArrayList<>();
        CategoryVo currentCategory = null;
        if (null != id) {
            currentCategory = categoryService.queryObject(id);
        }
        //获取子分类数据
        if (null != currentCategory && null != currentCategory.getId()) {
            params.put("parent_id", currentCategory.getId());
//            List<CategoryVo> subCategorys = categoryService.queryList(params);
            List<CategoryVo> subCategorys = categoryService.queryListOfGoodsNotNull(params);
            if (!CollectionUtils.isEmpty(subCategorys)) {
                for (CategoryVo vo : subCategorys) {
                    if ("热销".equals(vo.getName()) && "其他".equals(currentCategory.getName())) {
                        newCategorys.add(vo);
                    } else {
//            			List<GoodsVo> goods = apiGoodsMapper.quertGoodsByCategory(vo.getId().toString());
                        if (vo.getWap_banner_url() != null) {
                            newCategorys.add(vo);
                        }
                    }
                }
            }
            currentCategory.setSubCategoryList(newCategorys);
        }
        resultObj.put("currentCategory", currentCategory);
        return toResponsSuccess(resultObj);
    }

    /**
     * 查询根分类或者子分类列表
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "查询根分类或者子分类列表")
    @ApiImplicitParams({@ApiImplicitParam(name = "id", value = "id", paramType = "query", required = false)})
    @PostMapping(value = "queryCategory")
    public Object queryCategory(Integer id) {
        Map<String, Object> resultObj = new HashMap();
        if (null == id) {
            resultObj.put("data", "error");
            return resultObj;
        }
        List<CategoryVo> list = categoryService.quertSubCategorys(id);
        resultObj.put("data", list);
        return resultObj;
    }

}