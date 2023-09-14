//author: James O'Connell

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

public class Worker2 extends Node {
    static final int DEFAULT_SRC_PORT = 51291; // worker2 port
    static final int DEFAULT_DST_PORT = 50001; // server port
    static final String DEFAULT_DST_NODE = "172.60.0.2";
    InetSocketAddress dstAddress;
    InetSocketAddress dstServerAddress;
    /*
     *
     */
    Worker2(String dstServerHost, int dstServerPort, int srcWorker2Port) {
        try {
            dstAddress= new InetSocketAddress(dstServerHost, dstServerPort); // address of client, ip of client
            dstServerAddress = new InetSocketAddress("ingress",50001 );
            socket= new DatagramSocket(srcWorker2Port);
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
//            System.out.println("File being read by bytes....");

            PacketContent content= PacketContent.fromDatagramPacket(packet);

            if (content.getType()==PacketContent.FILEINFO) {
                System.out.println("File name: " + ((FileInfoContent)content).getFileName());
                System.out.println("File size: " + ((FileInfoContent)content).getFileSize());
                System.out.println(content.getType());

                DatagramPacket response;
                response= new AckPacketContent("OK - Received this").toDatagramPacket();


                response.setSocketAddress(packet.getSocketAddress());
                socket.send(response);
                packet.setSocketAddress(dstServerAddress);
                System.out.println("Dest address:");
                System.out.println(dstAddress);
                System.out.println("File with its contents by byte chunks being sent to ingress...");
                readFileByBytes(((FileInfoContent)content).getFileName());
            }
        }
        catch(Exception e) {e.printStackTrace();}
    }

    public Boolean findFile (String fileName)
    {
        Path newPath = Paths.get("message.txt");

        if (Files.exists(newPath) && !Files.isDirectory(newPath))
        {
            return true;
        }
        return false;
    }

    public void readFileByBytes (String filename) throws IOException {
        if (findFile(filename))
        {
            File fileToRead = new File(filename);
            FileInputStream fileInputStream = null;
            InputStream readBytesInputStream = null;
            byte[] bytesFile;
            bytesFile = Files.readAllBytes(Paths.get(filename));
            byte[][] readBytes = new byte[200][200];
            //int bytesChunkLimiter = 20;
            //byte[] bytesFile = new byte[(int) fileToRead.length()];

            int sizeByteFile = 0;

            for (int i = 0; i < bytesFile.length; i++)
            {
                System.out.print((char) bytesFile[i]);
                sizeByteFile++;
            }

            try
            {
                for (int i = 0; i < fileToRead.length()-1;i++)
                {

                    readBytes[i] = Arrays.copyOf(bytesFile, 20);

                    PacketWithByteContent packetWithBytes;


                    DatagramPacket packetOfBytes= null;



                    packetWithBytes = new PacketWithByteContent( readBytes[i]);
                    String s = new String(packetWithBytes.bytesFile, StandardCharsets.UTF_8);
                    System.out.println(s);
                    packetOfBytes= packetWithBytes.toDatagramPacket();
                    packetOfBytes.setSocketAddress(dstServerAddress);
                    System.out.println("bytes being sent to ingress...");
                    System.out.println("File size: " + sizeByteFile);
                    socket.send(packetOfBytes);

                }
                //Read bytes with InputStream
//                fileInputStream = new FileInputStream(fileToRead);
//                fileInputStream.read(bytesFile);
//                fileInputStream.close();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }

//            PacketWithByteContent packetWithBytes;
//
//
//            DatagramPacket packetOfBytes= null;
//
//            packetWithBytes = new PacketWithByteContent( bytesFile);
//            packetOfBytes= packetWithBytes.toDatagramPacket();
//            packetOfBytes.setSocketAddress(dstServerAddress);
//            System.out.println("File with its contents by bytes being sent to ingress...");
//            System.out.println("File size: " + sizeByteFile);
//            socket.send(packetOfBytes);
        }

    }


    public synchronized void start() throws Exception {
        System.out.println("Waiting for contact");
        this.wait();
    }

    /*
     *
     */
    public static void main(String[] args) {
        try {
            (new Worker2(DEFAULT_DST_NODE, DEFAULT_DST_PORT, DEFAULT_SRC_PORT)).start();
            System.out.println("Program completed");
        } catch(java.lang.Exception e) {e.printStackTrace();}
    }
}