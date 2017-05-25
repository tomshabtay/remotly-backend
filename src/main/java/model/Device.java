package model;

import java.io.Serializable;

import ssh.SSHManager;

public class Device implements Serializable {

	private static final long serialVersionUID = 1L;
	
	public String name;
	public String ip;
	public String username;
	public String password;
	public boolean isCentos = true;
	
	
	
	public SSHManager ssh_manager;
	
	boolean connected;
	
	public Device(String name, String ip, String username, String password){
		isCentos = true;
		this.name = name;
		this.ip = ip;
		this.password = password;
		this.username = username;
		this.connected = false;
	}
	
	public String testConnection(){
		String str = ssh_manager.sendMoreCommands();
		return str;
	}
	
	public void sendCommand(String command){
		if(!connected) {
			connect();
		}
		String output = ssh_manager.sendCommand(command);
		System.out.println("Device " + name + " :\n" + output);
		disconnect();
		
	}
	public String sendCommand2(String command){
		if(!connected) {
			connect();
		}
		String output = ssh_manager.sendCommand(command);
		//System.out.println("Device " + name + " :\n" + output);
		disconnect();
		return output;
	}
	
	public void updatePackage(String pack)//TODO
	{
		
	}

	
	
	
	
	public void connect(){
		
			ssh_manager = new SSHManager(username, password, ip, "");
			String status = ssh_manager.connect();
			System.out.println(status);
			connected = true;
			
	}
	
	public void disconnect(){
		ssh_manager.close();
		connected = false;
	}
	
}
