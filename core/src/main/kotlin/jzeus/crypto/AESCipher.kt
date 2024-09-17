package jzeus.crypto

import java.security.SecureRandom
import java.util.*
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

class AESCipher(keyString: String) {
    private val key: SecretKeySpec

    init {
        val keyBytes = keyString.toByteArray(Charsets.UTF_8)
        val paddedKey = ByteArray(32)
        keyBytes.copyInto(paddedKey, endIndex = minOf(keyBytes.size, 32))
        key = SecretKeySpec(paddedKey, "AES")
    }

    fun decrypt(encryptedData: String): String {
        // Base64解码
        val decodedData = Base64.getDecoder().decode(encryptedData)

        // 提取IV和加密数据
        val iv = decodedData.copyOfRange(0, 16)
        val encrypted = decodedData.copyOfRange(16, decodedData.size)

        // 初始化解密器
        val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
        cipher.init(Cipher.DECRYPT_MODE, key, IvParameterSpec(iv))

        // 解密数据
        val decrypted = cipher.doFinal(encrypted)
        return String(decrypted, Charsets.UTF_8)
    }

    fun encrypt(data: String): String {
        // 生成随机的16字节IV
        val iv = ByteArray(16)
        SecureRandom().nextBytes(iv)
        val ivSpec = IvParameterSpec(iv)

        // 初始化加密器
        val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
        cipher.init(Cipher.ENCRYPT_MODE, key, ivSpec)

        // 加密数据
        val encrypted = cipher.doFinal(data.toByteArray(Charsets.UTF_8))

        // 将IV和加密后的数据组合，并进行Base64编码
        val combined = iv + encrypted
        return Base64.getEncoder().encodeToString(combined)
    }
}

fun main() {
//    print("abc111".encryptAES("GgH7yHMPj3c2oyx"))
    print("N7A0Vc7ST8Ku4emLtpKKxiiV6bYq5EAUAGwCt41YhFA=".decryptAES("GgH7yHMPj3c2oyx"))
}
