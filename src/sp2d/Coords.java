package sp2d;

public class Coords {

	private int x, y;
	
	public Coords()
	{
		this.x = -1;
		this.y = -1;
	}
	
	public Coords(int x, int y)
	{
		this.x = x;
		this.y = y;
	}
	
	public int getX() { return this.x; }
	public int getY() { return this.y; }
	
	@Override
	public String toString()
	{
		return "(" + this.x + ", " + this.y + ")";
	}
	
	public String toJson()
	{
		return "[" + this.x + ", " + this.y + "]";
	}
	
	public boolean isPlaced() { return (this.x > -1); }
	
}
