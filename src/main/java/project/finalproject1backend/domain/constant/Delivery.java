package project.finalproject1backend.domain.constant;

import lombok.Getter;

@Getter

public enum Delivery {


    DOMESTIC("국내배송"),
    OVERSEA("해외배송");


    private final String ship;

    Delivery(String ship) {
        this.ship = ship;
    }
}
