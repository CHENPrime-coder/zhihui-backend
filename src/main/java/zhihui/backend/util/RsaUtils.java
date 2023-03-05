package zhihui.backend.util;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.util.Base64Utils;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;

/**
 * RSA加解密工具
 * @author CHENPrime-Coder
 */
@Component
@ConfigurationProperties(prefix = "rsa")
public class RsaUtils {

    private String privateKeyFilePath;
    private String publicKeyFilePath;

    /**
     * 获取公钥对象
     * @return 公钥对象
     * @throws IOException 获取公钥文件失败
     * @throws NoSuchAlgorithmException 算法获取失败
     * @throws InvalidKeySpecException 无效密钥
     */
    public PublicKey getRsaPublicKey() throws InvalidKeySpecException, NoSuchAlgorithmException, IOException {
        File file = new File(publicKeyFilePath);
        String key = new String(Files.readAllBytes(file.toPath()));

        String publicKeyPem = key
                .replace("-----BEGIN PUBLIC KEY-----", "")
                .replaceAll(System.lineSeparator(), "")
                .replace("-----END PUBLIC KEY-----", "");

        byte[] decoded = Base64Utils.decode(publicKeyPem.getBytes());

        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(decoded);
        return keyFactory.generatePublic(keySpec);
    }

    /**
     * 获取私钥对象
     * @return 私钥对象
     * @throws IOException 获取私钥文件失败
     * @throws NoSuchAlgorithmException 算法获取失败
     * @throws InvalidKeySpecException 无效密钥
     */
    public PrivateKey getRsaPrivateKey() throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        File file = new File(privateKeyFilePath);
        String key = new String(Files.readAllBytes(file.toPath()), Charset.defaultCharset());

        String privateKeyPem = key
                .replace("-----BEGIN PRIVATE KEY-----", "")
                .replaceAll(System.lineSeparator(), "")
                .replace("-----END PRIVATE KEY-----", "");

        byte[] decoded = Base64Utils.decode(privateKeyPem.getBytes());

        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(decoded);

        return keyFactory.generatePrivate(keySpec);
    }

    /**
     * 加密RSA
     * @param content 需要加密的字符串
     * @return 加密结果
     */
    public String encrypt(String content) {
        try{
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.ENCRYPT_MODE, getRsaPublicKey());
            byte[] output = cipher.doFinal(content.getBytes());
            byte[] encode = Base64Utils.encode(output);
            return new String(encode, "UTF-8");
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 解密RSA
     * @param content 需要解密的字符串
     * @return 解密结果
     */
    public String decrypt(String content) throws NoSuchPaddingException, NoSuchAlgorithmException, IOException, InvalidKeySpecException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException {
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.DECRYPT_MODE, getRsaPrivateKey());

        return getMaxResultDecrypt(content, cipher);
    }

    //长度过长分割解密
    private String getMaxResultDecrypt(String str, Cipher cipher) throws IllegalBlockSizeException, BadPaddingException, IOException {
        byte[] inputArray = Base64Utils.decode(str.getBytes("UTF-8"));
        int inputLength = inputArray.length;
        // 最大解密字节数，超出最大字节数需要分组加密
        int maxEncryptBlock = 256;
        // 标识
        int offSet = 0;
        byte[] resultBytes = {};
        byte[] cache = {};
        while (inputLength - offSet > 0) {
            if (inputLength - offSet > maxEncryptBlock) {
                cache = cipher.doFinal(inputArray, offSet, maxEncryptBlock);
                offSet += maxEncryptBlock;
            } else {
                cache = cipher.doFinal(inputArray, offSet, inputLength - offSet);
                offSet = inputLength;
            }
            resultBytes = Arrays.copyOf(resultBytes, resultBytes.length + cache.length);
            System.arraycopy(cache, 0, resultBytes, resultBytes.length - cache.length, cache.length);
        }
        return new String(resultBytes, "UTF-8");
    }

    public void setPrivateKeyFilePath(String privateKeyFilePath) {
        this.privateKeyFilePath = privateKeyFilePath;
    }

    public void setPublicKeyFilePath(String publicKeyFilePath) {
        this.publicKeyFilePath = publicKeyFilePath;
    }
}
