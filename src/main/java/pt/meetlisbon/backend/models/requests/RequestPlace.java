package pt.meetlisbon.backend.models.requests;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public
class RequestPlace {
    private String placeName;
    private String placeImageUrl;
    private String placeLatitude;
    private String placeLongitude;
    private String placeAddress;
    private String placeDescription;
}
