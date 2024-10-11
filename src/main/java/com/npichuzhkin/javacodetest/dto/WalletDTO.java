package com.npichuzhkin.javacodetest.dto;

import jakarta.validation.constraints.*;
import lombok.Data;
import org.hibernate.validator.constraints.UUID;

@Data
public class WalletDTO {

    @NotNull(message = "WalletId field must be filled in.")
    @UUID(message = "Invalid UUID format")
    private String id;

    @NotNull(message = "OperationType field must be filled in. It can take values DEPOSIT or WITHDRAW")
    @Pattern(regexp = "^[a-zA-Z]*$", message = "OperationType field can contain only letters")
    private String operationType;

    @NotNull(message = "Amount field must be filled in.")
    @Digits(message = "Amount field can contain only numbers, 7 digits in total", integer = 7, fraction = 0)
    @Min(value = 1, message = "Amount must be at least 1")
    @Max(value = 5_000_000, message = "Amount should be no more than 5 000 000")
    private long amount;
}
