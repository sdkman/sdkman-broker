Feature: Resource

  Scenario: Download the binary resource for SDKMAN installation and selfupdate
    Given a binary resource for "SDKMAN 5.5.11+256" is hosted at "https://sdkman.nyc3.digitaloceanspaces.com/dist/sdkman-cli-5.5.11+256.zip"
    When a download request is made on "/download/sdkman/install/5.5.11+256/darwin"
    Then a redirect to "https://sdkman.nyc3.digitaloceanspaces.com/dist/sdkman-cli-5.5.11+256.zip" is returned
    And an audit entry for sdkman 5.5.11+256 UNIVERSAL is recorded for DarwinX64

  Scenario: Read the current Stable SDKMAN binary resource version
    Given the Stable CLI version is "5.5.5+145"
    When a GET request is made for "/download/sdkman/version/stable"
    Then the service response status is 200
    And the content type is "text/plain"
    And the response is "5.5.5+145"

  Scenario: Read the current Beta SDKMAN binary resource version
    Given the Beta CLI version is "master+146"
    When a GET request is made for "/download/sdkman/version/beta"
    Then the service response status is 200
    And the content type is "text/plain"
    And the response is "master+146"
