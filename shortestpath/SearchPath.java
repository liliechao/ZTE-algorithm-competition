package shortestpath;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;

public class SearchPath{
	private int graph[][];		//��ʾͼ���ڽӾ���
	private Node[] nodeList;	//�洢ͼ�����н�㣬���ڸ���node��id�����node��
	private int numberOfNode;
	
	private ArrayList<Node> mustNodeList;	//�ؾ�����б�
	private ArrayList<Edge> mustEdgeList;	//�ؾ����б�
	private ArrayList<Edge> forbiddenEdgeList;		//��ֹ�������б�
	
	private int shortestPathWeight = 10000;		//�洢���·��Ȩֵ,��ʼֵ��Ϊ�ܴ�
	private ArrayList<Node> shortestPath = new ArrayList<Node>();		//�洢���·��
	
	private int dp[][];		//��ά���������洢Ȩֵ������Ϊ�ڵ���������Ϊ�ؾ��ڵ���+1
	private boolean color[][];
	private LinkedList<Node> s = new LinkedList<Node>();
	private int deepth = 0;		//ͳ��·����ȣ�������9
	private int mustPointLen;	//ͳ��·���бؾ��������
	private int curWeight;		//ͳ��·��Ȩֵ
	
	public static final boolean WHITE = false;
	public static final boolean BLACK = true;

	private SearchPath(int[][] graph){
		this.graph = graph;
		this.numberOfNode = graph.length;
		//��ʼ��nodeList
		nodeList = new Node[numberOfNode];
		for(int i= 0;i < numberOfNode;i++){
			nodeList[i] = new Node(i);
		}
		color = new boolean[numberOfNode][numberOfNode];
		for(int i = 0; i< numberOfNode;i++){
			for(int m = 0;m < numberOfNode;m++)
				color[i][m] = WHITE;	//��color�г�ʼֵ��Ϊδ����
		}
	}
	
	private SearchPath(int[][] graph,ArrayList<Node> mustnodelist){
		this(graph);
		this.mustNodeList = mustnodelist;
		//��ʼ��dp����
		dp = new int [numberOfNode][mustnodelist.size()+1];
		int column = dp[0].length;
		for(int i = 0; i< numberOfNode;i++){
			for(int j = 0;j< column;j++)
				dp[i][j] = 3000;	//��dp�г�ʼֵ��Ϊ�ܴ�
		}
	}
	
	public SearchPath(int[][] graph,ArrayList<Node> mustnodelist,ArrayList<Edge> mustedgelist,ArrayList<Edge> forbiddenedgelist){
		this(graph,mustnodelist);
		this.mustEdgeList = mustedgelist;
		this.forbiddenEdgeList = forbiddenedgelist;
		for(Edge e: mustEdgeList)
			this.mustVisitEdge(e);
		for(Edge e: forbiddenEdgeList)
			this.forbiddenVisitEdge(e);
	}

