package org.home.MoneyTransfer.dao;

public enum OperationStatus {
    PREPARED(1),
    SUCCESS(2),
    ERROR(3);

    private int value;

    OperationStatus(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
