package com.humber.its2020.ibourit.credential

import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import java.security.MessageDigest

class Credential {
    companion object {
        fun id() : String {
            val user = Firebase.auth.currentUser!!
            return hashString("SHA-1", user.email!!)
        }

        fun name() : String {
            val user = Firebase.auth.currentUser!!
            return user.displayName!!.replace("\\s".toRegex(), "")
        }

        fun hashString(type: String, input: String): String {
            val hexChars = "0123456789ABCDEF"
            val bytes = MessageDigest
                .getInstance(type)
                .digest(input.toByteArray())
            val result = StringBuilder(bytes.size * 2)

            bytes.forEach {
                val i = it.toInt()
                result.append(hexChars[i shr 4 and 0x0f])
                result.append(hexChars[i and 0x0f])
            }

            return result.toString()
        }
    }
}