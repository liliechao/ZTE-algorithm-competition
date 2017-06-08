package shortestpath;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;

public class SearchPath{
	private int graph[][];		//表示图的邻接矩阵
	private Node[] nodeList;	//存储图中所有结点，便于根据node的id，获得node。
	private int numberOfNode;
	
	private ArrayList<Node> mustNodeList;	//必经结点列表
	private ArrayList<Edge> mustEdgeList;	//必经边列表
	private ArrayList<Edge> forbiddenEdgeList;		//禁止经过边列表
	
	private int shortestPathWeight = 10000;		//存储最短路径权值,初始值设为很大
	private ArrayList<Node> shortestPath = new ArrayList<Node>();		//存储最短路径
	
	private int dp[][];		//二维矩阵，用来存储权值，行数为节点数，列数为必经节点数+1
	private boolean color[][];
	private LinkedList<Node> s = new LinkedList<Node>();
	private int deepth = 0;		//统计路径深度，不超过9
	private int mustPointLen;	//统计路径中必经结点数量
	private int curWeight;		//统计路径权值
	
	public static final boolean WHITE = false;
	public static final boolean BLACK = true;

	private SearchPath(int[][] graph){
		this.graph = graph;
		this.numberOfNode = graph.length;
		//初始化nodeList
		nodeList = new Node[numberOfNode];
		for(int i= 0;i < numberOfNode;i++){
			nodeList[i] = new Node(i);
		}
		color = new boolean[numberOfNode][numberOfNode];
		for(int i = 0; i< numberOfNode;i++){
			for(int m = 0;m < numberOfNode;m++)
				color[i][m] = WHITE;	//将color中初始值设为未访问
		}
	}
	
	private SearchPath(int[][] graph,ArrayList<Node> mustnodelist){
		this(graph);
		this.mustNodeList = mustnodelist;
		//初始化dp矩阵
		dp = new int [numberOfNode][mustnodelist.size()+1];
		int column = dp[0].length;
		for(int i = 0; i< numberOfNode;i++){
			for(int j = 0;j< column;j++)
				dp[i][j] = 3000;	//将dp中初始值设为很大
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
				if(graph[s.peek().id][i] != 0 && color[peek.id][i] == WHITE){	//判断是否存在未访问的相邻结点
					Node nextnode = nodeList[i];
					adjnode = true;
					color[peek.id][nextnode.id] = BLACK;
					if(nextnode.id != end.id && deepth < 8){	//判断nextNode是否为终点
						s.push(nextnode);
						deepth++;
						curWeight += graph[peek.id][i];
						if(mustNodeList.contains(nextnode))
							mustPointLen++;
						//if(curWeight > dp[s.peek().id][mustPointLen] || curWeight > shortestPathWeight){	//判断是否满足剪枝条件
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