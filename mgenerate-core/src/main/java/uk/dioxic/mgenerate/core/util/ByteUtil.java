package uk.dioxic.mgenerate.core.util;

import org.bson.Document;
import org.bson.types.ObjectId;
import uk.dioxic.mgenerate.core.codec.MgenDocumentCodec;

public class ByteUtil {

    static final int[] intBitMask = new int[32];
    static final long[] longBitMask = new long[64];

    static {
        StringBuilder sb = new StringBuilder();
        for (int i=1; i< 32; i++) {
            sb.append(1);
            intBitMask[i] = Integer.parseInt(sb.toString(), 2);
        }

        sb = new StringBuilder();
        for (int i=1; i< 64; i++) {
            sb.append(1);
            longBitMask[i] = Long.parseLong(sb.toString(), 2);
        }
    }

    public static byte[] longToBytes(long l) {
        byte[] result = new byte[8];
        for (int i = 7; i >= 0; i--) {
            result[i] = (byte)(l & 0xFF);
            l >>= 8;
        }
        return result;
    }

    public static long bytesToLong(byte[] b) {
        long result = 0;
        for (int i = 0; i < 8; i++) {
            result <<= 8;
            result |= (b[i] & 0xFF);
        }
        return result;
    }

    public static byte[] intToBytes(int l) {
        byte[] result = new byte[4];
        for (int i = 3; i >= 0; i--) {
            result[i] = (byte)(l & 0xFF);
            l >>= 8;
        }
        return result;
    }

    public static int bytesToInt(byte[] b) {
        int result = 0;
        for (int i = 0; i < 4; i++) {
            result <<= 8;
            result |= (b[i] & 0xFF);
        }
        return result;
    }

    public static byte[] getBytes(Object o) {
        if (o instanceof Long) {
            return longToBytes((Long) o);
        }
        else if (o instanceof Integer) {
            return intToBytes((Integer) o);
        }
        else if (o instanceof ObjectId) {
            return ((ObjectId) o).toByteArray();
        }
        else if (o instanceof Document) {
            return ((Document) o).toJson(new MgenDocumentCodec()).getBytes();
        }
        else {
            return o.toString().getBytes();
        }
    }

    public static int bitShiftLeft(int i, int bits) {
        if (bits > 32 || bits < 1) {
            throw new IllegalArgumentException("cannot bit shift an integer by more than 32 bits or less than 1 bit");
        }
        return(i & intBitMask[bits]) << bits;
    }

    public static long bitShiftLeft(long i, int bits) {
        if (bits > 64 || bits < 1) {
            throw new IllegalArgumentException("cannot bit shift a long by more than 64 bits or less than 1 bit");
        }
        return(i & longBitMask[bits]) << bits;
    }

}
