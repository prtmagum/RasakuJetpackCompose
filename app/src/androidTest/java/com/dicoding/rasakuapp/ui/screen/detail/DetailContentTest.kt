package com.dicoding.rasakuapp.ui.screen.detail

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.printToLog
import com.dicoding.jetreward.onNodeWithStringId
import com.dicoding.rasakuapp.R
import com.dicoding.rasakuapp.model.OrderRasaku
import com.dicoding.rasakuapp.model.Rasaku
import com.dicoding.rasakuapp.ui.theme.RasakuAppTheme
import org.junit.*

class DetailContentTest {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()
    private val fakeOrderRasaku = OrderRasaku(
        rasaku = Rasaku(4, R.drawable.rasaku_4, "Lepet Isi Kacang", 10000, "Gemblong adalah camilan tradisional khas Kuningan yang terbuat dari ketan dan gula merah. Teksturnya yang renyah di luar dan kenyal di dalam menjadikannya cemilan yang digemari oleh berbagai kalangan."),
        count = 0
    )

    // New: Dummy variable for favorite state
    private var isFavorite = false

    @Before
    fun setUp() {
        composeTestRule.setContent {
            RasakuAppTheme{
                DetailContent(
                    fakeOrderRasaku.rasaku.image,
                    fakeOrderRasaku.rasaku.title,
                    fakeOrderRasaku.rasaku.price,
                    fakeOrderRasaku.count,
                    fakeOrderRasaku.rasaku.description,
                    onBackClick = {},
                    onAddToCart = { /* Add action for add to cart */ },
                    id = fakeOrderRasaku.rasaku.id,
                    isFavorite = isFavorite,
                    onFavoriteButtonClicked = { _, newState ->
                        isFavorite = newState
                    }
                )
            }
        }
        composeTestRule.onRoot().printToLog("currentLabelExists")
    }

    @Test
    fun detailContent_isDisplayed() {
        composeTestRule.onNodeWithText(fakeOrderRasaku.rasaku.title).assertIsDisplayed()
        composeTestRule.onNodeWithText(
            composeTestRule.activity.getString(
                R.string.price_rasaku,
                fakeOrderRasaku.rasaku.price
            )
        ).assertIsDisplayed()
    }

    @Test
    fun increaseProduct_buttonEnabled() {
        composeTestRule.onNodeWithContentDescription("Order Button").assertIsNotEnabled()
        composeTestRule.onNodeWithStringId(R.string.plus_symbol).performClick()
        composeTestRule.onNodeWithContentDescription("Order Button").assertIsEnabled()
    }

    @Test
    fun increaseProduct_correctCounter() {
        composeTestRule.onNodeWithStringId(R.string.plus_symbol).performClick().performClick()
        composeTestRule.onNodeWithTag("count").assert(hasText("2"))
    }

    @Test
    fun favoriteButton_click_updatesFavoriteState() {
        // Ensure the favorite button starts as not favorite
        composeTestRule.onNodeWithTag("favorite_detail_button")
            .assert(hasText(composeTestRule.activity.getString(R.string.add_favorite)))

        // Click on the favorite button to toggle favorite state
        composeTestRule.onNodeWithTag("favorite_detail_button").performClick()

        // Check if the state has been updated (favorited)
        composeTestRule.onNodeWithTag("favorite_detail_button")
            .assert(hasText(composeTestRule.activity.getString(R.string.delete_favorite)))
    }
}