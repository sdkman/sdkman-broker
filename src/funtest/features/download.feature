Feature: Download a Candidate Version
	
	Scenario: Redirect to a valid target
		Given a valid UNIVERSAL Candidate Version groovy 2.4.7 hosted at http://dl.bintray.com/groovy/maven/apache-groovy-binary-2.4.7.zip
		When the service is queried on /download/groovy/2.4.7
		Then a response 302 status redirecting to http://dl.bintray.com/groovy/maven/apache-groovy-binary-2.4.7.zip is returned
		And an audit entry for groovy 2.4.7 is recorded

	Scenario: Respond to an invalid Candidate Version
		Given a valid UNIVERSAL Candidate Version groovy 2.4.7 hosted at http://dl.bintray.com/groovy/maven/apache-groovy-binary-2.4.7.zip
		When the service is queried on /download/groovy/2.4.8
		Then the service response status is 404
		And an audit entry for groovy 2.4.8 is not recorded
		
	Scenario: Respond to an invalid Candidate
		Given a Candidate groovy does not exist
		When the service is queried on /download/groovy
		Then the service response status is 404
		