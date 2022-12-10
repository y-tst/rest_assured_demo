package api;

import api.spec.Specifications;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class ReqresTestNotPojo {
    private final static String REQRES_URL = "https://reqres.in";

    @Test
    @DisplayName("Avatar matches ser ID test - w/o using POJO")
    public void checkAvatarMatchIdAndEmailCorrectnessNotPojoTest() {
        Specifications.installSpecification(Specifications.requestSpec(REQRES_URL), Specifications.responseSpecOK200());

        String host = "api/users?page=2";

        Response response = given()
                .get(host)
                .then().log().all()
                .body("page", equalTo(2))
                .body("data.id", notNullValue())
                .body("data.email", notNullValue())
                .body("data.first_name", notNullValue())
                .body("data.last_name", notNullValue())
                .body("data.avatar", notNullValue())
                .extract().response();

        JsonPath jsonPath = response.jsonPath();
        List<String> emails = jsonPath.get("data.email");
        List<Integer> ids = jsonPath.get("data.id");
        List<String> avatars = jsonPath.get("data.avatar");

        for (int i = 0; i < avatars.size(); i++) {
            Assertions.assertTrue(avatars.get(i).contains(ids.get(i).toString()));
        }

        Assertions.assertTrue(emails.stream().allMatch(x -> x.endsWith("@reqres.in")));
    }

    @Test
    @DisplayName("Successful User registration test w/ POJO")
    public void successfulRegistrationTestNotPojo() {
        Specifications.installSpecification(Specifications.requestSpec(REQRES_URL), Specifications.responseSpecOK200());
        String email = "eve.holt@reqres.in";
        String password = "pistol";
        Integer expectedResponseId = 4;
        String expectedResponseToken = "QpwL5tke4Pnpja7X4";
        String host = "api/register";

        Map<String, String> user = new HashMap<>();
        user.put("email", email);
        user.put("password", password);

        // 1st way - via just given()

        given()
                .body(user)
                .when()
                .post(host)
                .then().log().all()
                .body("id", equalTo(expectedResponseId))
                .body("token", equalTo(expectedResponseToken));

        // 2nd way - via response:

        Response response = given()
                .body(user)
                .when()
                .post(host)
                .then().log().all()
                .extract().response();

        JsonPath jsonPath = response.jsonPath();

        int actualResponseId = jsonPath.get("id");
        String actualResponseToken = jsonPath.get("token");

        Assertions.assertEquals(actualResponseId, expectedResponseId);
        Assertions.assertEquals(actualResponseToken, expectedResponseToken);
    }

    @Test
    @DisplayName("Unsuccessful User registration test w/o POJO")
    public void unSuccessfulRegistrationTest() {
        Specifications.installSpecification(Specifications.requestSpec(REQRES_URL), Specifications.responseSpec400());
        String email = "sydney@fife";
        String password = "";
        String expectedError = "Missing password";
        String host = "api/register";

        Map<String, String> user = new HashMap<>();
        user.put("email", email);
        user.put("password", password);

        // 1st way - via just given()

        given()
                .body(user)
                .when()
                .post(host)
                .then().log().all()
                .body("error", equalTo(expectedError));

        // 2nd way - via response:

        Response response = given()
                .body(user)
                .when()
                .post(host)
                .then().log().all()
                .extract().response();

        JsonPath jsonPath = response.jsonPath();

        String actualError = jsonPath.get("error");

        Assertions.assertEquals(actualError, expectedError);
    }
}
