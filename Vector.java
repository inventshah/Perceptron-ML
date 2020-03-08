/** 
* Sachin Shah
* version 1
*/

public class Vector
{
	public double a;
	public double b;
	public double c;
	public double d;

	public Vector(double a, double b)
	{
		this.a = a;
		this.b = b;
		this.c = 0;
		this.d = 0;
	}

	public Vector(double a, double b, double c)
	{
		this.a = a;
		this.b = b;
		this.c = c;
		this.d = 0;
	}

	public Vector(double a, double b, double c, double d)
	{
		this.a = a;
		this.b = b;
		this.c = c;
		this.d = d;
	}

	public void set(Vector v)
	{
		this.a = v.a;
		this.b = v.b;
		this.c = v.c;
		this.d = v.d;
	}

	public Vector plus(Vector v){
		return new Vector(v.a+this.a, v.b+this.b, v.c+this.c);
	}

	public void add(Vector v){
		this.a += v.a;
		this.b += v.b;
		this.c += v.c;
	}

	public double dot(Vector v){
		return (this.a*v.a + this.b*v.b + this.c*v.c);
	}

	public Vector times(double s){
		return new Vector(s*this.a, s*this.b, s*this.c);
	}

	public String toString(){
		return String.format("(%f, %f, %f, %f)", a, b, c, d);
	}

	public boolean equals(Vector v){
		return (v.a == this.a && v.b == this.b && v.c == this.c && v.d == this.d);
	}
}
