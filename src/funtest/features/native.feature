Feature: Native SDKMAN binaries

  Scenario Outline: Download the native binary resource for SDKMAN installation and selfupdate
    Given a native "<distribution>" binary resource for SDKMAN "<version>" is hosted at "<resolved_url>"
    When a download request is made on "<broker_uri>"
    Then the service response status is 302
    And a redirect to "<resolved_url>" is returned
    And an audit entry for sdkman_native <version> <distribution> is recorded for <platform>
    Examples:
    | platform    | distribution  | version | broker_uri                                   | resolved_url                                                                                                               |
    | DarwinX64   | MAC_OSX       | 0.1.0   | /download/native/install/0.1.0/darwinx64     | https://github.com/sdkman/sdkman-cli-native/releases/download/v0.1.0/sdkman-cli-native-0.1.0-x86_64-apple-darwin.zip       |
    | DarwinARM64 | MAC_ARM64     | 0.1.0   | /download/native/install/0.1.0/darwinarm64   | https://github.com/sdkman/sdkman-cli-native/releases/download/v0.1.0/sdkman-cli-native-0.1.0-aarch64-apple-darwin.zip      |
    | LinuxX64    | LINUX_64      | 0.1.0   | /download/native/install/0.1.0/linuxx64      | https://github.com/sdkman/sdkman-cli-native/releases/download/v0.1.0/sdkman-cli-native-0.1.0-x86_64-unknown-linux-gnu.zip  |
    | LinuxARM64  | LINUX_ARM64   | 0.1.0   | /download/native/install/0.1.0/linuxarm64    | https://github.com/sdkman/sdkman-cli-native/releases/download/v0.1.0/sdkman-cli-native-0.1.0-aarch64-unknown-linux-gnu.zip |
    | WindowsX64  | WINDOWS_64    | 0.1.0   | /download/native/install/0.1.0/cygwin_nt-6.1 | https://github.com/sdkman/sdkman-cli-native/releases/download/v0.1.0/sdkman-cli-native-0.1.0-x86_64-pc-windows-msvc.zip    |
