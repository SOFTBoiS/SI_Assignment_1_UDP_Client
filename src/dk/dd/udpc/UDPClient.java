package dk.dd.udpc;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Arrays;
import java.util.Scanner;

/**
 * @author Dora Di
 */
public class UDPClient {
    private static final int serverPort = 7777;

    // buffers for the messages
    public static String message;
    private static byte[] dataIn = new byte[1024];
    private static byte[] dataOut;
    // Path to the image you wish to send to the server
    private static String filePath;

    // In UDP messages are encapsulated in packages and sent over sockets
    private static DatagramPacket requestPacket;
    private static DatagramPacket responsePacket;
    private static DatagramSocket clientSocket;

    public static void main(String[] args) throws IOException {
        // Enter server's IP address as a parameter from Run/Edit Configuration/Application/Program Arguments
        clientSocket = new DatagramSocket();
        InetAddress serverIP = InetAddress.getByName(args[0]);
        filePath = args[1];
        System.out.println(serverIP);
        sendRequest(serverIP);
        receiveResponse();
        clientSocket.close();
    }

    public static void sendRequest(InetAddress serverIP) throws IOException {
        // Read image
        BufferedImage bImage = ImageIO.read(new File(filePath));
        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        // Convert image to byteArray
        ImageIO.write(bImage, "jpg", bos);
        dataOut = bos.toByteArray();

        // Send packet
        requestPacket = new DatagramPacket(dataOut, dataOut.length, serverIP, serverPort);
        System.out.println(dataOut.length);
        clientSocket.send(requestPacket);
    }

    public static void receiveResponse() throws IOException {
        responsePacket = new DatagramPacket(dataIn, dataIn.length);
        clientSocket.receive(responsePacket);
        String message = new String(responsePacket.getData(), 0, responsePacket.getLength());
        System.out.println("Response from Server: " + message);
    }
}
