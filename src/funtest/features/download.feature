Feature: Download a Candidate Version

	Scenario: Download a Universal binary
		Given a valid UNIVERSAL binary for groovy 2.4.7 hosted at http://dl.bintray.com/groovy/maven/apache-groovy-binary-2.4.7.zip
		When a request is made on /download/groovy/2.4.7 using Linux
		Then a redirect to http://dl.bintray.com/groovy/maven/apache-groovy-binary-2.4.7.zip is returned
		And an audit entry for groovy 2.4.7 UNIVERSAL is recorded

	Scenario: Download a specific binary for a supported platform
		Given a valid MAC_OSX binary for java 8u101 hosted at http://download.oracle.com/otn-pub/java/jdk/8u101-b13/jdk-8u101-macosx-x64.dmg
		When a request is made on /download/java/8u101 using Darwin
		Then a redirect to http://download.oracle.com/otn-pub/java/jdk/8u101-b13/jdk-8u101-macosx-x64.dmg is returned
		And an audit entry for java 8u101 MAC_OSX is recorded

	Scenario: Download a specific binary for an unsupported platform
		Given a valid MAC_OSX binary for java 8u101 hosted at http://download.oracle.com/otn-pub/java/jdk/8u101-b13/jdk-8u101-macosx-x64.dmg
		When a request is made on /download/java/8u101 using FreeBSD
		Then the service response status is 404
		And an audit entry for java 8u101 FREE_BSD is not recorded

	Scenario: Download a specific binary for an unknown platform
		Given a valid MAC_OSX binary for java 8u101 hosted at http://download.oracle.com/otn-pub/java/jdk/8u101-b13/jdk-8u101-macosx-x64.dmg
		When a request is made on /download/java/8u101 using FooBar
		Then the service response status is 400
		And an audit entry for java 8u101 FooBar is not recorded

	Scenario: Attempt downloading an invalid Candidate Version
		When a request is made on /download/groovy/2.9.9 using Linux
		Then the service response status is 404
		And an audit entry for groovy 2.4.8 UNIVERSAL is not recorded
		
	Scenario: Attempt downloading an invalid Candidate
		Given a Candidate groovy does not exist
		When a request is made on /download/groovy using Linux
		Then the service response status is 404