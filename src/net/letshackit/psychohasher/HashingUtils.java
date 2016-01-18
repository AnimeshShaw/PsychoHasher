package net.letshackit.psychohasher;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 *
 * @author <b>Psycho_Coder </b> (<em>Animesh Shaw</em>)
 */
public class HashingUtils {

    /**
     *
     * @param bytes
     * @return
     */
    private String byteArrayToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).
                    substring(1));
        }
        return sb.toString();
    }

    /**
     *
     * @param data
     * @param hashAlgo
     * @return
     */
    private String getHash(String data, String hashAlgo) {
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
     * Returns the hash of the file whose path and the type of Hashing Algo
     * scheme is passed as arguments to the method.
     *
     * @param path path of the file whose hash is to be calculated. path is of
     * type java.nio.file.Path
     * @param hashAlgo Hashing algorithm to be used.
     * @return Hex encoded hash of the file
     */
    private String getFileHash(Path path, String hashAlgo) {
        byte[] mdbytes = null;

        try {
            if (Files.notExists(path, LinkOption.NOFOLLOW_LINKS)) {
                throw new FileNotFoundException("File not found!");
            }

            if (Files.isDirectory(path, LinkOption.NOFOLLOW_LINKS)) {
                throw new IllegalArgumentException("Path shouldn't be a directory");
            }

            MessageDigest md = MessageDigest.getInstance(hashAlgo);
            FileInputStream fis = new FileInputStream(path.toString());
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
     * Returns the hash of the file whose path and the type of Hashing Algo
     * scheme is passed as arguments to the method.
     *
     * @param path path of the file whose hash is to be calculated. path is of
     * type java.io.File
     * @param hashAlgo Hashing algorithm to be used.
     * @return Hex encoded hash of the file
     */
    private String getFileHash(File file, String hashAlgo) {
        return getFileHash(file.toPath(), hashAlgo);
    }

    /**
     * Returns the hash of the file whose path and the type of Hashing Algo
     * scheme is passed as arguments to the method.
     *
     * @param path path of the file whose hash is to be calculated.
     * @param hashAlgo Hashing algorithm to be used.
     * @return Hex encoded hash of the file
     */
    private String getFileHash(String file, String hashAlgo) {
        return getFileHash(Paths.get(file), hashAlgo);
    }

    public static void main(String[] args) {
        HashingUtils hUtils = new HashingUtils();
        String path = "/home/psychocoder/AndroidBugs.pdf";
        System.out.println(hUtils.getFileHash(path, Hash.SHA256.getValue()));
        System.out.println(hUtils.getHash("Hello World!", "SHA-512"));
    }
}
