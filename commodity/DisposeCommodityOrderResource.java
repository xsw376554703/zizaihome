package com.zizaihome.api.resources.commodity;

import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.restlet.Request;

import com.zizaihome.api.db.model.BuddnistCeremonyCommodityOrderModel;
import com.zizaihome.api.db.model.OrderModel;
import com.zizaihome.api.resources.base.BaseResource;
import com.zizaihome.api.resources.utils.PushUtils;
import com.zizaihome.api.service.BuddnistCeremonyCommodityOrderService;
import com.zizaihome.api.service.OrderService;
import com.zizaihome.api.service.ZizaijiaTempleContentManagerNotifyService;
import com.zizaihome.api.utils.Encryptstr;
import com.zizaihome.api.utils.QRCodeUtils;

import net.sf.json.JSONObject;

public class DisposeCommodityOrderResource extends BaseResource {

	@Override
	protected void initParams(Request request) {
		
	}

	@Override
	protected JSONObject getMethod(JSONObject json) {
		ZizaijiaTempleContentManagerNotifyService notifyService = new ZizaijiaTempleContentManagerNotifyService();
		BuddnistCeremonyCommodityOrderService buddnistCeremonyCommodityOrderService = new BuddnistCeremonyCommodityOrderService();
		OrderService orderService = new OrderService();
		String oid = getParameter("orderId","");
		int orderId = 0;
		if(!oid.equals("")){
			String oidDecodeStr = Encryptstr.decode(oid);
		    Pattern pattern = Pattern.compile("[0-9]*"); 
		    Matcher isNum = pattern.matcher(oidDecodeStr);
		    if( !isNum.matches() ){
		    	orderId = Integer.parseInt(oid);
		    }
		    else{
		    	orderId = Integer.parseInt(oidDecodeStr);
		    }
		}
//		int orderId = !oid.equals("")?Integer.parseInt(Encryptstr.decode(oid)):0;
		String picUrl = getParameter("picUrl","");
		String videoUrl = getParameter("videoUrl","");
		//过滤二维码
		picUrl = QRCodeUtils.filterQRCode(picUrl);
		BuddnistCeremonyCommodityOrderModel buddnistCeremonyCommodityOrderModel = buddnistCeremonyCommodityOrderService.getModel(orderId);
		buddnistCeremonyCommodityOrderModel.setIs_finish(1);
		buddnistCeremonyCommodityOrderModel.setDispose_pic_url(picUrl);
		buddnistCeremonyCommodityOrderModel.setDispose_video_url(videoUrl);
		buddnistCeremonyCommodityOrderModel.setUpdate_time(new Date());
		buddnistCeremonyCommodityOrderService.updateAny(buddnistCeremonyCommodityOrderModel);
		OrderModel order = orderService.getModel(buddnistCeremonyCommodityOrderModel.getOrder_id());
		//如果处理的订单是转单的话查询出原订单并设置为已完成和存储处理图片
		if(buddnistCeremonyCommodityOrderModel.getConversion_type() == 2){
			//如果该订单为拆单的订单
			if(buddnistCeremonyCommodityOrderModel.getConversion_father_order_id() != 0) {
				//获取大礼包订单囊括的所有规格订单
				List<BuddnistCeremonyCommodityOrderModel> conversionOrderList = buddnistCeremonyCommodityOrderService.getConversionFatherOrderId(buddnistCeremonyCommodityOrderModel.getConversion_father_order_id());
				int conversionOrderListIsFinish = 1;
				//判断大礼包订单囊括的所有规格订单是否都已经处理完成了
				for(BuddnistCeremonyCommodityOrderModel conversionOrder:conversionOrderList) {
					if(conversionOrder.getIs_finish() != 1) {
						conversionOrderListIsFinish = 0;
					}
				}
				//如果大礼包订单囊括的所有规格订单都已经处理完成
				if(conversionOrderListIsFinish == 1) {
					//将大礼包的订单设置成已经完成
					BuddnistCeremonyCommodityOrderModel conversionOrderModel = buddnistCeremonyCommodityOrderService.getModel(buddnistCeremonyCommodityOrderModel.getConversion_father_order_id());
					conversionOrderModel.setIs_finish(1);
					conversionOrderModel.setUpdate_time(new Date());
					buddnistCeremonyCommodityOrderService.updateAny(conversionOrderModel);
					//将主订单表的订单设置成已完成
					order.setIs_finished(1);
				}
			}
			//普通的佛事转单处理流程
			else {
				BuddnistCeremonyCommodityOrderModel conversionOrderModel = buddnistCeremonyCommodityOrderService.findConversionOrderId(buddnistCeremonyCommodityOrderModel.getId());
				conversionOrderModel.setIs_finish(1);
				conversionOrderModel.setDispose_pic_url(picUrl);
				conversionOrderModel.setDispose_video_url(videoUrl);
				conversionOrderModel.setUpdate_time(new Date());
				buddnistCeremonyCommodityOrderService.updateAny(conversionOrderModel);
				//将主订单表的订单设置成已完成
				order.setIs_finished(1);
			}
			//更新处理订单提醒
			notifyService.dealNotifyModel(3, orderId);
		}
		else{
			//将主订单表的订单设置成已完成
			order.setIs_finished(1);
			//更新处理订单提醒
			notifyService.dealNotifyModel(1, orderId);
		}
		orderService.updateAny(order);
		
		
		
		
		
//		BuddnistCeremonyCommodityModel buddnistCeremonyCommodityModel = buddnistCeremonyCommodityService.getModel(buddnistCeremonyCommodityOrderModel.getCommodity_id());
//		BuddnistCeremonyCommoditySubdivideModel buddnistCeremonyCommoditySubdivideModel = buddnistCeremonyCommoditySubdivideService.getModel(buddnistCeremonyCommodityOrderModel.getSubdiride_id());
//		UserModel user = userService.getModel(buddnistCeremonyCommodityOrderModel.getUser_id());
//		HashMap<String,Object> pushData = new HashMap<String,Object>();
//		pushData.put("type", "pushDisposeCommodityOrder");
//		pushData.put("buddnistCeremonyCommodityOrderId", buddnistCeremonyCommodityOrderModel.getId());
//		pushData.put("commodityName", buddnistCeremonyCommodityModel.getName());
//		if(buddnistCeremonyCommoditySubdivideModel != null){
//			pushData.put("subdivideName", buddnistCeremonyCommoditySubdivideModel.getName());
//		}
//		else{
//			pushData.put("subdivideName", "");
//		}
//		pushData.put("templeMobile", templeMobile);
//		pushData.put("templeName", temple.getName());
//		pushData.put("feedbackImgUrl", picUrl);
//		LowLevelTaskThreadPool.getThreadPool().execute(new RunPush2WeiXinUtil(user,pushData));
//		pushWeiXinPool.addTask(new RunPush2WeiXinUtil(user,pushData));		
		PushUtils.pushDisposeCommodityOrder(buddnistCeremonyCommodityOrderModel.getUser_id(), buddnistCeremonyCommodityOrderModel.getId(), picUrl);
		return succRequest("处理成功",json);
	}

	@Override
	protected JSONObject postMethod(JSONObject json) {
		return this.getMethod(json);
	}

}
