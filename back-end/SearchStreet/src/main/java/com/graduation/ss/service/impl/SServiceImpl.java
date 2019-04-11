package com.graduation.ss.service.impl;

import java.util.ArrayList;
import java.util.Comparator;
//import java.lang.annotation.Annotation;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.graduation.ss.dao.ServiceDao;
import com.graduation.ss.dao.ServiceImgDao;
import com.graduation.ss.dto.ImageHolder;
import com.graduation.ss.dto.ServiceExecution;
import com.graduation.ss.entity.ServiceImg;
import com.graduation.ss.entity.ServiceInfo;
import com.graduation.ss.enums.ServiceStateEnum;
import com.graduation.ss.exceptions.ServiceOperationException;
import com.graduation.ss.service.SService;
import com.graduation.ss.util.ImageUtil;
import com.graduation.ss.util.PageCalculator;
import com.graduation.ss.util.PathUtil;

@Service
public class SServiceImpl implements SService {
	@Autowired
	private ServiceDao serviceDao;
	@Autowired
	private ServiceImgDao serviceImgDao;
	// 字符串按照整型排序比较器
    static class PriorityComparator implements Comparator<ServiceInfo> {
        private boolean reverseOrder; // 是否倒序
        public PriorityComparator(boolean reverseOrder) {
            this.reverseOrder = reverseOrder;
        }
        
        public int compare(ServiceInfo arg0, ServiceInfo arg1) {
            if(reverseOrder) 
                return (int)(arg1.getServicePriority() - arg0.getServicePriority());
            else 
                return (int)(arg0.getServicePriority() - arg1.getServicePriority());
        }


    }

