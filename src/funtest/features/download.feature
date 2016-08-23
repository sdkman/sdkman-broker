Feature: Download a Candidate Version
	
	Scenario: Redirect to a valid target
		Given a valid Candidate Version "groovy" "2.4.7" hosted at "http://dl.bintray.com/groovy/maven/apache-groovy-binary-2.4.7.zip"
		When I request the download URI "/download/groovy/2.4.7"
		Then a "FOUND" status redirecting to "http://dl.bintray.com/groovy/maven/apache-groovy-binary-2.4.7.zip" is issued
		
	Scenario: Respond to an invalid Candidate Version
		Given a valid Candidate Version "groovy" "2.4.7" hosted at "http://dl.bintray.com/groovy/maven/apache-groovy-binary-2.4.7.zip"
		When I request the download URI "/download/groovy/2.4.8"
		Then a "NOT_FOUND" status is issued
		
	Scenario: Respond to an invalid Candidate
		Given a Candidate "groovy" does not exist
		When I request the download URI "/download/groovy"
		Then a "NOT_FOUND" status is issued
		