package controllers;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;


import javax.print.attribute.standard.RequestingUserName;

import com.google.gson.Gson;

import model.Device;
import model.DeviceManager;
import model.Pack;
import server.Server;
import slack.SlackBot;
import spark.Request;
import spark.Response;
import spark.Route;
import ssh.SSHManager;

public class DevicesControllers {
	
	//Add new device
	public static Route addDevice = (Request request, Response response) -> {
		
		
		Map<String,String> map = DeviceManager.queryToMap(request.params(":query"));
		Server.device_manager.addDevice(map.get("name"), map.get("ip"), map.get("username"), map.get("password"));
		Device d = Server.device_manager.getDevice(map.get("name"));
		if(d != null){
			SlackBot.sendMsg("Added Device '" + map.get("name") + "' on IP: " + map.get("ip"), "general");
			return "success";
		}
		else{
			SlackBot.sendMsg("Error Adding Device '" + map.get("name") + "'", "general");
			return "fail";
		}
		
	};
	
	
	//List all available devices
	public static Route listDevices = (Request request, Response response) -> {
		
		
		
		
		Gson gson = new Gson();
		String jsonInString = gson.toJson(Server.device_manager.devices_map);
	    response.type("application/json");
	    System.out.println(jsonInString);
		return jsonInString;
	    
	};

	//Deletes all the devices
	public static Route deleteAllDevices = (Request request, Response response) -> {
		
		Server.device_manager.deleteAllDevices();
		return "deleted.";
	    
	};


	public static Route deviceDetails = (Request request, Response response) -> {
		
		
		Gson gson = new Gson();
		String name = request.params(":name");
		Device d = Server.device_manager.devices_map.get(name);
		d.updatePackagesList();
		String jsonInString = gson.toJson(Server.device_manager.devices_map.get(name));
		response.type("application/json");
		return jsonInString;
	    
	};


	public static Route deleteDevice= (Request request, Response response) -> {
		

		String name = request.params(":name");
		SlackBot.sendMsg("Device '" + name + "' deleted.", "general");
		Server.device_manager.devices_map.remove(name);
		
		
		return "success";
		
	};


}
