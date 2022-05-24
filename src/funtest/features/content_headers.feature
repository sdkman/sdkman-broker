Feature: Write content headers when downloading a Candidate

  Scenario: Propagate HTTP header with zip type
    Given a valid UNIVERSAL binary for groovy 2.4.7 hosted at http://dl.bintray.com/groovy/maven/apache-groovy-binary-2.4.7.zip
    When a download request is made on "/download/groovy/2.4.7/linuxx64"
    Then a redirect to "http://dl.bintray.com/groovy/maven/apache-groovy-binary-2.4.7.zip" is returned
    And a header with archive type "zip" is returned

  Scenario: Propagate HTTP header with tarball type
    Given a valid MAC_OSX binary for java 8u101 hosted at http://download.oracle.com/otn-pub/java/jdk/8u101-b13/jdk-8u101-macosx-x64.dmg
    And a valid LINUX_64 binary for java 8u101 hosted at http://download.oracle.com/otn-pub/java/jdk/8u101-b13/jdk-8u101-linux-x64.tar.gz
    When a download request is made on "/download/java/8u101/linuxx64"
    Then a redirect to "http://download.oracle.com/otn-pub/java/jdk/8u101-b13/jdk-8u101-linux-x64.tar.gz" is returned
    And a header with archive type "tar" is returned
