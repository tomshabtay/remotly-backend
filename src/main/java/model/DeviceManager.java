package model;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;

import server.Server;

public class DeviceManager {
	public HashMap<String, Device> devices_map;
	
	public DeviceManager(){
		devices_map = new HashMap<String, Device>();
		//loadHashMap();
		addDummyData();
		
	}
	
	public void addDevice(String name, String ip, String username, String password){
		Device device = new Device(name, ip, username, password);
		devices_map.put(device.name, device);
		

		
	}
	public void addDummyData()
	{
		Pack pack = new Pack("vim","1.0");
		Pack pack2 = new Pack("emacs","1.2");
		Pack pack3 = new Pack("ubuntu", "12.4");
		Pack pack4 = new Pack("centos", "7.0");
		Pack pack5 = new Pack("windows", "XP");
		
		Device d1 = new Device("centos", "10.0.0.29", "root", "a");
		d1.packages.add(pack);
		d1.packages.add(pack2);
		d1.packages.add(pack3);
		Device d2 = new Device("windowz", "192.168.1.1", "root", "a");
		d2.packages.add(pack4);
		d2.packages.add(pack5);
		d2.packages.add(pack2);
		Device d3 = new Device("nezeros", "111.111.111.111", "root", "a");
		d3.packages.add(pack2);
		d3.packages.add(pack);
		d3.packages.add(pack4);
		d1.setCentos(true);
		d2.setCentos(false);
		d3.setCentos(false);
		
		devices_map.put(d1.name, d1);
		devices_map.put(d2.name, d2);
		devices_map.put(d3.name, d3);

	}
	
	public Device getDevice(String name){
		Device d = devices_map.get(name);
		return d;
		
	}
	
	public void deleteAllDevices(){
		devices_map.clear();
		//saveHashMap();
	}
	
	
	public static Map<String, String> queryToMap(String query){
		Map<String, String> result = new HashMap<String, String>();
		for (String param : query.split("&")) {
			String pair[] = param.split("=");
			if (pair.length>1) {
				result.put(pair[0], pair[1]);
			}else{
				result.put(pair[0], "");
			}
		}
		return result;
	}
	
	
	public void saveHashMap() {
		try {
			FileOutputStream fosStud = new FileOutputStream("model.ser");
			ObjectOutputStream oosStud = new ObjectOutputStream(fosStud);

			oosStud.writeObject(this.devices_map);
			oosStud.close();

		} catch (IOException e) {

			e.printStackTrace();
		}

	}

	@SuppressWarnings({ "unchecked" })
	//Loading the students map into a file
	public void loadHashMap() {
		try {
			FileInputStream fisStud = new FileInputStream("model.ser");
			ObjectInputStream oisStud = new ObjectInputStream(fisStud);

			devices_map = (HashMap) oisStud.readObject();
			oisStud.close();
			fisStud.close();

		} catch (IOException ioe) {
			ioe.printStackTrace();
		} catch (ClassNotFoundException c) {
			System.out.println("Class not found");
			c.printStackTrace();
			return;
		}

	}
}
