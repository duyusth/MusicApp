package com.example.musicapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class SongAdapter : ListAdapter<Track, SongAdapter.SongViewHolder>(SongDiffCallback()) {

    class SongViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val songTitle: TextView = view.findViewById(R.id.tvSongTitle)
        private val artistName: TextView = view.findViewById(R.id.tvArtistName)
        private val albumName: TextView = view.findViewById(R.id.tvAlbumName)
        private val albumCover: ImageView = view.findViewById(R.id.ivAlbumCover)

        fun bind(track: Track) {
            songTitle.text = track.title
            artistName.text = track.artist.name
            albumName.text = track.album.title

            Glide.with(itemView.context)
                .load(track.album.cover_medium)
                .placeholder(R.drawable.ic_launcher_foreground) // Add a placeholder image
                .error(R.drawable.ic_launcher_foreground) // Add an error fallback image
                .into(albumCover)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_song, parent, false)
        return SongViewHolder(view)
    }

    override fun onBindViewHolder(holder: SongViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

class SongDiffCallback : DiffUtil.ItemCallback<Track>() {
    override fun areItemsTheSame(oldItem: Track, newItem: Track): Boolean = oldItem.id == newItem.id
    override fun areContentsTheSame(oldItem: Track, newItem: Track): Boolean = oldItem == newItem
}
