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

And(~/^an audit entry for (.*) (.*) (.*) is recorded$/) { String candidate, String version, String platform ->
    readAuditEntry(db, candidate, version, platform).orElseThrow { -> new CucumberException("No audit entry found for $candidate $version on $platform") }
}

And(~/^an audit entry for (.*) (.*) (.*) is not recorded$/) { String candidate, String version, String platform ->
    readAuditEntry(db, candidate, version, platform).ifPresent { x -> new CucumberException("Audit entry found for $candidate $version on $platform") }
}