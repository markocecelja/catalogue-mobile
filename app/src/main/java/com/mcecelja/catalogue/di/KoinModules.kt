package com.mcecelja.catalogue.di

import com.mcecelja.catalogue.ui.LoadingViewModel
import com.mcecelja.catalogue.ui.organization.details.MapsViewModel
import com.mcecelja.catalogue.location.LocationLiveData
import com.mcecelja.catalogue.ui.organization.details.OrganizationDetailsViewModel
import com.mcecelja.catalogue.ui.catalogue.CatalogueViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    factory { LocationLiveData(androidContext()) }
}

val viewModelModule = module {
    viewModel { LoadingViewModel() }
    viewModel { CatalogueViewModel() }
    viewModel { OrganizationDetailsViewModel() }
    viewModel { MapsViewModel(get()) }
}