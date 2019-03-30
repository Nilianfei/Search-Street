package com.graduation.ss.service.impl;

import java.util.ArrayList;
//import java.lang.annotation.Annotation;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.graduation.ss.dao.OrderDao;
import com.graduation.ss.dao.ServiceDao;
import com.graduation.ss.dao.ShopCommentDao;
import com.graduation.ss.dao.ShopDao;
import com.graduation.ss.dto.ShopCommentExecution;
import com.graduation.ss.entity.OrderInfo;
import com.graduation.ss.entity.ServiceInfo;
import com.graduation.ss.entity.Shop;
import com.graduation.ss.entity.ShopComment;
import com.graduation.ss.enums.ShopCommentStateEnum;
import com.graduation.ss.exceptions.ShopCommentOperationException;
import com.graduation.ss.service.ShopCommentService;
import com.graduation.ss.util.PageCalculator;

@Service
public class ShopCommentServiceImpl implements ShopCommentService {
	@Autowired
	private ShopCommentDao shopCommentDao;
	@Autowired
	private ShopDao shopDao;
	@Autowired
	private ServiceDao serviceDao;
	@Autowired
	private OrderDao orderDao;

	@Override
	public ShopCommentExecution getShopCommentList(ShopComment shopCommentCondition, int pageIndex, int pageSize) {
		// 将页码转换成行码
		int rowIndex = PageCalculator.calculateRowIndex(pageIndex, pageSize);
		// 依据查询条件，调用dao层返回相关的评论列表
		List<ShopComment> shopCommentList = shopCommentDao.queryShopCommentList(shopCommentCondition, rowIndex, pageSize);
		// 依据相同的查询条件，返回评论总数
		int count = shopCommentDao.queryShopCommentCount(shopCommentCondition);
		ShopCommentExecution se = new ShopCommentExecution();
		if (shopCommentList != null) {
			se.setShopCommentList(shopCommentList);
			se.setCount(count);
		} else {
			se.setState(ShopCommentStateEnum.INNER_ERROR.getState());
		}
		return se;
	}
	@Override
	public ShopCommentExecution getByShopId(long shopId, int pageIndex, int pageSize){
		// 将页码转换成行码
		int rowIndex = PageCalculator.calculateRowIndex(pageIndex, pageSize);
		// 依据查询条件，调用dao层返回相关的评论列表
		
		ShopComment shopCommentCondition = new ShopComment();
		shopCommentCondition.setShopId(shopId);
		List<ShopComment> shopCommentList = shopCommentDao.queryShopCommentList(shopCommentCondition, rowIndex, pageSize);
		ShopCommentExecution se = new ShopCommentExecution();
		// 依据相同的查询条件，返回评论总数
		int count = shopCommentDao.queryShopCommentCount(shopCommentCondition);
		if (shopCommentList != null) {
			se.setShopCommentList(shopCommentList);
			se.setCount(count);
		} else {
			se.setState(ShopCommentStateEnum.INNER_ERROR.getState());
		}
		return se;
	}
	@Override
	public ShopCommentExecution getByShopId2(long shopId){
		// 依据查询条件，调用dao层返回相关的评论列表
		
		ShopComment shopCommentCondition = new ShopComment();
		shopCommentCondition.setShopId(shopId);
		List<ShopComment> shopCommentList = shopCommentDao.queryShopCommentList2(shopCommentCondition);
		ShopCommentExecution se = new ShopCommentExecution();
		// 依据相同的查询条件，返回评论总数
		int count = shopCommentDao.queryShopCommentCount(shopCommentCondition);
		if (shopCommentList != null) {
			se.setShopCommentList(shopCommentList);
			se.setCount(count);
		} else {
			se.setState(ShopCommentStateEnum.INNER_ERROR.getState());
		}
		return se;
	}
	@Override
	public Shop getAvgByShopId(long shopId)
	{
		int serviceAvg = shopCommentDao.queryAvgServiceRating(shopId);
		int starAvg=shopCommentDao.queryAvgStarRating(shopId);
		Shop shop=shopDao.queryByShopId(shopId);
		List<OrderInfo> orderlist=new ArrayList<OrderInfo>();
		ServiceInfo service=new ServiceInfo();
		service.setShopId(shopId);
		List<ServiceInfo> servicelist=serviceDao.queryServiceList2(service);
		List<OrderInfo> torderlist=new ArrayList<OrderInfo>();
		for(int i=0;i<servicelist.size();i++)
		{
			OrderInfo  orderCondition = new OrderInfo ();
			orderCondition.setServiceId(servicelist.get(i).getServiceId());
			torderlist.addAll(orderDao.queryOrderList3(orderCondition));
			orderCondition.setOrderStatus(3);
			orderlist.addAll(orderDao.queryOrderList2(orderCondition));
		}
		int torderNum=torderlist.size();
		if(torderNum!=0)
		{
			int forderNum=torderNum-orderlist.size();
			float s=((float)forderNum/torderNum)*100;
			int successRate=(int)s;
			shop.setSuccessRate(successRate);
		}
		shop.setServiceAvg(serviceAvg);
		shop.setStarAvg(starAvg);
		return shop;
		
	}
	@Override
	public ShopCommentExecution getByUserId2(long userId){
		// 依据查询条件，调用dao层返回相关的评论列表
		
		ShopComment shopCommentCondition = new ShopComment();
		shopCommentCondition.setUserId(userId);

		List<ShopComment> shopCommentList = shopCommentDao.queryShopCommentList2(shopCommentCondition);
		ShopCommentExecution se = new ShopCommentExecution();
		// 依据相同的查询条件，返回评论总数
		int count = shopCommentDao.queryShopCommentCount(shopCommentCondition);
		if (shopCommentList != null) {
			se.setShopCommentList(shopCommentList);
			se.setCount(count);
		} else {
			se.setState(ShopCommentStateEnum.INNER_ERROR.getState());
		}
		return se;
	}
	@Override
	public ShopCommentExecution getByUserId(long userId, int pageIndex, int pageSize){
		// 将页码转换成行码
		int rowIndex = PageCalculator.calculateRowIndex(pageIndex, pageSize);
		// 依据查询条件，调用dao层返回相关的评论列表
		
		ShopComment shopCommentCondition = new ShopComment();
		shopCommentCondition.setUserId(userId);
		List<ShopComment> shopCommentList = shopCommentDao.queryShopCommentList(shopCommentCondition, rowIndex, pageSize);
		ShopCommentExecution se = new ShopCommentExecution();
		// 依据相同的查询条件，返回评论总数
		int count = shopCommentDao.queryShopCommentCount(shopCommentCondition);
		if (shopCommentList != null) {
			se.setShopCommentList(shopCommentList);
			se.setCount(count);
		} else {
			se.setState(ShopCommentStateEnum.INNER_ERROR.getState());
		}
		return se;
	}
	@Override
	public ShopComment getByShopCommentId(long shopCommentId) {
		return shopCommentDao.queryByShopCommentId(shopCommentId);
	}
   
