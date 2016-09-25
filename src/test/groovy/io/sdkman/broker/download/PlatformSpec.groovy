package io.sdkman.broker.download

import spock.lang.Specification

class PlatformSpec extends Specification {

    void "should resolve empty if uname is null"() {
        expect:
        Platform.of(null) == Optional.empty()
    }

    void "should resolve mac osx uname as a platform"() {
        expect:
        Platform.of("Darwin").get() == Platform.MAC_OSX
    }

    void "should resolve linux uname as a platform"() {
        expect:
        Platform.of("Linux").get() == Platform.LINUX
    }

    void "should resolve sun os uname as sunos"() {
        expect:
        Platform.of("SunOS").get() == Platform.SUN_OS
    }

    void "should resolve freebsd uname as freebsd"() {
        expect:
        Platform.of("FreeBSD").get() == Platform.FREE_BSD
    }

    void "should resolve cygwin uname to windows 64"() {
        expect:
        Platform.of("CYGWIN_NT-6.1").get() == Platform.WINDOWS_64
        Platform.of("CYGWIN_NT-6.1-WOW").get() == Platform.WINDOWS_64
        Platform.of("CYGWIN_NT-6.1-WOW64").get() == Platform.WINDOWS_64
        Platform.of("CYGWIN_NT-6.3").get() == Platform.WINDOWS_64
        Platform.of("CYGWIN_NT-6.3-WOW").get() == Platform.WINDOWS_64
        Platform.of("CYGWIN_NT-10.0").get() == Platform.WINDOWS_64
        Platform.of("CYGWIN_NT-10.0-WOW").get() == Platform.WINDOWS_64
    }

    void "should resolve msys uname to windows 64"() {
        expect:
        Platform.of("MSYS_NT-6.1").get() == Platform.WINDOWS_64
        Platform.of("MSYS_NT-6.3").get() == Platform.WINDOWS_64
        Platform.of("MSYS_NT-10.0").get() == Platform.WINDOWS_64
    }

    void "should resolve mingw 32 uname to windows 32"() {
        expect:
        Platform.of("MINGW32_NT-6.1").get() == Platform.WINDOWS_32
        Platform.of("MINGW32_NT-6.1-WOW").get() == Platform.WINDOWS_32
        Platform.of("MINGW32_NT-6.2").get() == Platform.WINDOWS_32
    }

    void "should resolve mingw 64 uname to windows 64"() {
        expect:
        Platform.of("MINGW64_NT-6.1").get() == Platform.WINDOWS_64
        Platform.of("MINGW64_NT-6.3").get() == Platform.WINDOWS_64
        Platform.of("MINGW64_NT-10.0").get() == Platform.WINDOWS_64
    }
}
