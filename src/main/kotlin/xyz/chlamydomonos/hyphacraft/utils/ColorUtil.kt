package xyz.chlamydomonos.hyphacraft.utils

object ColorUtil {
    fun rgba(color: Long): Int {
        val colorRGB = (color shr 8) and 0xffffff
        val colorA = color and 0xff
        val colorARGB = (colorA shl 24) or colorRGB
        return if (colorARGB > 0x7fffffff) (colorARGB - 0x100000000).toInt() else colorARGB.toInt()
    }
}