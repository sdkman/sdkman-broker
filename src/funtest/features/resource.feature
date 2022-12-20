Feature: Resource

  Scenario: Download the stable binary resource for SDKMAN installation and selfupdate
    Given a binary resource for SDKMAN "5.5.11" is hosted at "https://github.com/sdkman/sdkman-cli/releases/download/5.5.11/sdkman-cli-5.5.11.zip"
    When a download request is made on "/download/sdkman/install/5.5.11/darwin"
    Then a redirect to "https://github.com/sdkman/sdkman-cli/releases/download/5.5.11/sdkman-cli-5.5.11.zip" is returned
    And an audit entry for sdkman 5.5.11 UNIVERSAL is recorded for DarwinX64

  Scenario: Download the beta binary resource for SDKMAN installation and selfupdate
    Given a binary resource for SDKMAN "latest+abcdef" is hosted at "https://github.com/sdkman/sdkman-cli/releases/download/latest/sdkman-cli-latest+abcdef.zip"
    When a download request is made on "/download/sdkman/install/latest+abcdef/darwin"
    Then a redirect to "https://github.com/sdkman/sdkman-cli/releases/download/latest/sdkman-cli-latest+abcdef.zip" is returned
    And an audit entry for sdkman latest+abcdef UNIVERSAL is recorded for DarwinX64

  Scenario: Read the current Stable Bash SDKMAN binary resource version
    Given the Stable Bash CLI version is "5.5.5"
    When a GET request is made for "/version/sdkman/bash/stable"
    Then the service response status is 200
    And the content type is "text/plain"
    And the response is "5.5.5"

  Scenario: Read the current Stable Native SDKMAN binary resource version
    Given the Stable Native CLI version is "1.0.0"
    When a GET request is made for "/version/sdkman/native/stable"
    Then the service response status is 200
    And the content type is "text/plain"
    And the response is "1.0.0"

  Scenario: Read the current Beta Bash SDKMAN binary resource version
    Given the Beta Bash CLI version is "latest+abcdef"
    When a GET request is made for "/version/sdkman/bash/beta"
    Then the service response status is 200
    And the content type is "text/plain"
    And the response is "latest+abcdef"

  Scenario: Read the current Beta Native SDKMAN binary resource version
    Given the Beta Native CLI version is "0.0.15"
    When a GET request is made for "/version/sdkman/native/beta"
    Then the service response status is 200
    And the content type is "text/plain"
    And the response is "0.0.15"

# TODO: Retire these legacy endpoints!!!
  Scenario: Read the legacy current Stable Bash SDKMAN binary resource version
    Given the Stable Bash CLI version is "5.5.5"
    When a GET request is made for "/download/sdkman/version/stable"
    Then the service response status is 200
    And the content type is "text/plain"
    And the response is "5.5.5"

  Scenario: Read the legacy current Beta Bash SDKMAN binary resource version
    Given the Beta Bash CLI version is "latest+abcdef"
    When a GET request is made for "/download/sdkman/version/beta"
    Then the service response status is 200
    And the content type is "text/plain"
    And the response is "latest+abcdef"

