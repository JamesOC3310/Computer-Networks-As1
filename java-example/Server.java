//author: James O'Connell

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;

public class Server extends Node {
	static final int DEFAULT_SRC_PORT = 50001;
	static final int DEFAULT_DST_PORT = 51191;
	static final int CLIENT_PORT = 50000;
	static final int WORKER2_PORT = 51291;
	static final int WORKER3_PORT = 51391;
	static final String DEFAULT_DST_NODE = "172.60.0.3"; // address of receiver (worker class)
	static final String DEFAULT_DST_NODE_CLIENT = "172.60.0.3"; // address of receiver for bytes (client class)
	InetSocketAddress dstAddress;
	InetSocketAddress dstWorker1Address;
	InetSocketAddress dstClientAddress;
	InetSocketAddress dstWorker2Address;
	InetSocketAddress dstWorker3Address;
	/*static final String DEFAULT_DST_NODE_W1 = "172.60.0.3";
	static final String DEFAULT_DST_NODE_W2 = "172.60.0.4";
	static final String DEFAULT_DST_NODE_W3 = "172.60.0.5";
	/*
	 *
	 */
	Server(String dstClientHost, int dstClientPort, int srcIngressPort) {
		try {
			dstAddress= new InetSocketAddress(dstClientHost, dstClientPort); // address of client, ip of client
			dstWorker1Address = new InetSocketAddress("workern1",51191 );
			dstClientAddress = new InetSocketAddress("client", 50000);
			dstWorker2Address = new InetSocketAddress("workern2", 51291);
			dstWorker3Address = new InetSocketAddress("172.60.0.5", 51391);
			socket= new DatagramSocket(srcIngressPort);
			listener.go();
		}
		catch(java.lang.Exception e) {e.printStackTrace();}
	}

	/**
	 * Assume that incoming packets contain a String and print the string.
	 */
	public void onReceipt(DatagramPacket packet) {
		try {
			System.out.println("Received packet");





			PacketContent content= PacketContent.fromDatagramPacket(packet);

			System.out.println(content.getType());

			if (content.getType()==PacketContent.FILEINFO) {
				System.out.println("File name: " + ((FileInfoContent)content).getFileName());
				System.out.println("File size: " + ((FileInfoContent)content).getFileSize());

				DatagramPacket response;
				response= new AckPacketContent("OK - Received this").toDatagramPacket();
				response.setSocketAddress(packet.getSocketAddress());
				socket.send(response);

				System.out.println("Dest address:");
				System.out.println(dstWorker1Address);
				packet.setSocketAddress(dstWorker1Address);
				socket.send(packet);
				System.out.println("Packet sent to worker 1.");

				System.out.println("Dest address:");
				System.out.println(dstWorker2Address);
				packet.setSocketAddress(dstWorker2Address);
				socket.send(packet);
				System.out.println("Packet sent to worker 2.");

				System.out.println("Dest address:");
				System.out.println(dstWorker3Address);
				packet.setSocketAddress(dstWorker3Address);
				socket.send(packet);
				System.out.println("Packet sent to worker 3.");
			}

			 else if (content.getType()==PacketContent.FILE_BYTE_CONTENT) {
				//System.out.println("File name: " + ((PacketWithByteContent)content).getFileName());
				//System.out.println("File size: " + ((PacketWithByteContent)content).getFileSize());

				DatagramPacket byteResponse;


				byteResponse= new AckPacketContent("OK - Received this file by bytes").toDatagramPacket();
				byteResponse.setSocketAddress(packet.getSocketAddress());
				socket.send(byteResponse);

				 PacketContent contentBytes = PacketContent.fromDatagramPacket(packet);
				 PacketWithByteContent fileContent = (PacketWithByteContent) contentBytes;
				 String s = new String(fileContent.bytesFile, StandardCharsets.UTF_8);




				System.out.println("Dest address:");
				System.out.println(dstClientAddress);
				packet.setSocketAddress(dstClientAddress);
				socket.send(packet);
				System.out.println("File bytes sent to client from workers.");

			}




		}
		catch(Exception e) {e.printStackTrace();}
	}


	public synchronized void start() throws Exception {
		System.out.println("Waiting for contact");

		//System.out.println("Sending packet w/ name & length"); // Send packet with file name and length

		this.wait();
	}

	/*
	 *
	 */
	public static void main(String[] args) {
		try {
			(new Server(DEFAULT_DST_NODE, DEFAULT_DST_PORT, DEFAULT_SRC_PORT)).start();
			System.out.println("Program completed");
		} catch(java.lang.Exception e) {e.printStackTrace();}
	}
}
