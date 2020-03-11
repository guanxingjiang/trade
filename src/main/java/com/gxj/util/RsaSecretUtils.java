package com.gxj.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.rsa.crypto.RsaRawEncryptor;

import java.io.InputStream;

/**
 * @version 1.0
 * @description:
 * @author: guanxingjiang
 * @time: 2018-08-14 15:55
 */
public class RsaSecretUtils {

    private final static Logger LOGGER     = LoggerFactory.getLogger(RsaSecretUtils.class);

    private static final String PRIVATE_KEY_PATH = "conf/privatekey.txt";

    /** 解密*/
    private static RsaRawEncryptor rsaSecretEncryptor;

    /**
     * 解密
     * @param encrypt 密文
     * @return 明文
     */
    public  static String decrypt(String encrypt){
        if(StringUtils.isBlank(encrypt)){return null;}
        try{
            encrypt = encrypt.replace(" ","+");
            LOGGER.info("encrypt: "+encrypt);
                return  rsaSecretEncryptor.decrypt(encrypt);
        }catch (Exception e){
            LOGGER.error(e.getMessage(),e);
        }
        return null;
    }

   static  {
        InputStream in = null;
        try{
            in = RsaSecretUtils.class.getClassLoader().getResourceAsStream(PRIVATE_KEY_PATH);
            String privateKey = FileUtils.read(in,"UTF-8");
            privateKey = privateKey.replace("\r\n","");
            LOGGER.info("privateKey: "+privateKey);
            rsaSecretEncryptor = new RsaRawEncryptor(privateKey);
        }finally {
            StreamUtils.close(in);
        }
    }

    public static void main(String[] args) {
        System.out.println(decrypt("Qcm2uJoxkfI31R0d1JQbAzPnSqSIMNnFbAu2h5EgfkSiFITTnNZFt8oqLusRhwQcUk0v0JJXowYgRYdamfheE7OohZIm9V2eTOREWolPvtfa8aCqXQxNeIGXoRyXXnBkGCwfEMpjqLlhsORWWS7/7fbBYbUm8oA3QsHoJH/xGEc="));
    }
}
