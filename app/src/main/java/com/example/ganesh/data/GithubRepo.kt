package com.example.ganesh.data

import java.io.Serializable

data class GithubRepo(
    val author: String,
    val name: String,
    val avatar: String,
    val url: String,
    val description: String,
    val language: String,
    val languageColor: String,
    val stars: String,
    val forks: String,
    val currentPeriodStars: String,
    val builtBy: List<Contributor>
) : Serializable {
    var isExpanded: Boolean = false
}


data class Contributor(
    val username: String,
    val href: String,
    val avatar: String
) : Serializable

//sample data to parse
/*
"author":"tmrowco",
"name":"electricitymap-contrib",
"avatar":"https://github.com/tmrowco.png",
"url":"https://github.com/tmrowco/electricitymap-contrib",
"description":"A real-time visualisation of the CO2 emissions of electricity consumption",
"language":"Python",
"languageColor":"#3572A5",
"stars":1046,
"forks":242,
"currentPeriodStars":21,
"builtBy":[
{
    "username":"corradio",
    "href":"https://github.com/corradio",
    "avatar":"https://avatars1.githubusercontent.com/u/1655848"
},
{
    "username":"systemcatch",
    "href":"https://github.com/systemcatch",
    "avatar":"https://avatars0.githubusercontent.com/u/30196510"
},
{
    "username":"alixunderplatz",
    "href":"https://github.com/alixunderplatz",
    "avatar":"https://avatars1.githubusercontent.com/u/25743609"
},
{
    "username":"maxbellec",
    "href":"https://github.com/maxbellec",
    "avatar":"https://avatars3.githubusercontent.com/u/7253902"
},
{
    "username":"brunolajoie",
    "href":"https://github.com/brunolajoie",
    "avatar":"https://avatars3.githubusercontent.com/u/22095643"
}
]
}
*/