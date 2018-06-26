package com.zizaihome.api.resources.commodity;

import java.util.List;

import net.sf.json.JSONObject;

import org.restlet.Request;

import com.zizaihome.api.db.model.BuddnistCeremonyCommodityOrderModel;
import com.zizaihome.api.db.model.BuddnistCeremonyCommodityShoppingCartModel;
import com.zizaihome.api.db.model.OrderModel;
import com.zizaihome.api.resources.base.BaseResource;
import com.zizaihome.api.service.BuddnistCeremonyCommodityOrderService;
import com.zizaihome.api.service.BuddnistCeremonyCommodityShoppingCartService;
import com.zizaihome.api.service.OrderService;

public class ShoppingCartPayResultResource extends BaseResource {

	@Override
	protected void initParams(Request request) {
		
	}

	@Override
	protected JSONObject getMethod(JSONObject json) {
		OrderService orderService = new OrderService();
		BuddnistCeremonyCommodityShoppingCartService buddnistCeremonyCommodityShoppingCartService = new BuddnistCeremonyCommodityShoppingCartService();
		BuddnistCeremonyCommodityOrderService buddnistCeremonyCommodityOrderService = new BuddnistCeremonyCommodityOrderService();
		int orderId = getParameter("orderId",0);
		OrderModel order = orderService.getModel(orderId);
		if(order.getStatus() == 1){
			List<BuddnistCeremonyCommodityOrderModel> buddnistCeremonyCommodityOrderList = buddnistCeremonyCommodityOrderService.findByOrderId(order.getId());
			for(BuddnistCeremonyCommodityOrderModel buddnistCeremonyCommodityOrder:buddnistCeremonyCommodityOrderList){
				BuddnistCeremonyCommodityShoppingCartModel buddnistCeremonyCommodityShoppingCart = buddnistCeremonyCommodityShoppingCartService.findByUserIdAndcommodityIdAndSubdivideId(userId, buddnistCeremonyCommodityOrder.getCommodity_id(), buddnistCeremonyCommodityOrder.getSubdiride_id());
				buddnistCeremonyCommodityShoppingCart.setOp_status(-1);
				buddnistCeremonyCommodityShoppingCartService.updateAny(buddnistCeremonyCommodityShoppingCart);
			}
			return succRequest("支付成功",json);
		}
		return failRequest("正在支付中",json);
	}

	@Override
	protected JSONObject postMethod(JSONObject json) {
		return this.getMethod(json);
	}

}
