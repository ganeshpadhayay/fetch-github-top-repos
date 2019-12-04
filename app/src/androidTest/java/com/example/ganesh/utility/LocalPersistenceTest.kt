package com.example.ganesh.utility

import android.content.Context
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.ExpectedException

class LocalPersistenceTest {

    @Rule
    var thrown = ExpectedException.none()!!

    private var fileName: String? = null
    private var dummyObject: Any? = null
    private var context: Context? = null

    @Before
    fun setUp() {
        //to initialize the global variables required for testing
        fileName = null
        dummyObject = "Dummy String for Testing"
        context = InstrumentationRegistry.getInstrumentation().context
    }

    @Test
    fun writeObjectToFile_CheckException_IfNoFileNameIsGiven() {
        thrown.expect(NullPointerException::class.java)
        LocalPersistence.writeObjectToFile(context!!, dummyObject, fileName)
    }

    @Test
    fun readObjectFromFile_CheckException_IfFileNameIsNotGiven() {
        thrown.expect(NullPointerException::class.java)
        LocalPersistence.readObjectFromFile(context!!, fileName)
    }

    @Test
    fun writeObjectToFile_CheckFileNotFoundException_IfCorrectFileNameIsGiven() {
        thrown.expect(NullPointerException::class.java)
        LocalPersistence.writeObjectToFile(context!!, dummyObject, "sample.txt")
    }

    @Test
    fun readObjectFromFile_CheckNoException_IfCorrectFileNameIsGiven() {
        thrown.expect(NullPointerException::class.java)
        LocalPersistence.readObjectFromFile(context!!, "sample.txt")
    }

    @After
    fun tearDown() {
        //to free the global variables used in the testing
        fileName = null
        dummyObject = null
        context = null
    }
}