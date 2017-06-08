package shortestpath;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class InputOutputTool {
	public static int[][] readTopoCsv(){
		int linecnt = getFileLineCount();
		int[][] graph = new int [linecnt][linecnt];
		BufferedReader reader = null;
		try {
			/* topo.csv文件中内容说明：
			 * 第i行第j个数字含义为，编号为i的结点到编号为j的结点的距离。
			 * */
            reader = new BufferedReader(new FileReader("topo.csv"));
            String line = null;
            int row = 0;
            while((line=reader.readLine())!=null){ 
                String item[] = line.split(",");//CSV格式文件为逗号分隔符文件，这里根据逗号切分
                for(int column = 0;column < linecnt;column++)
                	graph[row][column] = Integer.parseInt(item[column]);
                row++;
            } 
        } catch (Exception e) { 
            e.printStackTrace(); 
        } finally{
        	try {
				reader.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
		/*//检验输入的邻接矩阵是否为对称矩阵，因为无向图的邻接矩阵是对称的
		boolean flag = true;
		for(int m = 0; m < linecnt;m++)
			for(int n= 0;n < linecnt;n++){
				if(graph[m][n] != graph[n][m]){
					flag = false;
					System.out.printf("m = %d  n = %d",m,n);
					System.out.println();
				}
			}
		System.out.printf(String.valueOf(flag));*/
		return graph;
	}
	
	public static void readDemandCsv(Node[] startandend,ArrayList<Node> mustnodelist,ArrayList<Edge> mustedgelist,ArrayList<Edge> forbiddenedgelist){
		BufferedReader reader = null;
		try {
			/* demand.csv文件中内容说明：
			 * 第一行存放起始结点，节点间以逗号隔开
			 * 第二行存放必经结点，节点间以逗号隔开
			 * 第三行存放必经边，边的头结点与尾节点之间用“-”隔开，边与边之间用逗号隔开
			 * 第四行存放禁止经过的边，边的头结点与尾节点之间用“-”隔开，边与边之间用逗号隔开
			 * */
            reader = new BufferedReader(new FileReader("demand.csv"));
            String line = null;
            int row = 0;
            while((line=reader.readLine())!=null){
            	if(!line.equals("")){
	                String item[] = line.split(",");//CSV格式文件为逗号分隔符文件，这里根据逗号切分
	                if (row == 0){
	                	startandend[0] = new Node(Integer.parseInt(item[0]));
	                	startandend[1] = new Node(Integer.parseInt(item[1]));
	                }else if (row == 1){
	                	for(String s : item){
	                    	mustnodelist.add(new Node(Integer.parseInt(s)));
	                	}
	                }else if (row == 2){
	                	for(String s : item){
	                		String node[] = s.split("-");
	                		mustedgelist.add(new Edge(new Node(Integer.parseInt(node[0])),new Node(Integer.parseInt(node[1]))));
	                	}
	                }else if (row == 3){
	                	for(String s : item){
	                		String node[] = s.split("-");
	                		forbiddenedgelist.add(new Edge(new Node(Integer.parseInt(node[0])),new Node(Integer.parseInt(node[1]))));
	                	}
	                }
            	}
                row++;
            } 
        } catch (Exception e) { 
            e.printStackTrace(); 
        } finally{
        	try {
				reader.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
	}
	
	public static void writeResultCsv(ArrayList<Node> shortestpath,boolean hasFound){
		BufferedWriter bw = null;
		try { 
			File csv = new File("result.txt");
			bw = new BufferedWriter(new FileWriter(csv));
			if (hasFound) {
				bw.write("search path success");
				bw.newLine();	
				bw.write("the required path is : ");
				int length = shortestpath.size();
				for (int i = 0; i < length; i++) {
					bw.write(shortestpath.get(i).toString());
					if (i != length - 1)
						bw.write("-");
				}
			} else{
				bw.write("no such path available");
				//bw.newLine();	
				//bw.write("the best choice is : ");
			}
			/*int length = shortestpath.size();
			for (int i = 0; i < length; i++) {
				bw.write(shortestpath.get(i).toString());
				if (i != length - 1)
					bw.write("-");
			}*/
        } catch (IOException e) { 
        	e.printStackTrace(); 
        } finally{
        	 try {
				bw.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
        }
	}
	
	private static int getFileLineCount() {
        int cnt = 0;
        BufferedReader reader = null;
        try {
			reader = new BufferedReader(new FileReader("topo.csv"));
			String line=reader.readLine();
			String item[] = line.split(",");
			cnt = item.length;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
        	try {
				reader.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
        return cnt;
    }
}
