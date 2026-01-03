package com.example.beer.data.enums

enum class Bitterness(val description: String) {
    HARDLY_BITTER("Hardly bitter"),
    LIGHTLY_BITTER("Lightly bitter"),
    NOTICEABLY_BITTER("Noticeably bitter"),
    STRONGLY_BITTER("Strongly bitter");

    override fun toString(): String = description
}