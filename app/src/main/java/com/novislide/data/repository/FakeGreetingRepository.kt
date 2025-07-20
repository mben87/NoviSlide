package com.novislide.data.repository

import com.novislide.domain.repository.GreetingRepository
import javax.inject.Inject

class FakeGreetingRepository @Inject constructor() : GreetingRepository {
    override fun getGreeting(name: String): String = "Hello $name!"
}
