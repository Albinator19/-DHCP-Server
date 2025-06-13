package code;


import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Serveur {
	 public static void main(String[] args) throws Exception {
	        ServerSocket serverSocket = new ServerSocket(4555);
	        IPPool pool = new IPPool("192.168.1.10", "192.168.1.20");
	        String serverIP = InetAddress.getLocalHost().getHostAddress();
	        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
	        scheduler.scheduleAtFixedRate(pool::verifierBauxExpirés, 0, 10, TimeUnit.SECONDS);


	       
	        System.out.println("[SERVEUR] DHCP Server démarré sur " + serverIP);
	        
	     // Thread pour la console admin
	        new Thread(() -> {
	            Scanner scanner = new Scanner(System.in);
	            while (true) {
	                System.out.print("[SERVEUR-CONSOLE] Commande (list | leases | exit): ");
	                String cmd = scanner.nextLine();

	                if (cmd.equalsIgnoreCase("list")) {
	                    System.out.println("Adresses IP disponibles: " + pool.getDispos());
	                } else if (cmd.equalsIgnoreCase("leases")) {
	                    System.out.println("Adresses IP allouées:");
	                    for (Map.Entry<String, Long> entry : pool.getBaux().entrySet()) {
	                        long remaining = (entry.getValue() - System.currentTimeMillis()) / 1000;
	                        System.out.println("  IP: " + entry.getKey() + " - Temps restant: " + remaining + "s");
	                    }
	                } else if (cmd.equalsIgnoreCase("exit")) {
	                    DHCPLogger.log("[SERVEUR-CONSOLE] Arrêt manuel du serveur.");
	                    System.out.println("[SERVEUR-CONSOLE] Arrêt du serveur.");
	                    System.exit(0);
	                } else {
	                    System.out.println("Commande inconnue.");
	                }
	            }
	        }).start();

	        while (true) {
	            Socket socket = serverSocket.accept();
	            new ThreadComm(socket, pool, serverIP).start();
	        }
	    }
}
