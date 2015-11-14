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
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Psycho_Coder
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
            sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
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
            Logger.getLogger(HashingUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
        return byteArrayToHex(mdBytes);
    }

    /**
     *
     * @param path
     * @param hashAlgo
     * @return
     */
    private String getFileHash(Path path, String hashAlgo) {
        byte[] mdbytes = null;

        try {
            if (!Files.exists(path, LinkOption.NOFOLLOW_LINKS)) {
                throw new FileNotFoundException("File not found!");
            }

            MessageDigest md = MessageDigest.getInstance(hashAlgo);
            FileInputStream fis = new FileInputStream(path.toString());
            byte[] dataBytes = new byte[1024];

            int nread;
            while ((nread = fis.read(dataBytes)) != -1) {
                md.update(dataBytes, 0, nread);
            }

            mdbytes = md.digest();

        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(HashingUtils.class.getName()).log(Level.SEVERE, null, ex);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(HashingUtils.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(HashingUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
        return byteArrayToHex(mdbytes);

    }

    /**
     *
     * @param file
     * @param hashAlgo
     * @return
     */
    private String getFileHash(File file, String hashAlgo) {
        return getFileHash(file.toPath(), hashAlgo);
    }

    /**
     *
     * @param file
     * @param hashAlgo
     * @return
     */
    private String getFileHash(String file, String hashAlgo) {
        return getFileHash(Paths.get(file), hashAlgo);
    }

    /**
     *
     * @param args
     */
    public static void main(String[] args) {
        HashingUtils hUtils = new HashingUtils();
        String path = "/home/psychocoder/AndroidBugs.pdf";
        System.out.println(hUtils.getFileHash(path, "SHA-384"));
        System.out.println(hUtils.getHash("Hello World!", "SHA-512"));

    }
}
