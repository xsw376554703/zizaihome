package com.zizaihome.api.resources.article;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.sf.json.JSONObject;

import org.restlet.Request;

import com.zizaihome.api.db.model.UserModel;
import com.zizaihome.api.db.model.ZizaijiaArticleModel;
import com.zizaihome.api.db.model.ZizaijiaArticlePayModel;
import com.zizaihome.api.db.model.ZizaijiaArticleVisterModel;
import com.zizaihome.api.db.model.ZizaijiaArticleZanModel;
import com.zizaihome.api.resources.base.BaseResource;
import com.zizaihome.api.service.UserService;
import com.zizaihome.api.service.ZizaijiaArticlePayService;
import com.zizaihome.api.service.ZizaijiaArticleService;
import com.zizaihome.api.service.ZizaijiaArticleVisterService;
import com.zizaihome.api.service.ZizaijiaArticleZanService;
import com.zizaihome.api.utils.JSONFromBean;
import com.zizaihome.api.utils.ZizaihomeJSONUtils;

public class GetArticleResource extends BaseResource {

	@Override
	protected void initParams(Request request) {
		
	}

	@Override
	protected JSONObject getMethod(JSONObject json) {
		ZizaijiaArticleService zizaijiaArticleService = new ZizaijiaArticleService();
		ZizaijiaArticlePayService zizaijiaArticlePayService = new ZizaijiaArticlePayService();
		ZizaijiaArticleZanService zizaijiaArticleZanService = new ZizaijiaArticleZanService();
		ZizaijiaArticleVisterService zizaijiaArticleVisterService = new ZizaijiaArticleVisterService();
		UserService userService = new UserService();
		int articleId = getParameter("articleId",0);
		ZizaijiaArticleModel zizaijiaArticle = zizaijiaArticleService.getModel(articleId);
		if(zizaijiaArticle.getStatus() == 3){
			return failRequest("该文章已经被屏蔽",json);
		}
		if(zizaijiaArticle.getStatus() == 4){
			return failRequest("该文章已经被删除",json);
		}
		JSONFromBean zizaijiaArticleJSON = new JSONFromBean(zizaijiaArticle, ZizaihomeJSONUtils.emptyArticleFilter());
		List<ZizaijiaArticlePayModel> zizaijiaArticlePayList = zizaijiaArticlePayService.findLimit24(zizaijiaArticle.getId());
		List<String> picList = new ArrayList<String>();
		for(ZizaijiaArticlePayModel zizaijiaArticlePay:zizaijiaArticlePayList){
			UserModel user = userService.getModel(zizaijiaArticlePay.getUser_id());
			if(user != null){
				if(zizaijiaArticlePay.getIs_anonymous() == 0){
					picList.add(user.getHead_img());
				}
				else{
					picList.add("https://pic.zizaihome.com/1495867003416.png");
				}
			}
		}
		ZizaijiaArticleZanModel zizaijiaArticleZanModel = zizaijiaArticleZanService.findByUserIdAndNoDel(userId, articleId);
		if(zizaijiaArticleZanModel != null){
			zizaijiaArticleJSON.addPropertyObj("isZan", 1, null);
		}
		else{
			zizaijiaArticleJSON.addPropertyObj("isZan", 0, null);
		}
		ZizaijiaArticleVisterModel zizaijiaArticleVisterModel = zizaijiaArticleVisterService.findByUserId(userId, articleId);
		if(zizaijiaArticleVisterModel == null){
			ZizaijiaArticleVisterModel newZizaijiaArticleVisterModel = new ZizaijiaArticleVisterModel();
			newZizaijiaArticleVisterModel.setAdd_time(new Date());
			newZizaijiaArticleVisterModel.setArticle_id(articleId);
			newZizaijiaArticleVisterModel.setUpdate_time(new Date());
			newZizaijiaArticleVisterModel.setUser_id(userId);
			zizaijiaArticleVisterService.insert(newZizaijiaArticleVisterModel);
			zizaijiaArticleService.updateArticleReadNum(articleId);
		}
		zizaijiaArticleService.updateArticleVisitSum(articleId);
		zizaijiaArticleJSON.addPropertyObj("pics", picList, null);
		int isChanzai = 0;
		if(!sid.equals("")){
			isChanzai = 1;
		}
		zizaijiaArticleJSON.addPropertyObj("isChanzai", isChanzai, null);
		zizaijiaArticleJSON.addPropertyObj("websiteUrl", "https://wx.zizaihome.com/commodity/templateWebsiteInfo?templeId="+zizaijiaArticle.getTemple_id(), null);
		try {
			json.put("data", zizaijiaArticleJSON.buildString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return succRequest("成功",json);
	}

	@Override
	protected JSONObject postMethod(JSONObject json) {
		return this.getMethod(json);
	}

}
