Feature: Download a Candidate Version

	Scenario: Download a Universal binary
		Given a valid UNIVERSAL binary for groovy 2.4.7 hosted at http://dl.bintray.com/groovy/maven/apache-groovy-binary-2.4.7.zip
		When a request is made on /download/groovy/2.4.7/linux
		Then a redirect to http://dl.bintray.com/groovy/maven/apache-groovy-binary-2.4.7.zip is returned
		And an audit entry for groovy 2.4.7 UNIVERSAL is recorded for Linux

	Scenario: Download a specific binary for a supported platform
		Given a valid MAC_OSX binary for java 8u101 hosted at http://download.oracle.com/otn-pub/java/jdk/8u101-b13/jdk-8u101-macosx-x64.dmg
		And a valid Linux binary for java 8u101 hosted at http://download.oracle.com/otn-pub/java/jdk/8u101-b13/jdk-8u101-linux-x64.tar.gz
		When a request is made on /download/java/8u101/darwin
		Then a redirect to http://download.oracle.com/otn-pub/java/jdk/8u101-b13/jdk-8u101-macosx-x64.dmg is returned
		And an audit entry for java 8u101 MAC_OSX is recorded for Darwin

	Scenario: Download a specific binary for an unsupported platform
		Given a valid MAC_OSX binary for java 8u101 hosted at http://download.oracle.com/otn-pub/java/jdk/8u101-b13/jdk-8u101-macosx-x64.dmg
		And a valid Linux binary for java 8u101 hosted at http://download.oracle.com/otn-pub/java/jdk/8u101-b13/jdk-8u101-linux-x64.tar.gz
		When a request is made on /download/java/8u101/freebsd
		Then the service response status is 404
		And an audit entry for java 8u101 FREE_BSD is not recorded for FreeBSD

	Scenario: Download a specific binary for an unknown platform
		Given a valid MAC_OSX binary for java 8u101 hosted at http://download.oracle.com/otn-pub/java/jdk/8u101-b13/jdk-8u101-macosx-x64.dmg
		And a valid Linux binary for java 8u101 hosted at http://download.oracle.com/otn-pub/java/jdk/8u101-b13/jdk-8u101-linux-x64.tar.gz
		When a request is made on /download/java/8u101/foobar
		Then the service response status is 400
		And an audit entry for java 8u101 FooBar is not recorded for FooBar

	Scenario: Attempt downloading an invalid Candidate Version
		When a request is made on /download/groovy/2.9.9/linux
		Then the service response status is 404
		And an audit entry for groovy 2.4.8 UNIVERSAL is not recorded for Linux
		
	Scenario: Attempt downloading an invalid Candidate
		Given a Candidate groovy does not exist
		When a request is made on /download/groovy/linux
		Then the service response status is 404