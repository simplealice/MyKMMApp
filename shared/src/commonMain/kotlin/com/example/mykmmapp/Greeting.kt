package com.example.mykmmapp

class Greeting {
    fun greeting(): String {
        return "Hello, ${Platform().platform}!"
    }
}