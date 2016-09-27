package steps

import cucumber.runtime.CucumberException

import static cucumber.api.groovy.EN.And
import static support.MongoHelper.*

And(~/^an initialised database$/) { ->
    insertAliveInDb(db)
}

And(~/^a valid (.*) binary for (.*) (.*) hosted at (.*)$/) { String platform, String candidate, String version, String url ->
    insertCandidateVersionInDb(db, candidate, version, platform, url)
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
    readAuditEntry(db, candidate, version, distribution, platform).orElseThrow { -> new CucumberException("No audit entry found for $candidate $version $distribution for $platform") }
}

And(~/^an audit entry for (.*) (.*) (.*) is not recorded for (.*)$/) { String candidate, String version, String distribution, String platform ->
    readAuditEntry(db, candidate, version, distribution, platform).ifPresent { x -> new CucumberException("Audit entry found for $candidate $version $distribution for $platform") }
}