package com.novislide.domain.repository

interface GreetingRepository {
    fun getGreeting(name: String): String
}
