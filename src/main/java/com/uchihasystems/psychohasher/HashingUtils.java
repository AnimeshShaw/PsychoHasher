package com.uchihasystems.psychohasher;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Iterator;

/**
 * Core class containing Hashing Utility functions.
 *
 * @author <b>Psycho_Coder </b> (<em>Animesh Shaw</em>)
 */
public class HashingUtils {

    /**
     * <p>
     * Converts array of bytes to hex string.</p>
     *
     * @param bytes Byte Array to be converted to Hex String.
     * @return Returns the hex string for {@code bytes} array.
     */
    public static String byteArrayToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).
                    substring(1));
        }
        return sb.toString();
    }

    /**
     * <p>
     * Hash of string data following a particular algorithm.</p>
     *
     * @param data String data whose hash will be computed.
     * @param hashAlgo Algorithm to be used to hash {@code data}
     * @return Returns the hash of string data as Hex String.
     */
    public static String getHash(String data, String hashAlgo) {
        byte[] mdBytes = null;
        try {
            MessageDigest md = MessageDigest.getInstance(hashAlgo);
            md.update(data.getBytes());
            mdBytes = md.digest();
        } catch (NoSuchAlgorithmException ex) {
            System.err.println("Couldn't determine the hashing algorithm." + ex.
                    getMessage());
        }
        return byteArrayToHex(mdBytes);
    }

    /**
     * <p>
     * Hash of string data following a particular algorithm.</p>
     *
     * @param data String data whose hash will be computed.
     * @param hashAlgo Hashing algorithm to be used. It is of type
     * {@link com.uchihasystems.psychohasher.HashType}
     * @see com.uchihasystems.psychohasher.HashType
     * @return Hex encoded hash of the string data.
     */
    public static String getHash(String data, HashType hashAlgo) {
        return getHash(data, hashAlgo.getValue());
    }

    /**
     * <p>
     * Returns the hash of the file whose path and the type of Hashing Algo
     * scheme is passed as arguments to the method.</p>
     *
     * @param path path of the file whose hash is to be calculated. {@code path}
     * is of type {@link java.nio.file.Path}
     * @param hashAlgo Hashing algorithm to be used.
     * @return Hex encoded hash of the file
     */
    public static String getFileHash(Path path, String hashAlgo) {
        byte[] mdbytes = null;

        try (FileInputStream fis = new FileInputStream(path.toString())) {
            MessageDigest md = MessageDigest.getInstance(hashAlgo);
            byte[] dataBytes = new byte[1024];
            int nread;
            while ((nread = fis.read(dataBytes)) != -1) {
                md.update(dataBytes, 0, nread);
            }
            mdbytes = md.digest();
        } catch (NoSuchAlgorithmException | FileNotFoundException ex) {
            System.err.println("Couldn't determine the hashing algorithm or "
                    + "path to the file not found" + ex.getMessage());
        } catch (IOException ex) {
            System.err.println("Internal IO Exception" + ex.getMessage());
        }

        return byteArrayToHex(mdbytes);
    }

    /**
     * <p>
     * Returns the hash of the file whose path and the type of Hashing Algo
     * scheme is passed as arguments to the method.</p>
     *
     * @param path path of the file whose hash is to be calculated. {@code path}
     * is of type {@link java.nio.file.Path}
     * @param hashAlgo Hashing algorithm to be used. It is of type
     * {@link com.uchihasystems.psychohasher.HashType}
     * @see com.uchihasystems.psychohasher.HashType
     * @return Hex encoded hash of the file
     */
    public static String getFileHash(Path path, HashType hashAlgo) {
        return getFileHash(path, hashAlgo.getValue());
    }

    /**
     * <p>
     * Returns the hash of the file whose path and the type of Hashing Algo
     * scheme is passed as arguments to the method.</p>
     *
     * @param file location of the file whose hash is to be calculated.
     * {@code file} is of type {@link java.io.File}
     * @param hashAlgo Hashing algorithm to be used.
     * @return Hex encoded hash of the file
     */
    public static String getFileHash(File file, String hashAlgo) {
        return getFileHash(file.toPath(), hashAlgo);
    }

    /**
     * <p>
     * Returns the hash of the file whose path and the type of Hashing Algo
     * scheme is passed as arguments to the method.</p>
     *
     * @param file location of the file whose hash is to be calculated.
     * {@code file} is of type {@link java.io.File}
     * @param hashAlgo Hashing algorithm to be used. It is of type
     * {@link com.uchihasystems.psychohasher.HashType}
     * @see com.uchihasystems.psychohasher.HashType
     * @return Hex encoded hash of the file
     */
    public static String getFileHash(File file, HashType hashAlgo) {
        return getFileHash(file.toPath(), hashAlgo.getValue());
    }

    /**
     * <p>
     * Returns the hash of a group of files collectively</p>
     *
     * @param filesGrp Files group array to be hashed.
     * @param hashAlgo Hashing algorithm to be used.
     * @return Hex encoded hash of the file
     */
    public static String getGroupFilesHash(File[] filesGrp, String hashAlgo) {
        byte[] dataBytes = new byte[1024];
        byte[] mdbytes = null;
        try {
            MessageDigest md = MessageDigest.getInstance(hashAlgo);

            for (File file : filesGrp) {
                try (FileInputStream fis = new FileInputStream(file)) {
                    int nread;
                    while ((nread = fis.read(dataBytes)) != -1) {
                        md.update(dataBytes, 0, nread);
                    }
                } catch (FileNotFoundException ex) {
                    System.err.println(ex.getMessage());
                } catch (IOException ex) {
                    System.err.println(ex.getMessage());
                }
                mdbytes = md.digest();
            }
        } catch (NoSuchAlgorithmException ex) {
            System.err.println(ex.getMessage());
        }

        return byteArrayToHex(mdbytes);
    }

    /**
     * <p>
     * returns the hash of a group of files collectively</p>
     *
     * @param filesGrp Iterator representing group of files to be hashed.
     * @param hashAlgo Hashing algorithm to be used.
     * @return Hex encoded hash of the file
     */
    public static String getGroupFilesHash(Iterator<File> filesGrp, String hashAlgo) {
        byte[] dataBytes = new byte[1024];
        byte[] mdbytes = null;
        try {
            MessageDigest md = MessageDigest.getInstance(hashAlgo);

            while (filesGrp.hasNext()) {
                try (FileInputStream fis = new FileInputStream(filesGrp.next())) {
                    int nread;
                    while ((nread = fis.read(dataBytes)) != -1) {
                        md.update(dataBytes, 0, nread);
                    }
                } catch (FileNotFoundException ex) {
                    System.err.println(ex.getMessage());
                } catch (IOException ex) {
                    System.err.println(ex.getMessage());
                }
                mdbytes = md.digest();
            }
        } catch (NoSuchAlgorithmException ex) {
            System.err.println(ex.getMessage());
        }

        return byteArrayToHex(mdbytes);
    }

    /**
     * <p>
     * returns the hash of a group of files collectively</p>
     *
     * @param filesGrp Iterator representing group of files to be hashed.
     * @param hashAlgo Hashing algorithm to be used. It is of type
     * {@link com.uchihasystems.psychohasher.HashType}
     * @see com.uchihasystems.psychohasher.HashType
     * @return Hex encoded hash of the file
     */
    public static String getGroupFilesHash(Iterator<File> filesGrp, HashType hashAlgo) {
        return getGroupFilesHash(filesGrp, hashAlgo.getValue());
    }

    /**
     * <p>
     * Returns the hash of a group of files collectively</p>
     *
     * @param filesGrp Files group array to be hashed.
     * @param hashAlgo Hashing algorithm to be used. It is of type
     * {@link com.uchihasystems.psychohasher.HashType}
     * @see com.uchihasystems.psychohasher.HashType
     * @return Hex encoded hash of the file
     */
    public static String getGroupFilesHash(File[] filesGrp, HashType hashAlgo) {
        return getGroupFilesHash(filesGrp, hashAlgo.getValue());
    }
}
