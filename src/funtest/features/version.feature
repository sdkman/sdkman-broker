Feature: Version

	Scenario: Read the Name and Version of the application
		Given the service is queried on "/version"
		Then the service response status is 200
		And the response contains "appName" as "SDKMAN! Broker"
		And the response contains an "appVersion"