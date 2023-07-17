package org.example;

import data.BookingData;
import data.PartialBookingData;
import data.TokenCreds;
import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.annotations.Test;

import static data.CreateBookingDataBuilder.createBookingData;
import static data.CreateBookingDataBuilder.createPartialBookingData;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.testng.AssertJUnit.assertEquals;

public class RestfulBookerTest extends BaseSetup {
    private final BookingData NEW_BOOKING_DATA = createBookingData();
    private String authToken;
    private int bookingId;
    private final String BOOKING_ENDPOINT = "/booking";
    private final int OK_HTTP_RESPONSE = 200;

    @Test
    public void testCreateNewBooking() {
        bookingId = given()
                .body(NEW_BOOKING_DATA)
                .when()
                .post(BOOKING_ENDPOINT)
                .then()
                .statusCode(OK_HTTP_RESPONSE)
                .header("Content-Type", "application/json; charset=utf-8")
                .body("booking.firstname", equalTo(NEW_BOOKING_DATA.getFirstname()),
                        "booking.lastname", equalTo(NEW_BOOKING_DATA.getLastname()),
                        "booking.totalprice", equalTo(NEW_BOOKING_DATA.getTotalprice()),
                        "booking.depositpaid", equalTo(NEW_BOOKING_DATA.isDepositpaid()),
                        "booking.additionalneeds", equalTo(NEW_BOOKING_DATA.getAdditionalneeds()),
                        "booking.bookingdates.checkin", equalTo(NEW_BOOKING_DATA.getBookingdates().getCheckin()),
                        "booking.bookingdates.checkout", equalTo(NEW_BOOKING_DATA.getBookingdates().getCheckout()),
                        "bookingid", is(notNullValue()))
                .extract().jsonPath().get("bookingid");
    }

    @Test
    public void testGetCreatedBooking() {
        given()
                .when()
                .pathParam("id", bookingId)
                .get(BOOKING_ENDPOINT + "/{id}")
                .then()
                .statusCode(OK_HTTP_RESPONSE)
                .statusLine("HTTP/1.1 200 OK")
                .header("Content-Type", "application/json; charset=utf-8")
                .body("firstname", equalTo(NEW_BOOKING_DATA.getFirstname()),
                        "lastname", equalTo(NEW_BOOKING_DATA.getLastname()),
                        "totalprice", equalTo(NEW_BOOKING_DATA.getTotalprice()),
                        "depositpaid", equalTo(NEW_BOOKING_DATA.isDepositpaid()),
                        "additionalneeds", equalTo(NEW_BOOKING_DATA.getAdditionalneeds()),
                        "bookingdates.checkin", equalTo(NEW_BOOKING_DATA.getBookingdates().getCheckin()),
                        "bookingdates.checkout", equalTo(NEW_BOOKING_DATA.getBookingdates().getCheckout()));
    }

    @Test
    public void testCreateAuthToken() {
        TokenCreds tokenCreds = new TokenCreds();

        authToken = given()
                .body(tokenCreds)
                .when()
                .post("/auth")
                .then()
                .statusCode(OK_HTTP_RESPONSE)
                .body("token", is(notNullValue()))
                .extract().jsonPath().get("token");
    }

    @Test
    public void testUpdateExistingBooking() {
        BookingData updateBookingData = createBookingData();

        given()
                .header("Cookie", "token=" + authToken)
                .body(updateBookingData)
                .when()
                .pathParam("id", bookingId)
                .put(BOOKING_ENDPOINT + "/{id}")
                .then()
                .statusCode(OK_HTTP_RESPONSE)
                .header("Content-Type", "application/json; charset=utf-8")
                .body("firstname", equalTo(updateBookingData.getFirstname()),
                        "lastname", equalTo(updateBookingData.getLastname()),
                        "totalprice", equalTo(updateBookingData.getTotalprice()),
                        "depositpaid", equalTo(updateBookingData.isDepositpaid()),
                        "additionalneeds", equalTo(updateBookingData.getAdditionalneeds()),
                        "bookingdates.checkin", equalTo(updateBookingData.getBookingdates().getCheckin()),
                        "bookingdates.checkout", equalTo(updateBookingData.getBookingdates().getCheckout()));
    }

    @Test
    public void testUpdatePartialBookingData() {
        PartialBookingData partialBookingData = createPartialBookingData();
        given()
                .header("Cookie", "token=" + authToken)
                .body(partialBookingData)
                .when()
                .pathParam("id", bookingId)
                .patch(BOOKING_ENDPOINT + "/{id}")
                .then()
                .statusCode(OK_HTTP_RESPONSE)
                .header("Content-Type", "application/json; charset=utf-8")
                .body("firstname", equalTo(partialBookingData.getFirstname()),
                        "lastname", equalTo(partialBookingData.getLastname()));
    }

    @Test
    public void testDeleteBooking() {
        given()
                .header("Cookie", "token=" + authToken)
                .when()
                .pathParam("id", bookingId)
                .delete(BOOKING_ENDPOINT + "/{id}")
                .then()
                .statusCode(201);
    }

    @Test
    public void testGetDeletedBookingId() {
        given()
                .header("Cookie", "token=" + authToken)
                .when()
                .get("/" + bookingId)
                .then()
                .statusCode(404);
    }

    @Test
    public void testGetAllBookingIDs() {
        String bookingid = "bookingid";
        String message = "";

        String responseBody = given()
                .when()
                .get(BOOKING_ENDPOINT)
                .getBody().asString();

        JSONArray jsonArray = new JSONArray(responseBody);
        JSONObject objectID = jsonArray.getJSONObject(0);
        int bookingID = objectID.getInt(bookingid);
        System.out.println(bookingID);

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject object = jsonArray.getJSONObject(i);

            if (object.get(bookingid) == bookingid) {
                message = "Booking ID exist in the system";
            } else {
                message = "Booking ID does not exist";
            }
        }

        assertEquals(message, "Booking ID does not exist");
    }

}
