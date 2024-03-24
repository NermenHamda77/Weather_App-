package com.example.weatherfinalapp.model

import com.example.weatherfinalapp.Network.WeatherRemoteDataSourceImp
import com.example.weatherfinalapp.db.WeatherLocalDataSourceImp
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class WeatherRepositoryImp private constructor(
    private val remoteSource: WeatherRemoteDataSourceImp,
   private val localDataSource: WeatherLocalDataSourceImp
) : WeatherRepository {

    companion object {
        private var repo: WeatherRepositoryImp? = null

        fun getInstance(
            remoteSource: WeatherRemoteDataSourceImp,
            localDataSource: WeatherLocalDataSourceImp
        ): WeatherRepositoryImp {
            if (repo == null) {
                repo = WeatherRepositoryImp(remoteSource , localDataSource )
            }
            return repo!!
        }
    }

    override fun getStoredFavLocations(): Flow<List<FavoriteLocation>> {
        return localDataSource.getAllStoredFavLocations()

    }

    override suspend fun insertLocation(location: FavoriteLocation) {
       localDataSource.addLocation(location)
    }

    override suspend fun deleteLocation(location: FavoriteLocation) {
        localDataSource.removeLocation(location)
    }


    override suspend fun getWeather(
        lat: Double,
        lon: Double,
        units: String?,
        lang: String?,
        appid: String?
    ): Flow<WeatherResponse> {
            return flowOf(remoteSource.makeNetworkCall(lat, lon, units, lang, appid))
       // return remoteSource.makeNetworkCall(lat, lon, units, lang, appid)

    }

    override suspend fun getWeatherOfCity(city: String, appid: String?): Flow<WeatherResponse> {
        return flowOf(remoteSource.makeNetworkCallCity(city , appid))
    }


}

/*
class ProductsRepositoryImp private constructor(
    private val remoteSource: ProductsRemoteDataSourceImp,
    private val localDataSource: ProductsLocalDataSourceImp
) : ProductsRepository {

    companion object {
        private var repo: ProductsRepositoryImp? = null

        fun getInstance(
            remoteSource: ProductsRemoteDataSourceImp,
            localDataSource: ProductsLocalDataSourceImp
        ): ProductsRepositoryImp {
            if (repo == null) {
                repo = ProductsRepositoryImp(remoteSource, localDataSource)
            }
            return repo!!
        }
    }

     override suspend fun insertProduct(product: Product) {
         localDataSource.insert(product)
     }

     override suspend fun deleteProduct(product: Product) {
        localDataSource.delete(product)
     }

    override fun getStoredProducts(): Flow<List<Product>> {
        return localDataSource.getAllStoredProducts()
    }
    override suspend fun getAllProducts(): Flow<ProductResponse> {
        return flowOf(remoteSource.makeNetworkCall())
    }

}

 */


/*
private constructor(
    private val remoteSource: ProductsRemoteDataSourceImp,
    private val localDataSource: ProductsLocalDataSourceImp
) : ProductsRepository {

    companion object {
        private var repo: ProductsRepositoryImp? = null

        fun getInstance(
            remoteSource: ProductsRemoteDataSourceImp,
            localDataSource: ProductsLocalDataSourceImp
        ): ProductsRepositoryImp {
            if (repo == null) {
                repo = ProductsRepositoryImp(remoteSource, localDataSource)
            }
            return repo!!
        }
    }

     override suspend fun insertProduct(product: Product) {
         localDataSource.insert(product)
     }

     override suspend fun deleteProduct(product: Product) {
        localDataSource.delete(product)
     }

    override fun getStoredProducts(): Flow<List<Product>> {
        return localDataSource.getAllStoredProducts()
    }
    override suspend fun getAllProducts(): Flow<ProductResponse> {
        return flowOf(remoteSource.makeNetworkCall())
    }
 */