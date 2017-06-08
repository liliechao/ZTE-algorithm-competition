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
			/* topo.csv�ļ�������˵����
			 * ��i�е�j�����ֺ���Ϊ�����Ϊi�Ľ�㵽���Ϊj�Ľ��ľ��롣
			 * */
            reader = new BufferedReader(new FileReader("topo.csv"));
            String line = null;
            int row = 0;
            while((line=reader.readLine())!=null){ 
                String item[] = line.split(",");//CSV��ʽ�ļ�Ϊ���ŷָ����ļ���������ݶ����з�
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
		/*//����������ڽӾ����Ƿ�Ϊ�Գƾ�����Ϊ����ͼ���ڽӾ����ǶԳƵ�
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
			/* demand.csv�ļ�������˵����
			 * ��һ�д����ʼ��㣬�ڵ���Զ��Ÿ���
			 * �ڶ��д�űؾ���㣬�ڵ���Զ��Ÿ���
			 * �����д�űؾ��ߣ��ߵ�ͷ�����β�ڵ�֮���á�-�������������֮���ö��Ÿ���
			 * �����д�Ž�ֹ�����ıߣ��ߵ�ͷ�����β�ڵ�֮���á�-�������������֮���ö��Ÿ���
			 * */
            reader = new BufferedReader(new FileReader("demand.csv"));
            String line = null;
            int row = 0;
            while((line=reader.readLine())!=null){
            	if(!line.equals("")){
	                String item[] = line.split(",");//CSV��ʽ�ļ�Ϊ���ŷָ����ļ���������ݶ����з�
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
