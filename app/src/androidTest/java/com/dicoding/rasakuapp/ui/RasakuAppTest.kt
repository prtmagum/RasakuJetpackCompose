package com.dicoding.rasakuapp.ui

import androidx.activity.ComponentActivity
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollToIndex
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.testing.TestNavHostController
import com.dicoding.jetreward.assertCurrentRouteName
import com.dicoding.jetreward.onNodeWithStringId
import com.dicoding.rasakuapp.R
import com.dicoding.rasakuapp.ui.navigation.Screen
import com.dicoding.rasakuapp.RasakuApp
import com.dicoding.rasakuapp.model.FakeRasakuDataSource
import com.dicoding.rasakuapp.ui.theme.RasakuAppTheme
import org.junit.*

class RasakuAppTest {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()
    private lateinit var navController: TestNavHostController

    @Before
    fun setUp() {
        composeTestRule.setContent {
            RasakuAppTheme {
                navController = TestNavHostController(LocalContext.current)
                navController.navigatorProvider.addNavigator(ComposeNavigator())
                RasakuApp(navController = navController)
            }
        }
    }

    @Test
    fun navHost_verifyStartDestination() {

        navController.assertCurrentRouteName(Screen.Home.route)
    }

    @Test
    fun navHost_clickItem_navigatesToDetailWithData() {
        composeTestRule.onNodeWithTag("ProdukList").performScrollToIndex(9)
        composeTestRule.onNodeWithText(FakeRasakuDataSource.dummyRasaku[9].title).performClick()
        navController.assertCurrentRouteName(Screen.DetailRasaku.route)
        composeTestRule.onNodeWithText(FakeRasakuDataSource.dummyRasaku[9].title)
            .assertIsDisplayed()
    }

    @Test
    fun navHost_bottomNavigation_working() {
        composeTestRule.onNodeWithStringId(R.string.menu_cart).performClick()
        navController.assertCurrentRouteName(Screen.Cart.route)
        composeTestRule.onNodeWithStringId(R.string.menu_profile).performClick()
        navController.assertCurrentRouteName(Screen.Profile.route)
        composeTestRule.onNodeWithStringId(R.string.menu_home).performClick()
        navController.assertCurrentRouteName(Screen.Home.route)
        composeTestRule.onNodeWithStringId(R.string.menu_favorite).performClick()
        navController.assertCurrentRouteName(Screen.Favorite.route)
    }

    @Test
    fun navHost_clickItem_navigatesBack() {
        composeTestRule.onNodeWithTag("ProdukList").performScrollToIndex(9)
        composeTestRule.onNodeWithText(FakeRasakuDataSource.dummyRasaku[9].title).performClick()
        navController.assertCurrentRouteName(Screen.DetailRasaku.route)
        composeTestRule.onNodeWithContentDescription(composeTestRule.activity.getString(R.string.back))
            .performClick()
        navController.assertCurrentRouteName(Screen.Home.route)
    }

    @Test
    fun navHost_checkout_rightBackStack() {
        composeTestRule.onNodeWithText(FakeRasakuDataSource.dummyRasaku[4].title).performClick()
        navController.assertCurrentRouteName(Screen.DetailRasaku.route)
        composeTestRule.onNodeWithStringId(R.string.plus_symbol).performClick()
        composeTestRule.onNodeWithContentDescription("Order Button").performClick()
        navController.assertCurrentRouteName(Screen.Cart.route)
        composeTestRule.onNodeWithStringId(R.string.menu_home).performClick()
        navController.assertCurrentRouteName(Screen.Home.route)
    }

}