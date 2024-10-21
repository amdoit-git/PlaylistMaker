package com.example.playlistmaker.ui.mediaLibrary.playlists

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.domain.models.Playlist
import kotlin.math.abs

class PlaylistAdapter(
    private val playlists: MutableList<Playlist>,
    private val onPlaylistClick: (Playlist) -> Unit,
    private val scrollListToTop: (Int) -> Unit,
    private val trackCounterDeclination: String,
    private val type: PlaylistRvType
) : RecyclerView.Adapter<PlaylistViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistViewHolder {
        return when (type) {
            PlaylistRvType.GRID -> PlaylistViewHolderGrid.create(parent)
            PlaylistRvType.LIST -> PlaylistViewHolderList.create(parent)
        }
    }

    override fun getItemCount(): Int {
        return playlists.size
    }

    override fun onBindViewHolder(holder: PlaylistViewHolder, position: Int) {
        holder.bind(playlists[position], onPlaylistClick, trackCounterDeclination)
    }

    fun setNewPlaylists(playlists: List<Playlist>) {

        if (ifItemMoved(
                old = this.playlists,
                new = playlists
            )
        ) return

        if (ifItemDeleted(
                old = this.playlists,
                new = playlists
            )
        ) return

        if (ifItemAdded(
                old = this.playlists,
                new = playlists
            )
        ) return


        this.playlists.clear()

        playlists.forEach {
            this.playlists.add(it)
        }

        this.notifyDataSetChanged()
    }

    private fun ifItemAdded(old: MutableList<Playlist>, new: List<Playlist>): Boolean {

        //добавление плейлиста

        if (new.size == old.size + 1 && new.containsAll(old)) {

            val t: Playlist = new.minus(old)[0]

            val toIndex = new.indexOf(t)

            val copy = old.toMutableList()

            copy.add(toIndex, t)

            if (copy == new) {

                old.add(toIndex, t)

                this.notifyItemInserted(toIndex)

                scrollListToTop(toIndex)

                return true
            }
        }

        return false
    }

    private fun ifItemDeleted(old: MutableList<Playlist>, new: List<Playlist>): Boolean {

        //удаление плейлиста

        if (old.size == new.size + 1 && old.containsAll(new)) {

            val t: Playlist = old.minus(new)[0]

            val fromIndex = old.indexOf(t)

            val copy = old.toMutableList()

            copy.removeAt(fromIndex)

            if (copy == new) {

                old.removeAt(fromIndex)

                this.notifyItemRemoved(fromIndex)

                return true
            }
        }

        return false
    }

    private fun ifItemMoved(old: MutableList<Playlist>, new: List<Playlist>): Boolean {

        //Перемещение плейлиста

        getMovedPlaylist(old, new)?.let { t ->

            val copy = old.toMutableList()

            val toIndex = new.indexOf(t)

            val fromIndex = old.indexOf(t)

            copy.add(toIndex, copy.removeAt(fromIndex))

            if (copy == new) {

                old.add(toIndex, old.removeAt(fromIndex))

                if (toIndex < fromIndex) {
                    scrollListToTop(toIndex)
                }

                this.notifyItemMoved(fromIndex, toIndex)

                return true
            }
        }

        return false
    }


    private fun getMovedPlaylist(old: List<Playlist>, new: List<Playlist>): Playlist? {

        if (old.size != new.size || !old.containsAll(new)) {
            return null
        }

        var maxMove = 0
        var maxMoveIndex = -1

        for (i in old.indices) {

            for (j in new.indices) {

                if (new[j] == old[i]) {
                    if (abs(i - j) > maxMove) {
                        maxMove = abs(i - j)
                        maxMoveIndex = i
                    }
                    break
                }
            }
        }

        return if (maxMove > 0) {
            old[maxMoveIndex]
        } else {
            null
        }
    }

    fun clearPlaylists() {
        this.playlists.clear()
        this.notifyDataSetChanged()
    }
}