package com.example.chattingapp.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.chattingapp.MyViewModel
import com.example.chattingapp.R
import com.example.chattingapp.databinding.FragmentLoginPageBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth


class LoginPage : Fragment() {
    private var _binding: FragmentLoginPageBinding? = null
    private val binding get() = _binding!!
    private lateinit var auth: FirebaseAuth
    private val navArgs by navArgs<LoginPageArgs>()
    private val myViewModel by lazy { ViewModelProvider(this)[MyViewModel::class.java] }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginPageBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = Firebase.auth
        binding.etUserId.setText(navArgs.email)
        binding.btnCreateAccount.setOnClickListener {
            findNavController().navigate(R.id.action_loginPage_to_registerNamePage)
        }

        binding.txtForgotPassword.setOnClickListener {
            if (myViewModel.checkEmpty(
                    binding.etUserId.text.toString(),
                    context = requireContext()
                )
            ) {
                auth.sendPasswordResetEmail(binding.etUserId.text.toString())
                    .addOnCompleteListener {
                        if (it.isSuccessful) {
                            myViewModel.toast(
                                "Password reset link is successfully send to your registered Email",
                                requireContext()
                            )
                        } else {
                            Toast.makeText(
                                requireContext(),
                                it.exception?.message.toString(),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
            }
        }
        binding.btnLogin.setOnClickListener {
            if (myViewModel.checkEmpty(
                    binding.etUserId.text.toString(),
                    binding.etPassword.text.toString(),
                    requireContext()
                )
            ) {
                auth.signInWithEmailAndPassword(
                    binding.etUserId.text.toString(),
                    binding.etPassword.text.toString()
                ).addOnCompleteListener {
                    if (it.isSuccessful) {
                        myViewModel.toast("Login Successfully", requireContext())
                        findNavController().navigate(R.id.action_loginPage_to_homePage)
                    } else {
                        Toast.makeText(
                            requireContext(),
                            it.exception?.message.toString(),
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    }
                }
            }
        }
    }
}