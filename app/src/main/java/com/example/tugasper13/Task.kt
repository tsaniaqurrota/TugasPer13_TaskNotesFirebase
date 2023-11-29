package com.example.tugasper13

import java.io.Serializable

data class Task(
    var id: String = "",
    var title : String = "",
    var description : String = "",
    var dosen : String = "",
    var deadline : String = ""
) : Serializable
