import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.jgrapht.Graph;
import org.jgrapht.graph.SimpleGraph;

/*simulator.java
*Created on 2018年3月12日
*/

public class Simulator {
	private int numberOfVertex;
  	private Graph<Vertex, Edge> graph = new SimpleGraph<>(Edge.class);
  	private ArrayList<Vertex> vertexList=new ArrayList<>();
  	private CountDownLatch latch;
  	private int phase=0;
  	private int rounds;
  	private int numberOfMessage=0;
	public Simulator(int n){
		this.numberOfVertex=n;
	}
	
	public Output generateGraph(int num) throws InterruptedException{
		ArrayList<Integer> idList=new ArrayList<>();
		for(int i=0;i<num;i++){
			idList.add(uniqueId(num, idList));
		}
		for(int i=0;i<num;i++){
			 Vertex vertex=new Vertex(idList.get(i), "unknown", 0);
	   		 vertexList.add(vertex);
	   		 graph.addVertex(vertex);
	   	 }
	   	 for(int i=0;i<num-1;i++){
	   		 Edge edge=new Edge(vertexList.get(i), vertexList.get(i+1));
	   		 graph.addEdge(vertexList.get(i), vertexList.get(i+1), edge);
	   	 }
	   	 Edge edge=new Edge(vertexList.get(num-1), vertexList.get(0));
	   	 graph.addEdge(vertexList.get(num-1), vertexList.get(0), edge);
	   	 for(Vertex vertex: graph.vertexSet()){
	   		 System.out.println(vertex.toString());
	   	 }
	   	 System.out.println("---------------------------\n");
	   	 while(!checkLeader(graph)){
	   		 int distance=distance(phase);
	   		 ExecutorService executor=Executors.newCachedThreadPool();
	   		 for(int i=0;i<vertexList.size();i++){
	   			 Vertex vertex=vertexList.get(i);
	   			 executor.execute(new HS_Algorithm(graph, vertex, distance));
	   		 }
	   		 executor.shutdown();
	   		 phase++;
	   	 }
	   	 for(Vertex vertex: graph.vertexSet()){
	   		 System.out.println(vertex.toString());
	   		 if(vertex.getStatus().equals("leader")){
	   			 rounds=vertex.getRound();
	   		 }
	   	 }
	   	 Output output=new Output(numberOfMessage, rounds);
	   	 return output;
   }
   
	private Vertex leftNeighbour(Graph<Vertex, Edge> g, Vertex v){
		Edge edge;
		Vertex neighbour=null;
		Iterator<Edge> itr=g.edgesOf(v).iterator();
		while(itr.hasNext()){
			edge=itr.next();
			if(edge.getVertex1().getMyId()==v.getMyId()){
				neighbour=edge.getVertex2();
			}
		}
		return neighbour;
	}
	
	private Vertex rightNeighbour(Graph<Vertex, Edge> g, Vertex v){
		Edge edge;
		Vertex neighbour=null;
		Iterator<Edge> itr=g.edgesOf(v).iterator();
		while(itr.hasNext()){
			edge=itr.next();
			if(edge.getVertex2().getMyId()==v.getMyId()){
				neighbour=edge.getVertex1();
			}
		}
		return neighbour;
	}
	
	private int distance(int n){
		int result=1;
		if(n>0){
			for(int i=0;i<=n;i++){
				result=2*result;
			}
		}
		return result;
	}
	
	private boolean checkLeader(Graph<Vertex, Edge> g){
		boolean result=false;
		Iterator<Vertex> itr=g.vertexSet().iterator();
		while(itr.hasNext()){
			if(itr.next().getStatus().equals("leader")){
				result=true;
			}
		}
		return result;
	}
	private int uniqueId(int num, ArrayList<Integer> idList){
		int randomId=(int) (Math.random()*3*num);
		if(idList.contains(randomId)||randomId==0){
			randomId=uniqueId(num, idList);
		}
		return randomId;
	}
	
	private class HS_Algorithm implements Runnable{
		private Graph graph;
		private Vertex vertex;
		private Vertex neighbourRight, neighbourLeft;
		private int distance;
		HS_Algorithm(Graph g, Vertex v, int d){
			this.graph=g;
			this.vertex=v;
			this.distance=d;
		}
		public void run(){
			achieve();
		}
		
		public void achieve(){
			neighbourRight=vertex;
			neighbourLeft=vertex;
			int leftMessage=0;
			int rightMessage=0;
			int leftRound=0;
			int rightRound=0;
			int token=vertex.getMyId();
			for(int i=1;i<=distance;i++){
				Vertex v=neighbourRight;
				neighbourRight=rightNeighbour(graph, v);
				if(neighbourRight.getMyId()>token){
					rightRound++;
					rightMessage++;
					break;
				}else if(neighbourRight.getMyId()==token){
					rightRound++;
					rightMessage++;
					vertex.setStatus("leader");
					break;
				}else if(neighbourRight.getMyId()<token){
					rightRound++;
					rightMessage++;
					if(i==distance){
						rightMessage=rightMessage*2;
						rightRound++;
					}
				}
			}
			for(int i=1;i<=distance;i++){
				Vertex v=neighbourLeft;
				numberOfMessage++;
				neighbourLeft=leftNeighbour(graph, v);
				if(neighbourLeft.getMyId()>token){
					leftRound++;
					leftMessage++;
					break;
				}else if(neighbourLeft.getMyId()==token){
					leftRound++;
					leftMessage++;
					vertex.setStatus("leader");
					break;
				}else if(neighbourLeft.getMyId()<token){
					leftRound++;
					leftMessage++;
					if(i==distance){
						leftRound++;
						leftMessage=leftMessage*2;
					}
				}
			}
			if(leftRound>rightRound){
				vertex.setRound(vertex.getRound()+leftRound);
			}else{
				vertex.setRound(vertex.getRound()+leftRound);
			}
			numberOfMessage=numberOfMessage+leftMessage+rightMessage;
		}
	}
}
