package org.home.MoneyTransfer.service;

import lombok.AllArgsConstructor;
import org.home.MoneyTransfer.dto.ApiRequest;
import org.home.MoneyTransfer.dto.ConfirmRequest;
import org.home.MoneyTransfer.dto.TransferRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class ApiService {

    private ConfirmService confirmService;
    private TransferService transferService;

    /**
     * Validating request and create transfer operation
     * @param request
     * @return
     */
    public ResponseEntity transfer(TransferRequest request) {
        // validate request
        // do transfer
        HttpStatus status;
        String errMsg = null, operId = null;
        if (transferService.checkValidRequest(request)) {
            operId = transferService.transfer(request);
            if (operId != null) {
                status = HttpStatus.OK;
            } else {
                status = HttpStatus.INTERNAL_SERVER_ERROR;
                errMsg = "Error transfer";
            }
        } else {
            status = HttpStatus.BAD_REQUEST;
            errMsg = "Error input data";
        }
        return ApiRequest.builder()
                .statusTemp(status)
                .errMessageTemp(errMsg)
                .operationId(operId).build().getResponseEntity();
    }

    /**
     * Validating request and confirm operation
     * @param request
     * @return
     */
    public ResponseEntity confirm(ConfirmRequest request) {
        HttpStatus status;
        String errMsg = null, operId = null;
        if (confirmService.checkValidRequest(request)) {
            try {
                status = HttpStatus.OK;
                operId = confirmService.confirm(request);
            } catch (Exception e) {
                status = HttpStatus.INTERNAL_SERVER_ERROR;
                errMsg = "Error confirm";
            }
        } else {
            status = HttpStatus.BAD_REQUEST;
            errMsg = "Error input data";
        }
        return ApiRequest.builder()
                .statusTemp(status)
                .errMessageTemp(errMsg)
                .operationId(operId).build().getResponseEntity();
    }

}
