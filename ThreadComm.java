package code;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

class ThreadComm extends Thread {
    private Socket socket;
    private IPPool pool;
    private String serverIP;

    public ThreadComm(Socket socket, IPPool pool, String serverIP) {
        this.socket = socket;
        this.pool = pool;
        this.serverIP = serverIP;
    }

    @Override
    public void run() {
        try (ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
             ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream())) {

            DHCPMessage msg = (DHCPMessage) in.readObject();

            if (msg.type == DHCPEnum.DISCOVER) {
                DHCPLogger.log("[SERVEUR] Reçu DISCOVER de " + msg.clientId);
                String ip = pool.donneIP(msg.clientId);
                if (ip != null) {
                    DHCPMessage offer = new DHCPMessage(DHCPEnum.OFFER);
                    offer.ip = ip;
                    offer.serverIP = serverIP;
                    out.writeObject(offer);
                    DHCPLogger.log("[SERVEUR] Envoyé OFFER avec IP: " + ip);

                    msg = (DHCPMessage) in.readObject();
                    if (msg.type == DHCPEnum.REQUEST) {
                        DHCPLogger.log("[SERVEUR] Reçu REQUEST pour IP: " + msg.ip);
                        DHCPMessage ack = new DHCPMessage(DHCPEnum.ACK);
                        ack.ip = msg.ip;
                        ack.leaseTime = 60;
                        out.writeObject(ack);
                        DHCPLogger.log("[SERVEUR] Envoyé ACK pour IP: " + ack.ip);
                    }
                } else {
                    out.writeObject(null);
                    DHCPLogger.log("[SERVEUR] Aucun IP disponible pour DISCOVER de " + msg.clientId);
                }
            }

        } catch (IOException | ClassNotFoundException e) {
            System.err.println("[SERVEUR-THREAD] Erreur: " + e.getMessage());
            DHCPLogger.log("[SERVEUR-THREAD] Erreur: " + e.getMessage());
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                System.err.println("[SERVEUR-THREAD] Erreur lors de la fermeture du socket : " + e.getMessage());
            }
        }
    }
}