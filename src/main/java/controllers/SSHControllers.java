package controllers;

import org.w3c.dom.NameList;

import model.Device;
import model.Pack;
import server.Server;
import spark.Request;
import spark.Response;
import spark.Route;
import ssh.SSHManager;

public class SSHControllers {
	
	public static Route sshTest = (Request request, Response response) -> {
		Device d = Server.device_manager.getDevice(request.params(":name"));
		// String str = SSHManager.testSendCommand(d,"cd Desktop");
		d.connect();
		String result = d.testConnection();

		return result;

	};
	
	public static Route sshRunCommand = (Request request, Response response) -> {
		Device d = Server.device_manager.getDevice(request.params(":name"));
		d.sendCommand(request.params(":command"));
		return "Run Command";
		

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
		
		//return "Run Command";
		return str;
	};
	

}
