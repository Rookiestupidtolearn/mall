package com.platform.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.platform.dao.DoubaoSearchGoodsMapper;
import com.platform.entity.DoubaoSearchGoods;

@Service
public class DoubaoSearchGoodsService {
		
		@Autowired
		private DoubaoSearchGoodsMapper doubaoSearchGoodsMapper;

		/**
		 * 通过关键字检索商品id和分类id
		 * @param keyword
		 * @return
		 */
		public List<DoubaoSearchGoods> queryDoubaoSearchGoodsList(String keyword) {
			// TODO Auto-generated method stub
			return doubaoSearchGoodsMapper.queryDoubaoSearchGoodsList(keyword);
		}

		/**
		 * 获取商品id
		 * @param params
		 * @return
		 */
		public List<Integer> qureyGoodsIdByparam(Map<String, Object> params) {
			// TODO Auto-generated method stub
			return doubaoSearchGoodsMapper.qureyGoodsIdByparam(params);
		} 
		
		
		
		
}
