class BootStrap {

    def downloadManagerService

    def init = { servletContext ->
        downloadManagerService.startService()
    }
    def destroy = {
        downloadManagerService.stopService()
    }
}
