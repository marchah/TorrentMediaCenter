package torrentmediacenter

import grails.transaction.Transactional

@Transactional
class DownloadManagerService extends Thread {

    int count
    boolean isRunning

    DownloadManagerService() {
        count = 10
        isRunning = false
        println("*** CREATE DDL MANAGER***")
    }

    void startService() {
        if (!isRunning) {
            println("*** START DOWNLOAD MANAGER SERVICE ***")
            isRunning = true
            this.setName("DownloadManagerService")
            this.start()
        }
    }

    void run() {
        while (true) {
            println("*** RUNNING " + count + " ***")
            //   sleep(1000)

            synchronized (this) {
                wait()
            }
        }
    }

    synchronized void add() {
        count++
        //this.run()
        notify()
    }

    void stopService() {
        println("*** STOP ***")
    }
}
