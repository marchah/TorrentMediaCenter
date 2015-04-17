class UrlMappings {

	static mappings = {
        /*"/$controller/$action?/$id?(.$format)?"{
            constraints {
                // apply constraints here
            }
        }*/

        "/"(view:"/index")
        "500"(view:'/error')

        "/api/v1/torrents"(controller: "torrents", action: "add", method: "POST", namespace:'v1')
	}
}
