package com.example.kotlintest.features_home.presentation.data.model

enum class Status(val code: Int) {
    PENDING(1),
    CONFIRMED(2),
    STARTED(3),
    COMPLETED(4),
    CANCELED(-3),
    EXPIRED(-2),
    DELETED(-1);

    companion object {
        fun fromCode(code: Int): Status =
            values().firstOrNull { it.code == code }
                ?: throw IllegalArgumentException("Unknown Status code: $code")
    }
}