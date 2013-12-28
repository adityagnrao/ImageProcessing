import java.awt.Color;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;




public class VectorQuantize {
	
	static Pixels[]p=new Pixels[352*288];
	static int  N=0;
	 public static void main(String[] args) {
		   	
		//Width and Height hardcoded 
		 
		    Vectors[] v=new Vectors[352*288/2];
		    
		    
		    for(int i=0;i<352*288;i++)
		    {
		    p[i]=new Pixels();	
		    if(i<(352*288/2))
		    		{
		    	v[i]=new Vectors();
		    		}
		    }
		
		    int width = 352; 
			int height = 288;
			N=Integer.parseInt(args[1]);
		    BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		    BufferedImage img1 = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		    int loop=0;
		    if(N<=256&&N>=128)
		    {
		    	loop=1;
		    }
		    else 
		    if(N<=64&&N>16)
		    {
		    	loop=8;
		    }else
		    {
		    	loop=100;
		    }
		    
		    try {
			    File file = new File(args[0]);
			    InputStream is = new FileInputStream(file);

			    long len = file.length();
			    byte[] bytes = new byte[(int)len];
			    int offset=0;
			    int numRead=0;
			    while (offset < bytes.length && (numRead=is.read(bytes, offset, bytes.length-offset)) >= 0) {
		            offset += numRead;
		        }
		        is.close();
		    
		    int ind=0;
		    int vind=0;
		    for(int y = 0; y < height; y++){
		    	
				for(int x = 0; x < width-1; x+=2){
					
					byte r = bytes[ind];
					byte g = bytes[ind+height*width];
					byte b = bytes[ind+height*width*2]; 
					byte r1= bytes[ind+1];
					byte g1 = bytes[1+ind+height*width];
					byte b1=bytes[1+ind+height*width*2];
					
					int pix = 0xff000000 | (r & 0xFF) << 16 | (g & 0xFF) << 8 | b & 0xFF;	
					int pix2=0xff000000 | (r1 & 0xFF) << 16 | (g1 & 0xFF) << 8 | b1 & 0xFF;
					img.setRGB(x,y,pix);
					img.setRGB(x+1, y, pix2);
					Color c1=new Color(img.getRGB(x, y));
					Color c2=new Color(img.getRGB(x+1, y));
					p[ind].setR(c1.getRed());
					p[ind+1].setR(c2.getRed());
//					p[ind].setG(g);
//					p[ind].setB(b);
					
					 Vectors v1=new Vectors();
					    v1.setPix1(p[ind].getR());
						 v1.setPix2(p[ind+1].getR());
//						p[ind].setV(v1);
//						p[ind+1].setV(v1);
						v[vind].setPix1(v1.getPix1());
						v[vind].setPix2(v1.getPix2());
						v[vind].x=ind;
						v[vind].y=ind+1;
						ind+=2;
						vind++;
					
				}
				}

             //Initializing CodeBook of Vectors
             Integer init_scope=(height*width/2)/N;
             
 		    CodeBook []c=new CodeBook[N];
 		    for (int i=0;i<N;i++)
 		    {
 		    	c[i]=new CodeBook();
 		    	c[i].setX(v[init_scope*i].getPix1());
 		    	c[i].setY(v[init_scope*i].getPix2());
// 		    	c.v.add(v[init_scope*i]);
 		    	//System.out.print(c[i].getX()+"/"+c[i].getY()+"    ");
 		    }
 		    
 		    int max=0;
 		    int min=0;
 		  c=assign_vectors(width,height,c,N,v);
// 		  int close_vector=-1;
 		   for (int i=0;i<N;i++)
		    {
 			  			  
		    	if(c[i].v.size()==0)
		    	{
		    		
//		    		c[i]=assign_random_vector(c[i]);
		    		c[i]=assign_random_vector(c[i]);
		    		for(int j=0;j<N;j++)
		    		{
		    			if(i!=j){
		    				
		    			while(c[i].x==c[j].x&&c[i].y==c[j].y)
		    				c[i]=assign_random_vector(c[i]);
		    			}
		    		}
//		    		Vectors v1=new Vectors();
//		    		for(int j=0;j<N;j++)
//			    		{
//		    			if(i!=j&&(i!=close_vector)){
//		    		while(c[i].x==c[j].x&&c[i].y==c[j].y)
//		    		{
//		    		v1.setPix1(c[j].getX());
//		    		v1.setPix2(c[j].getY());
//		    		close_vector=calculate_distance(v1,N,c);
//		    		if(c[close_vector].v.size()>1)
//		    		{
//		    		c[i].setX(c[close_vector].v.get(c[close_vector].v.size()-1).getPix1());
//		    		c[i].setY(c[close_vector].v.get(c[close_vector].v.size()-1).getPix2());
//		    		}
//		    		
//		    		}
//		    			}
//		    	}
		    		
		    	}
		    	if(c[i].v.size()>c[max].v.size())
		    	{
		    		max=i;
		        }
		    	if(c[i].v.size()<c[min].v.size())
		    	{
		    		min=i;
		        }
		    }
 		  
// 		 int min=0;
  		int max_size=c[max].v.size(); 
  		int min_size=c[min].v.size();
  		int loop1=0;
		  do
		  {
			  System.out.println("inside loop"+loop1+"  please wait");
			  max_size=c[max].v.size();
			  min_size=c[min].v.size();
 		  
 		   for (int i=0;i<N;i++)
		    {
 			   int xsum=0;
 			   int ysum=0;
		    	
		    	for(int j=0;j<c[i].v.size();j++)
		    	{
		    	      xsum=xsum+c[i].v.get(j).getPix1();
		    	      ysum=ysum+c[i].v.get(j).getPix2();
		    	}
		    	if(c[i].v.size()!=0)
		    	{
		    	c[i].setX(xsum/c[i].v.size());
		    	c[i].setY(ysum/c[i].v.size());
		    	c[i].v.clear();
		    	}
		    	c=assign_vectors(width,height,c,N,v);
//		    	System.out.println("loop="+loop+"  "+i+"/"+c[i].v.size());
		    
// 		   System.out.println(loop);
// 		   if(c[i].v.size()>c[max].v.size())
// 		   {
// 			   max=i;
// 		   }
 		   }
 		   loop--;
 		   loop1++;
// 		   }while(((c[max].v.size()-max_size)!=0)&&((c[min].v.size()-min_size)!=0));
		  }while(((c[max].v.size()-max_size)!=0)&&((c[min].v.size()-min_size)!=0)&&(loop!=0));

 		  
 		  ind=0;
 		  int pix1,pix2;
 		  //System.out.println("I am here");
 		 for(int y = 0; y < height; y++){
		    	
				for(int x = 0; x < width; x+=2){
					
					int rgb=new Color(c[p[ind].index].getX(),c[p[ind].index].getX(),c[p[ind].index].getX()).getRGB();
					pix1=rgb;
					rgb=new Color(c[p[ind+1].index].getY(),c[p[ind+1].index].getY(),c[p[ind+1].index].getY()).getRGB();
					pix2=rgb;

					img1.setRGB(x, y, pix1);
					img1.setRGB(x+1, y, pix2);
					ind+=2;
				}
 		 }
 		  
 		 
 		  
 		
		    
				
		    }
		    catch (FileNotFoundException e) {
		        e.printStackTrace();
		      } catch (IOException e) {
		        e.printStackTrace();
		      }
				

		    JFrame frame = new JFrame();
		    
		    JPanel subPanel1 = new JPanel();
		    subPanel1.setPreferredSize (new Dimension(width, height));
		    subPanel1.setBackground (Color.gray);
		    if(N==256)
            {
           	 img1=img;
            }
		   JLabel label1= new JLabel("",new ImageIcon(img),SwingConstants.CENTER);

		   JLabel label2 = new JLabel("",new ImageIcon(img1),SwingConstants.LEFT);

		   subPanel1.add (label1);

		   subPanel1.add (label2);
		   


		   frame.setSize(width*4, height*4);
		   
		    frame.setVisible(true);
		    frame.add(subPanel1);
	 }
	 
	 
	 public static int calculate_distance(Vectors v1,int N,CodeBook[] c)
	 {
		 Double []dlist=new Double[N];
		 
		 double x,y;
		 for(int j=0;j<N;j++)
		 {
			 x=Math.pow((c[j].getX()- v1.getPix1()),2);
			y=Math.pow((c[j].getY()-v1.getPix2()), 2);
			 dlist[j]= Math.sqrt(x+y);
		 }
		 
		 return find_minimum(dlist);
	 }
	 public static int find_minimum(Double []dlist1)
	 {
		 int min=0;
		 for(int k=0;k<dlist1.length;k++)
		 {
			 if(dlist1[k]<dlist1[min])
			 {
				 min=k;
			 }
		 }
		 return (int)min;
		
	 }
	 public static CodeBook[] assign_vectors(int width,int height, CodeBook[] c,int N,Vectors[] v)
	 {
		 int close_code;
		 for(int i=0;i<width*height/2;i++)
		    {
			 close_code=calculate_distance(v[i],N,c);
			
		    	  	c[close_code].v.add(v[i]);
		    	// 	System.out.print(v[i].x+" "+ v[i].y+" ");
		    	  	//System.out.println(close_code);
			    p[v[i].x].index=close_code;
			    p[v[i].y].index=close_code;
			  //  System.out.println(p[v[i].x].index);
			   
			 
		    }
		 return c;
	 }
	 
	 public static CodeBook assign_random_vector(CodeBook c)
	 {
		 double rand=(Math.random()*1000)%256;
		 c.setX((int) rand);
		 c.setY((int) rand/2);
		 return c;
		 
	 }
}


