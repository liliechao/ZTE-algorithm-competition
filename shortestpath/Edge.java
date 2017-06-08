package shortestpath;

public class Edge {
	private Node head;
	private Node tail;
	private int weight;
	
	public Edge (Node head,Node tail){
		this.head = head;
		this.tail = tail;
	}
	
	@Override
	public String toString(){
		return String.valueOf(this.head) + "-" + String.valueOf(this.tail);
	}
	
	@Override
	public boolean equals(Object n){
		if(this == n){
			return true;
		}
		if (n instanceof Edge)
			return this.head == ((Edge)n).head && this.tail == ((Edge)n).tail && this.weight == ((Edge)n).weight;
		return false;
		
	}
	
	public Node getHead() {
		return head;
	}
	public void setHead(Node head) {
		this.head = head;
	}
	public Node getTail() {
		return tail;
	}
	public void setTail(Node tail) {
		this.tail = tail;
	}
	public int getWeight() {
		return weight;
	}
	public void setWeight(int weight) {
		this.weight = weight;
	}
}
