package com.example.playlistmaker.viewModels.mediaLibrary.playlists

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.models.Playlist
import com.example.playlistmaker.domain.models.ShareData
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.domain.repository.common.GetStringResourceUseCase
import com.example.playlistmaker.domain.repository.common.NoticeInteractor
import com.example.playlistmaker.domain.repository.mediaLibrary.playlists.PlaylistsInteractor
import com.example.playlistmaker.domain.repository.settings.ExternalNavigatorInteractor
import com.example.playlistmaker.viewModels.common.LiveDataWithStartDataSet
import com.example.playlistmaker.viewModels.common.NumDeclension
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class PlaylistScreenViewModel(
    private val playlists: PlaylistsInteractor,
    private val notice: NoticeInteractor,
    private val strings: GetStringResourceUseCase,
    private val navigator: ExternalNavigatorInteractor,
    private val playlistId: Int
) :
    ViewModel(), NumDeclension {

    private val liveData = LiveDataWithStartDataSet<PlaylistScreenData>()

    init {

        viewModelScope.launch(Dispatchers.Main) {

            launch {
                playlists.getPlaylistInfo(playlistId).collect { playlist ->

                    if (playlist != null) {
                        liveData.setValue(PlaylistScreenData.Info(playlist = playlist))
                    } else {

                        liveData.setValue(PlaylistScreenData.GoBack(true))
                    }
                }
            }

            launch {
                playlists.getTracks(playlistId).collect { list ->

                    liveData.setValue(PlaylistScreenData.Tracks(tracks = list))
                }
            }
        }
    }

    fun getLiveData(): LiveData<PlaylistScreenData> {
        return liveData
    }

    fun setMenuBsState(opened: Boolean) {
        liveData.setValue(PlaylistScreenData.MenuBsState(opened))
    }

    fun sharePlaylist() {

        viewModelScope.launch(Dispatchers.Main) {

            val playlist: Playlist = playlists.getPlaylistInfo(playlistId).first()!!

            val tracks = playlists.getTracks(playlistId).first()

            if (tracks.isNotEmpty()) {

                val text = mutableListOf<String>()

                with(playlist) {
                    text.add(title)
                    text.add(description)
                    text.add(declension(tracksTotal, strings(R.string.track_counter_declination)))
                }

                for (i in 0 until tracks.size) {
                    val num = i + 1
                    with(tracks[i]) {
                        text.add("$num. $artistName - $trackName ($trackTime)")
                    }
                }

                navigator.share(
                    ShareData(
                        text = text.filterNot { it.isBlank() }.joinToString(separator = "\n")
                    ),
                    chooseApp = true
                )

            } else {
                notice.setMessage(
                    strings(R.string.playlist_no_tracks_to_share).replace(
                        "[playlist]",
                        playlist.title
                    )
                )
            }
        }
    }

    fun setTracksBsState(opened: Boolean) {
        liveData.setStartValue(PlaylistScreenData.TracksBsState(opened))
    }

    fun deleteTrack(track: Track) {

        viewModelScope.launch {
            playlists.deleteTrack(track.trackId, playlistId)
        }
    }

    fun deletePlaylist() {

        GlobalScope.launch(Dispatchers.Main) {

            delay(500)

            playlists.deletePlaylist(playlistId)
        }

        liveData.setValue(PlaylistScreenData.GoBack(true))
    }
}