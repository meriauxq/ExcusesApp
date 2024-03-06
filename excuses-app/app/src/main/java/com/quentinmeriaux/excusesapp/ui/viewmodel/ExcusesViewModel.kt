package com.quentinmeriaux.excusesapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.quentinmeriaux.excusesapp.data.remote.dto.Excuse
import com.quentinmeriaux.excusesapp.data.remote.repository.ExcusesRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlin.random.Random
import kotlin.random.nextInt
import kotlin.time.Duration.Companion.seconds

class ExcusesViewModel(private val repository: ExcusesRepository) : ViewModel() {

    private var excuseList: MutableList<Excuse> = mutableListOf()

    private val _searchedExcuse = MutableStateFlow<Excuse?>(null)
    val searchedExcuse: StateFlow<Excuse?> get() = _searchedExcuse

    private val _randomExcuse = MutableStateFlow<Excuse?>(null)
    val randomExcuse: StateFlow<Excuse?> get() = _randomExcuse

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> get() = _loading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> get() = _error

    init {
        // Retrieve all excuses
        getExcuses()
    }

    /**
     * Fetch all excuses
     */
    private fun getExcuses() {
        viewModelScope.launch {
            _loading.value = true

            // Get the excuses from the repository
            val excuses = repository.getExcuses()

            if (excuses == null) {
                _error.value = "Could not initialize the excuses"
            } else {
                excuseList = excuses.toMutableList()
                // Generate the initial random excuse after fetching all excuses
                generateRandomExcuse()
            }

            _loading.value = false
        }
    }

    /**
     * Fetch a single excuse by [httpCode]
     */
    fun getExcuse(httpCode: Int?) {
        if (httpCode == null) {
            _error.value = "Please enter a valid number"
            return
        }
        viewModelScope.launch {
            _loading.value = true

            // Get the excuse from the repository
            val excuse = repository.getExcuse(httpCode)

            if (excuse == null) {
                _error.value = "Could not fetch excuse $httpCode"
            } else {
                _searchedExcuse.value = excuse
            }

            _loading.value = false
        }
    }

    /**
     * Add a new [excuse]
     */
    fun addExcuse(excuse: Excuse) {
        viewModelScope.launch {
            _loading.value = true

            // Post the excuse via the repository
            val addedExcuse = repository.postExcuse(excuse)

            if (addedExcuse == null) {
                _error.value = "Could not add excuse"
            } else {
                excuseList.add(addedExcuse)
            }

            _loading.value = false
        }
    }

    /**
     * Get a random excuse from the fetched excuses that is not the current [randomExcuse]
     */
    suspend fun generateRandomExcuse() {
        _loading.value = true

        // Fake a loading delay
        delay(Random.nextInt(1..5).seconds)

        // Get the random excuse
        _randomExcuse.value = excuseList.filterNot { it == randomExcuse.value }.randomOrNull()

        _loading.value = false
    }
}
