import static ratpack.groovy.Groovy.ratpack
import static ratpack.jackson.Jackson.json

class HealthResponse {
    String message
}

ratpack {
    handlers {
        get("alive") {
            render json(new HealthResponse(message: "OK"))
        }
    }
}
