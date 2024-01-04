package com.example.health_tracking.activityTracking.activityUI
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.health_tracking.activityTracking.data.Exercise
import com.example.health_tracking.activityTracking.data.ActivityViewModel
import com.example.health_tracking.activityTracking.util.cropToBlob
import com.example.health_tracking.activityTracking.util.errorDialog
import com.example.health_tracking.databinding.FragmentInsertBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch

class InsertFragment : Fragment() {

    private lateinit var binding: FragmentInsertBinding
    private val nav by lazy { findNavController() }
    private val vm: ActivityViewModel by activityViewModels ()

    private val launcher = registerForActivityResult(StartActivityForResult()) {
        if (it.resultCode == Activity.RESULT_OK) {
            binding.imgPhoto.setImageURI(it.data?.data)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentInsertBinding.inflate(inflater, container, false)

        reset()
        binding.imgPhoto.setOnClickListener { select() }
        binding.btnReset.setOnClickListener { reset() }
        binding.btnSubmit.setOnClickListener { submit() }

        return binding.root
    }

    private fun select() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        launcher.launch(intent)
    }

    private fun reset() {
        binding.edtId.text.clear()
        binding.edtName.text.clear()
        binding.edtAge.text.clear()
        binding.imgPhoto.setImageDrawable(null)
        binding.edtId.requestFocus()
    }

    private fun submit() {
        // TODO: Insert (set)
        val e = Exercise(
            index = binding.edtId.text.toString().trim().uppercase(),
            exercise = binding.edtName.text.toString().trim(),
            calories = binding.edtAge.text.toString().toIntOrNull() ?: 0,
            photo = binding.imgPhoto.cropToBlob(300,300),
        )


        Firebase.firestore
            .collection("Exercises")
            .document(e.index)
            .set(e)

        lifecycleScope.launch {
            val err = vm.validate(e)
            if(err != ""){
                errorDialog(err)
                return@launch
            }
            vm.set(e)
            nav.navigateUp()
        }
    }

}