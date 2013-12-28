
import java.awt.*;
import java.awt.image.*;
import java.io.*;
import javax.swing.*;
import com.jhlabs.image.*;


public class imageFilterSampling {

  
   public static void main(String[] args) {
   	

	
   	int width = 352; 
	int height = 288;
	int y_sample=Integer.parseInt(args[1]);
	int u_sample=Integer.parseInt(args[2]);
	int v_sample=Integer.parseInt(args[3]);
	int Q=Integer.parseInt(args[4]);
    BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
    BufferedImage img1= new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
    BufferedImage filimg=new BufferedImage(width,height,BufferedImage.TYPE_INT_RGB);
    BufferedImage img2 = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
    GaussianFilter gf= new GaussianFilter();
    int ind=0;
    
    
   
    
      
    try {
	    File file = new File(args[0]);
	    InputStream is = new FileInputStream(file);
	    long len = file.length();
	    
	    byte[] bytes = new byte[(int)len];
	    int offset = 0;
        int numRead = 0;
	    while (offset < bytes.length && (numRead=is.read(bytes, offset, bytes.length-offset)) >= 0) {
            offset += numRead;
        }
        is.close();
        	    
		for(int y = 0; y < height; y++){
			
			for(int x = 0; x < width; x++){
		 
				byte a = 0;
				byte r = bytes[ind];
				byte g = bytes[ind+height*width];
				byte b = bytes[ind+height*width*2]; 
				
				int pix = 0xff000000 | ((r & 0xff) << 16) | ((g & 0xff) << 8) | (b & 0xff);
				//int pix = ((a << 24) + (r << 16) + (g << 8) + b);
				img.setRGB(x,y,pix);
				img1.setRGB(x, y,pix);
				ind++;
			}
		   }
        } catch (FileNotFoundException e) {
        e.printStackTrace();
        } catch (IOException e) {
        e.printStackTrace();
       }
		
    
	   //Gaussian Filtering
	   
//	   
//	  img1 = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
//			Graphics gr = img1.createGraphics();
//			gr.drawImage(img, 0, 0, null);
		gf.setRadius(2);
		gf.filter(img,filimg);
	    
	    
//	    changing input from 0 to 1 for Y, U, and V if specified as 0
	    if(y_sample==0 || u_sample==0 || v_sample == 0)
	    {
	    	System.out.println("Taking input as 1 for 0");
	    	if(y_sample==0)
	    	{
	    		y_sample=1;
	    	}
	    	if(u_sample==0)
	    	{
	    		u_sample=1;
	    	}
	    	if(v_sample==0)
	    	{
	    		v_sample=1;
	    	}
	    }
	    
	    Integer[] yuv=new Integer[width*height*4];
	    for(int k=0; k<yuv.length;k++)
	    {
	    	yuv[k]=0;
	    }
	    
	    
        
//        Finding the number of bits required to represent r g b based on the Q value
        Integer no_of_bits=(int)Math.round(Math.log10(Q)/Math.log10(2));
        Integer q= (8-no_of_bits);
//        System.out.println(q);
        q= (int) Math.pow(2, q);
        System.out.println("no of bits per color channel  "+no_of_bits);
       
//        
//        for(int j=0;j<height;j++)
//        {
//        	for(int k=0;k<width;k++)
//        	{
//        		bytes[j]=(byte) filimg.getRGB(j, k);
//        		
//        	}
//        	
//        }
//        
    				
        ind = 0;
		Integer lu,lv;
		
		for(int y = 0; y < height; y++){
	
			for(int x = 0; x < width; x++){
				lu=ind+width*height;
				lv=ind+width*height*2;
//				byte a = 0;
				Color c=new Color(filimg.getRGB(x, y));
				int r = c.getRed();
				int g = c.getGreen();
				int b = c.getBlue(); 
				
				if(ind%y_sample==0){
				Integer Y =( (r & 0xff) *(299)+(g  & 0xff)*(587)+(b  & 0xff)*(114))/1000;
					
				yuv[ind]=Y;
				}else {yuv[ind]=-1;}
				
				
				if(ind%u_sample==0){
					Integer U =( (r  & 0xff)*(-147)+(g  & 0xff)*(-289)+(b  & 0xff)*(436))/1000;
					yuv[lu]=U;

				}else {yuv[lu]=-1;}
				
				
				if(ind%v_sample==0){
				Integer V =( (r  & 0xff)*(615)+(g  & 0xff)*(-515)+(b  & 0xff)*(-100))/1000;
				yuv[lv]=V;
				}else {yuv[lv]=-1;}
				
				
				
				int pix = 0xff000000 | ((r & 0xff) << 16) | ((g & 0xff) << 8) | (b & 0xff);
				img.setRGB(x,y,pix);
				ind++;
			}
		}
		
	
			ind=0;	
			
		for(int y = 0; y < height; y++){
			
			for(int x = 0; x < width; x++){
				lu=ind+width*height;
				lv=ind+width*height*2;

				if(ind<(height*width))
               {
				if(ind%y_sample!=0){
//				yuv[ind]=(yuv[ind-1]+yuv[ind+1]+yuv[ind+width]+yuv[ind+width+1]+yuv[ind+width-1])/5;
//					yuv[ind]=(yuv[ind-(ind%y_sample)]+yuv[ind+y_sample-(ind%y_sample)])/2;
//					yuv[ind]=(yuv[ind-1]+yuv[ind+y_sample-(ind%y_sample)])/2;
//					yuv[ind]=(yuv[ind-1]+yuv[ind+1])/2;
//					yuv[ind]=yuv[0]+((yuv[ind]-yuv[0])/ind)*x;
					
//					liner interpolation
					yuv[ind]=yuv[ind-(ind%y_sample)]+((yuv[ind+y_sample-(ind%y_sample)]-yuv[ind-(ind%y_sample)])/y_sample)*(ind%(y_sample));
					System.out.print(yuv[ind]+" ");
				}
				if(ind%u_sample!=0){
//					yuv[lu]=(yuv[lu-1]+yuv[lu+1])/2;
//					yuv[lu]=(yuv[lu-(ind+(width*height)%5)]+yuv[lu+u_sample-((lu)%u_sample)])/2;
//					yuv[lu]=(yuv[lu-1]+yuv[lu+u_sample-((lu)%u_sample)])/2;
//					yuv[lu]=(yuv[lu-1]+yuv[lu+1]
//							+yuv[(lu)+width]+yuv[(lu)+width+1]+yuv[(lu)+width-1])/5;
//					yuv[lu]=yuv[lu]+(yuv[lu]-yuv[width*height])/ind;
					
//					liner interpolation
					yuv[lu]=yuv[lu-(lu%u_sample)]+((yuv[lu+u_sample-(lu%u_sample)]-yuv[lu-(lu%u_sample)])/u_sample)*(ind%u_sample);

				}
				if(ind%v_sample!=0){
//					yuv[lu*2]=(yuv[(lu*2)-1]+yuv[(lu*2)+1])/2;
//					yuv[ind+(width*height*2)]=(yuv[(ind+(width*height*2))-(ind+(width*height*2))%5]+yuv[(ind+(width*height*2))+v_sample-((ind+(width*height*2))%v_sample)])/2;
//					yuv[ind+(width*height*2)]=(yuv[(ind+(width*height*2))-1]+yuv[(ind+(width*height*2))+v_sample-((ind+(width*height*2))%v_sample)])/2;
//					yuv[ind+(width*height*2)]=yuv[width*height*2]+(yuv[ind+(width*height*2)]-yuv[width*height*2])/ind;
				
//					liner interpolation
					yuv[lv]=yuv[lv-(lv%v_sample)]+((yuv[lv+v_sample-(lv%v_sample)]-yuv[lv-(lv%v_sample)])/v_sample)*(ind%v_sample);
				}
				
               }

				
				Integer r1 = (yuv[ind]*(999)+yuv[lu]*(0000)+yuv[lv]*(1140))/1000;
				Integer g1 = (yuv[ind]*(1000)+yuv[lu]*(-395)+yuv[lv]*(-581))/1000;
				Integer b1 = (yuv[ind]*(1000)+yuv[lu]*(2032)+yuv[lv]*(-0000))/1000;
				
				
				byte rb= (byte)Integer.parseInt(Integer.toString(Math.abs(r1)));
				byte gb= (byte)Integer.parseInt(Integer.toString(Math.abs(g1)));
				byte bb= (byte)Integer.parseInt(Integer.toString(Math.abs(b1)));
				
				
				//int pix = ((a << 24) + (rb << 16) + (gb << 8) + bb);
				
				

				//applying quantization
	
				if((rb & 0xFF)%q!=0)
				{
					double mid=Math.round(((rb+(rb%q))+(rb-(rb%q)))/2);
					if(rb-(rb%q)<mid)
					{
					rb=(byte) (rb-(rb%q));
					}
					else
					{
						rb=(byte)(rb+(rb%q));
					}
				}
				if((gb & 0xFF)%q!=0)
				{
					double mid=Math.round(((gb+(gb%q))+(gb-(gb%q)))/2);
					if(gb-(gb%q)<mid)
					{
					gb=(byte) (gb-(gb%q));
					}
					else
					{
						gb=(byte)(gb+(gb%q));
					}
				}
				if((bb & 0xFF)%q!=0)
				{
					double mid=Math.round(((bb+(bb%q))+(bb-(bb%q)))/2);
					if(bb-(bb%q)<mid)
					{
					bb=(byte) (bb-(bb%q));
					}
					else
					{
						bb=(byte)(bb+(bb%q));
					}
				}
				int pix2 = 0xff000000 | (rb & 0xFF) << 16 | (gb & 0xFF) << 8 | bb & 0xFF;
				img2.setRGB(x,y,pix2);
				ind++;
		}}
		
		
		//to display original Image
		JFrame frame = new JFrame();
		   
	    JPanel subPanel1 = new JPanel();
	    subPanel1.setPreferredSize (new Dimension(width, height));
	    subPanel1.setBackground (Color.gray);
	   
	   JLabel label1= new JLabel("",new ImageIcon(img1),SwingConstants.CENTER);
	   subPanel1.add (label1);
	   frame.setSize(width*4, height*4);
	    frame.setVisible(true);
	  // frame.add(subPanel1);
    
    
    // Use a label to display the image
		  
   JLabel label2 = new JLabel("",new ImageIcon(img2),SwingConstants.LEFT);
   subPanel1.add (label2);
    frame.add(subPanel1);
   
   }
   
   
  
}