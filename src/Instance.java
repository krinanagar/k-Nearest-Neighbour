/**
 * Instance class describes one set of data in the iris data set. Each instance is made up of
 * the flowers sepal length, sepal width, petal length, petal width and the type of flower it is.
 * @author Krina Nagar
 *
 */
public class Instance {
	private double sepalLength;
	private double sepalWidth;
	private double petalLength;
	private double petalWidth;
	private String type;
	
	public Instance(double sepalLength, double sepalWidth, double petalLength, double petalWidth, String type){
		this.sepalLength = sepalLength;
		this.sepalWidth = sepalWidth;
		this.petalLength = petalLength;
		this.petalWidth = petalWidth;
		this.type = type;
	}

	public double getSepalLength() {
		return sepalLength;
	}

	public double getSepalWidth() {
		return sepalWidth;
	}

	public double getPetalLength() {
		return petalLength;
	}

	public double getPetalWidth() {
		return petalWidth;
	}

	public String getType() {
		return type;
	}
	
}
