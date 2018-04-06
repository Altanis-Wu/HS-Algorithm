/*Edge.java
*Created on 2018年3月13日
*/

public class Edge {
	private Vertex vertex1;
	private Vertex vertex2;
	public Edge(Vertex v1, Vertex v2){
		this.vertex1=v1;
		this.vertex2=v2;
	}
	
	public Vertex getVertex1(){
		return vertex1;
	}
	
	public Vertex getVertex2(){
		return vertex2;
	}
	
	public String toString(){
		return vertex1+" "+vertex2;
	}
}
