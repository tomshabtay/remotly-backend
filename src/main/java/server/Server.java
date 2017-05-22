package server;

import static spark.Spark.*;

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
	public static DeviceManager device_manager;

	// Constructor
	public Server() {
		device_manager = new DeviceManager();
		setRoutes();
	}

	public void setRoutes() {

		// Devices Managment
		get("devices/add/:query", DevicesControllers.addDevice);
		get("devices/list", DevicesControllers.listDevices);
		get("devices/delete-all", DevicesControllers.deleteAllDevices);

		// SSH
		get("ssh/test/:name", SSHControllers.sshTest);
		get("ssh/do/:name/:command", SSHControllers.sshRunCommand);
		// get("ssh/ses-start/:name/:", SSHControllers.sshRunCommand);

		// Server functions
		get("stop", ServerControllers.stopServer);
		get("/", ServerControllers.index);

	}

}