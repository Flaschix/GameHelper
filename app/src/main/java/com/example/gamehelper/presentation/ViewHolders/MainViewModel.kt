package com.example.gamehelper.presentation.ViewHolders

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.gamehelper.domain.entity.Game
import com.example.gamehelper.domain.entity.Match
import com.example.gamehelper.domain.entity.Player
import com.example.gamehelper.domain.entity.Template
import com.google.firebase.database.*
import kotlinx.coroutines.launch

class MainViewModel(application: Application): AndroidViewModel(application){
    private val dataBaseReference: DatabaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://gamehelper-af012-default-rtdb.firebaseio.com/")


    private var _template: MutableLiveData<List<Template>> = MutableLiveData(emptyList())
    private var _game: MutableLiveData<List<Game>> = MutableLiveData(emptyList())
    private var _match: MutableLiveData<List<Match>> = MutableLiveData(emptyList())


    val template: LiveData<List<Template>>
        get() = _template

    val game: LiveData<List<Game>>
        get() = _game ?: throw RuntimeException("game == null")

    val match: LiveData<List<Match>>
        get() = _match ?: throw RuntimeException("match == null")


    fun GetTemplateListUseCase(login: String){
        dataBaseReference.child(TABLE_TEMPLATES).child(login).get().addOnSuccessListener {
            val list = it.children.toList()
            val values = list.map { it.children.map { it.value } }

            val res = mutableListOf<Template>()
            for (i in list.indices) res.add(Template(login, list[i].key.toString(), values[i][2].toString().toInt(), values[i][0].toString().toBoolean(), values[i][4].toString().toBoolean(), values[i][1].toString().toBoolean(),values[i][3].toString().toBoolean()))
            _template.value = res
        }
    }

    fun GetMatchesListUseCase(login: String, gameName: String){
        dataBaseReference.child(TABLE_MATCH).child(login).get().addOnSuccessListener {
            val list = mutableListOf<List<Any>>()
            val list2 = mutableListOf<Any>()
//
            for (item in it.children){
//
                list2.clear()
                if(item.child(MATCH_NAME).value.toString() == gameName){

                    val key = item.key.toString()
                    val dd = item.children.toList()

                    for (item2 in dd){
                        if (item2.childrenCount < 1) list2.add(item2.value.toString())
                        else {
                            val mm = item2.children.toList()
                            val list3 = mutableListOf<Player>()
                            for (itemPlayer in mm) {
                                var zz = itemPlayer.children.toList()
                                list3.add(Player(zz[0].value.toString(), zz[1].value.toString().toDouble()))
//
                            }
                            list2.add(list3)
                        }
                    }
                }
//
//                list2.add(0, item.key.toString())
                if(list2.isNotEmpty()) list.add(list2.toMutableList())
//
            }

            if(list.isNotEmpty()){
            val res = mutableListOf<Match>()
            for (i in list.indices) {
                res.add(convertToMatch(
                    login = login,
                    id = list[i][0], //каакаято ошибка
                    listOfPlayers = list[i][1],
                    name = list[i][2],
                    T = list[i][3],
                    title = list[i][4],
                    winner = list[i][6]
                ))
            }
            _match.value = res
            }
            else _match.value = arrayListOf<Match>()
        }
    }

    fun GetGamesListUseCase(login: String){
        dataBaseReference.child(TABLE_GAME).child(login).get().addOnSuccessListener {
            val list = mutableListOf<List<Any>>()
            val list2 = mutableListOf<Any>()

            for (item in it.children){
                list2.clear()
                for (item2 in item.children){
                     list2.add(item2.value.toString())
                }
//                list2.add(0, item.key.toString())
                list.add(list2.toMutableList())
            }

            val res = mutableListOf<Game>()

            for (i in list.indices) {
                res.add(convertToGame(list[i][0], list[i][1], list[i][2], list[i][3], list[i][4],list[i][5]))
            }

            _game.value = res
        }
    }

    fun deleteShopItem(item: Template){
        viewModelScope.launch {
            dataBaseReference.child(TABLE_TEMPLATES).child(item.user_login).child(item.name).removeValue()
        }

    }

    private fun convertToMatch(login: String, name: Any, T: Any, winner: Any, listOfPlayers: Any, id:Any, title: Any): Match{
        var t = T as String
        return Match(login, name as String, t.toInt(), winner as String, listOfPlayers as List<Player>, id as String, title as String)
    }

    private fun convertToGame(Avg_time: Any, BestScore: Any, Faster_game: Any, Game_count: Any, Game_name: Any, Slower_game: Any): Game{
        val avg_time = Avg_time as String
        val bestScore = BestScore as String
        val faster_game = Faster_game as String
        val game_count = Game_count as String
        val slower_game = Slower_game as String
        return Game(Game_name as String, game_count.toInt(), avg_time.toInt(), bestScore.toDouble(), faster_game.toInt(), slower_game.toInt())
    }

    companion object{
        private const val TABLE_TEMPLATES = "Templates"
        private const val TABLE_MATCH = "Matches"
        private const val TABLE_GAME = "Games"
        private const val MATCH_NAME = "name"
    }
}