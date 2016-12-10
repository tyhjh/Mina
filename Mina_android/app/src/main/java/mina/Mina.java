package mina;

/**
 * Created by Tyhj on 2016/12/11.
 */

public class Mina {
    private  static String ipAddress;
    private  static int ipPort=0;

    public Mina(String ip,int port) {
        ipAddress=ip;
        ipPort=port;
    }

    public static String getIpAddress() {
        return ipAddress;
    }

    public static void setIpAddress(String ipAddress) {
        Mina.ipAddress = ipAddress;
    }

    public static int getIpPort() {
        return ipPort;
    }

    public static void setIpPort(int ipPort) {
        Mina.ipPort = ipPort;
    }
}
