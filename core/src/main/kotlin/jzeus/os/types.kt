package jzeus.os

import jzeus.any.print
import jzeus.str.asCommandLine
import java.util.UUID

enum class OSVersion(val value: String) {
    WIN11("Windows 11")
}

data class MachineCode(
    val code: String
) {


    companion object {

        fun get():String {
        return    if(OSVersion.WIN11.matched)
                """ powershell -Command "& {(Get-ItemProperty -Path "HKLM:\SOFTWARE\Microsoft\Cryptography" -Name MachineGuid).MachineGuid}" """.asCommandLine()
                    .exec().trim()
            else UUID.randomUUID().toString()
        }
    }
}
