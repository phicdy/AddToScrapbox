package com.phicdy.addtoscrapbox.main

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import androidx.activity.viewModels
import androidx.core.widget.addTextChangedListener
import androidx.core.widget.doAfterTextChanged
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.phicdy.addtoscrapbox.core.Dispatcher
import com.phicdy.addtoscrapbox.main.databinding.ActivityMainBinding
import com.phicdy.addtoscrapbox.repository.SettingRepository
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val store: MainStore by viewModels {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return MainStore(dispatcher) as T
            }
        }
    }

    private val dispatcher = Dispatcher()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        initView()
        store.state.observe(this, Observer { state ->
            when (state) {
                MainState.Loading -> TODO()
                is MainState.Loaded -> startActivity(Intent(Intent.ACTION_VIEW, state.uri))
            }
        })
    }

    private val queryChannel = Channel<String>(Channel.CONFLATED)

    private fun initView() {
        binding.projectUrl.doAfterTextChanged {
            lifecycleScope.launch {
                queryChannel.send(it.toString())
            }
        }

        val repository = SettingRepository(applicationContext)
        lifecycleScope.launch {
            binding.projectUrl.setText(repository.getProjectUrl())
            queryChannel.receiveAsFlow()
                .debounce(500)
                .distinctUntilChanged()
                .collect {
                    repository.store(it)
                }
        }

        binding.addUrl.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE && event?.action != KeyEvent.ACTION_UP) {
                lifecycleScope.launch {
                    MainActionCreator(dispatcher).execute(binding.projectUrl.text.toString(), v.text.toString())
                }
                return@setOnEditorActionListener true
            }

            return@setOnEditorActionListener false
        }
    }
}
