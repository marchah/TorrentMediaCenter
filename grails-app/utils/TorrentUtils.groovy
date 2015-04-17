package torrentmediacenter

import Utils.TorrentType
import com.turn.ttorrent.client.SharedTorrent
import org.apache.commons.io.FileUtils
import grails.util.Holders
import org.apache.commons.io.FilenameUtils

import java.nio.file.Files
import java.nio.file.Paths

/**
 * Created by marcha on 4/16/15.
 */
class TorrentUtils {

    static def grailsApplication = Holders.getGrailsApplication()

    static File DownloadTorrentFile(String url) {
        File file = new File(grailsApplication.config.FolderTmpPath + System.currentTimeMillis());
        try {
            FileUtils.copyURLToFile(new URL(url), file, 10000, 10000);
        } catch (IOException e) {
            e.printStackTrace()
            return null;
        }
        return file;
    }

    static SharedTorrent GetSharedTorrent(File torrentFile) {
        File dirDest = new File(grailsApplication.config.TorrentDestFolderPath)
        SharedTorrent torrent =  SharedTorrent.fromFile(torrentFile, dirDest, false)
        return torrent
    }

    static void RemoveUselessFile(SharedTorrent torrent, int torrentType) {
        List<String> listFilesPath = torrent.getFilenames()

        if (torrent.isMultifile() && torrentType <= TorrentType.TypeFilesToKeep.size()) {
            def typeFilesToKeep = TorrentType.TypeFilesToKeep.get(torrentType)

            for (String filePath : listFilesPath) {
                if (!typeFilesToKeep.contains(FilenameUtils.getExtension(filePath))) {
                    torrent.removeFileByFilename(filePath)
                }
            }
        } else if (!torrent.isMultifile()) {
            def file = listFilesPath.get(0)
            def folder = FilenameUtils.removeExtension(file);
            torrent.setFilename(file, folder + File.separator + file)
            torrent.setName(folder)
        }

        listFilesPath = torrent.getFilenames()

        for (String filePath : listFilesPath) {
            println(filePath)
        }

        println(torrent.getName())
    }

    static boolean IsTorrentAlreadyPresent(SharedTorrent torrent) {
        return Files.exists(Paths.get((String)grailsApplication.config.TorrentDestFolderPath + torrent.getName()))
    }

    static void CreateTorrentFiles(SharedTorrent torrent) {
        File dirDest = new File(grailsApplication.config.TorrentDestFolderPath)
        torrent.createTorrentFiles(dirDest)
    }
}
