package controllers;

import static spark.Spark.stop;

import model.DeviceManager;
import spark.Request;
import spark.Response;
import spark.Route;


public class ServerControllers {
	
	//Index
	public static Route index = (Request request, Response response) -> {
		
		return "<h1>Welcome to Remotly</h1>";

	};
	

	//Stop route
	public static Route stopServer = (Request request, Response response) -> {
		stopServer();
		return "Server Stoped";
	};
	

	
	public static void stopServer() {
		stop();
	}
	

}
