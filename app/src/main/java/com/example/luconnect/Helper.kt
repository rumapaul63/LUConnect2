package com.example.luconnect

import android.util.Base64
import android.widget.Toast
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

object Helper {
    val SECRET_KEY = "secretKey1234567"
    val SECRET_IV = "secretIVsecretIV"

    fun encryptCBC(value: String): String {
        var encMsg: String = "";
        val encodedByte: ByteArray
        try {
            val iv = IvParameterSpec(SECRET_IV.toByteArray())
            val keySpec = SecretKeySpec(SECRET_KEY.toByteArray(), "AES")
            val cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING")
            cipher.init(Cipher.ENCRYPT_MODE, keySpec, iv)
            val crypted = cipher.doFinal(value.toByteArray())
            encodedByte = Base64.encode(crypted, Base64.DEFAULT)
            encMsg = String(encodedByte)
        }
        catch (ex: Exception)
        {
            throw ex;
        }
        return encMsg;
    }


    fun decryptCBC(value: String): String {
        val decodedByte: ByteArray = Base64.decode(value, Base64.DEFAULT)
        val iv = IvParameterSpec(SECRET_IV.toByteArray())
        val keySpec = SecretKeySpec(SECRET_KEY.toByteArray(), "AES")
        val cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING")
        cipher.init(Cipher.DECRYPT_MODE, keySpec, iv)
        val output = cipher.doFinal(decodedByte)
        return String(output)
    }

//    val strEncrypt = edt.text.toString().encryptCBC()
//    val strDecrypted = strEncrypt?.decryptCBC()
}