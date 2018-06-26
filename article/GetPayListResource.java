package com.zizaihome.api.resources.article;

import java.text.SimpleDateFormat;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.restlet.Request;

import com.zizaihome.api.db.model.UserModel;
import com.zizaihome.api.db.model.ZizaijiaArticlePayModel;
import com.zizaihome.api.resources.base.BaseResource;
import com.zizaihome.api.service.UserService;
import com.zizaihome.api.service.ZizaijiaArticlePayService;

public class GetPayListResource extends BaseResource {

	@Override
	protected void initParams(Request request) {
		
	}

	@Override
	protected JSONObject getMethod(JSONObject json) {
		ZizaijiaArticlePayService zizaijiaArticlePayService = new ZizaijiaArticlePayService();
		UserService userService = new UserService();
		int articleId = getParameter("articleId",0);
		int pageNumber = getParameter("pageNumber",0);
		int pageSize = getParameter("pageSize",0);
		if(pageSize == 0){
			pageSize = 20;
		}
		List<ZizaijiaArticlePayModel> zizaijiaArticlePayModelList = zizaijiaArticlePayService.findList(articleId, pageNumber, pageSize);
		JSONArray dataList = new JSONArray();  
		for(ZizaijiaArticlePayModel zizaijiaArticlePayModel:zizaijiaArticlePayModelList){
			JSONObject data = new JSONObject();
			UserModel user = userService.getModel(zizaijiaArticlePayModel.getUser_id());
			if(zizaijiaArticlePayModel.getIs_anonymous() == 0){
				if(StringUtils.isEmpty(zizaijiaArticlePayModel.getNick_name())){
					data.put("nickName", user.getNick_name());
				}
				else{
					data.put("nickName", zizaijiaArticlePayModel.getNick_name());
				}
				data.put("headImg", user.getHead_img());
			}else{
				data.put("nickName", "匿名");
				data.put("headImg", "https://pic.zizaihome.com/1495867003416.png");
			}
				
			if(user.getProvince() != null){
				data.put("address", user.getProvince()+user.getCity());
			}
			else{
				data.put("address", "");
			}
			if(user.getChanzaiMobile() != null && !user.getChanzaiMobile().equals("")){
				data.put("userType", 2);
			}
			else{
				data.put("userType", 1);
			}
				
			data.put("price", zizaijiaArticlePayModel.getPrice());
			SimpleDateFormat sdf=new SimpleDateFormat("MM-dd HH:mm");
			data.put("addTime", sdf.format(zizaijiaArticlePayModel.getAdd_time()));
			dataList.add(data);
		}
		json.put("data", dataList);
		if(zizaijiaArticlePayModelList.size() >= pageSize){
			json.put("pageNumber", pageNumber+1);
		}
		else{
			json.put("pageNumber", -1);
		}
		return succRequest("成功",json);
	}

	@Override
	protected JSONObject postMethod(JSONObject json) {
		return this.getMethod(json);
	}

}
