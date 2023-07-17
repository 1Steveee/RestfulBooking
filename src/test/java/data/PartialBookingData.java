package data;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PartialBookingData {

    private String firstname;
    private String lastname;

}
