package com.example.neostart.view

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.neostart.R
import com.example.neostart.databinding.ActivityCandidateBinding
import com.example.neostart.view.adapter.CandidateAdapter
import com.example.neostart.viewmodel.CandidateViewModel

class CandidateActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCandidateBinding
    private lateinit var candidateViewModel: CandidateViewModel
    private lateinit var candidateAdapter: CandidateAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCandidateBinding.inflate(layoutInflater)
        setContentView(binding.root)

        candidateViewModel = ViewModelProvider(this).get(CandidateViewModel::class.java)

        setupToolbar()
        setupRecyclerView()
        setupFab()
        observeViewModel()
    }

    override fun onResume() {
        super.onResume()
        getAllCandidates()
    }

    private fun getAllCandidates() {
        candidateViewModel.fetchAllCandidates().observe(this) { candidates ->
            candidateAdapter.submitList(candidates)
        }
    }

    private fun setupToolbar() {
        with(binding.inclCandidateAppToolbar) {
            toolbarTitle.text = getString(R.string.candidate_details)
        }
    }

    private fun setupRecyclerView() {
        candidateAdapter = CandidateAdapter()
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(this@CandidateActivity)
            adapter = candidateAdapter
        }
    }

    private fun setupFab() {
        binding.fabAddTask.setOnClickListener {
            addItem()
        }
    }

    private fun observeViewModel() {
        candidateViewModel.candidates.observe(this) { candidates ->
            candidateAdapter.submitList(candidates)
        }
    }

    private fun addItem() {
        val intent = Intent(this, RegistrationActivity::class.java)
        startActivity(intent)
    }
}
