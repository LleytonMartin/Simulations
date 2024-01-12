package covid;

public class person {
	public double x,y,sdRad,radius,velX,velY;
	public int age = 0,DTR = 0;
	public boolean infected = false;
	public boolean recovered = false;
	public boolean dead = false;
	public boolean hospital = false;
	public boolean isSD = true;
	public person(double x, double y,double radius,double sdRad,boolean isSD,double velX,double velY,int age) {
		this.x = x;
		this.y = y;
		this.age = age;
		this.radius = radius;
		this.sdRad = sdRad;
		this.isSD = isSD;
		this.velX = velX;
		this.velY = velY;
	}
	
	public String toString() {
		return "Age: " + this.age + " |Hospital: " + this.hospital + " |Social Distance: " + (this.sdRad/this.radius);
	}
}

