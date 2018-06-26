package com.zizaihome.api.resources.website;

import java.util.HashMap;
import java.util.Map;

import org.restlet.Request;

import com.zizaihome.api.common.Constants;
import com.zizaihome.api.db.model.EnterStatisticsModel;
import com.zizaihome.api.db.model.TempleModel;
import com.zizaihome.api.db.model.UserModel;
import com.zizaihome.api.db.model.ZizaijiaTempleWebsiteSortModel;
import com.zizaihome.api.service.BuddnistCeremonyCommodityOrderService;
import com.zizaihome.api.service.EnterStatisticsService;
import com.zizaihome.api.service.TempleService;
import com.zizaihome.api.service.UserService;
import com.zizaihome.api.service.ZizaijiaTempleWebsiteSortService;
import com.zizaihome.web.resource.base.BaseResultModel;
import com.zizaihome.web.resource.base.BaseWebResource;

public class MeritBoxAuthResource extends BaseWebResource {

	@Override
	protected void initParams(Request request) {
		
	}

	@Override
	protected void getMethod(BaseResultModel br) {
		BuddnistCeremonyCommodityOrderService buddnistCeremonyCommodityOrderService = new BuddnistCeremonyCommodityOrderService();
		TempleService templeService = new TempleService();
		ZizaijiaTempleWebsiteSortService templeWebsiteSortService = new ZizaijiaTempleWebsiteSortService();
		UserService userService = new UserService();
		int templeId = getParameter("templeId",0);
		int fromType = getParameter("p_fromType",0);
		//说明是满福屏过来的
		if(fromType == 1){
			EnterStatisticsService enterService = new EnterStatisticsService();
			EnterStatisticsModel enterModel = new EnterStatisticsModel();
			enterModel.setTempleId(templeId);
			enterModel.setUserId(userId);
			enterModel.setType(Constants.VISIT_TYPE_FUPING);
			enterService.insert(enterModel);
		}
		double sumPrice = buddnistCeremonyCommodityOrderService.sumUserPrice(userId, templeId);
		TempleModel temple = templeService.getModel(templeId);
		ZizaijiaTempleWebsiteSortModel templeWebsiteSort = templeWebsiteSortService.findTempleIdAndType(templeId, 4);
		int meritBoxId = 0;
		if(templeWebsiteSort != null){
			meritBoxId = templeWebsiteSort.getMessage_id();
		}
		int isChanzai = 0;
		if(sid.equals("")){
			isChanzai = 1;
		}
		UserModel user = userService.getModel(userId);
		Map<String, Object> dataModel = new HashMap<String,Object>();
		dataModel.put("access_token", accessToken);
		dataModel.put("templeName", temple.getName());
		dataModel.put("isChanzai", isChanzai);
		dataModel.put("chanzaiIsVirtual", chanzaiIsVirtual);
		dataModel.put("sumPrice", sumPrice);
		dataModel.put("userImage", user.getHead_img());
		dataModel.put("meritBoxId", meritBoxId);
		dataModel.put("nickName", user.getNick_name());
		dataModel.put("times",buddnistCeremonyCommodityOrderService.getMeritBoxTimes(templeId)+"");
		br.setDataModel(dataModel);
		br.setHtmlPath("temple/donate_box.html");
	}

	@Override
	protected void postMethod(BaseResultModel br) {
		
	}

}
