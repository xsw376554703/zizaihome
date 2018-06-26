package com.zizaihome.api.resources.article;

import java.util.Date;

import net.sf.json.JSONObject;

import org.restlet.Request;

import com.zizaihome.api.db.model.ZizaijiaArticleZanModel;
import com.zizaihome.api.resources.base.BaseResource;
import com.zizaihome.api.service.ZizaijiaArticleService;
import com.zizaihome.api.service.ZizaijiaArticleZanService;

public class ZanArticleResource extends BaseResource {

	@Override
	protected void initParams(Request request) {
		
	}

	@Override
	protected JSONObject getMethod(JSONObject json) {
		ZizaijiaArticleService zizaijiaArticleService = new ZizaijiaArticleService();
		ZizaijiaArticleZanService zizaijiaArticleZanService = new ZizaijiaArticleZanService();
		int articleId = getParameter("articleId",0);
		ZizaijiaArticleZanModel zizaijiaArticleZanModel = zizaijiaArticleZanService.findByUserId(userId, articleId);
		if(zizaijiaArticleZanModel != null){
			if(zizaijiaArticleZanModel.getStatus() == 0){
				zizaijiaArticleZanModel.setStatus(-1);
				zizaijiaArticleZanService.updateAny(zizaijiaArticleZanModel);
				zizaijiaArticleService.updateArticleZanNum(articleId, 2);
			}
			else{
				zizaijiaArticleZanModel.setStatus(0);
				zizaijiaArticleZanService.updateAny(zizaijiaArticleZanModel);
				zizaijiaArticleService.updateArticleZanNum(articleId, 1);
			}
		}
		else{
			ZizaijiaArticleZanModel newZizaijiaArticleZanModel = new ZizaijiaArticleZanModel();
			newZizaijiaArticleZanModel.setAdd_time(new Date());
			newZizaijiaArticleZanModel.setArticle_id(articleId);
			newZizaijiaArticleZanModel.setStatus(0);
			newZizaijiaArticleZanModel.setUpdate_time(new Date());
			newZizaijiaArticleZanModel.setUser_id(userId);
			zizaijiaArticleZanService.insert(newZizaijiaArticleZanModel);
			zizaijiaArticleService.updateArticleZanNum(articleId, 1);
		}
		return succRequest("成功",json);
	}

	@Override
	protected JSONObject postMethod(JSONObject json) {
		return this.getMethod(json);
	}

}
