package com.ktor.bal.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ktor.bal.api.InboxApi
import com.ktor.bal.core.CoreResponseStatus
import com.ktor.bal.model.InboxData
import com.ktor.bal.model.InboxPostData
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {
    private val _inboxList = mutableStateListOf<InboxData>()
    var errorMessage: String by mutableStateOf("")
    val inboxList: List<InboxData>
        get() = _inboxList

    private val apiService by lazy {
        InboxApi.create()
    }

    fun getInboxResponse(inboxPostData: InboxPostData) {
        viewModelScope.launch {
            try {
                val response = apiService.getInbox(inboxPostData)
                if (response?.Status == CoreResponseStatus.Ok) {
                    _inboxList.clear()
                    _inboxList.addAll(response.Value)
                }

            } catch (e: Exception) {
                errorMessage = e.message.toString()
            }
        }
    }
}
