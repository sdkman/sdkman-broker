package io.sdkman.broker.download

import spock.lang.Specification

class DownloadResolverSpec extends Specification {
    def downloadResolver = new DownloadResolver()

    void "should resolve a universal binary if no platform specific binary is found"() {
        given:
        def candidate = "groovy"
        def candidateVersion = "2.4.7"
        def url = "https://someurl/foo/bar/baz"
        def platform = "Linux"

        def version = new Version(candidate, candidateVersion, url, "UNIVERSAL")
        def versions = [version]

        when:
        def resolved = downloadResolver.resolve(versions, platform)

        then:
        resolved.isPresent()
        resolved.get() == version
    }

    void "should resolve a platform specific binary if an appropriate version is found"() {
        given:
        def platform = "LINUX"
        def version = new Version("java", "1.8.0", "https://someurl/bleeh/blah/bloo", "LINUX")
        def versions = [version]

        when:
        def resolved = downloadResolver.resolve(versions, platform)

        then:
        resolved.isPresent()
        resolved.get() == version
    }
}
