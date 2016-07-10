Feature: Alive
	Scenario: The Server is Healthy
		Given a running service
		When the service is queried on "/alive"
		Then the service response status is 200
		And the service response body is "OK"