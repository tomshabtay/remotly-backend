package controllers;

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
		String list;
		String[] brokenList;
		Device d = Server.device_manager.getDevice(request.params(":name"));
		if(d.isCentos)
		{
			list = d.sendCommand2("rpm -qa");
			brokenList = list.split("64");
			int finalPlace = -1;
			boolean isFound = false;
			for (int i = 0; i < brokenList.length; i++) {
				String str = brokenList[i];
//				String[] brokenPack = str.split("-");
				for(int j = 0; j<10; j++)
				{
					place = str.indexOf('-' + String.valueOf(j));
					if(place != -1 && !(isFound))
					{
						finalPlace = place;
						isFound = true;
					}
					
				}
				isFound = false;
				System.out.println("Place is:" + finalPlace);
				String name = str.substring(0, finalPlace);
				System.out.println(name);
				String ver = str.substring(finalPlace+1, str.length());
				Pack pack = new Pack(name,ver);
				d.getPackages().add(pack);
			}
			//testing
			System.out.println("testing");
			for (int i = 0; i <d.getPackages().size(); i++) {
				System.out.println("Device " + d.name + " :\n" +"name:" + d.getPackages().get(i).getName() + " \n");
				//System.out.println("version " + d.getPackages().get(i).getVer());
			}
		}
		

		return "Run Command";
	};

	public static Route updatePackage= (Request request, Response response) -> {
		Device d = Server.device_manager.getDevice(request.params(":name"));
		if(d.isCentos)
		{
			d.updatePackage(request.params(":pack"));
		}
		return "Run Command";
	};

}
