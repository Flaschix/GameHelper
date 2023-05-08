package com.example.gamehelper.presentation.fragments

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.os.SystemClock
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.*
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.example.gamehelper.R
import com.example.gamehelper.databinding.FragmentGameBinding
import com.example.gamehelper.domain.entity.Game
import com.example.gamehelper.domain.entity.Match
import com.example.gamehelper.domain.entity.Player
import com.example.gamehelper.presentation.ViewHolders.MainViewModel
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlin.math.max
import kotlin.math.min
import kotlin.math.roundToInt
import kotlin.random.Random


class GameFragment : Fragment() {

    private var _binding: FragmentGameBinding? = null
    private val binding: FragmentGameBinding
        get() = _binding ?: throw RuntimeException("FragmentGameBinding == null")

    private val dataBaseReference: DatabaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://gamehelper-af012-default-rtdb.firebaseio.com/")

    private lateinit var viewHolder: MainViewModel

    private val args by navArgs<GameFragmentArgs>()

    private val playerTable = mutableListOf<MutableList<EditText>>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGameBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnEndGame.setOnClickListener {
            saveOrNotMatch()
//            updateGamesTable()
        }

        binding.chmMainTimer.setOnChronometerTickListener {
            var time = SystemClock.elapsedRealtime() - it.base
            it.text = getTimeStringFromDouble(time.toDouble())
        }
        binding.chmMainTimer.start()

        setGame()

        viewHolder = ViewModelProvider(this)[MainViewModel::class.java]
        viewHolder.GetMatchesListUseCase(args.settings.user_login, args.settings.name)
        viewHolder.GetGamesListUseCase(args.settings.user_login)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setGame(){
        if(args.settings.dice) setDice()
        else binding.btnDice.isVisible = false

        if(args.settings.timer) setTimer()
        else binding.btnTimer.isVisible = false


        val table = binding.table
        if(args.settings.table) {
            createTable(table)

            binding.btnAddColl.setOnClickListener {
                addColumn(table)
            }
        }
        else {
            table.isVisible = false
            binding.btnAddColl.isVisible = false
        }

        val liners = binding.liners

        if(args.settings.liners) createLiners(liners)
        else liners.isVisible = false
    }

    private fun getTimeStringFromDouble(time: Double): String{
        var resultInt = time.roundToInt()
        val h = (time / 3600000).toInt()
        val m = (time - h * 3600000).toInt() / 60000
        val s = (time - h * 3600000 - m * 60000).toInt() / 1000

        return makeTimeString(h, m, s)
    }

    private fun makeTimeString(hours: Int, minutes: Int, seconds: Int) = String.format("%02d:%02d:%02d", hours, minutes, seconds)

    private fun setTimer(){
        binding.btnTimer.setOnClickListener {
            val dialog = Dialog(requireContext())
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setContentView(R.layout.dialog_timer)
            dialog.setCancelable(false)

            val btnStart = dialog.findViewById<Button>(R.id.btnStart)
            val btnReset = dialog.findViewById<Button>(R.id.btnReset)
            val btnStop = dialog.findViewById<Button>(R.id.btnStop)
            val btnClose = dialog.findViewById<ImageButton>(R.id.btnCloseDialog)
            val timer = dialog.findViewById<Chronometer>(R.id.chmTimer)
            val time = dialog.findViewById<EditText>(R.id.etnTime)

            var pauseAt: Long = 0
            timer.isCountDown = true
            var t: Long = 0

            btnClose.setOnClickListener {
                dialog.dismiss()
            }

            btnStart.setOnClickListener {
                t = if (time.text.isEmpty()) 0 else time.text.toString().toLong() * 1000
                timer.base = t + SystemClock.elapsedRealtime() - pauseAt
                timer.start()
            }

            btnStop.setOnClickListener {
                pauseAt = SystemClock.elapsedRealtime() - timer.base + t
                timer.stop()
            }

            btnReset.setOnClickListener {
                timer.stop()
                timer.base = SystemClock.elapsedRealtime()
                pauseAt = 0
            }

            timer.setOnChronometerTickListener {
                if (timer.text == "−00:01") dialog.dismiss()
            }

            dialog.show()
        }
    }

    private fun setDice(){
        binding.btnDice.setOnClickListener {
            val dialog = Dialog(requireContext())
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setContentView(R.layout.dialog_dice)
            dialog.setCancelable(true)

            val btnRoll = dialog.findViewById<Button>(R.id.btnRoll)
            val tvNumber = dialog.findViewById<TextView>(R.id.tvNumber)
            val etnSize = dialog.findViewById<EditText>(R.id.etnSize)

            btnRoll.setOnClickListener {
                tvNumber.text = Random.nextInt(0, etnSize.text.toString().toInt() + 1).toString()
            }

            dialog.show()
        }
    }

    private fun addColumn(table: TableLayout){
        for (i in 0 until  table.childCount){
            val row: TableRow = table.getChildAt(i) as TableRow
            var view: View
            if(i == 0){
                view = (LayoutInflater.from(context).inflate(R.layout.text_view_item, null) as TextView)
                view.text = (playerTable[0].size + 1).toString()
            } else {
                view = LayoutInflater.from(context).inflate(R.layout.edit_text_item, null) as EditText
                playerTable[i-1].add(view)
                view.width = 150
            }

            row.addView(view)
        }
    }

    private fun fillTable(table: TableLayout, name: String){
        val item = LayoutInflater.from(context).inflate(R.layout.table_row_item, null) as TableRow
        (item.getChildAt(0) as TextView).text = name
        table.addView(item)
    }

