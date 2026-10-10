package xyz.vmartinez.accesoaeventos.services

import xyz.vmartinez84.accesoaeventos.models.TokenModel

interface ApiCallback<T> {
    fun onSuccess(data: TokenModel?)
    fun onError(error: String)
}