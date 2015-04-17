package v1

import com.turn.ttorrent.client.SharedTorrent
import torrentmediacenter.TorrentUtils
import grails.converters.JSON
import grails.rest.RestfulController
import grails.transaction.Transactional
import org.springframework.http.HttpStatus

class TorrentsController extends RestfulController {

    static namespace = 'v1'
    static responseFormats = ['json']

    def torrentService
    def downloadManagerService

    TorrentsController() {
        super(Torrent)
    }

    @Transactional
    def add(Torrent torrent) {
        if (torrent == null) {
            render status: HttpStatus.NOT_FOUND.value()
        } else if (torrent.hasErrors()) {
            response.status = HttpStatus.BAD_REQUEST.value()
			render torrent.errors as JSON
        } else {
            downloadManagerService.add()
            File torrentFile = TorrentUtils.DownloadTorrentFile(torrent.url)
            if (torrentFile == null) {
                render status: HttpStatus.INTERNAL_SERVER_ERROR.value()
            } else {
                SharedTorrent sharedTorrent = TorrentUtils.GetSharedTorrent(torrentFile)
                TorrentUtils.RemoveUselessFile(sharedTorrent, torrent.type)
                if (!TorrentUtils.IsTorrentAlreadyPresent(sharedTorrent)) {
                    TorrentUtils.CreateTorrentFiles(sharedTorrent)
                    render torrent as JSON
                } else {
                    response.status = HttpStatus.I_AM_A_TEAPOT.value()
                    response.setContentType("application/json")
                    render "{Error: torrent already present}"
                }
            }
        }
    }
}
