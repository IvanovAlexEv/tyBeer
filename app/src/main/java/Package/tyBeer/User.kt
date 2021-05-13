package Package.tyBeer

import android.net.Uri

class User (val id: String , val nicknameU: String, val nameU: String, val surnameU: String, val emailU: String, var photoProfile: String, var nChangePP: Int, var nPost: Int){

    var friendsListU : MutableList<User> = ArrayList()
    var postList : MutableList<Post> = ArrayList()

    constructor() : this ("", "","","","", "", 0, 0)

}