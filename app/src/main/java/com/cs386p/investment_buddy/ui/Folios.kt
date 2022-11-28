package com.cs386p.investment_buddy.ui

import android.os.Bundle
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.cs386p.investment_buddy.MainViewModel
import com.cs386p.investment_buddy.databinding.ActivityFoliosBinding
import com.cs386p.investment_buddy.databinding.ContentFoliosBinding
import com.google.firebase.auth.FirebaseAuth

class Folios : AppCompatActivity() {
    private lateinit var binding: ContentFoliosBinding
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // inflate layout
        val activityFoliosBinding = ActivityFoliosBinding.inflate(layoutInflater)
        setContentView(activityFoliosBinding.root)
        binding = activityFoliosBinding.contentFolios

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        viewModel.fetchFoliosData(FirebaseAuth.getInstance().currentUser!!.uid)

        viewModel.observeFoliosData().observe(this){
            binding.foliosRecyclerView.layoutManager = LinearLayoutManager(this)
            binding.foliosRecyclerView.adapter = FoliosAdapter(it)
        }
    }

    private fun doFinish(){
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