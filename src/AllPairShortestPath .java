

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;


 class AllPairShortestPath {

    int V_MAX = 251; 
    HashMap<String, Integer> mapIp = new HashMap<String, Integer>(); 

    int ipcount = 0;

    private int[][] edgeDistance; 
    int[][] distance2; 
    int[][] distance1; 
    String[] ips;      
    int arcCount;      
    Arc[] firstArc;    
    
    public AllPairShortestPath(){
        
       
        edgeDistance = new int[V_MAX][V_MAX];
        distance2 = new int[V_MAX][V_MAX];
        distance1 = new int[V_MAX][V_MAX];
        firstArc = new Arc[V_MAX + 1];
        ips = new String[V_MAX + 1];
    }




   
    class Arc{
         public int next;
         public int distance; 
         public Arc nextArc; 
         public Arc(){}
         
       
         Arc(int next, int distance) {
             this.next = next;
             this.distance = distance;
             this.nextArc = null;
         }
    };



   
    void add_edge(int start, int end, int distance){
            Arc e = new Arc(end, distance);
            e.nextArc = firstArc[start];
            firstArc[start] = e;
    }

   
    int ip2int(String ip){
        Integer ret;
        if(mapIp.containsKey(ip)) 
            ret = mapIp.get(ip);
        else{ 
            mapIp.put(ip, ipcount);
            ret = ipcount;
            ips[ipcount] = ip;
            ++ipcount;
        }
        return ret;
    }

   
    int lastSegmentNumber(String ip) {
        String[] s = ip.split("\\.");
        return Integer.parseInt(s[3]); 
    }
    
    int INF = 100000000;



   
    void readGraphFromFile(String filename) throws FileNotFoundException{
            arcCount = 0;
            mapIp.clear();
            ipcount = 0;
           
            for(int i = 0; i < V_MAX; i++) for(int j = 0; j < V_MAX; j++){
                    if(i != j) edgeDistance[i][j] = INF;
                    else edgeDistance[i][j] = 0;
            }
            
          
            for(int i = 0; i < V_MAX; i++){
                    firstArc[i] = null;
            }
            
            Scanner scanner = new Scanner(new FileReader(filename)); 

            while(scanner.hasNextLine()){
                   
                    String s = scanner.nextLine();
                    
                    String[] tk = s.split(" ");

                    tk[0] = tk[0].replace(":", ""); 
                    int x = lastSegmentNumber(tk[0]);
                    int root = ip2int(tk[0]);
                    for(int i = 1; i < tk.length; i++){
                            int id = ip2int(tk[i]); 
                            int y = lastSegmentNumber(tk[i]); 
                            int weight = Math.abs(x - y); 
                            edgeDistance[root][id] = weight; 
                            add_edge(root, id, weight);
                            ++arcCount; 
                    }
            }
    }
    
    
   
    void outputResult(String filename, int n) throws FileNotFoundException{
        PrintWriter writer = new PrintWriter(filename); 
        
        for(int i = 0; i < n; i++){ 
             writer.print("\t" + ips[i]);
        }
        writer.println("");
            
        for(int i = 0; i < n; i++){ 
            writer.print(ips[i] + "\t"); 
            for(int j = 0; j < n; j++){
                if(distance1[i][j] < INF) 
                    writer.print(distance1[i][j]);
                else 
                    writer.print(-1);
                if(j == n - 1) writer.println(""); 
                else writer.print("\t");
            }
        }
        writer.close();

    }
    
    
   
    class Node implements Comparable<Node>
    {
        public int node; 
        public int cost; 

        public Node()
        {

        }

        public Node(int node, int cost)
        {
            this.node = node;
            this.cost = cost;
        }

        @Override
        public int compareTo(Node o) {
            return this.cost - o.cost;
        }
    }
    
  
    void dijkstra(int start, int V, int[] distance){ 

        
        for(int i = 0; i < V; i++) distance[i] = INF;
        distance[start] = 0;

        PriorityQueue<Node> q = new PriorityQueue<Node>();
        
      
        q.add(new Node(start, 0));
        
        int cnt = 0; 
        
        boolean[] checked = new boolean[V]; 
        for(int i = 0; i < V; i++)
            checked[i] = false; 

      
        
    
        while(!q.isEmpty()){

            
            Node top = q.poll(); 
            int u = top.node; 
            if(checked[u]) 
                continue;
            checked[u] = true; 
            
            ++cnt;
            if(cnt == V)
                break;
            for(Arc e = firstArc[u]; e != null; e = e.nextArc){ 
                int v = e.next;
                if(checked[v] == false && e.distance + distance[u] < distance[v]){ 
                        distance[v] = distance[u] + e.distance; 
                        q.add(new Node(v, distance[v])); 
                }
            }
        }
    }
    

    void AllPairShortestPathJohnson(int n){ 
        for(int i = 0; i < n; i++){ 
                dijkstra(i, n, distance1[i]); 
        }
    }
    
 

    void AllPairShortestPathFloydWarshall(int n){ 
        
       
        for(int i = 0; i < n; i++) for(int j = 0; j < n; j++){
            distance2[i][j] = edgeDistance[i][j];
        }
        
        for(int i = 0; i < n; i++) for(int j = 0; j < n; j++) for(int k = 0; k < n; k++){ 
                distance2[j][k] = Math.min(distance2[j][k], distance2[j][i] + distance2[i][k]); 
        }
    }
    
   
    void run() throws IOException{
     
        PrintWriter out = new PrintWriter("report.txt"); 
        int numberOfVertex[] = {50, 100, 150, 200, 250}; 
        for(int testcase = 0; testcase < 5; testcase++){ 
            String inputFileName;
            int V = numberOfVertex[testcase]; 
            for(int TestNumber = 1; TestNumber <= 20; TestNumber++){ 
                mapIp.clear();
                inputFileName = "" + V + "-" + (TestNumber * 5); 
                readGraphFromFile(inputFileName + "-in.txt"); 
                out.print(V + "\t" +  arcCount + "\t");
               
                long start = System.currentTimeMillis();
                AllPairShortestPathFloydWarshall(V);
                out.print((System.currentTimeMillis() - start)+ "\t");
                
               
                start = System.currentTimeMillis(); 
                AllPairShortestPathJohnson(V);
                out.print((System.currentTimeMillis() - start)+ "\n"); 
                
                
                
                
              
                for(int i = 0; i < V; i++) for(int j = 0; j < V; j++){
                    if(distance1[i][j] != distance2[i][j]){
                        System.out.println("err "  +i + " " + j + " " + distance1[i][j] + ":" + distance2[i][j]);
                    }
                }
              
                outputResult(inputFileName + "-out.txt", V);
            }
        }
        
        System.out.println(" The output is located in  AllPairShortestPath/duild/class\n for each type of graph we generate 20 diffrent random problem set");
        out.close();
    }
}
