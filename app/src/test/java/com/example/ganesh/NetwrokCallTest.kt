package com.example.ganesh

import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class NetworkCallTest {

    lateinit var server: MockWebServer

    @Before
    fun setUp() {
        // Create a MockWebServer
        server = MockWebServer()
    }

    @Test
    @Throws(Exception::class)
    fun testTheBasicResponse() {
        // Schedule some responses.
        server.enqueue(MockResponse().setBody("hello, world!"))
        server.enqueue(MockResponse().setBody("sup, bra?"))
        server.enqueue(MockResponse().setBody("yo dog"))

        // Start the server.
        server.start()
    }

    @After
    fun tearDown() {
        // Shut down the server. Instances cannot be reused.
        server.shutdown()
    }
}

