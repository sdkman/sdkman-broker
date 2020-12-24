Feature: Improved URL structure

	Scenario: Download a specific binary using platform as query parameter
		Given a valid MAC_OSX binary for java 8u101 hosted at http://download.oracle.com/otn-pub/java/jdk/8u101-b13/jdk-8u101-macosx-x64.dmg
		And a valid LINUX_64 binary for java 8u101 hosted at http://download.oracle.com/otn-pub/java/jdk/8u101-b13/jdk-8u101-linux-x64.tar.gz
		When a download request is made on "/download/java/8u101?platform=Darwin"
		Then a redirect to "http://download.oracle.com/otn-pub/java/jdk/8u101-b13/jdk-8u101-macosx-x64.dmg" is returned
		And an audit entry for java 8u101 MAC_OSX is recorded for DarwinX64

	Scenario: Download a specific binary using embedded platform url segment
		Given a valid MAC_OSX binary for java 8u101 hosted at http://download.oracle.com/otn-pub/java/jdk/8u101-b13/jdk-8u101-macosx-x64.dmg
		And a valid LINUX_64 binary for java 8u101 hosted at http://download.oracle.com/otn-pub/java/jdk/8u101-b13/jdk-8u101-linux-x64.tar.gz
		When a download request is made on "/download/java/8u101/darwin"
		Then a redirect to "http://download.oracle.com/otn-pub/java/jdk/8u101-b13/jdk-8u101-macosx-x64.dmg" is returned
		And an audit entry for java 8u101 MAC_OSX is recorded for DarwinX64
