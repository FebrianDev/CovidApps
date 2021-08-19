package com.febrian.covidapp.info

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.febrian.covidapp.databinding.ActivityDetailListInfoBinding
import com.febrian.covidapp.info.ListInfoAdapter.Companion.KEY_DESCRIPTION
import com.febrian.covidapp.info.ListInfoAdapter.Companion.KEY_IMAGE_INFO
import com.febrian.covidapp.info.ListInfoAdapter.Companion.KEY_TITLE_INFO

class DetailListInfoActivity : AppCompatActivity() {

    private lateinit var binding : ActivityDetailListInfoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailListInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val titleInfo = intent.getStringExtra(KEY_TITLE_INFO)
        val imageInfo = intent.getStringExtra(KEY_IMAGE_INFO)
        val descinfo = intent.getStringExtra(KEY_DESCRIPTION)
        binding.titleDetail.text = titleInfo
        binding.textDetail.text = descinfo
        Glide.with(applicationContext).load(imageInfo).into(binding.image)
    }
}