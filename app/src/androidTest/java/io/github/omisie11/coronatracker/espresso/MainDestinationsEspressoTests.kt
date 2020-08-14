package io.github.omisie11.coronatracker.espresso

import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.pressBack
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.hasDescendant
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import io.github.omisie11.coronatracker.R
import io.github.omisie11.coronatracker.ui.MainActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MainDestinationsEspressoTests {

    @get:Rule
    val activityRule = ActivityTestRule(MainActivity::class.java)

    @Test
    fun clickOnBottomNavOpenGlobalScreen() {
        onView(withId(R.id.global_dest))
            .perform(click())
        onView(withId(R.id.text_country_title))
            .check(matches(isDisplayed()))
            .check(matches(withText(R.string.global_summary)))
        onView(withId(R.id.pie_chart_global))
            .check(matches(isDisplayed()))
        onView(withId(R.id.stat_recovered))
            .check(matches(isDisplayed()))
            .perform(click())
        onView(withId(R.id.stat_confirmed))
            .check(matches(isDisplayed()))
            .perform(click())
        onView(withId(R.id.stat_deaths))
            .check(matches(isDisplayed()))
            .perform(click())
    }

    @Test
    fun clickOnBottomNavOpenLocalScreen() {
        onView(withId(R.id.local_dest))
            .perform(click())
        onView(withId(R.id.text_country_title))
            .check(matches(isDisplayed()))
        onView(withId(R.id.pie_chart_local))
            .check(matches(isDisplayed()))
        onView(withId(R.id.stat_recovered))
            .check(matches(isDisplayed()))
            .perform(click())
        onView(withId(R.id.stat_confirmed))
            .check(matches(isDisplayed()))
            .perform(click())
        onView(withId(R.id.stat_deaths))
            .check(matches(isDisplayed()))
            .perform(click())
    }

    @Test
    fun clickOnBottomNavOpenSettingsScreen() {
        onView(withId(R.id.settings_dest))
            .perform(click())

        validateSettingsScreen()
    }

    @Test
    fun clickOnBottomNavOpenAboutScreen() {
        onView(withId(R.id.about_dest))
            .perform(click())
        onView(withId(R.id.textView2))
            .check(matches(isDisplayed()))
            .check(matches(withText(R.string.created_by)))
        onView(withId(R.id.textView8))
            .check(matches(isDisplayed()))
            .check(matches(withText(R.string.omisie11)))
        onView(withId(R.id.horizontalScrollView))
            .check(matches(isDisplayed()))
        onView(withId(R.id.chip_github))
            .check(matches(isDisplayed()))
            .check(matches(withText(R.string.github)))
        onView(withId(R.id.chip_twitter))
            .check(matches(isDisplayed()))
            .check(matches(withText(R.string.twitter)))
        onView(withId(R.id.chip_website_omisie11))
            .check(matches(isDisplayed()))
            .check(matches(withText(R.string.website)))
        onView(withId(R.id.textView20))
            .check(matches(isDisplayed()))
            .check(matches(withText(R.string.get_the_app_source_code_on_github)))
        onView(withId(R.id.chip_source_code))
            .check(matches(isDisplayed()))
            .check(matches(withText(R.string.source_code)))
        onView(withId(R.id.textView18))
            .check(matches(isDisplayed()))
            .check(matches(withText(R.string.credits)))
        onView(withId(R.id.chip_attribution_api))
            .check(matches(isDisplayed()))
            .check(matches(withText(R.string.covid_19_api)))
        onView(withId(R.id.chip_used_libs))
            .check(matches(isDisplayed()))
            .check(matches(withText(R.string.awesome_libraries)))
        onView(withId(R.id.textView))
            .check(matches(isDisplayed()))
            .check(matches(withText(R.string.privacy)))
        onView(withId(R.id.textView5))
            .check(matches(isDisplayed()))
            .check(matches(withText(R.string.privacy_info)))
    }

    @Test
    fun clickOnEditLocationImageInLocalScreenOpenSettings() {
        onView(withId(R.id.local_dest))
            .perform(click())
        onView(withId(R.id.image_edit_location))
            .perform(click())

        validateSettingsScreen()
    }

    private fun validateSettingsScreen() {
        // Check if settings are present by trying to click on each one
        // RecyclerView actions are used because normal approach is not
        // working with preferences from support lib
        onView(withId(R.id.recycler_view))
            .perform(
                RecyclerViewActions.actionOnItem<RecyclerView.ViewHolder>(
                    hasDescendant(withText(R.string.settings_refresh_interval_title)),
                    click()
                )
            )
        pressBack()
        onView(withId(R.id.recycler_view))
            .perform(
                RecyclerViewActions.actionOnItem<RecyclerView.ViewHolder>(
                    hasDescendant(withText(R.string.settings_dark_mode_title)),
                    click()
                )
            )
        onView(withId(R.id.recycler_view))
            .perform(
                RecyclerViewActions.actionOnItem<RecyclerView.ViewHolder>(
                    hasDescendant(withText(R.string.country_showed_in_local_tab)),
                    click()
                )
            )
    }
}
