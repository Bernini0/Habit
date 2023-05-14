package com.example.habit

import junit.framework.TestCase
import org.junit.Test

class McqTest : TestCase() {

    var mcq : Mcq? = null

    public override fun setUp() {
        super.setUp()

        mcq = Mcq("question title", listOf("option1", "option2", "option3"))
    }

    @Test
    fun testGetQuestion() {
        assertNotNull(mcq)
        assertEquals( "question title", mcq?.question)

    }

    @Test
    fun testGetOptions() {
        assertEquals(3, mcq?.options?.size)
    }

    @Test
    fun testAddition() {
        val result = 2 + 2
        assertEquals(4, result)
    }

    @Test
    fun testSubtraction() {
        val result = 5 - 3
        assertEquals(2, result)
    }
}