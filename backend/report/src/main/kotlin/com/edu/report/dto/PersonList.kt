package com.edu.report.dto

object PersonList {
    fun getPersonList():MutableList<Person> {
        return mutableListOf(Person(17, "alex", "hitch"), Person(20, "john", "pole"), )
    }
}