	@Override
	public ShopCommentExecution addShopComment(ShopComment shopComment) throws ShopCommentOperationException {
		// 空值判断
		if (shopComment == null) {
			return new ShopCommentExecution(ShopCommentStateEnum.NULL_ShopComment);
		}
		try {
			// 添加评论信息（从前端读取数据）
			int effectedNum = shopCommentDao.insertShopComment(shopComment);
			if (effectedNum <= 0) {
				throw new ShopCommentOperationException("评论创建失败");
			}
		} catch (Exception e) {
			throw new ShopCommentOperationException("addShopComment error:" + e.getMessage());
		}
		return new ShopCommentExecution(ShopCommentStateEnum.SUCCESS, shopComment);
	}
	
	@Transactional
	@Override
	public ShopCommentExecution modifyShopComment(ShopComment shopComment) throws ShopCommentOperationException{
		// 空值判断
		if (shopComment == null) {
			return new ShopCommentExecution(ShopCommentStateEnum.NULL_ShopComment);
		}
		try {
			// 修改评论信息
			int effectedNum = shopCommentDao.updateShopComment(shopComment);
			if (effectedNum <= 0) {
				throw new ShopCommentOperationException("评论修改失败");
			}
		} catch (Exception e) {
			throw new ShopCommentOperationException("modifyShopComment error:" + e.getMessage());
		}
		return new ShopCommentExecution(ShopCommentStateEnum.SUCCESS, shopComment);
	}

	
	@Override
	public ShopCommentExecution deleteShopComment(ShopComment shopComment) throws ShopCommentOperationException
	{
		// 空值判断
				if (shopComment == null) {
					return new ShopCommentExecution(ShopCommentStateEnum.NULL_ShopComment);
				}
				try {
					// 删除评论信息
					int effectedNum = shopCommentDao.deleteShopComment(shopComment);
					if (effectedNum <= 0) {
						throw new ShopCommentOperationException("评论删除失败");
					}
				} catch (Exception e) {
					throw new ShopCommentOperationException("deleteShopComment error:" + e.getMessage());
				}
				return new ShopCommentExecution(ShopCommentStateEnum.SUCCESS, shopComment);
	}




}