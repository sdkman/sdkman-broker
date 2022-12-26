Feature: Resource

  Scenario: Download the stable bash binary zip for SDKMAN installation and selfupdate
    Given a binary resource for SDKMAN "5.5.11" is hosted at "https://github.com/sdkman/sdkman-cli/releases/download/5.5.11/sdkman-cli-5.5.11.zip"
    When a download request is made on "/download/sdkman/install/5.5.11/darwin"
    Then a redirect to "https://github.com/sdkman/sdkman-cli/releases/download/5.5.11/sdkman-cli-5.5.11.zip" is returned
    And an audit entry for sdkman 5.5.11 UNIVERSAL is recorded for DarwinX64

  Scenario: Download the beta bash binary zip for SDKMAN installation and selfupdate
    Given a binary resource for SDKMAN "latest+abcdef" is hosted at "https://github.com/sdkman/sdkman-cli/releases/download/latest/sdkman-cli-latest+abcdef.zip"
    When a download request is made on "/download/sdkman/install/latest+abcdef/darwin"
    Then a redirect to "https://github.com/sdkman/sdkman-cli/releases/download/latest/sdkman-cli-latest+abcdef.zip" is returned
    And an audit entry for sdkman latest+abcdef UNIVERSAL is recorded for DarwinX64

  Scenario Outline:
    Given the <channel> <implementation> CLI version is <version>
    When a GET request is made for "<url>"
    Then the service response status is 200
    And the content type is "text/plain"
    And the response is "<response>"
    Examples:
    |channel   | implementation  | version       | url                             | response       |
    |stable    | bash            | 5.5.5         | /version/sdkman/bash/stable     | 5.5.5          |
    |stable    | native          | 1.0.0         | /version/sdkman/native/stable   | 1.0.0          |
    |beta      | bash            | latest+abcdef | /version/sdkman/bash/beta       | latest+abcdef  |
    |beta      | native          | 0.1.0         | /version/sdkman/native/beta     | 0.1.0          |

# TODO: Retire these legacy endpoints!!!
  Scenario: Read the legacy current Stable Bash SDKMAN binary resource version
    Given the stable bash CLI version is 5.5.5
    When a GET request is made for "/download/sdkman/version/stable"
    Then the service response status is 200
    And the content type is "text/plain"
    And the response is "5.5.5"

  Scenario: Read the legacy current Beta Bash SDKMAN binary resource version
    Given the beta bash CLI version is latest+abcdef
    When a GET request is made for "/download/sdkman/version/beta"
    Then the service response status is 200
    And the content type is "text/plain"
    And the response is "latest+abcdef"

