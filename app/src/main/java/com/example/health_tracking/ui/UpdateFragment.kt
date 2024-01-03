package com.example.health_tracking.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.health_tracking.activityTracking.data.Exercise
import com.example.health_tracking.activityTracking.data.testActivityViewModel
import com.example.health_tracking.activityTracking.util.cropToBlob
import com.example.health_tracking.activityTracking.util.errorDialog
import com.example.health_tracking.activityTracking.util.toBitmap
import com.example.health_tracking.databinding.FragmentUpdateBinding
import java.text.SimpleDateFormat
import java.util.Locale

class UpdateFragment : Fragment() {

    private lateinit var binding: FragmentUpdateBinding
    private val nav by lazy { findNavController() }
    private val vm: testActivityViewModel by activityViewModels ()

    private val id by lazy { requireArguments().getString("index") ?: "" }
    private val formatter = SimpleDateFormat("dd MMMM yyyy '-' hh:mm:ss a", Locale.getDefault())

    private val launcher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                binding.imgPhoto.setImageURI(it.data?.data)
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUpdateBinding.inflate(inflater, container, false)

        reset()
        binding.imgPhoto.setOnClickListener { select() }
        binding.btnReset.setOnClickListener { reset() }
        binding.btnSubmit.setOnClickListener { submit() }
        binding.btnDelete.setOnClickListener { delete() }

        return binding.root
    }


    private fun select() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        launcher.launch(intent)
    }

    private fun reset() {
        // TODO: Load data
        val f = vm.get(id)
        if (f == null) {
            nav.navigateUp()
            return
        }
        load(f)

    }

    private fun load(e: Exercise) {
        binding.txtId.text = e.index
        binding.edtName.setText(e.exercise)
        binding.edtAge.setText(e.calories.toString())

        // TODO: Load photo and date
        binding.imgPhoto.setImageBitmap(e.photo.toBitmap())
        binding.txtDate.text = formatter.format(e.date)

        binding.edtName.requestFocus()
    }

    private fun submit() {
        // TODO: Update (set)
        val e = Exercise(
            index = id,
            exercise = binding.edtName.text.toString().trim(),
            calories = binding.edtAge.text.toString().toIntOrNull() ?: 0,
            photo = binding.imgPhoto.cropToBlob(300, 300)
        )

        val err = vm.validate(e, false)
        if (err != "") {
            errorDialog(err)
            return
        }
        vm.set(e)
        nav.navigateUp()
    }

    private fun delete() {
        // TODO: Delete

    }

}