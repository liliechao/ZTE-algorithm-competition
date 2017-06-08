package shortestpath;

public class Node{
	int id;
	
	public Node (int id){
		this.id = id;
	}
	
	@Override
	public String toString(){
		return String.valueOf(this.id);
	}
	
	@Override
	public boolean equals(Object n){
		if(this == n){
			return true;
		}
		if (n instanceof Node)
			return this.id == ((Node)n).id;
		return false;
	}
}