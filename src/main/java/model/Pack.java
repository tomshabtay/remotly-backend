package model;

public class Pack {
	
	String name;
	String ver;
	
	
	public Pack(String name, String ver) 
	{
		super();
		this.name = name;
		this.ver = ver;
	}
	
	public String getName() 
	{
		return name;
	}
	
	public void setName(String name) 
	{
		this.name = name;
	}
	
	public String getVer() 
	{
		return ver;
	}
	
	public void setVer(String ver) 
	{
		this.ver = ver;
	}
	

	@Override
	public boolean equals(Object obj) {
		//TODO check ver too
		
		Pack p = (Pack) obj;
		System.out.println(this.name + " " + p.name);
		if(this.name == p.name){
			return true;
		}
		return false;
	}

	
	

}
