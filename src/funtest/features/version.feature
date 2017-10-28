Feature: Version

  Scenario: Read the Name and Version of the application
    When a GET request is made for "/version"
    Then the service response status is 200
    And the response contains appName as SDKMAN! Broker
    And the response contains an appVersion