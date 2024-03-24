package com.example.weatherapp.Home.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherfinalapp.model.ApiWeather
import com.example.weatherfinalapp.model.WeatherRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class HomeViewModel(private val repo: WeatherRepository) : ViewModel() {

   val _weather: MutableStateFlow<ApiWeather> = MutableStateFlow(ApiWeather.Loading)
/*   private val _weather = MutableLiveData<WeatherResponse>()

    val weatherList: LiveData<WeatherResponse> = _weather*/



        suspend fun getAllWeathers(lat: Double, lon: Double, lang: String, units: String) {
            viewModelScope.launch {
                repo.getWeather(lat, lon, units, lang)
                    .catch { e ->
                        _weather.value = ApiWeather.Failure(e)
                    }
                    .collect { weatherResponse ->
                        _weather.value = ApiWeather.Success(weatherResponse)
                    }
            }


    }
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



    /*suspend fun getAllProducts(lat: Double, lon: Double, lang: String, units: String) {
        viewModelScope.launch {
            try {
                val weather = repo.getWeather(lat, lon, units, lang)
                _weather.value = weather
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
    }*/
}

/*
class ViewModelProduct(private val productRepo: ProductRepo) : ViewModel() {

    //private val productRepo: ProductRepo = ProductRepoImp(ProductDatabase.getInstance(context).getProductDao())
    //private val apiService = RetrofitHelper.retrofit.create(ApiService::class.java)

    private val _productLists = MutableLiveData<List<Product>>()

    val productList: LiveData<List<Product>> = _productLists

    fun fetchProducts() {
        viewModelScope.launch {
            try {
                //val products = apiService.getAllProducts().products
                val products= productRepo.getAllProducts()
                _productLists.value = products
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun fetchFav() {
        viewModelScope.launch {
            try {
                val favoriteProducts = productRepo.getFavorite()
                _productLists.value = favoriteProducts
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

     fun addToFavorites(product: Product) {
        viewModelScope.launch {
            productRepo.addToFavorites(product)
        }
    }

     fun removeFromFavorites(product: Product) {
        viewModelScope.launch {
            productRepo.removeFromFavorites(product)
        }
    }


}
 */