package api;


import org.junit.jupiter.api.Test;


import java.util.List;

import static io.restassured.RestAssured.given;

public class ReqresTest {

    private final static String REQRES_URL = "https://reqres.in";

    @Test
    public void checkAvatarAndIdTest() {
        List<UserData> users = given()
                .when()
                .get(REQRES_URL + "/api/users?page=2")
                .then()
                .log().all()
                .extract().body().jsonPath().getList("data", UserData.class);

        int i = 0;
    }
}
