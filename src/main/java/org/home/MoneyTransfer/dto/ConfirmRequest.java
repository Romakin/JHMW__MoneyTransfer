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
public class ConfirmRequest {

    @NotBlank
    private String operationId;

    @NotBlank
    @Pattern(regexp = "[0-9]{4}")
    private String code;

}
