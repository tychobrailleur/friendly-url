package friendlyurl

import groovy.transform.CompileStatic

@CompileStatic
class SlugNormalizer {
    public String normalize(String slug) {
        String result

        if (slug) {
            result = slug.toLowerCase().replaceAll(' ', '-')
        }

        return result
    }
}
