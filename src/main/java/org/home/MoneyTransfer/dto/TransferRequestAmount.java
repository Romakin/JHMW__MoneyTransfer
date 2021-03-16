package org.home.MoneyTransfer.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Validated
public class TransferRequestAmount {

    @Min(0)
    private Double value;

    @Pattern(regexp = "[A-Z]{3}")
    private String currency;

}
