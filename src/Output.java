/*Output.java
*Created on 2018年3月15日
*/

public class Output {
	private int numberOfMessage;
	private int rounds;
	public Output(int num, int r){
		this.numberOfMessage=num;
		this.rounds=r;
	}
	
	public int getNumberOfMessage(){
		return numberOfMessage;
	}
	
	public int getRounds(){
		return rounds;
	}
	
}
