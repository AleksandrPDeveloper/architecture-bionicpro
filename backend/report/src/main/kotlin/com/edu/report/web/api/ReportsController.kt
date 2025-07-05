package com.edu.report.web.api

import com.edu.report.dto.Person
import com.edu.report.dto.PersonList
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod

@Controller
@RequestMapping("/")
class ReportsController {
    @RequestMapping(method = [RequestMethod.GET], value = ["/reports"], produces = ["application/json"])
    fun propertyCatalogGet(

    ): ResponseEntity<MutableList<Person>> {
        return ResponseEntity(PersonList.getPersonList(), HttpStatus.OK)
    }
}