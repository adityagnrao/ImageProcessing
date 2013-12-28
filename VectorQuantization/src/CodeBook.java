import java.util.ArrayList;

public class CodeBook{
	int x,y;
	ArrayList<Vectors> v=new ArrayList<Vectors>();
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}
	CodeBook()
	{
		x=-1;
		y=-1;
	}
	
}