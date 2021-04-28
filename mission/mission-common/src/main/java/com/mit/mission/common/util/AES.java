package com.mit.mission.common.util;

import com.mit.mission.common.handler.YamlPropertySourceFactory;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.util.Calendar;
import java.util.Date;

@Data
@Component
@PropertySource(value = {"classpath:custom.yml"}, factory = YamlPropertySourceFactory.class)
@ConfigurationProperties(prefix = "common-aes")
public class AES {
    private String key;
    private String initVector;

    private static String encryptKey = "mission!saltkey*";

    private static String encryptInitVector = "0000000000000000";

    @PostConstruct
    public void init() {
        encryptKey = key;
        encryptInitVector = initVector;
    }

    /**
     *
     * 十六进制转换字符串
     */

    public static byte[] hexStr2Bytes(String hexStr) {
        //System.out.println("in len :" + hexStr.length());
        String str = "0123456789ABCDEF";
        char[] hexs = hexStr.toCharArray();
        byte[] bytes = new byte[hexStr.length() / 2];
        int n;
        for (int i = 0; i < bytes.length; i++) {
            n = str.indexOf(hexs[2 * i]) * 16;
            n += str.indexOf(hexs[2 * i + 1]);
            bytes[i] = (byte) (n & 0xff);
        }
        //System.out.println("out len :" + bytes.length);
        //System.out.println("ddd" + Arrays.toString(bytes));
        return bytes;
    }

    /**
     * bytes转换成十六进制字符串
     */
    public static String byte2HexStr(byte[] b) {
        String hs = "";
        String stmp = "";
        for (int n = 0; n < b.length; n++) {
            stmp = (Integer.toHexString(b[n] & 0XFF));
            if (stmp.length() == 1)
                hs = hs + "0" + stmp;
            else
                hs = hs + stmp;
        }
//		System.out.println(hs.toUpperCase());
        return hs.toUpperCase();
    }

    public static String encrypt(String value) {
        try {
            IvParameterSpec iv = new IvParameterSpec(encryptInitVector.getBytes("UTF-8"));
            SecretKeySpec skeySpec = new SecretKeySpec(encryptKey.getBytes("UTF-8"), "AES");

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);

            byte[] encrypted = cipher.doFinal(value.getBytes("UTF-8"));
//			System.out.println(Arrays.toString(encrypted));

            return byte2HexStr(encrypted);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return null;
    }

    public static String encrypt(String value, Date date) {
        try {
            IvParameterSpec iv = new IvParameterSpec(encryptInitVector.getBytes("UTF-8"));
            SecretKeySpec skeySpec = new SecretKeySpec(getKey(date).getBytes("UTF-8"), "AES");

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);

            byte[] encrypted = cipher.doFinal(value.getBytes("UTF-8"));
//			System.out.println(Arrays.toString(encrypted));

            return byte2HexStr(encrypted);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return null;
    }

    public static String decrypt(String encrypted) {
        try {
            IvParameterSpec iv = new IvParameterSpec(encryptInitVector.getBytes("UTF-8"));
            SecretKeySpec skeySpec = new SecretKeySpec(encryptKey.getBytes("UTF-8"), "AES");

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);

            byte[] original = cipher.doFinal(hexStr2Bytes(encrypted));

            return new String(original);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return null;
    }

    public static String decrypt(String encrypted,Date date) {
        try {
            IvParameterSpec iv = new IvParameterSpec(encryptInitVector.getBytes("UTF-8"));
            SecretKeySpec skeySpec = new SecretKeySpec(getKey(date).getBytes("UTF-8"), "AES");

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);

            byte[] original = cipher.doFinal(hexStr2Bytes(encrypted));

            return new String(original);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return null;
    }

    public static class StaticConstant {
        // 定义数组有60个值
        static String[] keyList = { "f8f41936d9753165", "ef342fdcf6b2d852", "0bb4809485c1bde2", "abe444e204b2b101",
                "afc49d674426588a", "14f43e8077b03ee6", "2b7495fc7b034610", "0034feb907c46714", "7f046f8c5e5827f4",
                "ca544e61f60b3ea6", "3fc431682d56a87c", "2c245716b9035052", "e6c4bb3bfec0a64b", "fe04c1442b5e9de6",
                "0fb488081e1639a2", "31d4b3efd8407aa5", "95b412cdce494ea0", "9f64ff7b4cfe024a", "b8443ff7da3ad6ec",
                "423484fbde24f0eb", "a3d4bf7df41280f3", "b3543836c4e81e5d", "12948597ad1051c3", "9364054f8fc4b226",
                "aa74a810fb778a8a", "96b4a3bf1f48a2f3", "5cd4bf839c6989d3", "9e44e49b69e4cacd", "bc64280b4751d5fa",
                "043453552827d92d", "e264ec2ff04130c4", "5cf42973b2bc6556", "7f744b309b9c95f9", "b9840ab5cef25130",
                "bfc491f71add7777", "86c4d5a33432571f", "5a7415719a27441e", "49441f8492cb3665", "62942fa0481eeead",
                "c734f034ffde1284", "766405e0669244e1", "2c747390194e0919", "f22446ae6bf21eb5", "9244b029f8d98780",
                "d67453e599389c40", "ac24081a3e6a94a7", "a9e4ee0aa67bc961", "b9f45f8c26f18c16", "e934b6305a5b8139",
                "aec451baaa5c784c", "840430cfcd68d2f8", "6b0457261104c56b", "7f04ff4b125eeb8a", "f2f41dcba4ee3397",
                "51d4b5fa05ba4687", "6d1485e937f9d09f", "c724e1f4927dc9db", "d8b4c0bb8bf1304b", "0454b55c9c94413f",
                "13542a3d5025a403", };
    }

    public static String getKey(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int minute = calendar.get(Calendar.MINUTE);
        System.out.println(minute);
        return StaticConstant.keyList[minute];
    }
}
