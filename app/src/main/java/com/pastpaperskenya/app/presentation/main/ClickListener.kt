package com.pastpaperskenya.app.presentation.main

interface ClickListener<T> {
    fun onClick(data: T)
}