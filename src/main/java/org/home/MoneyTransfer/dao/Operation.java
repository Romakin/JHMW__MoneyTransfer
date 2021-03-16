package org.home.MoneyTransfer.dao;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Entity
public class Operation {

    @Id
    @GeneratedValue(generator = "oid-generator")
            @GenericGenerator(name = "oid-generator",
            strategy = "org.home.MoneyTransfer.dao.generator.OperationIdGenerator",//"${daoOperationGeneratorStrategy}",
            parameters = @org.hibernate.annotations.Parameter(name = "prefix", value = "OID_"))
    String operationId;

    @OneToOne @NotNull
    PayCard payCardFrom;

    @OneToOne @NotNull
    PayCard payCardTo;

    @Column(nullable = false)
    double amountValue;

    @Column(nullable = false)
    String amountCurrency;

    @Column(nullable = false)
    String verificationHash;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    OperationStatus operationStatus;

}
