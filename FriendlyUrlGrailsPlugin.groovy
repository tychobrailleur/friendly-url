import friendlyurl.SlugNormalizer
import friendlyurl.SluggedUpdateListener
import org.codehaus.groovy.grails.commons.GrailsClassUtils
import org.codehaus.groovy.grails.exceptions.RequiredPropertyMissingException

class FriendlyUrlGrailsPlugin {
    def version = '0.1'
    def grailsVersion = '2.4 > *'
    def pluginExcludes = [
        'grails-app/views/error.gsp'
    ]
    def title = 'Friendly Url Plugin'
    def author = 'Sébastien Le Callonnec'
    def authorEmail = 'sebastien@weblogism.com'
    def description = 'This plugin creates pretty human-friendly URLs.'
    def documentation = 'http://grails.org/plugin/friendly-url'
    def issueManagement = [ system: 'github', url: 'https://github.com/tychobrailleur/friendly-url/issues' ]
    def scm = [ url: 'https://github.com/tychobrailleur/friendly-url' ]
    def license = 'MIT'

    def doWithSpring = {
        slugNormalizer(SlugNormalizer)
    }

    def doWithDynamicMethods = { ctx ->
    }

    def doWithApplicationContext = { ctx ->
        def slugNormalizer = ctx.getBean('slugNormalizer')
        application.domainClasses.each {
            if (it.hasProperty('slug')) {
                addSlugGeneration(it, ctx)
            }
        }

        application.mainContext.eventTriggeringInterceptor.datastores.each { k, datastore ->
            ctx.addApplicationListener(new SluggedUpdateListener(datastore))
        }
    }

    private void addSlugGeneration(subject, ctx) {
        def slugNormalizer = ctx.getBean('slugNormalizer')

        subject.metaClass.normalizeSlug = { ->
//            if (!GrailsClassUtils.getStaticFieldValue(getClass(), 'slugCandidate')) {
//                throw new RequiredPropertyMissingException("${subject.name}: slugCandidate is required.")
//            }

            // TODO find a way to check slugCandidate's existence without throwing exception.
            try {
                def candidate = getProperty(slugCandidate)
                slug = slugNormalizer.normalize(candidate)
            } catch (MissingPropertyException e) {
                throw new RequiredPropertyMissingException("${subject.name}: slugCandidate is required.")
            }
        }
    }
}
