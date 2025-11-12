package com.hms.billing_service.enums;

public enum Status {
    ACTIVE, // Account is active and in use
    PENDING, // Account created and waiting for usage
    CLOSED, // Account is closed
    PAID,   // All dues are paid
    OVERDUE, // Payment is overdue
    FINALIZED, // The Account is finalized and no further changes can be made
}
