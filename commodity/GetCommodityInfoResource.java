package com.zizaihome.api.resources.commodity;

import org.restlet.Request;

import com.zizaihome.api.db.model.BuddnistCeremonyCommodityOrderModel;
import com.zizaihome.api.db.model.BuddnistCeremonyCommoditySubdivideModel;
import com.zizaihome.api.resources.base.BaseResource;
import com.zizaihome.api.service.BuddnistCeremonyCommodityOrderService;
import com.zizaihome.api.service.BuddnistCeremonyCommoditySubdivideService;

import net.sf.json.JSONObject;

public class GetCommodityInfoResource extends BaseResource{

	@Override
	protected void initParams(Request request) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected JSONObject getMethod(JSONObject json) {
		return postMethod(json);
	}

	@Override
	protected JSONObject postMethod(JSONObject json) {
		int orderId=getParameter("orderId", 0);
		if (orderId==0) {
			return failRequest("参数错误", json);
		}
		BuddnistCeremonyCommodityOrderService orderService=new BuddnistCeremonyCommodityOrderService();
		BuddnistCeremonyCommodityOrderModel orderModel=orderService.getModel(orderId);
		if (orderModel==null) {
			return failRequest("不存在", json);
		}
		BuddnistCeremonyCommoditySubdivideService subdivideService=new BuddnistCeremonyCommoditySubdivideService();
		BuddnistCeremonyCommoditySubdivideModel subdivideModel=subdivideService.getModel(orderModel.getSubdiride_id());
		if (subdivideModel.getSubdivide_type()==0) {
			subdivideModel.setSubdivide_type(1);
		}
		JSONObject res=new JSONObject();
		res.put("posiscript", orderModel.getPosiscript());
		res.put("subdivide_type",subdivideModel.getSubdivide_type());
		json.put("data", res);
		return succRequest("成功", json);
	}

}
