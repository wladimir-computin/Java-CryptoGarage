package CryptoGarage.libcryptogarage.protocol;

import CryptoGarage.Config;

import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Arrays;

/**
 * Created by spamd on 08.03.2017.
 */

public class Crypter {
    private SecretKeySpec secretKeySpec;
    private Cipher aes;
    private SecureRandom random;

    private static Crypter instance;
    private String devicePass;

    public static Crypter init(String devicePass){
        if(instance == null){
            instance = new Crypter(devicePass);
        } else if (!instance.devicePass.equals(devicePass)){
            instance = new Crypter(devicePass);
        }
        return instance;
    }

    private Crypter(String devicePass){
        byte[] key = (devicePass + Config.key_salt).getBytes(StandardCharsets.UTF_8);
        this.random = new SecureRandom();
        try {
            MessageDigest sha = MessageDigest.getInstance("SHA-512");
            int count = Config.sha_rounds + 1;
            for(int i = 0; i < count; i++){
                key = sha.digest(key);
            }
            this.secretKeySpec = new SecretKeySpec(Arrays.copyOf(key, 32), "AES");
            this.aes = Cipher.getInstance("AES/GCM/NoPadding");
            this.devicePass = devicePass;
        } catch (Exception x){
            x.printStackTrace();
        }
    }

    public byte[] encrypt(byte[] message, byte[] iv){
        if (iv.length == 12 && message != null) {
            try {
                final GCMParameterSpec spec = new GCMParameterSpec(16 * 8, iv);
                aes.init(Cipher.ENCRYPT_MODE, secretKeySpec, spec);
                return aes.doFinal(message);
            } catch (Exception x) {
                x.printStackTrace();
            }
        }
        return null;
    }

    public byte[] decrypt(byte[] message, byte[] iv, byte[] tag){
        if (iv.length == 12 && tag.length == 16 &&  message != null) {
            try {
                byte[] messageWithTag = new byte[message.length + tag.length];
                System.arraycopy(message, 0, messageWithTag, 0, message.length);
                System.arraycopy(tag, 0, messageWithTag, message.length, tag.length);
                final GCMParameterSpec spec = new GCMParameterSpec(16 * 8, iv);
                aes.init(Cipher.DECRYPT_MODE, secretKeySpec, spec);
                return aes.doFinal(messageWithTag);
            } catch (Exception x) {
                x.printStackTrace();
            }
        }
        return null;
    }

    public byte[] getRandomIV(){
        byte[] iv = new byte[12];
        random.nextBytes(iv);
        return iv;
    }

    public byte[] getRandomChallenge(){
        byte[] challenge = new byte[12];
        random.nextBytes(challenge);
        return challenge;
    }
}
