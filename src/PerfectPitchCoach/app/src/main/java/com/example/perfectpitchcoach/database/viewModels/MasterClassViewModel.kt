package com.example.perfect_pitch_trainer.database.viewModels

/*
 * Copyright (C) 2017 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import com.example.perfect_pitch_trainer.database.WordRoomDatabase
import com.example.perfect_pitch_trainer.database.model.MasterClass
import com.example.perfect_pitch_trainer.database.repository.MasterClassRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

/**
 * View Model to keep a reference to the word repository and
 * an up-to-date list of all words.
 */

class MasterClassViewModel(application: Application) : AndroidViewModel(application) {

    private var parentJob = Job()
    // By default all the coroutines launched in this scope should be using the Main dispatcher
    private val coroutineContext: CoroutineContext
        get() = parentJob + Dispatchers.Main
    private val scope = CoroutineScope(coroutineContext)

    val repository: MasterClassRepository
    // Using LiveData and caching what getAlphabetizedWords returns has several benefits:
    // - We can put an observer on the data (instead of polling for changes) and only update the
    //   the UI when the data actually changes.
    // - Repository is completely separated from the UI through the ViewModel.
    val allMasterClasses: LiveData<List<MasterClass>>

    init {
        val masterClassDao = WordRoomDatabase.getDatabase(
            application,
            scope
        ).masterClassDao()
        repository = MasterClassRepository(masterClassDao)
        allMasterClasses = repository.allMasterClasses
    }

    /**
     * Launching a new coroutine to insert the data in a non-blocking way
     */
    fun insert(masterClass: MasterClass) = scope.launch(Dispatchers.IO) {
        repository.insert(masterClass)
    }


    fun update(userId: String, solved: String) = scope.launch(Dispatchers.IO) {
        repository.update(userId, solved)
    }

    override fun onCleared() {
        super.onCleared()
        parentJob.cancel()
    }
}
