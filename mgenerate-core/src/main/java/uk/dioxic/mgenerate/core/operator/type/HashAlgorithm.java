package uk.dioxic.mgenerate.core.operator.type;

import uk.dioxic.mgenerate.core.util.ByteUtil;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.function.Function;

public enum HashAlgorithm {
    MD5(o -> digest(o, "MD5")),
    SHA1(o -> digest(o, "SHA-1")),
    SHA256(o -> digest(o, "SHA-256")),
    HASHCODE(Object::hashCode);

    private Function<Object, Integer> hashFn;

    HashAlgorithm(Function<Object, Integer> hash) {
        this.hashFn = hash;
    }

    public Integer hash(Object value) {
        return hashFn.apply(value);
    }

    private static int digest(Object o, String algorithm) {
        try {
            byte[] b = ByteUtil.getBytes(o);
            byte[] digest = MessageDigest.getInstance(algorithm).digest(b);
            return ByteUtil.bytesToInt(digest);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}