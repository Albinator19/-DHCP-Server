package code;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.UUID;

public class Client {
	 public static void main(String[] args) {
		 try (Socket socket = new Socket("localhost", 4555)) {
	            System.out.println("[CLIENT] Connecté au serveur !");

	            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
	            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

	            // DISCOVER
	            DHCPMessage discover = new DHCPMessage(DHCPEnum.DISCOVER);
	            discover.clientId = UUID.randomUUID().toString();
	            out.writeObject(discover);

	            // OFFER
	            DHCPMessage offer = (DHCPMessage) in.readObject();
	            System.out.println("[CLIENT] Offre reçue: " + offer.ip);

	            // REQUEST
	            DHCPMessage request = new DHCPMessage(DHCPEnum.REQUEST);
	            request.clientId = discover.clientId;
	            request.ip = offer.ip;
	            request.serverIP = offer.serverIP;
	            out.writeObject(request);

	            // ACK
	            DHCPMessage ack = (DHCPMessage) in.readObject();
	            System.out.println("[CLIENT] IP attribuée: " + ack.ip);

	        } catch (IOException | ClassNotFoundException e) {
	            System.err.println("Erreur du client : " + e.getMessage());
	        }
	    }

}
