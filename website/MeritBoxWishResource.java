package com.zizaihome.api.resources.website;

import net.sf.json.JSONObject;

import org.restlet.Request;

import com.zizaihome.api.common.Constants;
import com.zizaihome.api.db.model.BuddnistCeremonyCommodityOrderModel;
import com.zizaihome.api.db.model.EnterStatisticsModel;
import com.zizaihome.api.resources.base.BaseResource;
import com.zizaihome.api.resources.utils.SocketUtils;
import com.zizaihome.api.service.BuddnistCeremonyCommodityOrderService;
import com.zizaihome.api.service.EnterStatisticsService;
import com.zizaihome.api.utils.HttpRequestUtils;

public class MeritBoxWishResource extends BaseResource {

	@Override
	protected void initParams(Request request) {
		
	}

	@Override
	protected JSONObject getMethod(JSONObject json) {
		BuddnistCeremonyCommodityOrderService commodityOrderService = new BuddnistCeremonyCommodityOrderService();
		int orderId = getParameter("orderId",0);
		String wish = getParameter("wish","");
		int is_cryptonym_wish = getParameter("is_cryptonym_wish",0);
		int fromType = getParameter("p_fromType",0);
		BuddnistCeremonyCommodityOrderModel commodityOrder = commodityOrderService.getModel(orderId);
		if(commodityOrder != null){
			commodityOrder.setWish(wish);
			commodityOrder.setSubdivideName(wish);
			commodityOrder.setIs_cryptonym_wish(is_cryptonym_wish);
			commodityOrderService.updateAny(commodityOrder);
			if(fromType == 1){
				EnterStatisticsService enterService = new EnterStatisticsService();
				EnterStatisticsModel enterModel = new EnterStatisticsModel();
				enterModel.setTempleId(commodityOrder.getTemple_id());
				enterModel.setUserId(userId);
				enterModel.setType(Constants.VISIT_TYPE_FUPING);
				enterModel.setBusinessId(1);
				enterService.insert(enterModel);
				JSONObject data = new JSONObject();
				data.put("send", 1);
				data.put("orderId", orderId);
				SocketUtils.push2Socket(json);
			}
			return succRequest("提交成功",json);
		}
		return failRequest("提交失败",json);
	}

	@Override
	protected JSONObject postMethod(JSONObject json) {
		return this.getMethod(json);
	}

}
