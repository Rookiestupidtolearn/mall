package com.platform.dao;

import java.util.List;
import java.util.Map;

import com.platform.entity.DoubaoSearchGoods;

public interface DoubaoSearchGoodsMapper {

	List<DoubaoSearchGoods> queryDoubaoSearchGoodsList(String keyword);

	List<Integer> qureyGoodsIdByparam(Map<String, Object> params);

}
