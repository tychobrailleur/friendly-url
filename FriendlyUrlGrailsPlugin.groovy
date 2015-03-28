import friendlyurl.SlugNormalizer
import friendlyurl.SluggedUpdateListener

class FriendlyUrlGrailsPlugin {
    def version = "0.1"
    def grailsVersion = "2.4 > *"
    def pluginExcludes = [
        "grails-app/views/error.gsp"
    ]
    def title = "Friendly Url Plugin"
    def author = "Sébastien Le Callonnec"
    def authorEmail = "sebastien@weblogism.com"
    def description = "This plugin creates pretty human-friendly URLs."
    def documentation = "http://grails.org/plugin/friendly-url"
    def issueManagement = [ system: "JIRA", url: "http://jira.grails.org/browse/GPMYPLUGIN" ]
    def scm = [ url: "http://svn.codehaus.org/grails-plugins/" ]
    def license = 'MIT'
    
    def doWithSpring = {
        slugNormalizer(SlugNormalizer)
    }

    def doWithDynamicMethods = { ctx ->
    }

    def doWithApplicationContext = { ctx ->
        def slugNormalizer = ctx.getBean('slugNormalizer')
        application.domainClasses.each {
            addSlugGeneration(it, ctx)
        }

        application.mainContext.eventTriggeringInterceptor.datastores.each { k, datastore ->
            ctx.addApplicationListener(new SluggedUpdateListener(datastore))
        }
    }

    private void addSlugGeneration(subject, ctx) {
        def slugNormalizer = ctx.getBean('slugNormalizer')

        if (subject.hasProperty('slug')) {
            subject.metaClass.normalizeSlug = { ->
                slug = slugNormalizer.normalize(name)
            }
        }
    }
}
