package com.playground.real_project_api.utils.random;

import java.security.SecureRandom;
import java.util.Base64;

/**********************************************************************************************
 * @FileName : RandomStringGenerator.java
 * @Date : 2023-06-12
 * @작성자 : ilovepc
 * @설명 : 랜덤 스트링 생성기
 **********************************************************************************************/
public class RandomStringGenerator {

    /**********************************************************************************************
     * @Method 설명 : 랜덤 문자열 (숫자)
     * @작성일 : 2023-06-12
     * @작성자 : 정승주
     * @변경이력 :
     **********************************************************************************************/
    public static String getRandomStringDigit(final int RANDOM_STRING_LENGTH){
        SecureRandom random = new SecureRandom();
        StringBuilder stringBuilder = new StringBuilder();

        for (int i = 0; i < RANDOM_STRING_LENGTH; i++) {
            int randomDigit = random.nextInt(10);  // 0부터 9까지의 난수 생성
            stringBuilder.append(randomDigit);
        }

        return stringBuilder.toString();
    }
    
    
    /********************************************************************************************** 
     * @Method 설명 :  
     * @작성일 : 2023-06-12 
     * @작성자 : 정승주
     * @변경이력 : 
     **********************************************************************************************/
    public static String getSaltedRandomString(final int RANDOM_STRING_LENGTH) {
        byte[] salt = generateSalt(16);
        String randomString = generateRandomString(RANDOM_STRING_LENGTH);

        String saltedRandomString = Base64.getEncoder().encodeToString(salt) + randomString;
        return saltedRandomString.substring(0, RANDOM_STRING_LENGTH);
    }

    /********************************************************************************************** 
     * @Method 설명 : 16byte salt값 생성
     * @작성일 : 2023-06-12 
     * @작성자 : 정승주
     * @변경이력 : 
     **********************************************************************************************/
    private static byte[] generateSalt(int byteLength) {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[byteLength];
        random.nextBytes(salt);
        return salt;
    }

    /**********************************************************************************************
     * @Method 설명 : length길이의 랜덤 문자열을 생성
     * @작성일 : 2023-06-12
     * @작성자 : 정승주
     * @변경이력 :
     **********************************************************************************************/
    private static String generateRandomString(int length) {
        byte[] bytes = generateSalt(length);
        return Base64.getEncoder().encodeToString(bytes);
    }

}
