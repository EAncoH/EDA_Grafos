
import java.util.*;

public class Grafo {

	private Vertice[] nodes;

	private HashMap<Vertice, HashSet<Vertice>> graph;
	private HashMap<Vertice, HashSet<Arista>> incidencia;
	private final int numeroVertices; 
	private int numeroAristas; 
	private Boolean weighted; 


	public Grafo(int numVertices) {
		this.graph = new HashMap<Vertice, HashSet<Vertice>>();
		this.incidencia = new HashMap<Vertice, HashSet<Arista>>();
		this.numeroVertices = numVertices;
		this.nodes = new Vertice[numVertices];
		for (int i = 0; i < numVertices; i++) {
			Vertice n = new Vertice(i);
			this.nodes[i] = n;
			this.graph.put(n, new HashSet<Vertice>());
			this.incidencia.put(n, new HashSet<Arista>());
		}
		this.weighted = false;
	}


	public Grafo(int numVertices, String modelo) {
		this.graph = new HashMap<Vertice, HashSet<Vertice>>();
		this.incidencia = new HashMap<Vertice, HashSet<Arista>>();
		this.numeroVertices = numVertices;
		this.nodes = new Vertice[numVertices];
		Random coorX = new Random();
		Random coorY = new Random();
		if (modelo == "geo-uniforme") {
			for (int i = 0; i < numVertices; i++) {
				Vertice n = new Vertice(i, coorX.nextDouble(), coorY.nextDouble());
				this.nodes[i] = n;
				this.graph.put(n, new HashSet<Vertice>());
				this.incidencia.put(n, new HashSet<Arista>());
			}
		}
		this.weighted = false;
	}

	public int gradoVertice(int i) {
		Vertice n1 = this.getNode(i);
		return this.graph.get(n1).size();
	}

	public void conectarVertices(int i, int j) {
		Vertice n1 = this.getNode(i);
		Vertice n2 = this.getNode(j);
		HashSet<Vertice> aristas1 = this.getEdges(i);
		HashSet<Vertice> aristas2 = this.getEdges(j);

		aristas1.add(n2);
		aristas2.add(n1); 
		this.numeroAristas +=1; 
	}

	private Boolean existeConexion(int i, int j) {
		Vertice n1 = this.getNode(i);
		Vertice n2 = this.getNode(j);
		HashSet<Vertice> aristas1 = this.getEdges(i);
		HashSet<Vertice> aristas2 = this.getEdges(j);
		if (aristas1.contains(n2) || aristas2.contains(n1)) {
			return true;
		}
		else{
			return false;
		}
	}

	private double distanciaVertices(Vertice n1, Vertice n2) {
		return Math.sqrt(Math.pow((n1.getX() - n2.getX()), 2)
				+ Math.pow((n1.getY() - n2.getY()), 2));
	}

	public int getNumNodes() {return numeroVertices;}

	public int getNumEdges() {return numeroAristas;}

	public Vertice getNode(int i) {return this.nodes[i];}

	public Boolean getWeightedFlag() {return this.weighted;}

	public HashSet<Vertice> getEdges(int i) {
		Vertice n = this.getNode(i);
		return this.graph.get(n);
	}

	public HashSet<Arista> getWeightedEdges(int i) {
		Vertice n = this.getNode(i);
		return this.incidencia.get(n);
	}

	public void setWeighted() {this.weighted = true;}

	public void setIncidencia(int i, HashSet<Arista> aristasPeso) {
		this.incidencia.put(this.getNode(i), aristasPeso);}

	public void setAristaPeso(int i, int j, double peso) {
		if (!this.existeConexion(i, j)) this.conectarVertices(i, j);
		Arista aristaNuevaij = new Arista(i, j, peso);
		Arista aristaNuevaji = new Arista(j, i, peso);
		HashSet<Arista> aristasNodoi = this.getWeightedEdges(i);
		HashSet<Arista> aristasNodoj = this.getWeightedEdges(j);
		aristasNodoi.add(aristaNuevaij);
		aristasNodoj.add(aristaNuevaji);
		this.setIncidencia(i, aristasNodoi);
		this.setIncidencia(j, aristasNodoj);
		if (!this.getWeightedFlag()) this.setWeighted();
	}

	public String toString() {
		String salida;
		if (this.getWeightedFlag()) {
			salida ="graph {\n";     
			for (int i = 0; i < this.getNumNodes(); i++) {
				salida += this.getNode(i).getName() + " [label=\""
						+ this.getNode(i).getName() + " ("+ this.getNode(i).getDistance()
						+ ")\"];\n";
			}
			for (int i = 0; i < this.getNumNodes(); i++) {
				HashSet<Arista> aristas = this.getWeightedEdges(i);
				for (Arista e : aristas) {
					salida += e.getNode1() + " -- " + e.getNode2()
					+ " [weight=" + e.getWeight()+"" + " label="+e.getWeight()+""
					+ "];\n";
				}
			}
			salida += "}\n";
		}
		else { 
			salida ="graph {\n";
			for (int i = 0; i < this.getNumNodes(); i++) {
				salida += this.getNode(i).getName() + ";\n";
			}
			for (int i = 0; i < this.getNumNodes(); i++) {
				HashSet<Vertice> aristas = this.getEdges(i);
				for (Vertice n : aristas) {
					salida += this.getNode(i).getName() + " -- " + n.getName() + ";\n";
				}
			}
			salida += "}\n";
		}
		return salida;
	}
	
