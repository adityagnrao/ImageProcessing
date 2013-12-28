public class Vectors{
	int pix1,pix2;
	int x,y;
	Vectors()
	{
		pix1=-1;
		pix2=-1;
	}
	public int getPix1() {
		return pix1;
	}
	public void setPix1(int pix1) {
		this.pix1 = pix1;
	}
	public int getPix2() {
		return pix2;
	}
	public void setPix2(int pix2) {
		this.pix2 = pix2;
	}
	public boolean equals(Vectors v)
	{
		if((this.getPix1()==v.getPix1())&&(this.getPix2()==v.getPix2()))
		{
		return true;
		}
		else
		{
			return false;
		}
	}
}