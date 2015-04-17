package v1

class Torrent {

    String url
    int type

    static constraints = {
        url blank: false
    }
}
