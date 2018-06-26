package com.zizaihome.api.resources.commodity;

import java.text.SimpleDateFormat;

import org.restlet.Request;

import com.zizaihome.api.db.model.BuddnistCeremonyCommodityOrderModel;
import com.zizaihome.api.db.model.OrderModel;
import com.zizaihome.api.resources.base.BaseResource;
import com.zizaihome.api.resources.order.GetOrderMessageListResource;
import com.zizaihome.api.service.BuddnistCeremonyCommodityOrderService;
import com.zizaihome.api.service.OrderService;
import com.zizaihome.api.utils.Encryptstr;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class GetConversionOrderMessageResource extends BaseResource {

	@Override
	protected void initParams(Request request) {
		
	}

	@Override
	protected JSONObject getMethod(JSONObject json) {
		BuddnistCeremonyCommodityOrderService buddnistCeremonyCommodityOrderService = new BuddnistCeremonyCommodityOrderService();
		OrderService orderService = new OrderService();
		int orderId = getParameter("orderId",0);
//		int orderId = Integer.parseInt(Encryptstr.decode(orderIdStr));
		JSONObject orderJSON = new JSONObject();
		JSONArray orderJSONArray = new JSONArray();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		BuddnistCeremonyCommodityOrderModel ceremonyCommodityOrderModel = buddnistCeremonyCommodityOrderService.getModel(orderId);
		OrderModel order = orderService.getModel(ceremonyCommodityOrderModel.getOrder_id());
		if(ceremonyCommodityOrderModel.getConversion_father_order_id() != 0) {
			ceremonyCommodityOrderModel = buddnistCeremonyCommodityOrderService.getModel(ceremonyCommodityOrderModel.getConversion_father_order_id());
		}
		orderJSONArray = GetOrderMessageListResource.getCommodityOrderMessageJSON(ceremonyCommodityOrderModel);
		orderJSON.put("orderDetail", orderJSONArray);
		orderJSON.put("orderNo", order.getOrderNo());
		orderJSON.put("payType", order.getStatus());
		orderJSON.put("payTime", formatter.format(order.getPayTime()));
		orderJSON.put("urgeOrder", order.getUrgeOrder());
		orderJSON.put("orderType", order.getType());
		json.put("data", orderJSON);
		return succRequest("获取成功",json);
	}

	@Override
	protected JSONObject postMethod(JSONObject json) {
		return this.getMethod(json);
	}

}
