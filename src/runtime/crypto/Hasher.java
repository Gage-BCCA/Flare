package runtime.crypto;

import java.io.IOException;
import java.security.MessageDigest;
import java.io.File;
import java.security.NoSuchAlgorithmException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Hasher {

    private final File file;

    public Hasher(File file) {
        this.file = file;
    }

    public String getSha256AsString() throws NoSuchAlgorithmException, IOException {
        MessageDigest sha256 = MessageDigest.getInstance("SHA-256");
        byte[] array = Files.readAllBytes(Path.of(file.getAbsolutePath()));
        return bytesToHex(array);

    }

    // Shamelessly stolen from Baeldung
    private static String bytesToHex(byte[] hash) {
        StringBuilder hexString = new StringBuilder(2 * hash.length);
        for (int i = 0; i < hash.length; i++) {
            String hex = Integer.toHexString(0xff & hash[i]);
            if(hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }
}
