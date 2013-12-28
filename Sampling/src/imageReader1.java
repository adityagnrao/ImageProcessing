
import java.awt.*;
import java.awt.image.*;
import java.io.*;
import javax.swing.*;


public class imageReader1 {

  
   public static void main(String[] args) {
   	

	String fileName = args[0];
   	int width = Integer.parseInt(args[1]);
	int height = Integer.parseInt(args[2]);
	int y_sample=Integer.parseInt(args[3]);
	int u_sample=Integer.parseInt(args[4]);
	int v_sample=Integer.parseInt(args[5]);
    BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
    BufferedImage img_yuv = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
    BufferedImage img1 = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
    try {
	    File file = new File(args[0]);
	    InputStream is = new FileInputStream(file);

	    long len = file.length();
	    byte[] bytes = new byte[(int)len];
	    
	    Integer[] yuv=new Integer[(int)len];
	    
	    int offset = 0;
        int numRead = 0;
        while (offset < bytes.length && (numRead=is.read(bytes, offset, bytes.length-offset)) >= 0) {
            offset += numRead;
        }
    
    		
    	int ind = 0;
		for(int y = 0; y < height; y++){
	
			for(int x = 0; x < width; x++){
		 
				byte a = 0;
				byte r = bytes[ind];
				byte g = bytes[ind+height*width];
				byte b = bytes[ind+height*width*2]; 
				
				if(x%y_sample==0){
				Integer Y =( (r & 0xff) *(299)+(g  & 0xff)*(587)+(b  & 0xff)*(114))/1000;
				yuv[ind]=Y;
				}else {yuv[ind]=0;}
				
				
				if(x%u_sample==0){
					Integer U =( (r  & 0xff)*(-147)+(g  & 0xff)*(-289)+(b  & 0xff)*(436))/1000;
					yuv[ind+height*width]=U;

				}else {yuv[ind+height*width]=0;}
				
				
				if(x%v_sample==0){
				Integer V =( (r  & 0xff)*(615)+(g  & 0xff)*(-515)+(b  & 0xff)*(-100))/1000;
				yuv[ind+height*width*2]=V;
				}else {yuv[ind+height*width*2]=0;}
				
				int pix = 0xff000000 | ((r & 0xff) << 16) | ((g & 0xff) << 8) | (b & 0xff);
				img.setRGB(x,y,pix);
				ind++;
			}
		}
		
	
			ind=0;	
		for(int y = 0; y < height; y++){
			
			for(int x = 0; x < width; x++){
				
              if(ind<(height*width-1))
               {
				if(x%y_sample!=0){
				yuv[ind]=(yuv[ind-1]+yuv[ind+1])/2;
				}
				if(x%u_sample!=0){
					yuv[ind+width*height]=(yuv[ind+width*height-1]+yuv[ind+width*height+1])/2;

				}
				if(x%v_sample!=0){
					yuv[ind+width*height*2]=(yuv[(ind+width*height*2)-1]+yuv[(ind+width*height*2)+1])/2;
				}
				
               }
//				byte yb= (byte)Integer.parseInt((yuv[ind]).toString());
//				byte ub= (byte)Integer.parseInt((yuv[ind+width*height]).toString());
//				byte vb= (byte)Integer.parseInt((yuv[ind+width*height*2]).toString());
				
				Integer r1 = (yuv[ind]*(999)+yuv[ind+width*height]*(0000)+yuv[ind+width*height*2]*(1140))/1000;
				Integer g1 = (yuv[ind]*(1000)+yuv[ind+width*height]*(-395)+yuv[ind+width*height*2]*(-581))/1000;
				Integer b1 = (yuv[ind]*(1000)+yuv[ind+width*height]*(2032)+yuv[ind+width*height*2]*(-0000))/1000;
				
				
				byte rb= (byte)Integer.parseInt(Integer.toString(Math.abs(r1)));
				byte gb= (byte)Integer.parseInt(Integer.toString(Math.abs(g1)));
				byte bb= (byte)Integer.parseInt(Integer.toString(Math.abs(b1)));
				
				
				//int pix = ((a << 24) + (r << 16) + (g << 8) + b);
				
				
//				int pix1 = 0xff000000 | ((yb) & 0xff) << 16 | ((ub) & 0xff) << 8 | (vb) & 0xff ;
				int pix2 = 0xff000000 | (rb & 0xff) << 16 | (gb & 0xff) << 8 | bb & 0xff;
					//int pix = ((a << 24) + (r << 16) + (g << 8) + b);
				
				    
//					img_yuv.setRGB(x,y,pix1);
					img1.setRGB(x,y,pix2);
				ind++;
			}
		}
		
		
		
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
    
    // Use a label to display the image
    JFrame frame = new JFrame();
   // JFrame frame2 = new JFrame();
    JPanel subPanel1 = new JPanel();
    subPanel1.setPreferredSize (new Dimension(width, height));
    subPanel1.setBackground (Color.cyan);
   JLabel label = new JLabel("img1",new ImageIcon(img),SwingConstants.CENTER);
//   JLabel label2 = new JLabel("img2",new ImageIcon(img_yuv),SwingConstants.LEFT);
   JLabel label3 = new JLabel("img2",new ImageIcon(img1),SwingConstants.LEFT);
   subPanel1.add (label);
//   subPanel1.add (label2);
   subPanel1.add (label3);
   // frame.getContentPane().add(label, BorderLayout.CENTER);
   // frame2.getContentPane().add(label2, BorderLayout.CENTER);
   // frame2.getContentPane().add(label, BorderLayout.EAST);
   //frame.pack();
   frame.setSize(width*4, height*4);
   
    frame.setVisible(true);
    frame.add(subPanel1);
    //frame2.pack();
    //frame2.setVisible(true);
   }
   
   
  
}