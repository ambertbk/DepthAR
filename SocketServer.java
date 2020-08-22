package AR_app_server;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.ClassNotFoundException;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Base64;

import javax.imageio.ImageIO;

import java.util.*;

public class SocketServer {
	//static ServerSocket variable
	static Socket socket;
    static ServerSocket server;
    
    static InputStreamReader isReader;
    static BufferedReader bufReader;
    
    static String message;
    public static String fileName;
    
    public static void main(String args[]) throws IOException, ClassNotFoundException{
    	while(true) {
	    	try {
				server = new ServerSocket(2530);
				System.out.println("Waiting for connection..." +"\n");
				socket = server.accept();
				System.out.println("Accepted connection: " + socket + "\n");
			    isReader = new InputStreamReader(socket.getInputStream());
			    bufReader = new BufferedReader(isReader);
			        
			    message = bufReader.readLine();
			    System.out.println("Message received! Analyzing...");
			    String header = message.substring(0, 4);
			    if (header.equals("0000")) {
			    	String text = message.substring(4, message.length());
			    	if (!text.equals(null)) {
			    		System.out.println("Text message received: " + text + "\n");
			    	}
			    } else if (header.equals("0001")) {
			    	System.out.println("Image message received! Decoding...");
			    	String image = message.substring(4, message.length());
			    	byte[] imageByteArray = decodeData(image);
				    
				    ByteArrayInputStream bis = new ByteArrayInputStream(imageByteArray);
				    BufferedImage bImage = ImageIO.read(bis);
			 
				    fileName = new SimpleDateFormat("yyyyMMddHHmmss'.jpg'").format(new Date());
				    ImageIO.write(bImage, "jpg", new File(fileName));
				    
				    System.out.println("Decode completed! Image is saved to the project folder: PC Server/" + fileName + "\n");
			    	
			    }
			 
			    server.close();
			    socket.close();
	        
	    	} catch (IOException e) {
	    		e.printStackTrace();
	    	}      
    	}
    }
    
    public static byte[] decodeData(String imageDataString) {
    	return Base64.getDecoder().decode(imageDataString);
    }
    
//    public 
}
