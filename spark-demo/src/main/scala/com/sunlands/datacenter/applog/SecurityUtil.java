package com.sunlands.datacenter.applog;
import java.util.List;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;

import com.shangde.common.exception.MD5;

public class SecurityUtil {

    private static final String ivParameter = "0102030405060708";

    public static String encrypt(String content,String password) {
        try {

//			KeyGenerator kgen = KeyGenerator.getInstance("AES");
//			SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
//
//			secureRandom.setSeed(password.getBytes());
//			kgen.init(128, secureRandom);
//			SecretKey secretKey = kgen.generateKey();
//			byte[] enCodeFormat = secretKey.getEncoded();
//			SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");
//			Cipher cipher = Cipher.getInstance("AES");// 创建密码器
//			byte[] byteContent = content.getBytes("utf-8");
//			cipher.init(Cipher.ENCRYPT_MODE, key);// 初始化
//			byte[] result = cipher.doFinal(byteContent);
//			return Base64.encodeBase64String(result); // 加密

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            byte[] raw = password.getBytes("utf-8");
            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
            IvParameterSpec iv = new IvParameterSpec(ivParameter.getBytes());// 使用CBC模式，需要一个向量iv，可增加加密算法的强度
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
            byte[] encrypted = cipher.doFinal(content.getBytes("utf-8"));
            return Base64.encodeBase64URLSafeString(encrypted); // 加密

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String decrypt(String content,String password) {
        try {

//			KeyGenerator kgen = KeyGenerator.getInstance("AES");
//			SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
//			secureRandom.setSeed(password.getBytes("utf-8"));
//			kgen.init(128, secureRandom);
//			SecretKey secretKey = kgen.generateKey();
//			byte[] enCodeFormat = secretKey.getEncoded();
//			SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");
//			Cipher cipher = Cipher.getInstance("AES");// 创建密码器
//			cipher.init(Cipher.DECRYPT_MODE, key);// 初始化
//			byte[] result = cipher.doFinal(Base64.decodeBase64(content));
//			return new String(result); // 解密

            byte[] raw = password.getBytes("utf-8");
            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            IvParameterSpec iv = new IvParameterSpec(ivParameter.getBytes());
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
            byte[] encrypted1 = Base64.decodeBase64(content);
            byte[] original = cipher.doFinal(encrypted1);
            String originalString = new String(original, "utf-8");
            return originalString;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     *
     * 功能描述：根据版本号返回解密串，低于2.5.0的版本不解密，目的是兼容老版本
     *
     * @param data
     * @param version
     * @param token
     * @return
     *
     * @author 周赟
     *
     * @since 2016年12月20日
     *
     * @update:[变更日期YYYY-MM-DD][更改人姓名][变更描述]
     */
    @SuppressWarnings("deprecation")
    public static String getDecryptDataForParam(String data,String version,String token){

        if(StringUtils.isNotBlank(data) && StringUtils.isNotBlank(version) && StringUtils.isNotBlank(token)){
            try{

                if(version.endsWith("-debug")){
                    version = version.substring(0,version.length()-6);
                }
                version = version.replace(".","");
                if(StringUtils.isNumeric(version)){
                    int intVersion = Integer.valueOf(version);
                    if(intVersion > 250){

                        String dataEncrypt = MD5.getMD5(data);
                        if(dataEncrypt.equals(token)){

                            String apiKey = ConfigUtil.readProperties("applicationResourse", "API_KEY");

                            return decrypt(data,apiKey);
                        }
                    }
                }
            }catch(Exception e){
                e.printStackTrace();
            }

            return null;
        }
        return data;
    }

    /**
     *
     * 功能描述：根据版本号返回加密串，低于2.5.0的版本不加密，目的是兼容老版本
     *
     * @param data
     * @param version
     * @return
     *
     * @author 周赟
     *
     * @since 2016年12月20日
     *
     * @update:[变更日期YYYY-MM-DD][更改人姓名][变更描述]
     */
    public static Object getEncryptDataForParam(Object data,String version){
        if(data != null && StringUtils.isNotBlank(version)){
            if(version.endsWith("-debug")){
                version = version.substring(0,version.length()-6);
            }
            version = version.replace(".","");
            if(StringUtils.isNumeric(version)){
                int intVersion = Integer.valueOf(version);
                if(intVersion > 250){

                    String jsonData = null;
                    String apiKey = ConfigUtil.readProperties("applicationResourse", "API_KEY");

                    if(data instanceof List){
                        JSONArray jsonArray = JSONArray.fromObject(data);
                        jsonData = jsonArray.toString();
                    }else if(data instanceof String || data instanceof Integer){
                        jsonData = data.toString();
                    }else{
                        JSONObject jsonObj = JSONObject.fromObject(data);
                        jsonData = jsonObj.toString();
                    }

                    return encrypt(jsonData,apiKey);
                }
            }

        }
        return data;
    }

    public static void main(String [] args){
//		String data = "{\n  \"loginAccount\" : \"15210156211\",\n  \"loginPsw\" : \"111111\"\n}";
        //System.out.println(encrypt("liangjianan","sunlandsgogo2017"));
        System.out.println(decrypt("OPrnAqKTU6dz4ZZwIcgj1A","sunlandsgogo2017"));
        String  teString = "45e3LGHCCE_lH_J0GjKt9geTvfuZPNrLFYjZXIZeEXQwA7P4J6fCYdwaMWbFZ8zbxVx753QToszDw-K9Q3jRUWNMJoVLGOG7C3nRS0fm89OBohGUVlVv5oeeQvboM3Tr-sqLYlVzU-_oVlUYbFHABSD0KoK5qXcBxDMnFsRPRTWwfRhXXilzQhAsUi3VdCHMcEimoQJRf158nLzyP3Yy1zR07LU908YLwZf8TTNIf4GrP4NsXyx8L2KwnNv3-Cp7CunKR3j_FFr9dkpBVDmXY5_FvXobYvx7ax_oC_KWbVb8otSgDP0NN7CVKgpPcgdX9xnd5WuvENnmaFhnC38INc5USnXGkHAle1D_VCkNCDEgfMrmjY8KrpdNdsbp2GOgMrw3XfFWFzHZw178jrOfkPZLtneLIXaMGSfaeJV6IUc";
        String  ateString = "euQabxAD3dWGqgtFqaQuKZTzglE-7FgF0h_5wZke2YWHcTSnXzWu0grygPRpmoZuJADBxGCojTVXrQumOsssqS0jEpUCmcrD5kJo2ww5ojVH-kipGczT2RUgYkzJZQ05SJ4i9HO6z_0P7OQ7_94obV2wSbyzO-z5upli2LsLUR1FhE-XT0kDfldnpznadZRc5pFBYKTJhfCkDveyfWVq55f6XxmI6bppyfXD7H6QWwPm4uSKTYJvum4ZcgIRyfsrOKEPQ9k831hwiiFKpV-ucX_mjwHut4P5TUKOMkCPByJ3YZxyQIE3OSyzEoP4M60cLvlc3ida1YL9vS99tHGZFcAkquXB0ajR53dnzVH9o91FbdDdI5ychHY70cpJg6tS4anpfY4RBg-_q94rfqLcfg";
        //t_action_info_addition_yyyy-mm-dd
        System.out.println(decrypt(ateString,"XeXWDrMBAN[[XNDL"));
    }

}