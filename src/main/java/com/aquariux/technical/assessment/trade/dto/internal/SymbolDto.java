package com.aquariux.technical.assessment.trade.dto.internal;

import lombok.Data;

@Data
public class SymbolDto {
    private String baseSymbol;
    private String quoteSymbol;
    private Long cryptoPairId;
}