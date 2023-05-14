package project.finalproject1backend.domain.constant;

public enum ProductStatus {
    PUBLIC("공개"), PRIVATE("비공개");

    private final String status;

    ProductStatus(String status) {
        this.status = status;
    }
}