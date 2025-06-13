package code;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class IPPool {
	 private final Queue<String> IPdispos = new LinkedList<>();
	    private final Map<String, Long> bailMap = new ConcurrentHashMap<>();
	    private final Set<String> utilisee = new HashSet<>();

	    public IPPool(String startIP, String endIP) throws UnknownHostException {
	        long start = ipToLong(InetAddress.getByName(startIP));
	        long end = ipToLong(InetAddress.getByName(endIP));
	        for (long ip = start; ip <= end; ip++) {
	            IPdispos.offer(longToIp(ip));
	        }
	    }

	    public synchronized String donneIP(String clientId) {
	        String ip = IPdispos.poll();
	        if (ip != null) {
	            utilisee.add(ip);
	            bailMap.put(ip, System.currentTimeMillis() + 60000); // 60s lease
	            DHCPLogger.log("[POOL] IP attribuée: " + ip + " au client " + clientId);
	        }
	        return ip;
	    }

	    public synchronized void enleveIP(String ip) {
	        if (utilisee.remove(ip)) {
	            bailMap.remove(ip);
	            IPdispos.offer(ip);
	        }
	    }

	    public void verifierBauxExpirés() {
	    	long now = System.currentTimeMillis();
	        List<String> expirées = new ArrayList<>();
	        for (Map.Entry<String, Long> entry : bailMap.entrySet()) {
	            if (entry.getValue() < now) {
	                expirées.add(entry.getKey());
	            }
	        }
	        for (String ip : expirées) {
	            enleveIP(ip);
	            DHCPLogger.log("[BAUX] Bail expiré pour IP: " + ip);
	        }
	    }

	    private long ipToLong(InetAddress ip) {
	        byte[] octets = ip.getAddress();
	        long result = 0;
	        for (byte octet : octets) {
	            result = (result << 8) | (octet & 0xff);
	        }
	        return result;
	    }

	    private String longToIp(long ip) {
	        return String.format("%d.%d.%d.%d", (ip >> 24) & 0xff, (ip >> 16) & 0xff, (ip >> 8) & 0xff, ip & 0xff);
	    }

	    public Set<String> getUtilisee() {
	        return utilisee;
	    }

	    public Queue<String> getDispos() {
	        return IPdispos;
	    }
	    
	    public Map<String, Long> getBaux() {
	        return bailMap;
	    }

}
