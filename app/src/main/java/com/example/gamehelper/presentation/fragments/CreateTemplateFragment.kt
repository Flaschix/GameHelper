package com.example.gamehelper.presentation.fragments

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import com.example.gamehelper.R
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.gamehelper.databinding.FragmentCreateTemplateBinding
import com.example.gamehelper.domain.entity.Template
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.database.*

class CreateTemplateFragment : Fragment() {
    private var _binding: FragmentCreateTemplateBinding? = null
    private val binding: FragmentCreateTemplateBinding
        get() = _binding ?: throw RuntimeException("FragmentCreateTemplateBinding == null")

    private val dataBaseReference: DatabaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://gamehelper-af012-default-rtdb.firebaseio.com/")

    private val args by navArgs<CreateTemplateFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCreateTemplateBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setButton(args.mode)
//        Toast.makeText(context, "${args.template.name} ${args.template.playerCount} ${args.template.dice} ${args.template.timer} ${args.template.liners} ${args.template.table}", Toast.LENGTH_SHORT).show()
        setValues()
    }

    private fun correctData(name: String, count: Int): Boolean{
        if(name.isEmpty() || count < 1) return false
        return true
    }

    private fun setButton(arg: Int){
        var name: String
        var count: Int
        var dice: Boolean
        var timer: Boolean
        var liners: Boolean
        var table: Boolean

        when(arg){
            FROM_LIST -> {
                binding.btnCreate.setOnClickListener {
                    name = binding.ietGameName.text.toString()
                    count = binding.ietPlayerCount.text.toString().toInt()
                    dice = binding.swDice.isChecked
                    timer = binding.swTimer.isChecked
                    liners = binding.swLiners.isChecked
                    table = binding.swTable.isChecked
                    if(correctData(name, count)) addTemplate(name, count, dice, timer, liners, table)
                    else Toast.makeText(context, "${binding.swDice.isChecked}", Toast.LENGTH_SHORT).show()
                }
            }
            FROM_MAIN ->{
                binding.btnCreate.text = getString(R.string.btn_create_match)

                binding.btnCreate.setOnClickListener {
                    name = binding.ietGameName.text.toString()
                    count = binding.ietPlayerCount.text.toString().toInt()
                    dice = binding.swDice.isChecked
                    timer = binding.swTimer.isChecked
                    liners = binding.swLiners.isChecked
                    table = binding.swTable.isChecked

                    if(correctData(name, count)) { inputPlayers(Template(args.userLogin, name, count, dice, timer, liners, table)) }
                    else Toast.makeText(context, "${binding.swDice.isChecked}", Toast.LENGTH_SHORT).show()
                }


            }
        }

    }

    private fun inputPlayers(template: Template){
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_input_players)
        dialog.setCancelable(true)

        val btnInputNames = dialog.findViewById<Button>(R.id.btnInputNames)
        val ietNames = dialog.findViewById<TextInputEditText>(R.id.ietNames)

        btnInputNames.setOnClickListener {
            var str = ietNames.text.toString()
            val mass: List<String>
            if (str.isEmpty()) Toast.makeText(context, "Введите именна", Toast.LENGTH_SHORT).show()
            else {
                str = str.replace(" ", "")
                mass = str.split(',')
                if (mass.size != template.playerCount) Toast.makeText(context, "Количество имен не соответсвует количеству игроков", Toast.LENGTH_SHORT).show()
                else {
                    dialog.dismiss()
                    launchGameFragment(template, mass.toTypedArray())
                }
            }
        }

        dialog.show()
    }

    private fun addTemplate(name: String, count: Int, dice: Boolean, timer: Boolean, liners: Boolean, table: Boolean){
        dataBaseReference.child(TABLE_TEMPLATE).child(args.userLogin).addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                if(snapshot.hasChild(name)) {
                    Toast.makeText(context, "Данный шаблон уже существует", Toast.LENGTH_SHORT).show()
                }
                else {
//                    Toast.makeText(context, "Создано", Toast.LENGTH_SHORT).show()
                    dataBaseReference.child(TABLE_TEMPLATE).child(args.userLogin).child(name).child(
                        PLAYER_COUNT).setValue(count)
                    dataBaseReference.child(TABLE_TEMPLATE).child(args.userLogin).child(name).child(
                        DICE).setValue(dice)
                    dataBaseReference.child(TABLE_TEMPLATE).child(args.userLogin).child(name).child(
                        TIMER).setValue(timer)
                    dataBaseReference.child(TABLE_TEMPLATE).child(args.userLogin).child(name).child(
                        TABLE).setValue(table)
                    dataBaseReference.child(TABLE_TEMPLATE).child(args.userLogin).child(name).child(
                        LINERS).setValue(liners)
                    activity?.onBackPressed()
                }

            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }

    private fun setValues(){
        val template = args.template

        binding.ietGameName.setText(template.name)
        binding.ietPlayerCount.setText(template.playerCount.toString())
        binding.swDice.isChecked = template.dice
        binding.swTimer.isChecked = template.timer
        binding.swLiners.isChecked = template.liners
        binding.swTable.isChecked = template.table
    }

    private fun launchGameFragment(template: Template, names: Array<String>){
        findNavController().navigate(CreateTemplateFragmentDirections.actionCreateTemplateFragmentToGameFragment(template, names))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object{
        private const val TABLE_TEMPLATE = "Templates"
        private const val DICE = "Dice"
        private const val LINERS = "Liners"
        private const val TABLE = "Table"
        private const val TIMER = "Timer"
        private const val PLAYER_COUNT = "PlayerCount"

        const val FROM_LIST = 0
        const val FROM_MAIN = 1
    }
}