package com.anytypeio.anytype.domain.device

interface PathProvider {
    fun providePath(): String
    fun cachePath(): String
}