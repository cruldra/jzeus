package jzeus.crypto


/**
 * 使用指定[密钥][key]对这个字符串进行`AES`解密
 * @param key 密钥
 * @return 解密后的字符串
 */
fun String.decryptAES(key: String): String {
    return AESCipher(key).decrypt(this)
}

/**
 * 使用指定[密钥][key]对这个字符串进行`AES`加密
 *
 * @param key 密钥
 * @return 加密后的字符串
 */
fun String.encryptAES(key: String): String {
    return AESCipher(key).encrypt(this)
}
