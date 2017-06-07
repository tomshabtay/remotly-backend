package controllers;

import java.util.Iterator;
import java.util.Map;

import org.glassfish.grizzly.utils.Pair;
import org.w3c.dom.NameList;

import model.Device;
import model.DeviceManager;
import model.Pack;
import server.Server;
import slack.SlackBot;
import spark.Request;
import spark.Response;
import spark.Route;
import ssh.SSHManager;

public class SSHControllers {
	
	static public final int msg_len = 250;
	
	public static Route sshTest = (Request request, Response response) -> {
		Device d = Server.device_manager.getDevice(request.params(":name"));
		// String str = SSHManager.testSendCommand(d,"cd Desktop");
		d.connect();
		String result = d.testConnection();

		return result;

	};
	
	public static Route sshRunCommand = (Request request, Response response) -> {
		//TODO
		Device d = Server.device_manager.getDevice(request.params(":name"));
		String str = d.sendCommand2(request.params(":command"));
		SlackBot.sendMsg("Command: " + request.params(":command") + "\n" +
						"Device: " + request.params(":name") + "\n" +
						"Output: " + str.substring(str.length() < msg_len ? 0 : str.length() - msg_len, str.length()) + "\n"
						, "ssh");
		System.out.println(str);
		return str;
		

	};

	public static Route listPackages= (Request request, Response response) -> {
		int place = -1;
		String nameList;
		String verList;
		
		String[] splitVerList;
		String[] splitNameList;
		Device d = Server.device_manager.getDevice(request.params(":name"));
		if(d.isCentos)
		{
			
			nameList = d.sendCommand2("yum list installed | awk {' print $1 '}");
			verList = d.sendCommand2("yum list installed | awk {' print $2 '}");
			splitNameList = nameList.split("\n");
			splitVerList = verList.split("\n");
			d.removeAllPacksDetails();
			for (int i = 2; i < splitNameList.length; i++) { // for starts at 2 because there are 2 bad lines
				
				Pack pack = new Pack(splitNameList[i],splitVerList[i]);
				//System.out.println("name" + i + ": " + splitNameList[i] + ", " +  splitVerList[i]);

				d.getPackages().add(pack);

				
			}
			System.out.println("Obtaining " + splitNameList.length + " packages for device:" + d.name);

		}
		

		return "Run Command";
	};

	public static Route updatePackage= (Request request, Response response) -> {
		Device d = Server.device_manager.getDevice(request.params(":name"));
		String str = request.params(":pack");
		String res;
		if(d.isCentos)
		{
			//d.updatePackage(str);
			res = d.sendCommand2("yum update " + str +" -y");
		}
		else
		{
			res = d.sendCommand2("apt-get upgrade " + str + " -y");
		}
		
		SlackBot.sendMsg("Command: update package " + request.params(":pack") + "\n" +
				"Device: " + request.params(":name") + "\n" +
				"Output: " + res + "\n"
				, "ssh");
		return res;
	};

	public static Route updateAll= (Request request, Response response) -> {
		Device d = Server.device_manager.getDevice(request.params(":name"));
		String str;
		if(d.isCentos)
		{
			str = d.sendCommand2("yum update" + " -y");
		}
		else
		{
			str = d.sendCommand2("apt-get upgrade" + " -y");
		}
		
		SlackBot.sendMsg("Command: update all packages " + "\n" +
				"Device: " + request.params(":name") + "\n" +
				"Output: " + str.substring(str.length() < msg_len ? 0 : str.length() - msg_len, str.length()) + "\n"
				, "ssh");
		
		//return "Run Command";
		return str;
	};

	public static Route removePack= (Request request, Response response) -> {
		String packName;
		Device d = Server.device_manager.getDevice(request.params(":name"));
		packName = request.params(":pack");
		String str;
		if(d.isCentos)
		{
			str = d.sendCommand2("yum remove " + packName + " -y");
		}
		else
		{
			str = d.sendCommand2("apt-get purge " + packName + " -y");
		}

		SlackBot.sendMsg("Command: remove pack "+ request.params(":pack") + "\n" +
				"Device: " + request.params(":name") + "\n" +
				"Output: " + str.substring(str.length() < msg_len ? 0 : str.length() - msg_len, str.length()) + "\n"
				, "ssh");
		
		//return "Run Command";
		return str;
	};

