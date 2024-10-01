package rest_integration.utilities.api_utitilites.events.accounts;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rest_integration.utilities.api_utitilites.events.event_base.EventBase;
import rest_integration.utilities.api_utitilites.pojo.PojoDetails;

import static io.restassured.RestAssured.given;
import static rest_integration.utilities.api_utitilites.reqrepspec.RequestResponseSpecifications.responseSpecification;
import static rest_integration.utilities.config_utilities.ConfigReader.readProperty;

public class Accounts extends EventBase {
    private static final Logger LOG = LoggerFactory.getLogger(Accounts.class);

    public Accounts(String endpoint) {
        super.setEndPoint(endpoint);
    }

    public RequestSpecification requestSpecification() {
        return given().baseUri(readProperty("defaultAPIHost.8080"))
                .log().uri()
                .log().headers()
                .contentType(ContentType.JSON)
                .relaxedHTTPSValidation();
    }

    @Override
    public Response post() {
        try {
            return
                    requestSpecification().
                            when().post(getEndPoint()).
                            then().spec(responseSpecification())
                            .extract().response();
        } catch (Exception e) {
            LOG.error("Error in post", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public Response get() {
        try {
            return
                    requestSpecification().
                            when().get(getEndPoint()).
                            then().spec(responseSpecification())
                            .extract().response();
        } catch (Exception e) {
            LOG.error("Error in get", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public PojoDetails getBody() {
        return null;
    }
}
