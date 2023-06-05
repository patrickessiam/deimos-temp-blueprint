package com.deimosdev.data.utils

import com.deimosdev.utils.StringListConverter
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class StringListConverterTest {
    private val converter = StringListConverter()

    @Test
    fun `test toListOfStrings`() {
        val jsonString = """["string1", "string2", "string3"]"""
        val expectedList = listOf("string1", "string2", "string3")
        assertEquals(expectedList, converter.toListOfStrings(jsonString))
    }

    @Test
    fun `convert empty string to null`() {
        val result = converter.toListOfStrings("")
        assertNull(result)
    }

    @Test
    fun testFromListOfStrings_emptyList_returnsEmptyJsonArray() {
        val expectedJson = "[]"
        assertEquals(expectedJson, converter.fromListOfStrings(emptyList()))
    }

    @Test
    fun `convert list of strings to json string`() {
        val listOfStrings = listOf("item1", "item2", "item3")
        val result = converter.fromListOfStrings(listOfStrings)
        val expectedValue = """[
  "item1",
  "item2",
  "item3"
]"""
        assertEquals(expectedValue, result)
    }
}