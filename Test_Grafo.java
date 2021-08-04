

public class Test_Grafo {
	public static void main(String[] args) {
		
		Grafo g = new Grafo(5);
		
		g.setAristaPeso(1, 2, 10);
		g.setAristaPeso(2, 3, 5);
		g.setAristaPeso(3, 4, 2);
		
		g.EdgeValues(10, 20);
		
		System.out.println(g.gradoVertice(0));
		
		System.out.println(g.toString());
		
		System.out.println(g.BFS(1));
		System.out.println(g.DFS_R(1));
		
		
		
		
		
	}
}
