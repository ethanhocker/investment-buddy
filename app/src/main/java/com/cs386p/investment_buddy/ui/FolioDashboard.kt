package com.cs386p.investment_buddy.ui

import android.os.Bundle
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.cs386p.investment_buddy.MainViewModel
import com.cs386p.investment_buddy.databinding.ActivityFoliodashboardBinding
import com.cs386p.investment_buddy.databinding.ContentFoliodashboardBinding
import com.google.firebase.auth.FirebaseAuth

class FolioDashboard : AppCompatActivity() {
    private lateinit var binding: ContentFoliodashboardBinding
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // inflate layout
        val activityFolioDashboardBinding = ActivityFoliodashboardBinding.inflate(layoutInflater)
        setContentView(activityFolioDashboardBinding.root)
        binding = activityFolioDashboardBinding.contentFolioDashboard

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        viewModel.fetchHoldingsData(FirebaseAuth.getInstance().currentUser!!.uid)

        viewModel.observeHoldingsData().observe(this) {
            binding.folioDashboardRecyclerView.layoutManager = LinearLayoutManager(this)
            binding.folioDashboardRecyclerView.adapter = FolioDashboardAdapter(it)
        }
    }

    private fun doFinish() {
        finish()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item.itemId

        return if (id == android.R.id.home) {
            // If user clicks "up", then it it as if they clicked OK.  We commit
            // changes and return to parent
            finish()
            true
        } else super.onOptionsItemSelected(item)
    }
}