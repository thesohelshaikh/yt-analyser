package com.thesohelshaikh.ytanalyser

import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.thesohelshaikh.ytanalyser.ui.MainActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class VideoAnalyzeTest {

    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun testVideoIsBeingAnalyzed() {
/*        // Checking if the screen is visible
        onView(withId(R.id.ed_url))
            .check(matches(isDisplayed()))

        // Given a youtube url
        onView(withId(R.id.ed_url))
            .perform(typeText("https://www.youtube.com/watch?v=YKzUbeUtTak"))

        // when analysing the youtube video
        onView(withId(R.id.btn_analyse))
            .perform(click())

        // then correct video title should be shown
        onView(withId(R.id.tv_videoTitle))
            .check(matches(withText("10 Kotlin Tricks in 10(ish) Minutes by Jake Wharton")))

        // then correct channel name should be shown
        onView(withId(R.id.tv_channelTitle))
            .check(matches(withText("SquareEngineering")))

        // then correct duration should be shown
        onView(withId(R.id.tv_duration))
            .check(matches(withText("24m 58s")))*/
    }
}