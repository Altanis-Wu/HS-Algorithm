import java.util.Scanner;
 
public class Myproject {
 
    public static void main(String[] args) throws InterruptedException {
    	int sumOfMessage=0;
    	int sumOfRounds=0;
    	Scanner kb=new Scanner(System.in);
    	System.out.println("Please enter the number of vertex:");
    	int numOfVertex=kb.nextInt();
    	System.out.println("Please enter the times of simulation:");
    	int times=kb.nextInt();
    	for(int i=0;i<times;i++){
    		Simulator HS_Algorithm=new Simulator(numOfVertex);
        	Output output=HS_Algorithm.generateGraph(numOfVertex);
        	sumOfMessage=sumOfMessage+output.getNumberOfMessage();
        	sumOfRounds=sumOfRounds+output.getRounds();
    	}
    	System.out.println("The average number of message is "+sumOfMessage/times+".");
    	System.out.println("The average number of rounds is "+sumOfRounds/times+".");
    }   
}