	int liliechao;
	public ArrayList<Node> searchPath(Node start,Node end){
		s.push(start);
		deepth++;
		
		while(!s.isEmpty()){
			Node peek = s.peek();
			for(Node n :s){
				color[peek.id][n.id] = BLACK;
			}
			boolean adjnode = false;
			
			for(int i=0;i < numberOfNode;i++){
				if(graph[s.peek().id][i] != 0 && color[peek.id][i] == WHITE){	//�ж��Ƿ����δ���ʵ����ڽ��
					Node nextnode = nodeList[i];
					adjnode = true;
					color[peek.id][nextnode.id] = BLACK;
					if(nextnode.id != end.id && deepth < 8){	//�ж�nextNode�Ƿ�Ϊ�յ�
						s.push(nextnode);
						deepth++;
						curWeight += graph[peek.id][i];
						if(mustNodeList.contains(nextnode))
							mustPointLen++;
						//if(curWeight > dp[s.peek().id][mustPointLen] || curWeight > shortestPathWeight){	//�ж��Ƿ������֦����
						/*if(curWeight > shortestPathWeight){
							Node discard = s.pop();
							deepth--;
							curWeight -= graph[peek.id][i];
							if(mustNodeList.contains(discard))
								mustPointLen--;
							for(int kk = 0;kk < numberOfNode;kk++){
								color[discard.id][kk] = WHITE;
							}
						}else{
							dp[s.peek().id][mustPointLen] = curWeight;
							break;
						}*/
						dp[peek.id][mustPointLen] = curWeight;
						break;
					}else if(nextnode.id == end.id && deepth <= 8){
						s.push(nextnode);
						deepth++;
						//color[peek.id][nextnode.id] = BLACK;
						curWeight += graph[peek.id][i];
						if(curWeight < shortestPathWeight && mustPointLen == mustNodeList.size()){
							shortestPathWeight = curWeight;
							shortestPath.clear();
							shortestPath.addAll(s);
							Collections.reverse(shortestPath);
						}
						s.pop();
						deepth--;
						curWeight -= graph[peek.id][i];
						for(int kkk = 0;kkk < numberOfNode;kkk++){
							color[nextnode.id][kkk] = WHITE;
						}
					}/*else{
						Node discard = s.pop();
						deepth--;
						//curWeight -= graph[peek.id][i];
						curWeight -= graph[s.peek().id][discard.id];
						if(mustNodeList.contains(discard))
							mustPointLen--;
						for(int kkk = 0;kkk < numberOfNode;kkk++){
							color[discard.id][kkk] = WHITE;
						}
						break;
					}*/
				}
			}
			if(!adjnode){
				Node node = s.pop();
				deepth--;
				if(!s.isEmpty())
					curWeight -= graph[s.peek().id][node.id];
				if(mustNodeList.contains(node)){
					mustPointLen--;
				}
				for(int i = 0;i < numberOfNode;i++){
					color[node.id][i] = WHITE;
				}
			}
		}

		return shortestPath;
	}
	
	public void forbiddenVisitEdge(Edge e){
		graph[e.getHead().id][e.getTail().id] += 1000;
		graph[e.getTail().id][e.getHead().id] += 1000;
	}
	
	public void mustVisitEdge(Edge e){
		graph[e.getHead().id][e.getTail().id] += -1000;
		graph[e.getTail().id][e.getHead().id] += -1000;
	}
	
	public static void main(String[] args){
		int[][]graphtest = InputOutputTool.readTopoCsv();
		Node[] startandendtest = new Node[2];
		ArrayList<Node> mustnodelisttest = new ArrayList<Node>();
		ArrayList<Edge> mustedgelisttest = new ArrayList<Edge>();
		ArrayList<Edge> forbiddenedgelisttest = new ArrayList<Edge>();
		InputOutputTool.readDemandCsv(startandendtest, mustnodelisttest, mustedgelisttest, forbiddenedgelisttest);
		SearchPath sptest = new SearchPath(graphtest,mustnodelisttest,mustedgelisttest,forbiddenedgelisttest);
		Node starttest = startandendtest[0];
		Node endtest = startandendtest[1];
		sptest.searchPath(starttest,endtest);
		if(!mustedgelisttest.isEmpty()){
			if(sptest.shortestPathWeight <= -1000*(mustedgelisttest.size()-1)){
				InputOutputTool.writeResultCsv(sptest.shortestPath,true);
				System.out.printf("search path success\n");
				sptest.shortestPathWeight += 1000*mustedgelisttest.size();
				System.out.printf("shortestPathWeight is %d",sptest.shortestPathWeight);
			}else{
				InputOutputTool.writeResultCsv(sptest.shortestPath,false);
				System.out.printf("no such path available\n");
				System.out.printf("shortestPathWeight is %d",sptest.shortestPathWeight);
			}
		}else if(!sptest.shortestPath.isEmpty()){
			InputOutputTool.writeResultCsv(sptest.shortestPath,true);
			System.out.printf("search path success\n");
			System.out.printf("shortestPathWeight is %d",sptest.shortestPathWeight);
		}else{
			InputOutputTool.writeResultCsv(sptest.shortestPath,false);
			System.out.printf("no such path available\n");
			System.out.printf("shortestPathWeight is %d",sptest.shortestPathWeight);
		}
	}
}