Feature: Alive
	Scenario: Database is healthy
		Given an initialised database
		When the service is queried on "/health/alive"
		Then the service response status is 200

	Scenario: Database is inconsistent
		Given an uninitialised database
		When the service is queried on "/health/alive"
		Then the service response status is 503

 #can't implement because of infrastructure
	@manual
	Scenario: Database is inaccessible
		Given an inaccessible database
		When the service is queried on "/health/alive"
		Then the service response status is 503
