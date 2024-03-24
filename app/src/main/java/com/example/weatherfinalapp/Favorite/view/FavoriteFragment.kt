package com.example.weatherfinalapp.Favorite.view

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherfinalapp.Favorite.viewModel.FavLocationViewModel
import com.example.weatherfinalapp.Favorite.viewModel.FavLocationViewModelFactory
import com.example.weatherfinalapp.LocationDetailsScreen.view.LocationDetailsActivity
import com.example.weatherfinalapp.Map.view.MapActivity
import com.example.weatherfinalapp.Network.WeatherRemoteDataSourceImp
import com.example.weatherfinalapp.R
import com.example.weatherfinalapp.db.WeatherLocalDataSourceImp
import com.example.weatherfinalapp.model.FavoriteLocation
import com.example.weatherfinalapp.model.WeatherRepositoryImp
import com.google.android.material.floatingactionbutton.FloatingActionButton

class FavoriteFragment : Fragment(), OnFavClickListener {

    private lateinit var fabBtn : FloatingActionButton
    private lateinit var factory: FavLocationViewModelFactory
    private lateinit var viewModel: FavLocationViewModel
    private lateinit var recyclerView: RecyclerView
   // private lateinit var favoriteAdapter: FavoriteAdapter
   private lateinit var favoriteAdapter: FavoriteAdapter1
    private lateinit var layoutManager: LinearLayoutManager



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_favorite, container, false)
        fabBtn = view.findViewById(R.id.fab)
        recyclerView = view.findViewById(R.id.rv_fav_locations_fav_list)



        layoutManager = LinearLayoutManager(requireContext())
        layoutManager.orientation = RecyclerView.VERTICAL
        favoriteAdapter = FavoriteAdapter1(requireContext() , this)
        recyclerView.adapter = favoriteAdapter
        recyclerView.layoutManager = layoutManager

        factory = FavLocationViewModelFactory(
            WeatherRepositoryImp.getInstance(
            WeatherRemoteDataSourceImp.getInstance(),
            WeatherLocalDataSourceImp.getInstance(requireContext())
        ))

        viewModel = ViewModelProvider(this , factory).get(FavLocationViewModel::class.java)


        viewModel.location.observe(requireActivity()){ location ->

            if(location != null){
                if(location.size == 0){
                    recyclerView.visibility = View.GONE
                }
                else
                {
                    recyclerView.visibility = View.VISIBLE
                    favoriteAdapter.submitList(location)
                }
            }
        }

        fabBtn.setOnClickListener {
            startActivity(Intent(requireContext(), MapActivity::class.java))
        }

        return view
    }

    override fun deleteLocation(location: FavoriteLocation) {
        viewModel.removeLocationFromFav(location)
    }

    override fun showFavLocationDetails(location: FavoriteLocation) {
        val intent = Intent(requireContext(), LocationDetailsActivity::class.java)
        Toast.makeText(requireContext(),"from favorite location: $location", Toast.LENGTH_SHORT).show()

        intent.putExtra("favorite_location", location)  // Assuming FavoriteLocation is Serializable or Parcelable
        startActivity(intent)
    }


}