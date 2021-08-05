package com.febrian.covidapp.info

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.febrian.covidapp.databinding.FragmentInfoBinding
import com.febrian.covidapp.info.dropdown.CovidTestActivity
import com.febrian.covidapp.info.dropdown.GeneralActivity
import com.febrian.covidapp.info.dropdown.PeopleAtRiskActivity
import com.febrian.covidapp.info.dropdown.VaccinesActivity
import com.febrian.covidapp.info.list.ExerciseActivity
import com.febrian.covidapp.info.list.FoodActivity
import com.febrian.covidapp.info.list.PreventionActivity
import com.febrian.covidapp.info.list.SymptomsActivity

class InfoFragment : Fragment() {

    private lateinit var binding : FragmentInfoBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentInfoBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.general.setOnClickListener {
            val intent = Intent(view.context, GeneralActivity::class.java)
            startActivity(intent)
        }

        binding.vaccines.setOnClickListener {
            val intent = Intent(view.context, VaccinesActivity::class.java)
            startActivity(intent)
        }

        binding.peopleAtRisk.setOnClickListener {
            val intent = Intent(view.context, PeopleAtRiskActivity::class.java)
            startActivity(intent)
        }

        binding.covidTest.setOnClickListener {
            val intent = Intent(view.context, CovidTestActivity::class.java)
            startActivity(intent)
        }

        binding.prevention.setOnClickListener {
            val intent = Intent(view.context, PreventionActivity::class.java)
            startActivity(intent)
        }
        binding.symptoms.setOnClickListener {
            val intent = Intent(view.context, SymptomsActivity::class.java)
            startActivity(intent)
        }
        binding.food.setOnClickListener {
            val intent = Intent(view.context, FoodActivity::class.java)
            startActivity(intent)
        }
        binding.exercise.setOnClickListener {
            val intent = Intent(view.context, ExerciseActivity::class.java)
            startActivity(intent)
        }
    }


}