package code;

import java.io.Serializable;

class DHCPMessage implements Serializable {
    public DHCPEnum type;
    public String clientId;
    public String ip;
    public String subnetMask = "255.255.255.0";
    public int leaseTime = 60; 
    public String serverIP;

    public DHCPMessage(DHCPEnum type) {
        this.type = type;
    }
}
