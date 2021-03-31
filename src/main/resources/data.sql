INSERT INTO Pay_Card (card_number,
                      till_date,
                      CVVHash,
                      currency,
                      balance)
VALUES ('1234123412341234',
        '02/24',
        'DC5E819E186F11EF3F59E6C7D6830C35', --009
        'RUR',
        1000);
INSERT INTO Pay_Card (card_number,
                     till_date,
                     CVVHash,
                     currency,
                     balance)
VALUES ('4321432143214321',
        '09/21',
        'A96B65A721E561E1E3DE768AC819FFBB', --409
        'RUR',
        560123);
INSERT INTO Pay_Card (card_number,
                      till_date,
                      CVVHash,
                      currency,
                      balance)
VALUES ('7894789478947894',
        '12/22',
        '99C5E07B4D5DE9D18C350CDF64C5AA3D', --567
        'USD',
        1548);

-- CURRENCY RATES
INSERT INTO Currency_Rate (rate_id, currency_from, currency_to, rate_index)
VALUES (1, 'EUR', 'RUR', 86.750);
INSERT INTO Currency_Rate (rate_id, currency_from, currency_to, rate_index)
VALUES (2, 'EUR', 'USD', 1.160);

INSERT INTO Currency_Rate (rate_id, currency_from, currency_to, rate_index)
VALUES (3, 'RUR', 'EUR', 0.011);
INSERT INTO Currency_Rate (rate_id, currency_from, currency_to, rate_index)
VALUES (4, 'RUR', 'USD', 0.013);

INSERT INTO Currency_Rate (rate_id, currency_from, currency_to, rate_index)
VALUES (5, 'USD', 'RUR', 71.85);
INSERT INTO Currency_Rate (rate_id, currency_from, currency_to, rate_index)
VALUES (6, 'USD', 'EUR', 0.806);