package org.home.MoneyTransfer.controller;

import lombok.AllArgsConstructor;
import org.home.MoneyTransfer.dto.ApiRequest;
import org.home.MoneyTransfer.dto.TransferRequest;
import org.home.MoneyTransfer.dto.ConfirmRequest;
import org.home.MoneyTransfer.service.ConfirmService;
import org.home.MoneyTransfer.service.TransferService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor
@RestController
@RequestMapping("/")
@CrossOrigin(origins = "*", maxAge = 3600)
public class ApiController {

    private ConfirmService confirmService;
    private TransferService transferService;

    @PostMapping(value = "transfer")
    public ResponseEntity transfer(@RequestBody @Valid TransferRequest transferRequest) {
        ApiRequest.ApiRequestBuilder builder = ApiRequest.builder();
        if (transferService.checkValidRequest(transferRequest)) {
            String operId = transferService.transfer(transferRequest);
            if(operId != null)
                builder.statusTemp(HttpStatus.OK).operationId(operId);
            else
                builder.statusTemp(HttpStatus.INTERNAL_SERVER_ERROR).errMessageTemp("Error transfer");
        } else {
            builder.statusTemp(HttpStatus.BAD_REQUEST).errMessageTemp("Error input data");
        }
        return builder.build().getResponseEntity();
    }

    @PostMapping(value = "confirmOperation")
    public ResponseEntity confirmOperation(@RequestBody @Valid ConfirmRequest confirmRequest) {
        ApiRequest.ApiRequestBuilder builder = ApiRequest.builder();
        if (confirmService.checkValidRequest(confirmRequest)) {
            try {
                builder.statusTemp(HttpStatus.OK).operationId(confirmService.confirm(confirmRequest));
            } catch (Exception e) {
                builder.statusTemp(HttpStatus.INTERNAL_SERVER_ERROR).errMessageTemp("Error confirm");
            }
        } else {
            builder.statusTemp(HttpStatus.BAD_REQUEST).errMessageTemp("Error input data");
        }
        return builder.build().getResponseEntity();
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity catchIllegalArgumentException(IllegalArgumentException e) {
        Map<String, String> errMap = new HashMap<>();
        errMap.put("error", e.getLocalizedMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errMap);
    }
}
