package com.example.testapp

class FakeTracks {

    companion object {

        var index: Int = 0;

        val tracks: List<Track> = listOf(
            Track(
                "Smells Like Teen Spirit",
                "Nirvana",
                "5:01",
                "https://is5-ssl.mzstatic.com/image/thumb/Music115/v4/7b/58/c2/7b58c21a-2b51-2bb2-e59a-9bb9b96ad8c3/00602567924166.rgb.jpg/100x100bb.jpg",
                "https://audio-ssl.itunes.apple.com/itunes-assets/AudioPreview125/v4/8d/31/0c/8d310cde-3b26-4c8f-ddc8-c1bb416050d5/mzaf_15258511498557422878.plus.aac.p.m4a"
            ),

            Track(
                "Billie Jean",
                "Michael Jackson",
                "4:35",
                "https://is5-ssl.mzstatic.com/image/thumb/Music125/v4/3d/9d/38/3d9d3811-71f0-3a0e-1ada-3004e56ff852/827969428726.jpg/100x100bb.jpg",
                "https://audio-ssl.itunes.apple.com/itunes-assets/AudioPreview125/v4/8d/31/0c/8d310cde-3b26-4c8f-ddc8-c1bb416050d5/mzaf_15258511498557422878.plus.aac.p.m4a"
            ),

            Track(
                "Stayin' Alive",
                "Bee Gees",
                "4:10",
                "https://is4-ssl.mzstatic.com/image/thumb/Music115/v4/1f/80/1f/1f801fc1-8c0f-ea3e-d3e5-387c6619619e/16UMGIM86640.rgb.jpg/100x100bb.jpg",
                "https://audio-ssl.itunes.apple.com/itunes-assets/AudioPreview125/v4/8d/31/0c/8d310cde-3b26-4c8f-ddc8-c1bb416050d5/mzaf_15258511498557422878.plus.aac.p.m4a"
            ),

            Track(
                "Whole Lotta Love",
                "Led Zeppelin",
                "5:33",
                "https://is2-ssl.mzstatic.com/image/thumb/Music62/v4/7e/17/e3/7e17e33f-2efa-2a36-e916-7f808576cf6b/mzm.fyigqcbs.jpg/100x100bb.jpg",
                "https://audio-ssl.itunes.apple.com/itunes-assets/AudioPreview125/v4/8d/31/0c/8d310cde-3b26-4c8f-ddc8-c1bb416050d5/mzaf_15258511498557422878.plus.aac.p.m4a"
            ),

            Track(
                "Sweet Child O'Mine",
                "Guns N' Roses",
                "5:03",
                "https://is5-ssl.mzstatic.com/image/thumb/Music125/v4/a0/4d/c4/a04dc484-03cc-02aa-fa82-5334fcb4bc16/18UMGIM24878.rgb.jpg/100x100bb.jpg",
                "https://audio-ssl.itunes.apple.com/itunes-assets/AudioPreview125/v4/8d/31/0c/8d310cde-3b26-4c8f-ddc8-c1bb416050d5/mzaf_15258511498557422878.plus.aac.p.m4a"
            ),

            Track(
                "Очень длинное название трека, которое не влезет в поле и будет обрезано",
                "A very long track name that will not fit into the field and will be cut off",
                "14:33",
                "https://is5-ssl.mzstatic.com/image/thumb/Music125/v4/a0/4d/c4/a04dc484-03cc-02aa-fa82-5334fcb4bc16/18UMGIM24878.rgb.jpg/100x100bb.jpg",
                "https://audio-ssl.itunes.apple.com/itunes-assets/AudioPreview125/v4/8d/31/0c/8d310cde-3b26-4c8f-ddc8-c1bb416050d5/mzaf_15258511498557422878.plus.aac.p.m4a"
            ),

            Track(
                "Эта картинка не загрузится",
                "Ошибка загрузки",
                "00:00",
                "https://yandex.ru/12345.jpg",
                ""
            ),

            Track(
                "Картинка с http://",
                "Борис Николаевич",
                "01:19",
                "http://static.kremlin.ru/media/events/video/image/small/MrbdEh2T5jAMQaDBzMa3RQCS0I3kcY1i.jpg",
                "https://audio-ssl.itunes.apple.com/itunes-assets/AudioPreview125/v4/8d/31/0c/8d310cde-3b26-4c8f-ddc8-c1bb416050d5/mzaf_15258511498557422878.plus.aac.p.m4a"
            )
        );

        fun getTrack(): Track {
            return tracks[(index++) % tracks.size];
        }

        fun setZero() {
            index = 0;
        }
    }
}