package com.github.georgeh1998.simplesplit

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform