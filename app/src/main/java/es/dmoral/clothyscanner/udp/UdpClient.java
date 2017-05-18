package es.dmoral.clothyscanner.udp;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

/**
 * Created by grender on 19/05/17.
 */

public class UdpClient {
    private static final int HOST = 5042;

    private UdpCallback udpCallback;
    private String ip;

    public interface UdpCallback {
        void onSuccess();
        void onError();
    }

    public UdpClient(UdpCallback udpCallback, String ip) {
        this.udpCallback = udpCallback;
        this.ip = ip;
    }

    public void sendMessage(final String message) {
        new Thread(new Runnable() {
            DatagramSocket datagramSocket = null;
            @Override
            public void run() {
                try {
                    // Sending
                    datagramSocket = new DatagramSocket();
                    DatagramPacket datagramPacket = new DatagramPacket(message.getBytes(), message.length(), InetAddress.getByName(ip), HOST);
                    datagramSocket.setBroadcast(true);
                    datagramSocket.send(datagramPacket);

                    // Receiving
                    byte[] buffer = new byte[1024];
                    datagramPacket = new DatagramPacket(buffer, buffer.length);
                    datagramSocket.receive(datagramPacket);

                    if (new String(buffer, 0, datagramPacket.getLength()).equals("1"))
                        udpCallback.onSuccess();
                    else
                        udpCallback.onError();
                } catch (Exception e) {
                    e.printStackTrace();
                    udpCallback.onError();
                } finally {
                    if (datagramSocket != null) {
                        datagramSocket.close();
                    }
                }
            }
        }).start();
    }
}
