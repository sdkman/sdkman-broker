package io.sdkman.broker.binary

import ratpack.handling.Context
import ratpack.http.Headers
import ratpack.http.Request
import ratpack.path.PathTokens
import spock.lang.Specification

class BinaryDownloadRequestDetailsSpec extends Specification {
    def "should extract all relevant path segments and headers to an optional details"() {
        given:
        def pathTokens = Mock(PathTokens)
        pathTokens.get("command") >> "install"
        pathTokens.get("version") >> "5.5.11+256"
        pathTokens.get("platform") >> "darwin"

        def headers = Mock(Headers)
        headers.get("user-agent") >> "curl/7.54.0"
        headers.get("X-Real-IP") >> "127.0.0.1"

        def request = Mock(Request)
        request.getHeaders() >> headers

        def ctx = Mock(Context)
        ctx.getAllPathTokens() >> pathTokens
        ctx.getRequest() >> request

        when:
        def maybeDetails = BinaryDownloadHandler.RequestDetails.of(ctx)

        then:
        maybeDetails.isPresent()
        def details = maybeDetails.get()

        and:
        details.command == "install"
        details.version == "5.5.11+256"
        details.platform == "darwin"

        and:
        details.agent == "curl/7.54.0"
        details.host == "127.0.0.1"
    }

    def "should return an emtpy optional if command path token is not populated"() {
        given:
        def pathTokens = Mock(PathTokens)
        pathTokens.get("command") >> null
        pathTokens.get("version") >> "5.5.11+256"
        pathTokens.get("platform") >> "darwin"

        def headers = Mock(Headers)
        headers.get("user-agent") >> "curl/7.54.0"
        headers.get("X-Real-IP") >> "127.0.0.1"

        def request = Mock(Request)
        request.getHeaders() >> headers

        def ctx = Mock(Context)
        ctx.getAllPathTokens() >> pathTokens
        ctx.getRequest() >> request

        when:
        def maybeDetails = BinaryDownloadHandler.RequestDetails.of(ctx)

        then:
        !maybeDetails.isPresent()
    }

    def "should return an emtpy optional if version path token is not populated"() {
        given:
        def pathTokens = Mock(PathTokens)
        pathTokens.get("command") >> "update"
        pathTokens.get("version") >> null
        pathTokens.get("platform") >> "darwin"

        def headers = Mock(Headers)
        headers.get("user-agent") >> "curl/7.54.0"
        headers.get("X-Real-IP") >> "127.0.0.1"

        def request = Mock(Request)
        request.getHeaders() >> headers

        def ctx = Mock(Context)
        ctx.getAllPathTokens() >> pathTokens
        ctx.getRequest() >> request

        when:
        def maybeDetails = BinaryDownloadHandler.RequestDetails.of(ctx)

        then:
        !maybeDetails.isPresent()
    }

    def "should return an emtpy optional if platform path token is not populated"() {
        given:
        def pathTokens = Mock(PathTokens)
        pathTokens.get("command") >> "update"
        pathTokens.get("version") >> "5.5.11+256"
        pathTokens.get("platform") >> null

        def headers = Mock(Headers)
        headers.get("user-agent") >> "curl/7.54.0"
        headers.get("X-Real-IP") >> "127.0.0.1"

        def request = Mock(Request)
        request.getHeaders() >> headers

        def ctx = Mock(Context)
        ctx.getAllPathTokens() >> pathTokens
        ctx.getRequest() >> request

        when:
        def maybeDetails = BinaryDownloadHandler.RequestDetails.of(ctx)

        then:
        !maybeDetails.isPresent()
    }
}
