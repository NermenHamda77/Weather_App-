package com.example.weatherfinalapp.Favorite.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherfinalapp.model.ApiWeather
import com.example.weatherfinalapp.model.FavoriteLocation
import com.example.weatherfinalapp.model.WeatherRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class FavLocationViewModel(private val _repository: WeatherRepository) : ViewModel() {
    private var _location: MutableLiveData<List<FavoriteLocation>> =
        MutableLiveData<List<FavoriteLocation>>()
    val location: LiveData<List<FavoriteLocation>> get() = _location

    init {
        getLocalLocations()
    }


    fun removeLocationFromFav(location: FavoriteLocation) {
        viewModelScope.launch(Dispatchers.IO) {
            _repository.deleteLocation(location)
            getLocalLocations()
        }
    }

    fun getLocalLocations() {
        viewModelScope.launch(Dispatchers.IO) {
            _repository.getStoredFavLocations().collect {
                _location.postValue(it)
            }
        }

    }
}



/*  suspend fun getAllWeathers(lat: Double, lon: Double, lang: String, units: String) {
        viewModelScope.launch {
            _repository.getWeather(lat, lon, units, lang)
                .catch { e ->
                    _weather.value = ApiWeather.Failure(e)
                }
                .collect { weatherResponse ->
                    _weather.value = ApiWeather.Success(weatherResponse)
                }
        }
    }*/





/*
class FavProductViewModel(private val repo: ProductsRepository) : ViewModel() {
    private val _products: MutableLiveData<List<Product>> = MutableLiveData()
    val products: LiveData<List<Product>> get() = _products

    init {
        getLocalProducts()
    }

    fun deleteProduct(product: Product) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.deleteProduct(product)
            getLocalProducts()
        }
    }

    private fun getLocalProducts() {
        viewModelScope.launch(Dispatchers.IO) {
            repo.getStoredProducts().collect {
                _products.postValue(it)
            }
        }
    }
}

 */

/*
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mvvmproduct.model.Product
import com.example.mvvmproduct.model.ProductRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FavProductViewModel(private val _irepo : ProductRepository) : ViewModel() {

    private var _products : MutableLiveData<List<Product>> = MutableLiveData<List<Product>>()
    val products : LiveData<List<Product>> = _products

    init {
        getLocalProducts()
    }

    fun deleteProduct(product: Product){
        viewModelScope.launch(Dispatchers.IO){
            _irepo.deleteProduct(product)
            getLocalProducts()
        }


    }

    fun getLocalProducts(){
        viewModelScope.launch(Dispatchers.IO){
           _products.postValue(_irepo.getStoredProducts())
        }
    }
}


// PASS REPO IN CONSTRUCTOR OF viewModel:

/*  I told it when you want to create object from viewModel pass any implementation for repo to viewModel "in constructor"
"i don't care what is the implementation all i care is to pass any implementation"
 */

/* i don't define the repo in the body of view model, because it is not the responsibility of viewModel to create the repo
viewModel needs it to work, so implement it and pass it to viewModel
 */
///////////////////////////////////////////////////////////////////////////////////////////////////////////
// IF I DEFINE REPO IN BODY "I HAVE TO PROBLEMS"
/*
1. I told viewModel to do extra task : to create the repo -> "and it is not its responsibility"

 */



 */