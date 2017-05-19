package es.dmoral.clothyscanner.udp;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 * This file is part of ClothyScanner.
 *
 * ClothyScanner is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * ClothyScanner is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with ClothyScanner.  If not, see <http://www.gnu.org/licenses/>.
 */

public class UdpClient {
    private static final int HOST = 5042;

    private UdpCallback udpCallback;
    private String ip;

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

    public interface UdpCallback {
        void onSuccess();

        void onError();
    }
}
