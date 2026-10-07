package com.aquariux.technical.assessment.trade.mapper;

import com.aquariux.technical.assessment.trade.dto.internal.SymbolDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface CryptoPairMapper {
    

    @Select("""
            SELECT p.id as cryptoPairId, s1.symbol as baseSymbol, s2.symbol as quoteSymbol
            FROM crypto_pairs p 
            INNER JOIN symbols s1 ON p.base_symbol_id = s1.id AND p.pair_name = #{pairName}
            INNER JOIN symbols s2 ON p.quote_symbol_id = s2.id AND p.pair_name = #{pairName}
            ORDER BY s.symbol
            """)
    SymbolDto findSymbolByPair(String pairName);

    @Select("""
            SELECT id FROM crypto_pairs WHERE pair_name = #{pairName}
            """)
    Long findIdByPairName(String pairName);
}