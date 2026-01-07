package com.example.monitoringbackend.model;

public enum Condition {
    UNKNOWN,VERY_GOOD,GOOD,POOR,OOS;

    public Condition next() {
        int nextOrdinal = this.ordinal() + 1;
        Condition[] values = Condition.values();

        // If already last, stay last
        if (nextOrdinal >= values.length) {
            return this;
        }
        return values[nextOrdinal];
    }
}
