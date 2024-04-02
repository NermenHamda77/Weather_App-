package com.example.weatherfinalapp.Settings.view

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.RadioGroup
import com.example.weatherfinalapp.R


class SettingsFragment : Fragment() {


    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var sharedPreferencesManager: SharedPreferencesManager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPreferences = requireActivity().getSharedPreferences("Settings", Context.MODE_PRIVATE)
        sharedPreferencesManager = SharedPreferencesManager(requireContext())

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_settings, container, false)

        val currentUnit = sharedPreferences.getString("unit", "metric")

        val currentLanguage = sharedPreferences.getString("lang", "en")


        val radioGroup = view.findViewById<RadioGroup>(R.id.radioGroupUnit)
        when (currentUnit) {
            "metric" -> radioGroup.check(R.id.radioButtonMetric)
            "imperial" -> radioGroup.check(R.id.radioButtonImperial)
            "standard" -> radioGroup.check(R.id.radioButtonStandard)
        }

        radioGroup.setOnCheckedChangeListener { _, checkedId ->
            val radioButton = view.findViewById<RadioButton>(checkedId)
            val selectedUnit = when (radioButton.id) {
            R.id.radioButtonMetric -> "metric"
            R.id.radioButtonImperial -> "imperial"
            R.id.radioButtonStandard -> "standard"
            else -> "metric"
        }
            with(sharedPreferences.edit()) {
                putString("unit", selectedUnit)
                apply()
            }
            sharedPreferencesManager.saveSelectedUnit(selectedUnit)
        }


        ///Language
        val radioGroupLanguage = view.findViewById<RadioGroup>(R.id.radioGroupLanguage)
        when (currentLanguage) {
            "en" -> radioGroupLanguage.check(R.id.radioButtonEnglish)
            "ar"->radioGroupLanguage.check(R.id.radioButtonArabic)

        }

        radioGroupLanguage.setOnCheckedChangeListener { _, checkedId ->
            val radioButton = view.findViewById<RadioButton>(checkedId)
            val selectedLanguage = when (radioButton.id) {
                R.id.radioButtonEnglish -> "en"
                R.id.radioButtonArabic -> "ar"
                else -> "en"
            }
            with(sharedPreferences.edit()) {
                putString("lang", selectedLanguage)
                apply()
            }
            sharedPreferencesManager.saveSelectedLanguage(selectedLanguage)
            LanguageUtils.updateLanguage(requireContext(), selectedLanguage)
        }


        ///  wind
        val currentWind = sharedPreferences.getString("wind", "meter_per_sec")

        val radioGroupWind = view.findViewById<RadioGroup>(R.id.met_sec)
        when (currentWind) {
            "meter_per_sec" -> radioGroupWind.check(R.id.rbtn_meter_sec_option)
            "mile_per_hr" -> radioGroupWind.check(R.id.rbtn_mile_hr_option)
        }

        radioGroupWind.setOnCheckedChangeListener { _, checkedId ->
            val radioButton = view.findViewById<RadioButton>(checkedId)
            val selectedWind = when (radioButton.id) {
                R.id.rbtn_meter_sec_option -> "meter_per_sec"
                R.id.rbtn_mile_hr_option -> "mile_per_hr"
                else -> "meter_per_sec"
            }
            with(sharedPreferences.edit()) {
                putString("wind", selectedWind)
                apply()
            }
            sharedPreferencesManager.saveSelectedWindUnit(selectedWind)
        }



      // Location



        return view
    }


}