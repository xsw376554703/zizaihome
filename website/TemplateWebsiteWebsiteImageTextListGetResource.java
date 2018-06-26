package com.zizaihome.api.resources.website;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.sf.json.JSONObject;

import org.restlet.Request;

import com.zizaihome.api.db.model.BuddnistCeremonyCommodityModel;
import com.zizaihome.api.db.model.ZizaijiaArticleModel;
import com.zizaihome.api.db.model.ZizaijiaTempleWebsiteImageTextListModel;
import com.zizaihome.api.db.model.ZizaijiaTempleWebsiteImageTextModel;
import com.zizaihome.api.resources.base.BaseResource;
import com.zizaihome.api.service.BuddnistCeremonyCommodityPicsService;
import com.zizaihome.api.service.BuddnistCeremonyCommodityService;
import com.zizaihome.api.service.ZizaijiaArticleService;
import com.zizaihome.api.service.ZizaijiaTempleWebsiteImageTextListService;
import com.zizaihome.api.service.ZizaijiaTempleWebsiteImageTextService;
import com.zizaihome.api.utils.JSONFromBean;
import com.zizaihome.api.utils.ZizaihomeJSONUtils;

public class TemplateWebsiteWebsiteImageTextListGetResource extends BaseResource {

	@Override
	protected void initParams(Request request) {
		
	}

