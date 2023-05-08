package com.example.gamehelper.presentation.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.gamehelper.R
import com.example.gamehelper.databinding.SignInScreenFragmentBinding
import com.example.gamehelper.domain.entity.Game
import com.example.gamehelper.domain.entity.Match
import com.example.gamehelper.domain.entity.Player
import com.example.gamehelper.domain.entity.User
import com.google.firebase.database.*

class SignInFragment : Fragment() {

    private var _binding: SignInScreenFragmentBinding? = null
    private val binding: SignInScreenFragmentBinding
        get() = _binding ?: throw RuntimeException("SignInScreenFragmentBinding == null")

    private val dataBaseReference: DatabaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://gamehelper-af012-default-rtdb.firebaseio.com/")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = SignInScreenFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnSignUp.setOnClickListener {
//            launchSingUpFragment()
            val gameId = dataBaseReference.push().key!!
            val match = Match("flash", "Покер", 4, "Lucy", arrayListOf(Player("Lucy", 5.0), Player("Gleb", 2.0)), gameId, "Дважды два")

            dataBaseReference.child("Matches").child(match.user_login).child(match.id).setValue(match)
//            val game = Game("Карты", 0, 0, 0.0, 0, 0)
//            dataBaseReference.child("Games").child("flash").child(game.game_name).setValue(game)
        }

        binding.btnSignIn.setOnClickListener {



            val login = binding.ietLogin.text.toString()
            val password = binding.ietPassword.text.toString()

            if (correctInput(login, password)) {
                loginingUser(login, password)
            }
            else Toast.makeText(context, "Введите коректные данные", Toast.LENGTH_SHORT).show()

        }
    }

    private fun launchSingUpFragment(){
        findNavController().navigate(R.id.action_signInFragment_to_signUpFragment)
    }

    private fun launchNetworkErrorFragment(){
        findNavController().navigate(R.id.action_signInFragment_to_networkErrorFragment)
    }

    private fun launchMainScreenFragment(login: String, mail: String, password: String){
        findNavController().navigate(SignInFragmentDirections.actionSignInFragmentToMainScreenFragment2(
                User(login, mail, password)
            )
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun loginingUser(login: String, password: String){

        dataBaseReference.child(TABLE_USERS).addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                if(!snapshot.hasChild(login) || snapshot.child(login).child(PASSWORD).value.toString() != password) {
                    Toast.makeText(context, "Вы ввели некоректные данные", Toast.LENGTH_SHORT).show()
                }
                else {
//                    Toast.makeText(context, "${snapshot.child(login).child(MAIL).value.toString()}", Toast.LENGTH_SHORT).show()
                    launchMainScreenFragment(login, snapshot.child(login).child(MAIL).value.toString(), password)
                }

            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }

    private fun correctInput(login: String, password: String): Boolean{
        if(login.isEmpty() || password.isEmpty()) return false
        return true
    }


    companion object{
        private const val TABLE_USERS = "Users"
        private const val MAIL = "mail"
        private const val PASSWORD = "password"
    }
}