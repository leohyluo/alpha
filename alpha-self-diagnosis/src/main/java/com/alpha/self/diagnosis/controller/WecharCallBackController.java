package com.alpha.self.diagnosis.controller;

import java.io.IOException;
import java.io.PrintWriter;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alpha.commons.api.tencent.offical.callback.CallBackHandler;
import com.alpha.commons.api.tencent.offical.dto.FollowDTO;
import com.alpha.commons.enums.Event;
import com.alpha.commons.util.IoUtils;
import com.alpha.commons.util.WecharUtils;
import com.alpha.commons.util.XStreamUtils;

@RestController
public class WecharCallBackController {

	@Resource(name = "followCallBack")
	private CallBackHandler<FollowDTO> followCallBack; 
	@Resource(name = "unFollowCallBack")
	private CallBackHandler<FollowDTO> unfollowCallBack;
	@Resource(name = "scanCallBack")
	private CallBackHandler<FollowDTO> scanCallBack;
	
	private Logger logger = LoggerFactory.getLogger(getClass());

	@GetMapping("/wechar/callback")
	public void callback(HttpServletRequest request, HttpServletResponse response) {
		try {
			String signature = request.getParameter("signature");
	        //时间戳
	        String timestamp = request.getParameter("timestamp");
	        //随机数
	        String nonce = request.getParameter("nonce");
	        //随机字符串
	        String echostr = request.getParameter("echostr");

	        if (WecharUtils.checkSignature(signature, timestamp, nonce)) {
	            logger.info("[signature: "+signature + "]<-->[timestamp: "+ timestamp+"]<-->[nonce: "+nonce+"]<-->[echostr: "+echostr+"]");
	            PrintWriter writer = response.getWriter();
	            writer.write(echostr);
	            writer.flush();
	            writer.close();
	            return;
	        }
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@PostMapping("/wechar/callback")
	public void callback2(HttpServletRequest request, HttpServletResponse response) {
		String content;
		try {
			content = IoUtils.parseToString(request.getInputStream());
			content = content.replace(" ", "");
			logger.info("微信回调阿尔法服务器,回调内容为{}", content);
			String event = WecharUtils.getCallBackEvent(content);
			if(event.equals(Event.SUBSCRIBE.getValue())) {
				Object obj = XStreamUtils.getObjectFromXML(content, FollowDTO.class);
				FollowDTO followDTO = (FollowDTO) obj;
				followCallBack.handle(followDTO);
			} else if(event.equals(Event.UNSUBSCRIBE.getValue())) {
				Object obj = XStreamUtils.getObjectFromXML(content, FollowDTO.class);
				FollowDTO followDTO = (FollowDTO) obj;
				unfollowCallBack.handle(followDTO);
			} else if(event.equalsIgnoreCase(Event.SCAN.getValue())) {
				Object obj = XStreamUtils.getObjectFromXML(content, FollowDTO.class);
				FollowDTO followDTO = (FollowDTO) obj;
				scanCallBack.handle(followDTO);
			}
			PrintWriter writer = response.getWriter();
            writer.write("success");
            writer.flush();
            writer.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@PostMapping("/wechar/callback/test")
	public void testCallBack(String userId, String event) {
		FollowDTO followDTO = new FollowDTO();
		followDTO.setToUserName("xxxxxx2");
		followDTO.setFromUserName("xxxxxx");
		//followDTO.setMsgType("subscribe");
		followDTO.setMsgType(event);
		followDTO.setEventKey("qrscene_"+userId);
		if(event.equals(Event.UNSUBSCRIBE.getValue())) {
			unfollowCallBack.handle(followDTO);
		} else if(event.equals(Event.SUBSCRIBE.getValue())) {
			followCallBack.handle(followDTO);
		} else if(event.equalsIgnoreCase(Event.SCAN.getValue())) {
			scanCallBack.handle(followDTO);
		}
	}
}
