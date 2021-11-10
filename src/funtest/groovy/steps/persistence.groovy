package steps

import io.cucumber.core.exception.CucumberException
import spock.util.concurrent.PollingConditions

import static io.cucumber.groovy.EN.*
import static support.MongoHelper.*

conditions = new PollingConditions(timeout: 10, initialDelay: 1.5, factor: 1.25)

And(~/^an initialised database$/) { ->
    insertAliveInDb(db)
}

And(~/^a valid (.*) binary for (.*) (.*) hosted at (.*)$/) { String platform, String candidate, String version, String url ->
    insertCandidateVersionInDb(db, candidate, version, platform, url)
}

And(~/^the binary for candidate (.*) (.*) on platform (.*) has a checksum (.*) using algorithm (.*)$/) {
    String candidate, String version, String platform, String checksum, String algorithm ->
    addChecksumToVersionInDb(db, candidate, version, platform, algorithm, checksum)
}

And(~/^an uninitialised database$/) { ->
    clean(db)
}

And(~/^a Candidate (.*) does not exist$/) { String candidate ->
    clean(db)
}

And(~/^an inaccessible database$/) { ->
    //can't implement
}

And(~/^an audit entry for (.*) (.*) (.*) is recorded for (.*)$/) { String candidate, String version, String distribution, String platform ->
    conditions.eventually {
        readAuditEntry(db, candidate, version, distribution, platform).orElseThrow { -> new CucumberException("No audit entry found for $candidate $version $distribution for $platform") }
    }
}

And(~/^an audit entry for (.*) (.*) (.*) is not recorded for (.*)$/) { String candidate, String version, String distribution, String platform ->
    conditions.eventually {
        readAuditEntry(db, candidate, version, distribution, platform).ifPresent { x -> new CucumberException("Audit entry found for $candidate $version $distribution for $platform") }
    }
}

And(~/^the Stable CLI version is "(.*)"$/) { String version ->
    insertStableCliVersionInDb(db, version)
}

And(~/^the Beta CLI version is "(.*)"$/) { String version ->
    insertBetaCliVersionInDb(db, version)
}