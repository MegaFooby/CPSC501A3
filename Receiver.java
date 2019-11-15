import java.util.*;
import java.net.*;
import java.io.*;
import org.jdom2.*;
import org.jdom2.output.*;

public class Receiver {
	public static void main(String args[]) {
		if(args.length != 1) {
			System.out.println("Usage: java Receiver [port number]");
		}
		int port = Integer.parseInt(args[0]);
		//byte data[] = receive(port);
		Object received = receive_object(port);
		Document doc = Document.class.cast(received);
		System.out.println("Document received");
		Object obj = Deserializer.deserialize(doc);
		
		new Inspector().inspect(obj, true);
		
		Document sending = Serializer.serialize(obj);
		try{
			XMLOutputter xmlOutput = new XMLOutputter();
            xmlOutput.setFormat(Format.getPrettyFormat());
            xmlOutput.output(sending, new FileWriter("file2.xml"));
        }catch(Exception e){}
	}
	
	public static Object receive_object(int port) {
		ServerSocket server = null;
		ObjectInputStream in;
		try {
			server = new ServerSocket(port);
			System.out.println ("Server IP address: " + server.getInetAddress().getHostAddress() + ", port " + port);
			Socket sock = server.accept();
			in = new ObjectInputStream(sock.getInputStream());
			Object data = in.readObject();
			sock.close();
			server.close();
			
			return data;
		} catch(Exception e) {
			e.printStackTrace();
			System.exit(0);
		}
		return null;
	}
	
	public static byte[] receive(int port) {
		ServerSocket server = null;
		DataInputStream in;
		try {
			server = new ServerSocket(port);
			System.out.println ("Server IP address: " + server.getInetAddress().getHostAddress() + ", port " + port);
			Socket sock = server.accept();
			//System.out.println("Client accepted");
			in = new DataInputStream(sock.getInputStream());
			int length = in.readInt();
			//System.out.println(length);
			byte data[] = new byte[length];
			int red = 0;
			while(red < length) {
				red += in.read(data, red, length-red);
			}
			//System.out.println("Data received");
			sock.close();
			server.close();
			
			return data;
		} catch(Exception e) {
			e.printStackTrace();
			System.exit(0);
		}
		return null;
	}
}
