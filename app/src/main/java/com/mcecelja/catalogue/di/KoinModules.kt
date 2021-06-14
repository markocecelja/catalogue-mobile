package com.mcecelja.catalogue.di

import com.mcecelja.catalogue.ui.LoadingViewModel
import com.mcecelja.catalogue.ui.product.ProductViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { LoadingViewModel() }
    viewModel { ProductViewModel() }
}