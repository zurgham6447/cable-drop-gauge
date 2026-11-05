package com.cabledrop.gauge.core.di

import android.content.Context
import com.cabledrop.gauge.data.local.AppPreferences
import com.cabledrop.gauge.data.repository.RunRepositoryImpl
import com.cabledrop.gauge.data.repository.SettingsRepositoryImpl
import com.cabledrop.gauge.domain.repository.RunRepository
import com.cabledrop.gauge.domain.repository.SettingsRepository
import com.cabledrop.gauge.domain.usecase.CalculateDropUseCase
import com.cabledrop.gauge.domain.usecase.DiagramDropUseCase
import com.cabledrop.gauge.domain.usecase.PowerBudgetUseCase
import com.cabledrop.gauge.domain.usecase.RecommendWireUseCase
import com.cabledrop.gauge.domain.usecase.SuggestFuseUseCase

object ServiceLocator {

    private var runs: RunRepository? = null
    private var preferences: SettingsRepository? = null

    val calculateDrop = CalculateDropUseCase()
    val recommendWire = RecommendWireUseCase()
    val suggestFuse = SuggestFuseUseCase()
    val powerBudget = PowerBudgetUseCase()
    val diagramDrop = DiagramDropUseCase()

    val viewModelFactory = AppViewModelFactory()

    val runRepository: RunRepository
        get() = requireNotNull(runs) { INIT_MESSAGE }

    val settingsRepository: SettingsRepository
        get() = requireNotNull(preferences) { INIT_MESSAGE }

    fun init(context: Context) {
        if (runs != null) return
        val store = AppPreferences(context.applicationContext)
        preferences = SettingsRepositoryImpl(store)
        runs = RunRepositoryImpl(store)
    }

    private const val INIT_MESSAGE = "ServiceLocator.init must run before use"
}
