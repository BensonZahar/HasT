package com.ynk.virtualdisplay.tools

import android.system.Os

/**
 * Запускается через app_process под root (например, в эмуляторе Nox, где adbd работает от root):
 * сбрасывает uid/gid до shell (2000) и заменяет процесс переданной командой (exec).
 * Так Shizuku стартует как при обычной adb-отладке (uid 2000), и система принимает
 * пакет com.android.shell при создании виртуальных дисплеев.
 *
 * Пример:
 * CLASSPATH=<путь к base.apk> app_process / com.ynk.virtualdisplay.tools.RunAsShell id
 */
object RunAsShell {
    private const val SHELL_UID = 2000

    @JvmStatic
    fun main(args: Array<String>) {
        if (args.isEmpty()) {
            System.err.println("usage: RunAsShell <command...>")
            return
        }
        try {
            if (Os.getuid() == 0) {
                Os.setgid(SHELL_UID)
                Os.setuid(SHELL_UID)
            }
            Os.execv("/system/bin/sh", arrayOf("sh", "-c", args.joinToString(" ")))
        } catch (e: Throwable) {
            System.err.println("RunAsShell failed: $e")
        }
    }
}
