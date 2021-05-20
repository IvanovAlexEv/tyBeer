package Package.tyBeer

// STRUTTURA DI UN UTENTE

class User (val id: String , val nicknameU: String, val nameU: String, val surnameU: String, val emailU: String, var photoProfile: String, var nChangePP: Int, var nPost: Int){

    var friendsListU : MutableList<String> = ArrayList()
    var followerListU : MutableList<String> = ArrayList()
    var postList : MutableList<Post> = ArrayList()

    constructor() : this ("", "","","","", "", 0, 0)

}