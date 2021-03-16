package org.home.MoneyTransfer.controller;

import lombok.AllArgsConstructor;
import org.home.MoneyTransfer.dto.ApiRequest;
import org.home.MoneyTransfer.dto.TransferRequest;
import org.home.MoneyTransfer.dto.ConfirmRequest;
import org.home.MoneyTransfer.service.ApiService;
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

    ApiService service;

    @PostMapping(value = "transfer", produces = { "application/json" }, consumes = { "application/json" })
    public ResponseEntity transfer(@RequestBody @Valid TransferRequest tr) {
        return service.transfer(tr);
    }

    @PostMapping(value = "confirmOperation", produces = { "application/json" }, consumes = { "application/json" })
    public ResponseEntity confirmOperation(@RequestBody @Valid ConfirmRequest cr) {
        return service.confirm(cr);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity catchIllegalArgumentException(IllegalArgumentException e) {
        Map<String, String> errMap = new HashMap<>();
        errMap.put("error", e.getLocalizedMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errMap);
    }
}
