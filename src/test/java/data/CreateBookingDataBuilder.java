package data;


import net.datafaker.Faker;

import java.util.concurrent.TimeUnit;

public class CreateBookingDataBuilder {

    private static Faker faker = new Faker();

    public static BookingData createBookingData() {

        return BookingData.builder()
                .firstname(faker.name().firstName())
                .lastname(faker.name().lastName())
                .totalprice(faker.number().numberBetween(1,1000))
                .depositpaid(faker.bool().bool())
                .bookingdates(BookingDates.builder().checkin(faker.date().past(10, TimeUnit.DAYS,"YYYY-MM-dd"))
                        .checkout(faker.date().future(10,TimeUnit.DAYS,"YYYY-MM-dd")).build())
                .additionalneeds("Spanish").build();
    }

    public static PartialBookingData createPartialBookingData() {
        return PartialBookingData.builder()
                .firstname(faker.name().firstName())
                .lastname(faker.name().lastName())
                .build();
    }
}
