package io.github.omisie11.coronatracker.espresso

import android.app.Instrumentation
import android.content.Intent
import android.content.res.Resources
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers.hasAction
import androidx.test.espresso.intent.matcher.IntentMatchers.hasData
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import io.github.omisie11.coronatracker.R
import io.github.omisie11.coronatracker.ui.MainActivity
import org.hamcrest.Matchers.allOf
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AboutScreenEspressoTests {

    private lateinit var appResources: Resources

    @get:Rule
    val activityRule = ActivityTestRule(MainActivity::class.java)

    @Before
    fun setup() {
        appResources = InstrumentationRegistry.getInstrumentation().targetContext.resources
        Intents.init()
    }

    @After
    fun tearDown() {
        Intents.release()
    }

    @Test
    fun clickOnGitHubProfileChip_browserShouldOpen() {
        val expected = allOf(
            hasAction(Intent.ACTION_VIEW),
            hasData(appResources.getString(R.string.github_url_omisie11))
        )
        Intents.intending(expected)
            .respondWith(Instrumentation.ActivityResult(0, null))

        onView(withId(R.id.about_dest))
            .perform(click())
        onView(withId(R.id.chip_github))
            .perform(click())

        Intents.intended(expected)
    }

    @Test
    fun clickOnTwitterProfileChip_browserShouldOpen() {
        val expected = allOf(
            hasAction(Intent.ACTION_VIEW),
            hasData(appResources.getString(R.string.twitter_url_omisie11))
        )
        Intents.intending(expected)
            .respondWith(Instrumentation.ActivityResult(0, null))

        onView(withId(R.id.about_dest))
            .perform(click())
        onView(withId(R.id.chip_twitter))
            .perform(click())

        Intents.intended(expected)
    }

    @Test
    fun clickOnWebsiteChip_browserShouldOpen() {
        val expected = allOf(
            hasAction(Intent.ACTION_VIEW),
            hasData(appResources.getString(R.string.website_url_omisie11))
        )
        Intents.intending(expected)
            .respondWith(Instrumentation.ActivityResult(0, null))

        onView(withId(R.id.about_dest))
            .perform(click())
        onView(withId(R.id.chip_website_omisie11))
            .perform(click())

        Intents.intended(expected)
    }

    @Test
    fun clickOnSourceCodeChip_browserShouldOpen() {
        val expected = allOf(
            hasAction(Intent.ACTION_VIEW),
            hasData(appResources.getString(R.string.source_code_url))
        )
        Intents.intending(expected)
            .respondWith(Instrumentation.ActivityResult(0, null))

        onView(withId(R.id.about_dest))
            .perform(click())
        onView(withId(R.id.chip_source_code))
            .perform(click())

        Intents.intended(expected)
    }

    @Test
    fun clickOnApiChip_browserShouldOpen() {
        val expected = allOf(
            hasAction(Intent.ACTION_VIEW),
            hasData(appResources.getString(R.string.api_url))
        )
        Intents.intending(expected)
            .respondWith(Instrumentation.ActivityResult(0, null))

        onView(withId(R.id.about_dest))
            .perform(click())
        onView(withId(R.id.chip_attribution_api))
            .perform(click())

        Intents.intended(expected)
    }

    @Test
    fun clickOnLibrariesChip_listShouldBePresent() {
        val expected = allOf(hasAction(Intent.ACTION_VIEW))
        Intents.intending(expected)
            .respondWith(Instrumentation.ActivityResult(0, null))

        onView(withId(R.id.about_dest))
            .perform(click())
        onView(withId(R.id.chip_used_libs))
            .perform(click())
        onView(withId(R.id.recyclerView_libs))
            .perform(actionOnItemAtPosition<RecyclerView.ViewHolder>(1, click()))

        Intents.intended(expected)
    }
}
