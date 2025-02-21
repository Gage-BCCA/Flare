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

    public String getSha1Digest() throws NoSuchAlgorithmException, IOException {
        MessageDigest sha1 = MessageDigest.getInstance("SHA-1");
        String content = Files.readString(Path.of(file.getAbsolutePath()));
        String gitHeader = "blob " + file.length() + '\0';
        byte[] fileWithGitHeader = addArrays(gitHeader.getBytes(), content.getBytes());
        byte[] hash = sha1.digest(fileWithGitHeader);
        return byteArrayToHexString(hash);
    }

    public String byteArrayToHexString(byte[] b) {
        String result = "";
        for (int i=0; i < b.length; i++) {
            result += Integer.toString( ( b[i] & 0xff ) + 0x100, 16).substring( 1 );
        }
        return result;
    }

    public static byte[] addArrays(byte[] arr1, byte[] arr2) {
        int length1 = arr1.length;
        int length2 = arr2.length;
        byte[] result = new byte[length1 + length2];

        System.arraycopy(arr1, 0, result, 0, length1);
        System.arraycopy(arr2, 0, result, length1, length2);

        return result;
    }
}
