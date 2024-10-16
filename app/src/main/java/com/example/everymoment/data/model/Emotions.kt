package com.example.everymoment.data.model

enum class Emotions(private val unicode: String) {
    HAPPY(String(Character.toChars(0x1F60A))),
    SAD(String(Character.toChars(0x1F622))),
    INSENSITIVE(String(Character.toChars(0x1F610))),
    ANGRY(String(Character.toChars(	0x1F620))),
    CONFOUNDED(String(Character.toChars(0x1F616)));

    fun getEmotionUnicode(): String {
        return unicode
    }

    companion object {
        fun fromString(emotionString: String?): Emotions? {
            return entries.find { it.name.equals(emotionString, ignoreCase = true) }
        }
    }

}