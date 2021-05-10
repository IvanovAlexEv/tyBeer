package Package.tyBeer

import android.net.Uri

class User (val id: String , val nicknameU: String, val nameU: String, val surnameU: String, val emailU: String, var photoProfile: String, var nChangePP: Int){

    var friendsListU : MutableList<User> = ArrayList()

    constructor() : this ("", "","","","", "", 0)

}