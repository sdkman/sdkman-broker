import static cucumber.api.groovy.Hooks.After
import static support.MongoHelper.*

if(!binding.hasVariable("db")) {
    db = prepareDB()
}

After { scenario ->
    clean(db)
}