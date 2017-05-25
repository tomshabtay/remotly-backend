package controllers;

import java.util.Iterator;
import java.util.Map;

import javax.print.attribute.standard.RequestingUserName;

import model.Device;
import model.DeviceManager;
import server.Server;
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
			return "success";
		}
		else{
			return "fail";
		}
	};
	
	
	//List all available devices
	public static Route listDevices = (Request request, Response response) -> {
		
		StringBuilder str = new StringBuilder();
		int count = 1;
		
	    Iterator it = Server.device_manager.devices_map.entrySet().iterator();
	    while (it.hasNext()) {
	        Map.Entry pair = (Map.Entry)it.next();
	        Device d = (Device) pair.getValue();
	        str.append(count + ". " + "Name: " + d.name + " Ip: " + d.ip + "\n");
	        
	        count++;
//	        it.remove(); // avoids a ConcurrentModificationException
	    }
	    
	    return str.toString();
	    
	};

	//Deletes all the devices
	public static Route deleteAllDevices = (Request request, Response response) -> {
		
		Server.device_manager.deleteAllDevices();
		return "deleted.";
	    
	};


}
