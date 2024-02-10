package com.thesohelshaikh.ytanalyser

import com.thesohelshaikh.ytanalyser.ui.details.DurationsManger
import org.junit.Assert
import org.junit.Test

class DurationsManagerTest {

    @Test
    fun testLongUrl() {
        // given YouTube URL with watch?v= format,
        // when extracting video ID,
        // then should return correct ID
        val youtubeUrl = "https://www.youtube.com/watch?v=abcdefghijk"
        val videoId = DurationsManger.getIDfromURL(youtubeUrl)
        Assert.assertEquals("abcdefghijk", videoId)
    }

    @Test
    fun testShortUrl() {
        // given shortened YouTube URL,
        // when extracting video ID,
        // then should return correct ID
        val youtubeUrl = "https://youtu.be/0sPzDwS55wM?si=WU_0wxQUT8Ye_7ie"
        val videoId = DurationsManger.getIDfromURL(youtubeUrl)
        Assert.assertEquals("0sPzDwS55wM", videoId)
    }

    @Test
    fun testPlaylistUrl() {
        // given YouTube Playlist URL,
        // when extracting playlist ID,
        // then should return correct playlist id
        val invalidUrl =
            "https://youtube.com/playlist?list=PLBTRwhdaxW7Aas6LwaGF_H8Oiq9OO9mbI&si=p3qO0nbZzd9ng8be"
        val playlistId = DurationsManger.getIDfromURL(invalidUrl)
        Assert.assertEquals("PLBTRwhdaxW7Aas6LwaGF_H8Oiq9OO9mbI", playlistId)
    }

    @Test
    fun testVideoInPlaylistUrl() {
        // given a YouTube Video id from a Playlist URL,
        // when extracting playlist ID,
        // then should return correct video id
        val invalidUrl =
            "https://www.youtube.com/watch?v=eGoEcDzvDjk&list=PL24E8CD3214E5C748&index=1&t=263s"
        val playlistId = DurationsManger.getIDfromURL(invalidUrl)
        Assert.assertEquals("eGoEcDzvDjk", playlistId)
    }
}