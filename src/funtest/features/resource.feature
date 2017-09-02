Feature: Resource

  Scenario: Download the binary resource for SDKMAN installation and selfupdate
    Given a binary resource for "SDKMAN 5.5.11+256" is hosted at "https://bintray.com/artifact/download/sdkman/generic/sdkman-cli-5.5.11+256.zip"
    When a download request is made on "/download/sdkman/install/5.5.11+256/darwin"
    Then a redirect to "https://bintray.com/artifact/download/sdkman/generic/sdkman-cli-5.5.11+256.zip" is returned
    And an audit entry for sdkman 5.5.11+256 UNIVERSAL is recorded for Darwin