	public static Route installPack= (Request request, Response response) -> {
		String packName;
		Device d = Server.device_manager.getDevice(request.params(":name"));
		packName = request.params(":pack");
		String str;
		if(d.isCentos)
		{
			str = d.sendCommand2("yum install " + packName + " -y");
		}
		else
		{
			str = d.sendCommand2("apt-get install " + packName + " -y");
		}
		
		SlackBot.sendMsg("Command: install pack "+ request.params(":pack") + "\n" +
				"Device: " + request.params(":name") + "\n" +
				"Output: " + str.substring(str.length() < msg_len ? 0 : str.length() - msg_len, str.length()) + "\n"
				, "ssh");
		
		
		//return "Run Command";
		return str;
	};

	public static Route installAllPacks= (Request request, Response response) -> {
		Device d;
	    Iterator it =  Server.device_manager.devices_map.entrySet().iterator();
	    boolean[] results = new boolean[Server.device_manager.devices_map.size()];
	    String str = "";
	    int i = 0;
	    while (it.hasNext()) {
	        Map.Entry pair = (Map.Entry)it.next();
	        d = Server.device_manager.devices_map.get(pair.getKey());
	        if(d.isCentos)
	        {
				str = d.sendCommand2("yum update" + " -y");
				SlackBot.sendMsg("Command: update all packages " + "\n" +
				"Device: " + d.getName() + "\n" +
				"Output: " + str.substring(str.length() < msg_len ? 0 : str.length() - msg_len, str.length()) + "\n"
				, "ssh");
	        }
	        else
	        {
				str = d.sendCommand2("apt-get upgrade" + " -y");
				if (!(str == null))
				{
					SlackBot.sendMsg("Command: update all packages " + "\n" +
							"Device: " + d.getName() + "\n" +
							"Output: " + str.substring(str.length() < msg_len ? 0 : str.length() - msg_len, str.length()) + "\n"
							, "ssh");
				}

	        }
//			if(str.contains("Complete!"))
//			{
//				results[i] = true;
//			}
	        
	        
	    }

		
		
		return "success";
	};

	@SuppressWarnings("rawtypes")
	public static Route installPackAll= (Request request, Response response) -> {
		Device d;
	    Iterator it =  Server.device_manager.devices_map.entrySet().iterator();
	    boolean[] results = new boolean[Server.device_manager.devices_map.size()];
	    String str = "";
	    int i = 0;
	    String packName = request.params(":pack");
	    
	    
	    while (it.hasNext()) {
	        Map.Entry pair = (Map.Entry)it.next();
	        d = Server.device_manager.devices_map.get(pair.getKey());
	        if(d.isCentos)
	        {
				str = d.sendCommand2("yum install " + packName + " -y");

	        	if(str != null)
	        	{
					SlackBot.sendMsg("Command: install package " + "\n" +
							"Device: " + d.getName() +  "\n" +
							"Pack: " + packName + "\n" +
							"Output: " + str.substring(str.length() < msg_len ? 0 : str.length() - msg_len, str.length()) + "\n"
							, "ssh");
	        	}


	        }
	        else
	        {
				str = d.sendCommand2("apt-get install " + packName + " -y");
				if (!(str == null))
				{
					SlackBot.sendMsg("Command: install package " + "\n" +
							"Device: " + d.getName() + "\n" +
							"Pack: " + packName + "\n" +
							"Output: " + str.substring(str.length() < msg_len ? 0 : str.length() - msg_len, str.length()) + "\n"
							, "ssh");
				}

	        }

	        
	    }

		return "success";
	};

	public static Route removePackAll= (Request request, Response response) -> {
		Device d;
	    Iterator it =  Server.device_manager.devices_map.entrySet().iterator();
	    boolean[] results = new boolean[Server.device_manager.devices_map.size()];
	    String str = "";
	    int i = 0;
	    String packName = request.params(":pack");
	    
	    
	    while (it.hasNext()) {
	        Map.Entry pair = (Map.Entry)it.next();
	        d = Server.device_manager.devices_map.get(pair.getKey());
	        if(d.isCentos)
	        {
				str = d.sendCommand2("yum remove " + packName + " -y");

	        	if(str != null)
	        	{
					SlackBot.sendMsg("Command: remove package " + "\n" +
							"Device: " + d.getName() +  "\n" +
							"Pack: " + packName + "\n" +
							"Output: " + str.substring(str.length() < msg_len ? 0 : str.length() - msg_len, str.length()) + "\n"
							, "ssh");
	        	}


	        }
	        else
	        {
				str = d.sendCommand2("apt-get purge " + packName + " -y");
				if (!(str == null))
				{
					SlackBot.sendMsg("Command: remove package " + "\n" +
							"Device: " + d.getName() + "\n" +
							"Pack: " + packName + "\n" +
							"Output: " + str.substring(str.length() < msg_len ? 0 : str.length() - msg_len, str.length()) + "\n"
							, "ssh");
				}

	        }

	        
	    }

		return "success";
	};

}