	@Override
	protected JSONObject getMethod(JSONObject json) {
		ZizaijiaTempleWebsiteImageTextService imageTextService = new ZizaijiaTempleWebsiteImageTextService();
		ZizaijiaTempleWebsiteImageTextListService textListService = new ZizaijiaTempleWebsiteImageTextListService();
		BuddnistCeremonyCommodityService commodityService = new BuddnistCeremonyCommodityService();
		BuddnistCeremonyCommodityPicsService commodityPicsService = new BuddnistCeremonyCommodityPicsService();
		ZizaijiaArticleService articleService = new ZizaijiaArticleService();
		int templeId = getParameter("templeId",0);
		int imageTextId = getParameter("imageTextId",0);
		int pageNumber = getParameter("pageNumber",0);
		ZizaijiaTempleWebsiteImageTextModel imageText = imageTextService.getModel(imageTextId);
		if(imageText != null){
			List<String> templeWebsiteImageTextList2 = new ArrayList<String>();
			if(imageText.getShow_type() == 1){
				if(imageText.getContent_type() == 1){
					List<BuddnistCeremonyCommodityModel> commodityModelList = null;
					if(imageText.getBuddnist_ceremony_type_id() == 0){
						commodityModelList = commodityService.findByTempleId(pageNumber,  imageText.getList_num(), templeId);
					}
					else{
						commodityModelList = commodityService.findByCommodityTypeId(pageNumber, imageText.getList_num(), imageText.getBuddnist_ceremony_type_id());
					}
					for(BuddnistCeremonyCommodityModel commodityModel:commodityModelList){
						String pic = commodityPicsService.findByCommodityId(commodityModel.getId()).get(0).getPic_url();
						ZizaijiaTempleWebsiteImageTextListModel imageTextList = new ZizaijiaTempleWebsiteImageTextListModel();
						imageTextList.setBuddnist_ceremony_commodity_id(commodityModel.getId());
						imageTextList.setImage_text_id(imageText.getId());
						imageTextList.setLink_url("https://wx.zizaihome.com/commodity/commodityAuth?commodityId="+commodityModel.getId());
						imageTextList.setOp_status(0);
						imageTextList.setPic(pic);
						imageTextList.setTitle(commodityModel.getName());
						int progressType = 2;
						if(commodityModel.getStart_time() != null){
							if((commodityModel.getStart_time().getTime()-new Date().getTime()) > 0){
								progressType = 1;
							}
							else if(commodityModel.getIs_end() == 1){
								progressType = 3;
							}
						}
						else if(commodityModel.getIs_end() == 1){
							progressType = 3;
						}
						JSONFromBean imageTextListJSON = new JSONFromBean(imageTextList, ZizaihomeJSONUtils.emptyNullFilter());
						imageTextListJSON.addPropertyObj("progressType", progressType, null);
						try {
							templeWebsiteImageTextList2.add(imageTextListJSON.buildString());
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
				else{
					List<ZizaijiaTempleWebsiteImageTextListModel> imageTextListModelList = textListService.findByImageTextId(imageText.getId());
					List<ZizaijiaTempleWebsiteImageTextListModel> isEndCommodityList = new ArrayList<ZizaijiaTempleWebsiteImageTextListModel>();
					List<ZizaijiaTempleWebsiteImageTextListModel> notEndCommodityList = new ArrayList<ZizaijiaTempleWebsiteImageTextListModel>();
					for(ZizaijiaTempleWebsiteImageTextListModel imageTextListModel:imageTextListModelList){
						SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
						if(sdf.format(imageTextListModel.getUpdate_time()) == sdf.format(new Date())){
							break;
						}
						BuddnistCeremonyCommodityModel commodity = commodityService.getModel(imageTextListModel.getBuddnist_ceremony_commodity_id());
						if(commodity != null){
							if(commodity.getIs_end() == 1){
								isEndCommodityList.add(imageTextListModel);
							}
							else{
								notEndCommodityList.add(imageTextListModel);
							}
						}
					}
//					isEndCommodityList.addAll(notEndCommodityList);
					notEndCommodityList.addAll(isEndCommodityList);
					List<ZizaijiaTempleWebsiteImageTextListModel> imageTextListModelList2 = notEndCommodityList;
					int updateNum = 1;
					for(ZizaijiaTempleWebsiteImageTextListModel imageTextListModel:imageTextListModelList2){
						imageTextListModel.setSort(updateNum);
						imageTextListModel.setUpdate_time(new Date());
						textListService.updateAny(imageTextListModel);
						updateNum = updateNum+1;
					}
					List<ZizaijiaTempleWebsiteImageTextListModel> imageTextListModelList3 = textListService.findByImageTextIdForPage(imageText.getId(), pageNumber, imageText.getList_num());
					for(ZizaijiaTempleWebsiteImageTextListModel imageTextListModel:imageTextListModelList3){
						JSONFromBean imageTextListJSON = new JSONFromBean(imageTextListModel, ZizaihomeJSONUtils.emptyNullFilter());
						int progressType = 2;
						BuddnistCeremonyCommodityModel commodity = commodityService.getModel(imageTextListModel.getBuddnist_ceremony_commodity_id());
						if(commodity.getStart_time() != null){
							if((commodity.getStart_time().getTime()-new Date().getTime()) > 0){
								progressType = 1;
							}
							else if(commodity.getIs_end() == 1){
								progressType = 3;
							}
						}
						else if(commodity.getIs_end() == 1){
							progressType = 3;
						}
						imageTextListJSON.addPropertyObj("progressType", progressType, null);
						try {
							templeWebsiteImageTextList2.add(imageTextListJSON.buildString());
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			}
			else{
				if(imageText.getContent_type() == 1){
					List<ZizaijiaArticleModel> articleList = null;
					if(imageText.getArticle_type_id() == 0){
						articleList = articleService.findArticleList(templeId, pageNumber, imageText.getList_num());
					}
					else{
						articleList = articleService.findArticleAndTypeList(templeId, pageNumber, imageText.getList_num(),imageText.getArticle_type_id());
					}
					for(ZizaijiaArticleModel article:articleList){
						ZizaijiaTempleWebsiteImageTextListModel imageTextList = new ZizaijiaTempleWebsiteImageTextListModel();
						imageTextList.setBuddnist_ceremony_commodity_id(0);
						imageTextList.setImage_text_id(imageText.getId());
						imageTextList.setLink_url("https://wx.zizaihome.com/article/articleIndex?articleId="+article.getId());
						imageTextList.setOp_status(0);
						imageTextList.setPic(article.getPic());
						imageTextList.setTitle(article.getTitle());
						imageTextList.setArticle_id(article.getId());
						JSONFromBean imageTextListJSON = new JSONFromBean(imageTextList, ZizaihomeJSONUtils.emptyNullFilter());
						imageTextListJSON.addPropertyObj("progressType", 0, null);
						try {
							templeWebsiteImageTextList2.add(imageTextListJSON.buildString());
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
				else{
					List<ZizaijiaTempleWebsiteImageTextListModel> imageTextListModelList = textListService.findByImageTextIdForPage(imageText.getId(), pageNumber, imageText.getList_num());
					for(ZizaijiaTempleWebsiteImageTextListModel imageTextListModel:imageTextListModelList){
						JSONFromBean imageTextListJSON = new JSONFromBean(imageTextListModel, ZizaihomeJSONUtils.emptyNullFilter());
						imageTextListJSON.addPropertyObj("progressType", 0, null);
						try {
							templeWebsiteImageTextList2.add(imageTextListJSON.buildString());
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			}
			if(templeWebsiteImageTextList2.size() >= imageText.getList_num()){
				json.put("pageNumber", pageNumber+1);
			}
			else{
				json.put("pageNumber", -1);
			}
			json.put("data", templeWebsiteImageTextList2);
			return succRequest("成功",json);
		}
		return failRequest("找不到该寺院微站组件",json);
	}

	@Override
	protected JSONObject postMethod(JSONObject json) {
		return this.getMethod(json);
	}

}
