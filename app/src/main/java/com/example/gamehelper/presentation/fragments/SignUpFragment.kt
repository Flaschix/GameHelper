package com.example.gamehelper.presentation.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.gamehelper.R
import com.example.gamehelper.databinding.SignUpScreenFragmentBinding
import com.google.firebase.database.*

class SignUpFragment : Fragment() {

    private var _binding: SignUpScreenFragmentBinding? = null
    private val binding: SignUpScreenFragmentBinding
        get() = _binding ?: throw RuntimeException("SignUpScreenFragmentBinding == null")

    private val dataBaseReference: DatabaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://gamehelper-af012-default-rtdb.firebaseio.com/")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = SignUpScreenFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnSignIn.setOnClickListener {
            launchSingInFragment()
        }

        binding.btnSignUp.setOnClickListener {
            val login = binding.ietLogin.text.toString()
            val mail = binding.ietMail.text.toString()
            val password = binding.ietPassword.text.toString()

            if (correctInput(login, mail, password)) {
                registrationUser(login, mail, password)
            }
            else Toast.makeText(context, "Введите коректные данные", Toast.LENGTH_SHORT).show()
        }
    }

    private fun launchSingInFragment(){
        findNavController().navigate(R.id.action_signUpFragment_to_signInFragment)
    }

    private fun registrationUser(login: String, mail: String, password: String){
        dataBaseReference.child(TABLE_USERS).addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {

                if(snapshot.hasChild(login)) Toast.makeText(context, "Пользователь уже существует", Toast.LENGTH_SHORT).show()
                else {
                    dataBaseReference.child(TABLE_USERS).child(login).child(MAIL).setValue(mail)
                    dataBaseReference.child(TABLE_USERS).child(login).child(PASSWORD).setValue(password)
                    launchSingInFragment()
                }

            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }

    private fun correctInput(login: String, mail: String, password: String): Boolean{
        if(login.isEmpty() || mail.isEmpty() || password.isEmpty()) return false
        return true
    }



    private fun checkBd(login: String, tv: TextView){
        dataBaseReference.child(TABLE_USERS).addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {

                tv.text = "${snapshot.child(login).value}"

            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object{
        private const val TABLE_USERS = "Users"
        private const val MAIL = "mail"
        private const val PASSWORD = "password"
    }
}