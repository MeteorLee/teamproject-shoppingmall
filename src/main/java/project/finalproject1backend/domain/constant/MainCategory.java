package project.finalproject1backend.domain.constant;

import lombok.Getter;
@Getter
public enum MainCategory {


    GUEST_ROOM_SUPPLIES("객실용품"),
    BATHROOM_SUPPLIES("욕실용품"),
    HYGIENE_SUPPLIES("위생용품"),
    BEDDING("침구류"),
    ELECTRONIC_APPLIANCES("전자제품"),
    CLEANING_FACILITY_MANAGEMENT("청소/시설관리");

    private final String category;

    MainCategory(String category) {
        this.category = category;}
}