    private fun setLiners(table: TableLayout, name: String){
        val item = LayoutInflater.from(context).inflate(R.layout.liners_item, null) as TableRow
        val tv = (item.getChildAt(4) as TextView)
        val pb = (item.getChildAt(1) as ProgressBar)
        pb.max = 100
        (item.getChildAt(0) as TextView).text = name
        (item.getChildAt(2) as ImageView).setOnClickListener{
            changeTVValues(tv, 1, pb)
        }
        (item.getChildAt(3) as ImageView).setOnClickListener{
            changeTVValues(tv, -1, pb)
        }
        table.addView(item)
    }

    @SuppressLint("SetTextI18n")
    private fun changeTVValues(tv: TextView, num: Int, progressBar: ProgressBar){
        val k = (tv.text.toString().toInt() + num)
        tv.text = k.toString()

        progressBar.progress = k
    }

    private fun createTable(table: TableLayout){
        for(item in args.names){
            playerTable.add(mutableListOf())
            fillTable(table, item)
        }
    }

    private fun createLiners(table: TableLayout){
        for(item in args.names){
            setLiners(table, item)
        }
    }

    private fun sumTableScore(): Array<Int>{
        val mass = Array<Int>(playerTable.size) { 0 }
        for (i in 0 until playerTable.size){
            for(item in playerTable[i]){
                mass[i] += item.text.toString().toInt()
            }
        }
        return mass
    }

    private fun saveOrNotMatch(){
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_game_save_not)
        dialog.setCancelable(true)

        val btnNotSave = dialog.findViewById<Button>(R.id.btnNotSave)
        val btnSave = dialog.findViewById<Button>(R.id.btnSave)

        btnNotSave.setOnClickListener {
            dialog.dismiss()
            requireActivity().onBackPressed()
        }

        btnSave.setOnClickListener {
//            val mass = sumTableScore()
//            Toast.makeText(context, "${mass[0]} ${mass[mass.size-1]}", Toast.LENGTH_SHORT).show()
            dialog.dismiss()
            saveMatch()
        }

        dialog.show()
    }

    private fun saveMatch(){
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_save_name_game)
        dialog.setCancelable(true)

        val btnSaveMatch = dialog.findViewById<Button>(R.id.btnSaveMatch)
        val ietMatchName = dialog.findViewById<TextInputEditText>(R.id.ietMatchName)
        val ietWinnerName = dialog.findViewById<TextInputEditText>(R.id.ietWinnerName)

        btnSaveMatch.setOnClickListener {
            val title = ietMatchName.text.toString()
            val winner = ietWinnerName.text.toString()
//            Toast.makeText(context, "${timeToInt(binding.chmMainTimer.text.toString())}", Toast.LENGTH_SHORT).show()
            if(correctTitle(title) && correctWinner(winner)) {
                saveMatchToBD(title, winner)
                dialog.dismiss()

                requireActivity().onBackPressed()
            }
        }


        dialog.show()
    }

    private fun correctTitle(name: String): Boolean{
        if(name.isEmpty()) {
            Toast.makeText(context, "Введите название матча", Toast.LENGTH_SHORT).show()
            return false
        }

        return checkNameInDB(name)
    }

    private fun correctWinner(winner: String): Boolean{
        if(winner.isEmpty()) {
            Toast.makeText(context, "Введите имя побудителя", Toast.LENGTH_SHORT).show()
            return false
        }

        return true
    }

    private fun checkNameInDB(name: String): Boolean{
        for (item in viewHolder.match.value!!){
            if (item.title == name) {
                Toast.makeText(context, "Данное название уже существует", Toast.LENGTH_SHORT).show()
                return false
            }
        }

        return true
    }

    private fun createPlayers(): List<Player>{
        val players = mutableListOf<Player>()
        val mass = sumTableScore()
        for (i in args.names.indices){
            players.add(Player(args.names[i], mass[i].toDouble()))
        }

        return players.toList()
    }

    private fun saveMatchToBD(title: String, winner: String){
        val gameId = dataBaseReference.push().key!!
        val players = createPlayers()

        val match = Match(
            args.settings.user_login,
            args.settings.name,
            timeToInt(binding.chmMainTimer.text.toString()),
            winner,
            players,
            gameId,
            title)

        dataBaseReference.child(TABLE_MATCHES).child(match.user_login).child(match.id).setValue(match)

        updateGamesTable(match)
    }

    private fun updateGamesTable(match: Match){
        val mass = sumTableScore()

        dataBaseReference.child(TABLE_GAMES).child(args.settings.user_login).get().addOnSuccessListener {
            if(it.child(args.settings.name).childrenCount < 1) {
                val game = Game(match.name, 1, match.T, mass.max().toDouble(), match.T, match.T)
                dataBaseReference.child(TABLE_GAMES).child(args.settings.user_login).child(game.game_name).setValue(game)
            }
            else {
                viewHolder.game.value?.let {
                    val game = it.find { it.game_name == match.name }
                    game?.let {
                        val updateGame = game.copy(
                            game_count = game.game_count + 1,
                            avg_time = (game.avg_time * game.game_count + match.T)/(game.game_count+1),
                            bestScore = max(game.bestScore, mass.max().toDouble()),
                            faster_game = min(game.faster_game, match.T),
                            slower_game = max(game.slower_game, match.T)
                        )
                        dataBaseReference.child(TABLE_GAMES).child(args.settings.user_login).child(game.game_name).setValue(updateGame)
                    }
                }
            }

        }
    }

    private fun timeToInt(time: String): Int{
        val mass = time.split(':').map { it.toInt() }
        val summ = mass[0] * 60 * 60 + mass[1] * 60 + mass[2]

        return summ
    }

    companion object{
        private const val TABLE_MATCHES = "Matches"
        private const val TABLE_GAMES = "Games"
    }
}