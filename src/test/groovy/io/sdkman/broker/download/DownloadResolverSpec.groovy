package io.sdkman.broker.download

import io.sdkman.repos.Version
import scala.Option
import scala.Some
import spock.lang.Specification

class DownloadResolverSpec extends Specification {
    def downloadResolver = new DownloadResolver()

    void "should resolve a universal binary if no platform specific binary is found"() {
        given:
        def candidate = "groovy"
        def candidateVersion = "2.4.7"
        def url = "https://someurl/foo/bar/baz"
        def platform = "Linux"

        def version = new Version(candidate, candidateVersion, "UNIVERSAL", url, Option.empty(), Some.apply(true))
        def versions = [version]

        when:
        def resolved = downloadResolver.resolve(versions, platform)

        then:
        resolved.isPresent()
        resolved.get() == version
    }

    void "should resolve a platform specific binary if an appropriate version is found"() {
        given:
        def platform = "LINUX_64"
        def linuxVersion = new Version("java", "1.8.0",  "LINUX_64", "https://someurl/bleeh/blah/bloo", Option.empty(), Some.apply(true))
        def macVersion = new Version("java", "1.8.0", "MAC_OSX", "https://someurl/bleeh/blah/bloo", Option.empty(), Some.apply(true))
        def versions = [linuxVersion, macVersion]

        when:
        def resolved = downloadResolver.resolve(versions, platform)

        then:
        resolved.isPresent()
        resolved.get() == linuxVersion
    }

    void "should return empty if no matching platform is found"() {
        given:
        def platform = "FREE_BSD"
        def linuxVersion = new Version("java", "1.8.0", "LINUX_64", "https://someurl/bleeh/blah/bloo", Option.empty(), Some.apply(true))
        def macVersion = new Version("java", "1.8.0", "MAC_OSX", "https://someurl/bleeh/blah/bloo", Option.empty(), Some.apply(true))
        def versions = [linuxVersion, macVersion]

        expect:
        downloadResolver.resolve(versions, platform) == Optional.empty()
    }
}
