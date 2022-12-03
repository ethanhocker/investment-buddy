package com.cs386p.investment_buddy.ui

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.cs386p.investment_buddy.MainViewModel
import com.cs386p.investment_buddy.collections.HoldingsData
import com.cs386p.investment_buddy.databinding.ActivityFoliodashboardBinding
import com.cs386p.investment_buddy.databinding.ContentFoliodashboardBinding
import com.google.firebase.auth.FirebaseAuth
import java.math.RoundingMode
import java.text.DecimalFormat

class FolioDashboard : AppCompatActivity() {
    private lateinit var binding: ContentFoliodashboardBinding
    private val viewModel: MainViewModel by viewModels()
    private val adapter = FolioDashboardAdapter()
    private var localHoldingDataList = mutableListOf<HoldingsData>()
    private var totalBalance = 0.00
    private var unRealGains = 0.00
    private var totalROR = 0.00
    var folioPortNum = ""

    // when activity is resumed refetch favorites
    override fun onResume() {
        super.onResume()
        MainViewModel.fetchHoldingsData(FirebaseAuth.getInstance().currentUser!!.uid, folioPortNum)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // inflate layout
        val activityFolioDashboardBinding = ActivityFoliodashboardBinding.inflate(layoutInflater)
        setContentView(activityFolioDashboardBinding.root)
        binding = activityFolioDashboardBinding.contentFolioDashboard

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val layoutManager = LinearLayoutManager(this)
        binding.folioDashboardRecyclerView.layoutManager = layoutManager
        binding.folioDashboardRecyclerView.adapter = adapter

        val df = DecimalFormat("#.##")
        df.roundingMode = RoundingMode.HALF_UP

        val folioName = intent.getStringExtra("folioName").toString()
        folioPortNum = intent.getStringExtra("folioPortNum").toString()
        val folioStartingBalance = intent.getStringExtra("folioStartingBalance")!!.toDouble()
        val folioAAB = intent.getStringExtra("folioAAB")!!.toDouble()

        binding.folioNameFD.text = folioName
        binding.totalBalance.text = df.format(folioAAB).toString()
        unRealGains = 0.00
        totalROR = 0.00
        binding.unrealizedGain.text = df.format(unRealGains).toString()

        MainViewModel.fetchHoldingsData(FirebaseAuth.getInstance().currentUser!!.uid,folioPortNum)

        viewModel.observeHoldingsData().observe(this) {
            val results = viewModel.observeHoldingsData().value!!
            for (result in results){
                viewModel.finnhubQuoteRequest(result.stock_ticker)
            }
        }

        viewModel.observeFinnhubQuoteResults().observe(this){
            val quote = viewModel.observeFinnhubQuoteResults().value
            val holdingDataList = viewModel.observeHoldingsData().value
            if (holdingDataList != null && quote != null) {
                for (holdingData in holdingDataList){
                    if (holdingData.stock_ticker == quote.symbol){
                        holdingData.current_price = quote.currentPrice
                        val tempHolding = (localHoldingDataList.find { it.stock_ticker == holdingData.stock_ticker})
                        if (tempHolding != null){
                            val indexToReplace = localHoldingDataList.indexOf(tempHolding)
                            localHoldingDataList[indexToReplace] = holdingData
                        }
                        else{
                            localHoldingDataList.add(holdingData)
                        }
                    }
                }
            }
            adapter.submitList(localHoldingDataList)
            adapter.notifyDataSetChanged()

            totalBalance = 0.00

            for(holdingData in localHoldingDataList){
                val holdingWorth = holdingData.current_price.toDouble() * holdingData.units
                totalBalance += holdingWorth
            }

            totalBalance += folioAAB
            binding.totalBalance.text = df.format(totalBalance).toString()

            unRealGains = totalBalance - folioStartingBalance
            binding.unrealizedGain.text = df.format(unRealGains).toString()

            totalROR = unRealGains / folioStartingBalance * 100
            binding.totalReturnRate.text = df.format(totalROR).toString()

            if(unRealGains > 0)
            {
                binding.unrealizedGain.setTextColor(Color.GREEN)
            }
            else if (unRealGains < 0)
            {
                binding.unrealizedGain.setTextColor(Color.RED)
            }
            else
            {
                binding.unrealizedGain.setTextColor(Color.WHITE)
            }

            if(totalROR > 0)
            {
                binding.totalReturnRate.setTextColor(Color.GREEN)
                binding.totalReturnRateUnit.setTextColor(Color.GREEN)
            }
            else if (unRealGains < 0)
            {
                binding.totalReturnRate.setTextColor(Color.RED)
                binding.totalReturnRateUnit.setTextColor(Color.RED)
            }
            else
            {
                binding.totalReturnRate.setTextColor(Color.WHITE)
                binding.totalReturnRateUnit.setTextColor(Color.WHITE)
            }
        }

        binding.buyButtonFD.setOnClickListener(){
            val stockSearchClass = StockSearch()
            val intent = Intent(this, stockSearchClass::class.java)
            this.startActivity(intent)
        }
    }

    private fun doFinish() {
        localHoldingDataList.clear()
        adapter.submitList(localHoldingDataList)
        adapter.notifyDataSetChanged()
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
            doFinish()
            true
        } else super.onOptionsItemSelected(item)
    }
}