package slack;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.Date;

import com.ullink.slack.simpleslackapi.SlackChannel;
import com.ullink.slack.simpleslackapi.SlackSession;
import com.ullink.slack.simpleslackapi.impl.SlackSessionFactory;

public class SlackBot {
	
	 public static SlackSession session;
	 
	 public SlackBot(){
		 
		 session = SlackSessionFactory.createWebSocketSlackSession("xoxb-189805091159-XzQZ8G6uVn6ObGuIyhIvFIYU");
	    	try {
				session.connect();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
//	    	SlackChannel channel = session.findChannelByName("general"); //make sure bot is a member of the channel.
//	    	session.sendMessage(channel, "HI HI HI HI" );
	 }
	 
	 public static void sendMsg(String msg, String channelStr){
		 	
		 	
		 	String timeStr = "*(" + String.valueOf(ZonedDateTime.now().getHour()) + ":" + String.valueOf(ZonedDateTime.now().getMinute()) + ")* ";
		 	timeStr = "*(" + LocalDateTime.now().toString() + ")* \n";

		 	//channelStr = "general";
	    	SlackChannel channel = session.findChannelByName(channelStr); //make sure bot is a member of the channel.
	    	session.sendMessage(channel, timeStr + msg );
	 }
	 

}
