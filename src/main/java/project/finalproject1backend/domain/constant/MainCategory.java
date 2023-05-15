package project.finalproject1backend.domain.constant;

import lombok.Getter;
@Getter
public enum MainCategory {


    GUEST_ROOM_SUPPLIES("객실용품"),
    BATHROOM_SUPPLIES("욕실용품"),
    BEVERAGE("식음료"),
    HYGIENE_SUPPLIES("위생용품"),
    BEDDING("침구류"),
    SAFETY_EQUIPMENT("소방/안전설비"),
    ELECTRONIC_APPLIANCES("전자제품"),
    CLEANING_FACILITY_MANAGEMENT("청소/시설관리"),
    OFFICE_SUPPLIES("사무용품");

    private final String category;

    MainCategory(String category) {
        this.category = category;}
}