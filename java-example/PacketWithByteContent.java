//author: James O'Connell

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Class for packet content that represents file information
 *
 */
public class PacketWithByteContent extends PacketContent {

    byte[] bytesFile;

    /**
     * Constructor that takes in information about a file.
     *
//     * @param filename Initial filename.
//     * @param size     Size of filename.
     * @param bytesFile byte array containing bytes of packet sent by worker
     */
    PacketWithByteContent( byte[] bytesFile) {
        type = FILE_BYTE_CONTENT;
        this.bytesFile = bytesFile;
    }

    /**
     * Constructs an object out of a datagram packet.
     *
     * @param oin Packet that contains information about a file.
     */
    protected PacketWithByteContent(ObjectInputStream oin) {
        try {
            type = FILE_BYTE_CONTENT;
//            filename = oin.readUTF();
//            size = oin.readInt();

            //bytesFile = new byte[size];

            for(int i = 0; i < bytesFile.length; i++)
            {
                bytesFile[i] = oin.readByte();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Writes the content into an ObjectOutputStream
     */
    protected void toObjectOutputStream(ObjectOutputStream oout) {
        try {
//            oout.writeUTF(filename);
//            oout.writeInt(size);

            for (int i = 0; i < bytesFile.length; i++)
            {
                oout.writeByte(bytesFile[i]);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        return null;
    }


    /**
     * Returns the content of the packet as String.
     *
     * @return Returns the content of the packet as String.
     */
//    public String toString() {
//        return "Filename: " + filename + " - Size: " + size;
//    }
//
//    /**
//     * Returns the file name contained in the packet.
//     *
//     * @return Returns the file name contained in the packet.
//     */
//    public String getFileName() {
//        return filename;
//    }
//
//    /**
//     * Returns the file size contained in the packet.
//     *
//     * @return Returns the file size contained in the packet.
//     */
//    public int getFileSize() {
//        return size;
//    }

    public byte[] getBytesFile() {
        return bytesFile;
    }
}
