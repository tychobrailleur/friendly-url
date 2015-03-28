class UrlMappings {

	static mappings = {
        "/$controller/$action?/$id?" {}

        "/"(controller: 'test')
        "500"(view:'/error')
	}
}
