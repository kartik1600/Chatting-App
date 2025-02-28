package com.example.chattingapp.registername

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultRegistry
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.registerForActivityResult
import androidx.compose.runtime.Composable
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.chattingapp.MyViewModel
import com.example.chattingapp.UserData
import com.example.chattingapp.databinding.FragmentRegisterNamePageBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.database


class RegisterNamePage : Fragment() {
    var _binding: FragmentRegisterNamePageBinding? = null
    val binding get() = _binding!!
    lateinit var auth: FirebaseAuth
    var bitmap: Bitmap? = null
    private val myViewModel by lazy { ViewModelProvider(this)[MyViewModel::class.java] }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterNamePageBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = FirebaseAuth.getInstance()


        binding.viewUploadProfile.setOnClickListener {
            val pickImg = Intent(
                Intent.ACTION_PICK,
                MediaStore.Images.Media.INTERNAL_CONTENT_URI
            )
//            changeImage.launch(pickImg)
        }



        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.btnCreateAccount.setOnClickListener {
            if (myViewModel.checkEmpty(
                    binding.etUserId.text.toString(),
                    binding.etPassword.text.toString(),
                    requireContext()
                )
            )
                auth.createUserWithEmailAndPassword(
                    binding.etUserId.text.toString(),
                    binding.etPassword.text.toString()
                ).addOnCompleteListener {
                    if (it.isSuccessful) {
                        val user = FirebaseAuth.getInstance().currentUser
                        user?.run {
                            val userIdReference = Firebase.database.reference
                                .child("users").child(user.uid)
                            val userData = UserData(
                                name = binding.etUserName.text.toString(),
                                profile = bitmap.toString(),
                                uid = auth.currentUser?.uid.toString(),
                                email = auth.currentUser?.email.toString()
                            )
                            userIdReference.setValue(userData)
                        }
                        myViewModel.toast("Account Created Successfully", requireContext())
                        findNavController().navigate(
                            RegisterNamePageDirections.actionRegisterNamePageToLoginPage(
                                binding.etUserId.text.toString()
                            )
                        )
                    } else {
                        myViewModel.toast(it.exception?.message.toString(), requireContext())
                    }
                }
        }
    }
    }





//        registerForActivityResult(ActivityResultContracts.PickVisualMedia.ImageOnly) {
//            if (it.resultCode == RESULT_OK) {
//                val data = it.data
//                val imgUri = data?.data
//                bitmap = BitmapFactory.decodeFile(imgUri?.lastPathSegment.toString())
//                binding.viewUploadProfile.setImageURI(imgUri)
//            }






