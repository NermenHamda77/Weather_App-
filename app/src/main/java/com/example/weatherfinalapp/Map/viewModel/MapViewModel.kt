package com.example.weatherfinalapp.Map.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherfinalapp.model.FavoriteLocation
import com.example.weatherfinalapp.model.WeatherRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MapViewModel(private val _repository : WeatherRepository) : ViewModel() {
    fun addLocationToFav(location: FavoriteLocation){
        viewModelScope.launch(Dispatchers.IO){
            _repository.insertLocation(location)
        }
    }
}

/*
 /*
    class allProductViewModel(private val repo: ProductsRepository) : ViewModel() {

    val _products: MutableStateFlow<ApiState> = MutableStateFlow(ApiState.Loading)

    init {
        getAllProducts()
    }

    fun insertProduct(product: Product) {
        viewModelScope.launch {
            repo.insertProduct(product)
        }
    }
    private fun getAllProducts() {
        viewModelScope.launch {
            repo.getAllProducts()
                .catch { e ->
                    _products.value = ApiState.Failure(e)
                }
                .collect { productsResponse ->
                    _products.value = ApiState.Success(productsResponse)
                }
        }
    }
}

     */
 */