	public Grafo BFS(int s) {
		Grafo arbol = new Grafo(this.getNumNodes()); 
		Boolean[] discovered = new Boolean[this.getNumNodes()];  
		PriorityQueue<Integer> L = new PriorityQueue<Integer>();
		discovered[s] = true; 
		for (int i = 0; i < this.getNumNodes(); i++) {
			if (i != s) {
				discovered[i] = false;
			}
		}
		L.add(s);
		while (L.peek() != null) { 
			int u = L.poll();  
			HashSet<Vertice> aristas = this.getEdges(u); 
			for (Vertice n : aristas) {
				if(!discovered[n.getIndex()]) {
					arbol.conectarVertices(u, n.getIndex());
					discovered[n.getIndex()] = true;
					L.add(n.getIndex());
				}
			}
		}
		return arbol;
	}

	public Grafo DFS_R(int s) {
		Grafo arbol = new Grafo(this.getNumNodes()); 
		Boolean[] discovered = new Boolean[this.getNumNodes()]; 
		for (int i = 0; i < this.getNumNodes(); i++) {
			discovered[i] = false;  
		}
		recursivoDFS(s, discovered, arbol);
		return arbol;
	}

	private void recursivoDFS(int u, Boolean[] discovered, Grafo arbol) {
		discovered[u] = true; 
		HashSet<Vertice> aristas = this.getEdges(u);
		for (Vertice n : aristas) {
			if (!discovered[n.getIndex()]) {
				arbol.conectarVertices(u, n.getIndex());
				recursivoDFS(n.getIndex(), discovered, arbol);
			}
		}
	}

	public Grafo DFS_I(int s) {
		Grafo arbol = new Grafo(this.getNumNodes()); 
		Boolean[] explored = new Boolean[this.getNumNodes()];
		Stack<Integer> S = new Stack<Integer>();
		Integer[] parent = new Integer[this.getNumNodes()];
		for (int i = 0; i < this.getNumNodes(); i++) {
			explored[i] = false;
		}
		S.push(s); 
		while(!S.isEmpty()) {
			int u = S.pop(); 
			if(!explored[u]) {
				explored[u] = true; 
				if(u != s) {
					arbol.conectarVertices(u, parent[u]);
				}
				HashSet<Vertice> aristas = this.getEdges(u); 
				for (Vertice n : aristas) {
					S.push(n.getIndex());
					parent[n.getIndex()] = u; 
				}
			}
		}
		return arbol;
	}

	public Grafo EdgeValues(double min, double max) {
		Grafo grafoPesado = new Grafo(this.getNumNodes()); 
		Random rand = new Random();
		double peso;
		for (int i = 0; i < this.getNumNodes(); i++) {  
			for (int j = i; j < this.getNumNodes(); j++) { 
				if(this.existeConexion(i, j)) { 
					peso = rand.nextFloat()*(max - min) + min;
					grafoPesado.setAristaPeso(i, j, peso);
				}
			}
		}
		return grafoPesado;
	}

	public Grafo Dijkstra(int s) {
		Grafo arbol = new Grafo(this.getNumNodes()); 
		double inf = Double.POSITIVE_INFINITY;  
		Integer[] padres = new Integer[arbol.getNumNodes()];
		for (int i = 0; i < arbol.getNumNodes(); i++) {
			this.getNode(i).setDistance(inf);
			padres[i] = null;
		}
		this.getNode(s).setDistance(0.0);
		padres[s] = s;
		PriorityQueue<Vertice> distPQ = new PriorityQueue<>(vertexDistanceComp);
		for (int i = 0; i < this.getNumNodes(); i++) {
			distPQ.add(this.getNode(i));
		}
		while (distPQ.peek() != null) {  
			Vertice u = distPQ.poll(); 

			HashSet<Arista> aristas = this.getWeightedEdges(u.getIndex());
			for (Arista e : aristas) {
				if(this.getNode(e.getIntN2()).getDistance() > this.getNode(u.getIndex()).getDistance() + e.getWeight()) {
					this.getNode(e.getIntN2()).setDistance(this.getNode(u.getIndex()).getDistance() + e.getWeight());
					padres[e.getIntN2()] = u.getIndex();
				}
			}
		}
		for (int i = 0; i < arbol.getNumNodes(); i++) {
			arbol.setAristaPeso(i, padres[i], 1);
			arbol.getNode(i).setDistance(this.getNode(i).getDistance());
		}
		return arbol;
	}
	Comparator<Vertice> vertexDistanceComp = new Comparator<Vertice>() {
		public int compare(Vertice n1, Vertice n2) {
			return Double.compare(n1.getDistance(), n2.getDistance());
		}
	};

}