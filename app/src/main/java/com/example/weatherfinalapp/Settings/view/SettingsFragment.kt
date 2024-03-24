package com.example.weatherfinalapp.Settings.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.RadioGroup
import com.example.weatherfinalapp.R


class SettingsFragment : Fragment() {
    private lateinit var gpsBtn : RadioButton
    private lateinit var mapBtn : RadioButton
    private lateinit var englishBtn : RadioButton
    private lateinit var arabicBtn : RadioButton
    private lateinit var celsiusBtn : RadioButton
    private lateinit var kelvinBtn : RadioButton
    private lateinit var fahrenheitBtn : RadioButton
    private lateinit var meterBtn : RadioButton
    private lateinit var mileBtn : RadioButton
    private lateinit var enableBtn : RadioButton
    private lateinit var disableBtn : RadioButton




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_settings, container, false)

        gpsBtn = view.findViewById(R.id.rbtn_gps_option)
        mapBtn = view.findViewById(R.id.rbtn_map_option)
        englishBtn = view.findViewById(R.id.rbtn_english_option)
        arabicBtn = view.findViewById(R.id.rbtn_arabic_option)
        celsiusBtn = view.findViewById(R.id.rbtn_celsius_option)
        kelvinBtn = view.findViewById(R.id.rbtn_kelvin_option)
        fahrenheitBtn = view.findViewById(R.id.rbtn_fahrenheit_option)
        meterBtn = view.findViewById(R.id.rbtn_meter_sec_option)
        mileBtn = view.findViewById(R.id.rbtn_mile_hr_option)
        enableBtn = view.findViewById(R.id.rbtn_enable_option)
        disableBtn = view.findViewById(R.id.rbtn_disable_option)


        gpsBtn.setOnClickListener(View.OnClickListener {

        })

        mapBtn.setOnClickListener(View.OnClickListener {

        })

        englishBtn.setOnClickListener(View.OnClickListener {

        })

        arabicBtn.setOnClickListener(View.OnClickListener {

        })

        celsiusBtn.setOnClickListener(View.OnClickListener {

        })

        kelvinBtn.setOnClickListener(View.OnClickListener {

        })

        fahrenheitBtn.setOnClickListener(View.OnClickListener {

        })

        meterBtn.setOnClickListener(View.OnClickListener {

        })

        mileBtn.setOnClickListener(View.OnClickListener {

        })

        enableBtn.setOnClickListener(View.OnClickListener {

        })

        disableBtn.setOnClickListener(View.OnClickListener {

        })



        return view
    }


}