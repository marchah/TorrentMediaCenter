package torrentmediacenter

import com.turn.ttorrent.client.Client
import com.turn.ttorrent.client.Client.ClientState
import com.turn.ttorrent.client.SharedTorrent
import grails.transaction.Transactional
import org.apache.commons.io.FileUtils
import v1.Torrent

import java.util.concurrent.TimeUnit

@Transactional
class TorrentService {



    boolean processDownload(Torrent torrent) {
        File fileToDDL;
        if ((fileToDDL = downloadTorrentFile(torrent)) == null) {
            return false;
        }
        File dirDest = new File("./file/");
        SharedTorrent torrentInfo =  SharedTorrent.fromFile(fileToDDL, dirDest);
        Client client = new Client(InetAddress.getLocalHost(), torrentInfo);
        client.download();
        try {
        while (!ClientState.SEEDING.equals(client.getState())) {
            // Check if there's an error
            if (ClientState.ERROR.equals(client.getState())) {
                throw new Exception("ttorrent client Error State");
            }

            // Display statistics
            System.out
                    .printf("\n%f %% - %d bytes downloaded - %d bytes uploaded\n",
                    torrentInfo.getCompletion(),
                    torrentInfo.getDownloaded(),
                    torrentInfo.getUploaded());
            println("Status: " + client.getState().name() + " " + client.getState());
            // Wait one second
            TimeUnit.SECONDS.sleep(1);
        }
        } catch (InterruptedException e) {
            //Handle exception
            e.printStackTrace()
        }
        println("END DDL")
        client.waitForCompletion();
        client.stop();
        return true;
    }
}
