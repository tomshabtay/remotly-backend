package model;

import java.io.Serializable;
import java.util.ArrayList;

import server.Server;
import ssh.SSHManager;

public class Device {

//	private static final long serialVersionUID = 1L;
	
	public String name;
	public String ip;


	public String username;
	public String password;
	public boolean isCentos = true;
	ArrayList<Pack> packages;
	
	public transient SSHManager ssh_manager;
	boolean connected;
	
	
	
	public ArrayList<Pack> getPackages() {
		return packages;
	}

	public void setPackages(ArrayList<Pack> packages) {
		this.packages = packages;
	}


	
	public Device(String name, String ip, String username, String password){
		packages = new ArrayList<Pack>();
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
	public String sendCommand2(String command){ // HERE TO RETURN THE STRING
		if(!connected) {
			connect();
		}
		String output = ssh_manager.sendCommand(command);
		//System.out.println("Device " + name + " :\n" + output);
		disconnect();
		return output;
	}
	
	public void updatePackage(String pack)
	{
		connect();
		String response;
		
		if(this.isCentos)
		{
			StringBuilder sb = new StringBuilder();
			sb.append("yum update ");
			sb.append(pack);
			response = ssh_manager.sendCommand(sb.toString());
			System.out.println("Device " + name + " :\n" + response);
			response = ssh_manager.sendCommand("yum update dbus");
			System.out.println("Device " + name + " :\n" + response);

		}
		else
		{
			StringBuilder sb = new StringBuilder();
			sb.append("apt-get ");
			sb.append(pack);
			response = ssh_manager.sendCommand(sb.toString());
			System.out.println("Device " + name + " :\n" + response);
		}
		disconnect();
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
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isCentos() {
		return isCentos;
	}

	public void setCentos(boolean isCentos) {
		this.isCentos = isCentos;
	}

	public SSHManager getSsh_manager() {
		return ssh_manager;
	}

	public void setSsh_manager(SSHManager ssh_manager) {
		this.ssh_manager = ssh_manager;
	}

	public boolean isConnected() {
		return connected;
	}

	public void setConnected(boolean connected) {
		this.connected = connected;
	}

	public void updatePackagesList() {
		String nameList;
		String verList;
		
		String[] splitVerList;
		String[] splitNameList;
		
		if(isCentos)
		{
			
			nameList = sendCommand2("yum list installed | awk {' print $1 '}");
			verList = sendCommand2("yum list installed | awk {' print $2 '}");
			splitNameList = nameList.split("\n");
			splitVerList = verList.split("\n");
			this.packages.clear();
			for (int i = 2; i < splitNameList.length; i++) { // for starts at 2 because there are 2 bad lines
				
				Pack pack = new Pack(splitNameList[i],splitVerList[i]);
				//System.out.println("name" + i + ": " + splitNameList[i] + ", " +  splitVerList[i]);
				packages.add(pack);
				
			}
			System.out.println("Obtaining " + splitNameList.length + " packages for device:" + name);

		}
		
	}

	public void removeAllPacksDetails() {
		packages = new ArrayList<Pack>();
		
	}
	
}
