package io.sdkman.broker.download

import ratpack.handling.Context
import ratpack.http.Headers
import ratpack.http.Request
import ratpack.path.PathTokens
import ratpack.util.MultiValueMap
import spock.lang.Specification

class RequestDetailsSpec extends Specification {

    void "should extract platform query parameter"() {
        given:
        def platform = "Darwin"
        def ctx = Mock(Context)

        def pathTokens = Mock(PathTokens)
        pathTokens.get("candidate") >> "java"
        pathTokens.get("version") >> "8u111"
        ctx.getAllPathTokens() >> pathTokens

        def headers = Mock(Headers)

        def request = Mock(Request)
        request.getHeaders() >> headers
        ctx.getRequest() >> request

        def queryParams = Mock(MultiValueMap)
        queryParams.get("platform") >> platform
        request.getQueryParams() >> queryParams

        when:
        Optional<RequestDetails> details = RequestDetails.of(ctx)

        then:
        details.isPresent()
        details.get().platform == "darwin"
    }

    void "should extract platform path token"() {
        given:
        def platform = "darwin"
        def ctx = Mock(Context)

        def pathTokens = Mock(PathTokens)
        pathTokens.get("candidate") >> "java"
        pathTokens.get("version") >> "8u111"

        pathTokens.get("platform") >> platform
        ctx.getAllPathTokens() >> pathTokens

        def headers = Mock(Headers)

        def request = Mock(Request)
        request.getHeaders() >> headers
        ctx.getRequest() >> request

        def queryParams = Mock(MultiValueMap)
        request.getQueryParams() >> queryParams

        when:
        Optional<RequestDetails> details = RequestDetails.of(ctx)

        then:
        details.isPresent()
        details.get().platform == "darwin"
    }

    void "should return empty optional if candidate not present on request"() {
        given:
        def ctx = Mock(Context)

        def pathTokens = Mock(PathTokens)
        ctx.getAllPathTokens() >> pathTokens
        pathTokens.get("candidate") >> null
        pathTokens.get("version") >> "8u111"
        pathTokens.get("platform") >> "linux"

        def headers = Mock(Headers)

        def request = Mock(Request)
        request.getHeaders() >> headers
        ctx.getRequest() >> request

        def queryParams = Mock(MultiValueMap)
        request.getQueryParams() >> queryParams

        when:
        Optional<RequestDetails> details = RequestDetails.of(ctx)

        then:
        !details.isPresent()
    }

    void "should return empty optional if version not present on request"() {
        given:
        def ctx = Mock(Context)

        def pathTokens = Mock(PathTokens)
        ctx.getAllPathTokens() >> pathTokens
        pathTokens.get("candidate") >> "java"
        pathTokens.get("version") >> null
        pathTokens.get("platform") >> "linux"

        def headers = Mock(Headers)

        def request = Mock(Request)
        request.getHeaders() >> headers
        ctx.getRequest() >> request

        def queryParams = Mock(MultiValueMap)
        request.getQueryParams() >> queryParams

        when:
        Optional<RequestDetails> details = RequestDetails.of(ctx)

        then:
        !details.isPresent()
    }

    void "should return empty optional if platform not present on request"() {
        given:
        def ctx = Mock(Context)

        def pathTokens = Mock(PathTokens)
        ctx.getAllPathTokens() >> pathTokens
        pathTokens.get("candidate") >> "java"
        pathTokens.get("version") >> "8u111"
        pathTokens.get("platform") >> null

        def headers = Mock(Headers)

        def request = Mock(Request)
        request.getHeaders() >> headers
        ctx.getRequest() >> request

        def queryParams = Mock(MultiValueMap)
        request.getQueryParams() >> queryParams

        when:
        Optional<RequestDetails> details = RequestDetails.of(ctx)

        then:
        !details.isPresent()
    }


}