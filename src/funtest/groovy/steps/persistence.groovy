package steps

import static cucumber.api.groovy.EN.And
import static support.MongoHelper.*

And(~/^an initialised database$/) { ->
    insertAliveInDb(db)
}

And(~/^a valid Candidate Version "(.*)" "(.*)" hosted at "(.*)"$/) { String candidate, String version, url ->
    insertCandidateVersionInDb(db, candidate, version, url)
}

And(~/^an uninitialised database$/) { ->
    clean(db)
}

And(~/^a Candidate "(.*)" does not exist$/) { String candidate ->
    clean(db)
}

And(~/^an inaccessible database$/) { ->
    //can't implement
}