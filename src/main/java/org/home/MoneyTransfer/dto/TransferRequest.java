package org.home.MoneyTransfer.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Validated
public class TransferRequest {

    @NotBlank
    @Pattern(regexp = "[0-9]{19}")
    private String cardFromNumber;

    @NotBlank
    @Pattern(regexp = "[0-9]{2}/[0-9]{2}")
    private String cardFromValidTill;

    @NotBlank
    @Pattern(regexp = "[0-9]{3}")
    private String cardFromCVV;

    @NotBlank
    @Pattern(regexp = "[0-9]{19}")
    private String cardToNumber;

    private TransferRequestAmount amount;


}
