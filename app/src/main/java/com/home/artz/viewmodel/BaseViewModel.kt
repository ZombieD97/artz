package com.home.artz.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.home.artz.model.datamodel.UserMessage
import com.home.artz.model.repository.base.IBaseRepository
import kotlinx.coroutines.launch

abstract class BaseViewModel(private val baseRepository: IBaseRepository) : ViewModel() {

    var userMessage = mutableStateOf<UserMessage?>(null)

    init {
        viewModelScope.launch {
            baseRepository.userErrorMessage.collect {
                userMessage.value = it
            }
        }
    }

    fun clearUserMessage() {
        userMessage.value = null
    }
}