Feature: Download a Candidate Version

  Scenario Outline: Download platform specific binaries
    Given a valid <db_platform_id> binary for java 11.0.9-adpt hosted at http://example.org/java.tar.gz
    When a download request is made on "<api_url>"
    Then a redirect to "http://example.org/java.tar.gz" is returned
    And an audit entry for java 11.0.9-adpt <db_platform_id> is recorded for <audit_platform>
    Examples:
      | db_platform_id | api_url                                    | audit_platform |
      | MAC_OSX        | /download/java/11.0.9-adpt/darwinx64       | DarwinX64      |
      | MAC_ARM64      | /download/java/11.0.9-adpt/darwinarm64     | DarwinARM64    |
      | LINUX_64       | /download/java/11.0.9-adpt/linuxx64        | LinuxX64       |
      | LINUX_32       | /download/java/11.0.9-adpt/linuxx32        | LinuxX32       |
      | LINUX_ARM32    | /download/java/11.0.9-adpt/linuxarm32      | LinuxARM32     |
      | WINDOWS_64     | /download/java/11.0.9-adpt/msys_nt-10.0    | WindowsX64     |
      | WINDOWS_64     | /download/java/11.0.9-adpt/mingw64_nt-10.0 | WindowsX64     |
      | WINDOWS_64     | /download/java/11.0.9-adpt/cygwin_nt-10.0  | WindowsX64     |
      | WINDOWS_32     | /download/java/11.0.9-adpt/mingw32_nt-6.2  | WindowsX32     |
      | MAC_OSX        | /download/java/11.0.9-adpt/darwin          | DarwinX64      |
      | LINUX_64       | /download/java/11.0.9-adpt/linux64         | LinuxX64       |
      | LINUX_32       | /download/java/11.0.9-adpt/linux32         | LinuxX32       |

  Scenario: Download a Universal binary
    Given a valid UNIVERSAL binary for groovy 2.4.7 hosted at http://dl.bintray.com/groovy/maven/apache-groovy-binary-2.4.7.zip
    When a download request is made on "/download/groovy/2.4.7/linux"
    Then a redirect to "http://dl.bintray.com/groovy/maven/apache-groovy-binary-2.4.7.zip" is returned
    And an audit entry for groovy 2.4.7 UNIVERSAL is recorded for LinuxX64

  Scenario: Download a specific binary for a supported platform
    Given a valid MAC_OSX binary for java 8u101 hosted at http://download.oracle.com/otn-pub/java/jdk/8u101-b13/jdk-8u101-macosx-x64.dmg
    And a valid LINUX_64 binary for java 8u101 hosted at http://download.oracle.com/otn-pub/java/jdk/8u101-b13/jdk-8u101-linux-x64.tar.gz
    When a download request is made on "/download/java/8u101/darwin"
    Then a redirect to "http://download.oracle.com/otn-pub/java/jdk/8u101-b13/jdk-8u101-macosx-x64.dmg" is returned
    And an audit entry for java 8u101 MAC_OSX is recorded for DarwinX64

  Scenario: Download a specific binary for an unsupported platform
    Given a valid MAC_OSX binary for java 8u101 hosted at http://download.oracle.com/otn-pub/java/jdk/8u101-b13/jdk-8u101-macosx-x64.dmg
    And a valid LINUX_64 binary for java 8u101 hosted at http://download.oracle.com/otn-pub/java/jdk/8u101-b13/jdk-8u101-linux-x64.tar.gz
    When a download request is made on "/download/java/8u101/freebsd"
    Then the service response status is 404
    And an audit entry for java 8u101 FREE_BSD is not recorded for FreeBSD

  Scenario: Download a specific binary for an unknown platform
    Given a valid MAC_OSX binary for java 8u101 hosted at http://download.oracle.com/otn-pub/java/jdk/8u101-b13/jdk-8u101-macosx-x64.dmg
    And a valid LINUX_64 binary for java 8u101 hosted at http://download.oracle.com/otn-pub/java/jdk/8u101-b13/jdk-8u101-linux-x64.tar.gz
    When a download request is made on "/download/java/8u101/foobar"
    Then the service response status is 400
    And an audit entry for java 8u101 FooBar is not recorded for FooBar

  Scenario: Attempt downloading an invalid Candidate Version
    When a download request is made on "/download/groovy/2.9.9/linux"
    Then the service response status is 404
    And an audit entry for groovy 2.4.8 UNIVERSAL is not recorded for Linux

  Scenario: Attempt downloading an invalid Candidate
    Given a Candidate groovy does not exist
    When a download request is made on "/download/groovy/linux"
    Then the service response status is 404

  Scenario: Download a Universal binary with checksum info
    Given a valid UNIVERSAL binary for groovy 2.4.7 hosted at http://dl.bintray.com/groovy/maven/apache-groovy-binary-2.4.7.zip
    And the binary for candidate groovy 2.4.7 on platform UNIVERSAL has a checksum 6f1b9599f99393724ea134951aa605037178b63a using algorithm SHA-1
    And the binary for candidate groovy 2.4.7 on platform UNIVERSAL has a checksum 438dd6098252647e88ff12ac5737d0d0f7e16a8e4e42e8be3e05a4c43abefbd5 using algorithm SHA-256
    When a download request is made on "/download/groovy/2.4.7/linux"
    Then a redirect to "http://dl.bintray.com/groovy/maven/apache-groovy-binary-2.4.7.zip" is returned
    And a checksum "6f1b9599f99393724ea134951aa605037178b63a" for algorithm "SHA-1" is returned
    And a checksum "438dd6098252647e88ff12ac5737d0d0f7e16a8e4e42e8be3e05a4c43abefbd5" for algorithm "SHA-256" is returned
    And an audit entry for groovy 2.4.7 UNIVERSAL is recorded for LinuxX64

  Scenario: Download a specific binary for a supported platform with checksum info
    Given a valid MAC_OSX binary for java 8u101 hosted at http://download.oracle.com/otn-pub/java/jdk/8u101-b13/jdk-8u101-macosx-x64.dmg
    And a valid LINUX_64 binary for java 8u101 hosted at http://download.oracle.com/otn-pub/java/jdk/8u101-b13/jdk-8u101-linux-x64.tar.gz
    And the binary for candidate java 8u101 on platform MAC_OSX has a checksum MAC_OSXaa605037178b63a using algorithm SHA-1
    And the binary for candidate java 8u101 on platform LINUX_64 has a checksum LINUX_64aa605037178b63a using algorithm SHA-1
    When a download request is made on "/download/java/8u101/darwin"
    Then a redirect to "http://download.oracle.com/otn-pub/java/jdk/8u101-b13/jdk-8u101-macosx-x64.dmg" is returned
    And a checksum "MAC_OSXaa605037178b63a" for algorithm "SHA-1" is returned
    And an audit entry for java 8u101 MAC_OSX is recorded for DarwinX64