	@Override
	public ServiceExecution getServiceList(ServiceInfo serviceCondition, int pageIndex, int pageSize) {
		// 将页码转换成行码
		int rowIndex = PageCalculator.calculateRowIndex(pageIndex, pageSize);
		// 依据查询条件，调用dao层返回相关的服务列表
		List<ServiceInfo> serviceList = serviceDao.queryServiceList(serviceCondition, rowIndex, pageSize);
		// 依据相同的查询条件，返回服务总数
		int count = serviceDao.queryServiceCount(serviceCondition);
		 // Collections.sort(serviceList, new PriorityComparator(true));
		  ServiceExecution se = new ServiceExecution();
		if (serviceList != null) {
			se.setServiceList(serviceList);
			se.setCount(count);
		} else {
			se.setState(ServiceStateEnum.INNER_ERROR.getState());
		}
		return se;
	}
	@Override
	public ServiceExecution getByShopId(long shopId, int pageIndex, int pageSize){
		// 将页码转换成行码
		int rowIndex = PageCalculator.calculateRowIndex(pageIndex, pageSize);
		// 依据查询条件，调用dao层返回相关的服务列表
		
		ServiceInfo serviceCondition = new ServiceInfo();
		serviceCondition.setShopId(shopId);
		List<ServiceInfo> serviceList = serviceDao.queryServiceList(serviceCondition, rowIndex, pageSize);
		ServiceExecution se = new ServiceExecution();
		// 依据相同的查询条件，返回服务总数
		int count = serviceDao.queryServiceCount(serviceCondition);
		if (serviceList != null) {
			se.setServiceList(serviceList);
			se.setCount(count);
		} else {
			se.setState(ServiceStateEnum.INNER_ERROR.getState());
		}
		return se;
	}
	@Override
	public ServiceExecution getByShopId2(long shopId){
		// 依据查询条件，调用dao层返回相关的服务列表
		ServiceInfo serviceCondition = new ServiceInfo();
		serviceCondition.setShopId(shopId);
		List<ServiceInfo> serviceList = serviceDao.queryServiceList2(serviceCondition);
		ServiceExecution se = new ServiceExecution();
		// 依据相同的查询条件，返回服务总数
		
		if (serviceList != null) {
			se.setServiceList(serviceList);
			se.setCount(serviceList.size());
		} 
		else {
			se.setState(ServiceStateEnum.INNER_ERROR.getState());
		}
		return se;
	}
	@Override
	public ServiceInfo getByServiceId(long serviceId) {
		return serviceDao.queryByServiceId(serviceId);
	}
    //更新图片
	@Override
	@Transactional
	public ServiceExecution uploadImg(long serviceId, ImageHolder serviceImgHolder,Date createTime)throws ServiceOperationException 
	{
		ServiceExecution serviceExecution = null;
		try {	
			// 根据serviceId获取原来的图片
			ServiceImg serviceImg = serviceImgDao.getServiceImg(serviceId);
          if (serviceImgHolder != null && serviceImgHolder.getImage() != null && serviceImgHolder.getImageName() != null
	        && !"".equals(serviceImgHolder.getImageName())) {
        	  if (serviceImg!=null){
        		//图片存在，则删除图片
					serviceExecution=deleteServiceImg(serviceImg);
        	  }
        	  if (createTime==null) {
  					
  					return new ServiceExecution(ServiceStateEnum.NULL_SERVICEIMG_CREATETIME);
  				}
        		  serviceExecution=addServiceImg(serviceId, serviceImgHolder,createTime);
          }
		}
		catch (Exception e) {
			throw new ServiceOperationException("updateServiceImg error:" + e.getMessage());
		}
		return serviceExecution;
}
	@Override
	public ServiceExecution createServiceImg(Long serviceId, ImageHolder serviceImgHolder)
			throws ServiceOperationException {
		if (serviceId != null&&serviceId>0) {
			try {
				ServiceInfo service=new ServiceInfo();
				service=serviceDao.queryByServiceId(serviceId);
				if(service==null)
				{
					return new ServiceExecution(ServiceStateEnum.NULL_Service);
				}
				else
				{
					//图片存在，应更改图片，而非添加图片
					String imgaddr=service.getServiceImgAddr();
					if(imgaddr==null||imgaddr==""||imgaddr.equals(""))
					{
						addServiceImg(serviceId, serviceImgHolder,new Date());
						service.setServiceImgAddr(serviceImgDao.getServiceImg(serviceId).getImgAddr());
						int effectedNum = serviceDao.updateService(service);
						if (effectedNum <= 0) {
							throw new ServiceOperationException("服务修改失败");
						}
					}
					else
					{
						return new ServiceExecution(ServiceStateEnum.NOT_NULL_ServiceImg);
				
					}
				}
			} catch (Exception e) {
				throw new ServiceOperationException("createServiceImg error:" + e.toString());
			}
		} else {
			return new ServiceExecution(ServiceStateEnum.NULL_ServiceImg);
		}
		return new ServiceExecution(ServiceStateEnum.SUCCESS);
	}		

	@Override
	public ServiceExecution addService(ServiceInfo service) throws ServiceOperationException {
		// 空值判断
		if (service == null) {
			return new ServiceExecution(ServiceStateEnum.NULL_Service);
		}
		try {
			// 添加服务信息（从前端读取数据）
			int effectedNum = serviceDao.insertService(service);
			if (effectedNum <= 0) {
				throw new ServiceOperationException("服务创建失败");
			}
		} catch (Exception e) {
			throw new ServiceOperationException("addService error:" + e.getMessage());
		}
		return new ServiceExecution(ServiceStateEnum.SUCCESS, service);
	}
	@Transactional
	@Override
	public ServiceExecution modifyService(ServiceInfo service) throws ServiceOperationException{
		// 空值判断
		if (service == null) {
			return new ServiceExecution(ServiceStateEnum.NULL_Service);
		}
		try {
			// 修改服务信息
			int effectedNum = serviceDao.updateService(service);
			if (effectedNum <= 0) {
				throw new ServiceOperationException("服务修改失败");
			}
		} catch (Exception e) {
			throw new ServiceOperationException("modifyService error:" + e.getMessage());
		}
		return new ServiceExecution(ServiceStateEnum.SUCCESS, service);
	}

