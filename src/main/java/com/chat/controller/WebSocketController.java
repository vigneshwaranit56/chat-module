package com.chat.controller;

import java.security.Principal;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;

import com.google.gson.Gson;

@Controller
public class WebSocketController {

	@Autowired
	private SimpMessageSendingOperations messagingTemplate;

	@MessageMapping("/group/{groupid}")
//	@SendToUser("/queue/reply")
	public String sendToGroup(@Payload String message, Principal principal, @DestinationVariable("count") String count) throws Exception {
		String name = new Gson().fromJson(message, Map.class).get("name").toString();
		System.out.println("name "+name+" "+count);
		System.out.println("/queue/reply"+count);
		messagingTemplate.convertAndSendToUser(principal.getName(), "/queue/reply/"+count, name);
		return name;
		
	}
	
	@MessageMapping("/chat/{username}")
//	@SendToUser("/queue/reply")
	public String processMessageFromClient(@Payload String message, Principal principal, @DestinationVariable("username") String username) throws Exception {
		String name = new Gson().fromJson(message, Map.class).get("name").toString();
		System.out.println("name "+name+" "+username);
		System.out.println("/queue/reply"+username);
		messagingTemplate.convertAndSendToUser(principal.getName(), "/queue/reply/"+username, name);
		return name;
		
	}
	
	@MessageExceptionHandler
    @SendToUser("/queue/errors")
    public String handleException(Throwable exception) {
        return exception.getMessage();
    }

}
