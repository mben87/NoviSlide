package com.novislide.presentation.home

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.novislide.ui.theme.NoviSlideTheme
import org.junit.Rule
import org.junit.Test

class HomeScreenTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun clickButton_showsGreeting() {
        composeTestRule.setContent {
            NoviSlideTheme {
                HomeScreen(greeting = "", onShowGreeting = {})
            }
        }
        composeTestRule.onNodeWithText("Load Greeting").performClick()
    }
}
