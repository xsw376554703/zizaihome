package com.zizaihome.api.resources.commodity;

import java.util.List;

import net.sf.json.JSONObject;

import org.restlet.Request;

import com.zizaihome.api.db.model.BuddnistCeremonyCommodityModel;
import com.zizaihome.api.db.model.BuddnistCeremonyCommodityShoppingCartModel;
import com.zizaihome.api.db.model.BuddnistCeremonyCommoditySubdivideModel;
import com.zizaihome.api.resources.base.BaseResource;
import com.zizaihome.api.service.BuddnistCeremonyCommodityService;
import com.zizaihome.api.service.BuddnistCeremonyCommodityShoppingCartService;
import com.zizaihome.api.service.BuddnistCeremonyCommoditySubdivideService;
import com.zizaihome.api.utils.ConfigReadUtils;
import com.zizaihome.api.utils.HttpRequestUtils;

public class GetShoppingCartSumResource extends BaseResource {

	@Override
	protected void initParams(Request request) {
		
	}

	@Override
	protected JSONObject getMethod(JSONObject json) {
		BuddnistCeremonyCommodityShoppingCartService buddnistCeremonyCommodityShoppingCartService = new BuddnistCeremonyCommodityShoppingCartService();
		BuddnistCeremonyCommodityService buddnistCeremonyCommodityService = new BuddnistCeremonyCommodityService();
		BuddnistCeremonyCommoditySubdivideService buddnistCeremonyCommoditySubdivideService = new BuddnistCeremonyCommoditySubdivideService();
		
		if(sid.equals("")){
			sid = getParameter("sid","");
		}
		
		//如果禅在APP调用订单接口去禅在接口获取用户id
		if(!sid.equals("")){
			String chanzaiHost = ConfigReadUtils.loadConfigByConfDir("server.properties").getProperty("chanzai_host");
			String chanzaiUrl = chanzaiHost+"/app_h5/zizaihome/temple_check";
			JSONObject jsonParam = new JSONObject();
			jsonParam.put("sid", sid);
			JSONObject chanzaiResult = HttpRequestUtils.httpPostFormData(chanzaiUrl, jsonParam);
			if(chanzaiResult.getInt("code") == 1){
				JSONObject chanzaiResultData = chanzaiResult.getJSONObject("data");
				userId = Integer.parseInt(chanzaiResultData.getString("user_id"));
			}
		}
		
		int shoppingCartSum = 0;
		double shoppingCartMoneySum = 0.0;
		List<BuddnistCeremonyCommodityShoppingCartModel> shoppingCartList = buddnistCeremonyCommodityShoppingCartService.findByUserId(userId);
		for(BuddnistCeremonyCommodityShoppingCartModel shoppingCart:shoppingCartList){
			shoppingCartSum = shoppingCartSum+shoppingCart.getBuy_num();
			if(shoppingCart.getSubdivide_id() == 0){
				BuddnistCeremonyCommodityModel commodity = buddnistCeremonyCommodityService.getModel(shoppingCart.getCommodity_id());
				shoppingCartMoneySum = shoppingCartMoneySum+commodity.getPrice()*shoppingCart.getBuy_num();
			}
			else{
				BuddnistCeremonyCommoditySubdivideModel subdivide = buddnistCeremonyCommoditySubdivideService.getModel(shoppingCart.getSubdivide_id());
				shoppingCartMoneySum = shoppingCartMoneySum+subdivide.getPrice()*shoppingCart.getBuy_num();
			}
		}
		json.put("shoppingCartSum", shoppingCartSum);
		json.put("shoppingCartMoneySum", shoppingCartMoneySum);
		return succRequest("成功",json);
	}

	@Override
	protected JSONObject postMethod(JSONObject json) {
		return this.getMethod(json);
	}

}
