package com.example.musicapp


data class Track(
    val id: Long,
    val title: String,
    val artist: Artist,
    val album: Album,
    val preview: String // URL đoạn nhạc xem trước
)





