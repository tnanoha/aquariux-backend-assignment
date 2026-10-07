package com.aquariux.technical.assessment.trade.dto.response;

import lombok.Data;

@Data
public class TradeResponse {
    //TODO create unique transaction id (like UUID) giving each transaction a unique id to track.
    private String transactionId;
}