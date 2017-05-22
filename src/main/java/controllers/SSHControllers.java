package controllers;

import model.Device;
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

}
