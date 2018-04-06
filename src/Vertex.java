/*Vertex.java
*Created on 2018年3月13日
*/

public class Vertex {
	private int myId;
	private String status;
	private int round;
	public Vertex(int mId, String s, int r){
		this.myId=mId;
		this.status=s;
		this.round=r;
	}
	
	public int getMyId(){
		return myId;
	}
	
	public String getStatus(){
		return status;
	}
	
	public void setStatus(String state){
		this.status=state;
	}
	
	public int getRound(){
		return round;
	}
	
	public void setRound(int r){
		this.round=r;
	}
	
	public String toString(){
		return "Processor:"+ myId+" "+status;
	}
}
