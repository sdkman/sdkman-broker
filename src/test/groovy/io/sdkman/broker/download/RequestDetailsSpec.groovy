package io.sdkman.broker.download

import ratpack.handling.Context
import ratpack.http.Headers
import ratpack.http.Request
import ratpack.path.PathTokens
import ratpack.util.MultiValueMap
import spock.lang.Specification

public class RequestDetailsSpec extends Specification {

    void "should extract platform query parameter"() {
        given:
        def platform = "Darwin"
        def ctx = Mock(Context)

        def pathTokens = Mock(PathTokens)
        ctx.getAllPathTokens() >> pathTokens

        def headers = Mock(Headers)

        def request = Mock(Request)
        request.getHeaders() >> headers
        ctx.getRequest() >> request

        def queryParams = Mock(MultiValueMap)
        queryParams.get("platform") >> platform
        request.getQueryParams() >> queryParams

        when:
        def details = RequestDetails.of(ctx)

        then:
        details.uname == "darwin"
    }

    void "should extract platform path token"() {
        def platform = "darwin"
        def ctx = Mock(Context)

        def pathTokens = Mock(PathTokens)
        pathTokens.get("platform") >> platform
        ctx.getAllPathTokens() >> pathTokens

        def headers = Mock(Headers)

        def request = Mock(Request)
        request.getHeaders() >> headers
        ctx.getRequest() >> request

        def queryParams = Mock(MultiValueMap)
        request.getQueryParams() >> queryParams

        when:
        def details = RequestDetails.of(ctx)

        then:
        details.uname == "darwin"
    }
}