	private ServiceExecution addServiceImg(long serviceId, ImageHolder serviceImgHolder,Date createTime) {
		// 获取图片存储路径，这里直接存放到相应服务的文件夹底下
		String dest = PathUtil.getServiceImgPath(serviceId);
		String imgAddr = ImageUtil.generateNormalImg(serviceImgHolder, dest);
	    ServiceImg serviceImg = new ServiceImg();
	    serviceImg.setImgAddr(imgAddr);
	    serviceImg.setServiceId(serviceId);
	    serviceImg.setCreateTime(createTime);
		try {
			int effectedNum =serviceImgDao.insertServiceImg(serviceImg);
			if (effectedNum <= 0) {
				throw new ServiceOperationException("创建服务图片失败");
			}
		} catch (Exception e) {
			throw new ServiceOperationException("创建服务图片失败:" + e.toString());
		}
		return new ServiceExecution(ServiceStateEnum.SUCCESS, serviceImg);
	}
	@Override
	public ServiceExecution getServiceImgList(ServiceImg serviceImg,int pageIndex, int pageSize) {
		// 将页码转换成行码
		int rowIndex = PageCalculator.calculateRowIndex(pageIndex, pageSize);
		// 依据查询条件，调用dao层返回相关的服务图片列表
		List<ServiceImg> serviceImgList = serviceImgDao.queryServiceImg(serviceImg, rowIndex, pageSize);
		ServiceExecution aie = new ServiceExecution();
		int count = serviceImgDao.queryServiceImgCount(serviceImg);
		if (serviceImgList != null) {
			aie.setServiceImgList(serviceImgList);
			aie.setCount(count);
		} else {
			aie.setState(ServiceStateEnum.INNER_ERROR.getState());
		}
		return aie;
	}
	@Override
	public ServiceExecution getServiceImg(long serviceId) {
		// 依据查询条件，调用dao层返回相关的服务图片列表
		ServiceImg serviceImg = serviceImgDao.getServiceImg(serviceId);
		ServiceExecution aie = new ServiceExecution();
		if (serviceImg != null) {
			List<ServiceImg> serviceImgList =new ArrayList<ServiceImg>();
			serviceImgList.add(serviceImg);
			aie.setServiceImgList(serviceImgList);
			aie.setCount(1);
		} else {
			aie.setState(ServiceStateEnum.INNER_ERROR.getState());
		}
		return aie;
	}
	/**
	 * 删除某个店铺下的服务详情图
	 * 
	 * @param serviceId
	 */
	public ServiceExecution deleteServiceImg(ServiceImg serviceImg) {
		// 空值判断
		if (serviceImg == null) {
					return new ServiceExecution(ServiceStateEnum.NULL_ServiceImg);
		}
		// 删除原来的图片
	    ImageUtil.deleteFileOrPath(serviceImg.getImgAddr());	
		try {
			// 删除数据库里原有图片的信息
			int effectedNum = serviceImgDao.deleteServiceImg(serviceImg.getServiceId());
			if (effectedNum <= 0) {
				throw new ServiceOperationException("服务图片删除失败");
			}
		} catch (Exception e) {
			throw new ServiceOperationException("deleteServiceImg error:" + e.getMessage());
		}
		return new ServiceExecution(ServiceStateEnum.SUCCESS, serviceImg);
}

	@Override
	public ServiceExecution deleteService(long serviceId) throws ServiceOperationException
	{
				if(serviceId<=0)
				{
					return new ServiceExecution(ServiceStateEnum.NULL_SeriveId);
				}
				try {
					// 删除服务信息
					int effectedNum = serviceDao.deleteService(serviceId);
					
					if (effectedNum <= 0) {
						throw new ServiceOperationException("服务删除失败");					
					}
					ServiceImg serviceImg=serviceImgDao.getServiceImg(serviceId);
					if(serviceImg!=null)
					deleteServiceImg(serviceImg);
				} catch (Exception e) {
					throw new ServiceOperationException("deleteService error:" + e.getMessage());
				}
				return new ServiceExecution(ServiceStateEnum.SUCCESS,serviceDao.queryByServiceId(serviceId));
	}




}
