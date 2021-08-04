
public class Vertice {

	private String name;
	private Integer numAristas;
	private Integer index;

	private double x;
	private double y;

	private double distance;

	public Vertice(String name) {
		this.name = name;
		this.numAristas = 0;
	}

	public Vertice(int name) {
		this.index = name;
		this.name = "n" + String.valueOf(name);
		this.numAristas = 0;
	}

	public Vertice(int name, double x, double y) {
		this.index = name;
		this.name = "n" + String.valueOf(name);
		this.x = x;
		this.y = y;
	}

	public String getName() {
		return name;
	}

	public Integer getNumEdges() {
		return numAristas;
	}

	public Integer getIndex() {
		return index;
	}

	public double getDistance() {
		return distance;
	}

	public void setDistance(double d) {
		this.distance = d; 
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

}
