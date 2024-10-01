package rest_integration.utilities.api_utitilites.events.event_base;

import io.restassured.response.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rest_integration.utilities.api_utitilites.pojo.PojoBody;
import rest_integration.utilities.api_utitilites.pojo.PojoDetails;

import java.util.UUID;

import static io.restassured.RestAssured.given;
import static rest_integration.utilities.api_utitilites.reqrepspec.RequestResponseSpecifications.requestSpecification;
import static rest_integration.utilities.api_utitilites.reqrepspec.RequestResponseSpecifications.responseSpecification;


public abstract class EventBase {
    private static final Logger LOG = LoggerFactory.getLogger(EventBase.class);
    private PojoBody pojoBody;
    private PojoDetails pojoDetails;
    private String endPoint;
    private String sourceRef;

    public EventBase() {
        this.pojoBody = new PojoBody();
        this.pojoDetails = new PojoDetails();
    }

    public PojoBody getPojoBody() {
        return pojoBody;
    }

    public void setPojoBody(PojoBody pojoBody) {
        this.pojoBody = pojoBody;
    }

    public PojoDetails getPojoDetails() {
        return pojoDetails;
    }

    public void setPojoDetails(PojoDetails pojoDetails) {
        this.pojoDetails = pojoDetails;
    }

    public String getEndPoint() {
        return endPoint;
    }

    public void setEndPoint(String endPoint) {
        this.endPoint = endPoint;
    }

    public abstract PojoDetails getBody();

    public Response post() {
        this.sourceRef = UUID.randomUUID().toString();
        try {
            return
                    given()
                            .spec(requestSpecification(this.getBody()))
                            .header("SOURCE-REF", sourceRef).
                    when()
                            .post(this.endPoint).
                    then()
                            .spec(responseSpecification())
                            .extract().response();
        } catch (Exception e) {
            LOG.error("Error in post", e);
            throw new RuntimeException(e);
        }
    }

    public Response get() {
        if (this.sourceRef == null) {
            this.sourceRef = UUID.randomUUID().toString();
        }
        try {
            return
                    given()
                            .spec(requestSpecification(this.getBody()))
                            .header("SOURCE-REF", this.sourceRef).
                    when()
                            .get(this.endPoint).
                    then()
                            .spec(responseSpecification())
                            .extract().response();
        } catch (Exception e) {
            LOG.error("Error in get", e);
            throw new RuntimeException(e);
        }
    }

    public Response put() {
        try {
            return
                    given()
                            .spec(requestSpecification(this.getBody()))
                            .header("SUBSCRIPTION-REF", sourceRef).
                    when()
                            .put(this.endPoint).
                    then()
                            .spec(responseSpecification())
                            .extract().response();
        } catch (Exception e) {
            LOG.error("Error in put", e);
            throw new RuntimeException(e);
        }
    }


    public Response delete() {
        try {
            return
                    given()
                            .spec(requestSpecification(this.getBody()))
                            .header("SOURCE-REF", sourceRef).
                    when()
                            .delete(this.endPoint).
                    then()
                            .spec(responseSpecification())
                            .extract().response();
        } catch (Exception e) {
            LOG.error("Error in delete", e);
            throw new RuntimeException(e);
        }
    }
}