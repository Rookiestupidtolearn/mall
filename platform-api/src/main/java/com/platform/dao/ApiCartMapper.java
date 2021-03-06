package com.platform.dao;

import com.platform.entity.CartVo;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

/**
 * @author lipengjun
 * @email 939961241@qq.com
 * @date 2017-08-11 09:14:25
 */
public interface ApiCartMapper extends BaseDao<CartVo> {
    void updateCheck(@Param("productIds") String[] productIds,
                     @Param("isChecked") Integer isChecked, @Param("userId") Long userId);

    void deleteByProductIds(@Param("productIds") String[] productIds);

    void deleteByUserAndProductIds(@Param("user_id") Long user_id,@Param("productIds") String[] productIds);

    void deleteByCart(@Param("user_id") Long user_id, @Param("session_id") Integer session_id, @Param("checked") Integer checked);
    
    void deleteCartBy(@Param("user_id") Long user_id, @Param("goods_id") Integer goods_id);
    
    List<CartVo> getCarts(Map<String, Object> params);
    //根据用户id查询购物车
    List<CartVo> queryUserCarts(Long user_id);

	/**
	 * 根据购物车ids查询购物车信息
	 * @param cartEntityIds
	 * @return
	 */
	List<CartVo> queryCartsByCartId(Integer[] cartEntityIds);

	/**
	 * 根据条件查询购物车中的商品数据
	 * @param id
	 * @param i
	 * @return
	 */
	List<CartVo> queryCartListByGoodsId(Integer id, int i);
	/**
	 * 查询所有购物车
	 * @return
	 */
	List<CartVo> quertAllCarts();
	/**
	 * 批量查询购物车信息_下架
	 * @param userId
	 * @param cartEntityIds
	 * @return
	 */
	List<CartVo> queryRollbackUserCarts(@Param("cartEntityIds")Integer[] cartEntityIds,@Param("userId") Long userId);
}
