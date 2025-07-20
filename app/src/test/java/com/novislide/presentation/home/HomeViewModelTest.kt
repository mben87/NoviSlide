package com.novislide.presentation.home

import com.novislide.data.repository.FakeGreetingRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModelTest {
    private val repository = FakeGreetingRepository()
    private val viewModel = HomeViewModel(repository)

    @Test
    fun loadGreeting_updatesState() = runTest {
        viewModel.loadGreeting("Test")
        assertEquals("Hello Test!", viewModel.greeting.value)
    }
}
