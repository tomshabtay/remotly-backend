package server;

import static spark.Spark.*;
import static spark.SparkBase.stop;

import java.rmi.server.ServerCloneException;

import controllers.DevicesControllers;
import controllers.SSHControllers;
import controllers.ServerControllers;
import model.Device;
import model.DeviceManager;
import spark.Request;
import spark.Response;
import spark.Route;
import ssh.SSHManager;

public class Server {

	// The Model
	public static DeviceManager device_manager = new DeviceManager();

	// Constructor
	public Server() {
		setRoutes();
	}

	public void setRoutes() {

		// Devices Managment
		get("devices/add/:query", DevicesControllers.addDevice);
		get("devices/list", DevicesControllers.listDevices);
		get("devices/:name/delete",DevicesControllers.deleteDevice);
		get("devices/delete-all", DevicesControllers.deleteAllDevices);
		get("devices/:name/details", DevicesControllers.deviceDetails);
		

		// SSH1
		get("ssh/:name/listpack", SSHControllers.listPackages);
		get("ssh/:name/updateall", SSHControllers.updateAll);
		get("ssh/:name/remove/:pack", SSHControllers.removePack);
		get("ssh/:name/install/:pack", SSHControllers.installPack);

		get("ssh/:name/update/:pack", SSHControllers.updatePackage);
		get("ssh/test/:name", SSHControllers.sshTest);
		get("ssh/do/:name/:command", SSHControllers.sshRunCommand);
		// get("ssh/ses-start/:name/:", SSHControllers.sshRunCommand);

		// Server functions
		get("stop", ServerControllers.stopServer);
		get("/", ServerControllers.index);

	}
	
	public static void stopServer() 
	{
		stop();
	}
	

}