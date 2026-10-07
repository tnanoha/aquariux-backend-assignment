package com.aquariux.technical.assessment.trade.service.impl;

import com.aquariux.technical.assessment.trade.dto.internal.SymbolDto;
import com.aquariux.technical.assessment.trade.dto.internal.UserDto;
import com.aquariux.technical.assessment.trade.dto.internal.UserWalletDto;
import com.aquariux.technical.assessment.trade.dto.request.TradeRequest;
import com.aquariux.technical.assessment.trade.dto.response.TradeResponse;
import com.aquariux.technical.assessment.trade.entity.CryptoPrice;
import com.aquariux.technical.assessment.trade.entity.Trade;
import com.aquariux.technical.assessment.trade.entity.UserWallet;
import com.aquariux.technical.assessment.trade.enums.TradeType;
import com.aquariux.technical.assessment.trade.mapper.CryptoPriceMapper;
import com.aquariux.technical.assessment.trade.mapper.CryptoPairMapper;
import com.aquariux.technical.assessment.trade.mapper.TradeMapper;
import com.aquariux.technical.assessment.trade.mapper.UserMapper;
import com.aquariux.technical.assessment.trade.mapper.UserWalletMapper;
import com.aquariux.technical.assessment.trade.service.TradeServiceInterface;
import lombok.RequiredArgsConstructor;
import java.time.LocalDateTime;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TradeServiceImpl implements TradeServiceInterface {

    private final TradeMapper tradeMapper;
    private final UserMapper userMapper;
    private final UserWalletMapper userWalletMapper;
    private final CryptoPriceMapper crpytoPriceMapper;
    private final CryptoPairMapper cryptoPairMapper;

    // Add additional beans here if needed for your implementation

    @Override
    public TradeResponse executeTrade(TradeRequest tradeRequest) {
        // TODO: Implement the core trading engine
        // What should happen when a user executes a trade?

        //Step 1 validations
        //check if userId exist
        UserDto user = userMapper.findById(tradeRequest.getUserId());

        if(user == null){
            throw new UnsupportedOperationException("User not found.");
        }

        //check if user has enough balance based on base symbol pair
        SymbolDto symbolDto = cryptoPairMapper.findSymbolByPair(tradeRequest.getPairSymbol());

        if(symbolDto == null){
            throw new UnsupportedOperationException("Invalid Symbol.");
        }

        List<UserWalletDto> walletList = userWalletMapper.findByUserId(tradeRequest.getUserId());

        foreach(UserWalletDto u in walletList){
            if(u.getSymbol() == symbolDto.getBaseSymbol()){
                if(u.getBalance() < tradeRequest.getAmount()){
                    throw new UnsupportedOperationException("Insufficient funds.");
                }
            }
        }
        
        //Step 2 create Trade entity based on verified data
        List<CryptoPrice> priceList =  crpytoPriceMapper.findLatestPrices();
        BigDecimal price = null;

        foreach(CryptoPrice p in priceList){
            if(p.getCryptoPairId() == symbolDto.getCryptoPairId()){
                if(tradeRequest.getTradeType().equal(TradeType.BUY)){
                    price = p.getBidPrice();
                }

                else if(tradeRequest.getTradeType().equal(TradeType.SELL)){
                    price = p.getAskPrice();
                }
            }
        }

        if(price === null){
            throw new UnsupportedOperationException("Invalid Symbol.");
        }

        Trade trade = new Trade();
        trade.setUserId(tradeRequest.getUserId());
        trade.setCryptoPairId(tradeRequest.getPairSymbol());
        trade.setTradeType(tradeRequest.getTradeType());
        trade.setQuantity(tradeRequest.getAmount());
        trade.setPrice(price);
        trade.setTotalAmount(tradeRequest.getAmount() * price);
        trade.setTradeTime(LocalDateTime.now());

        //Step 3 insert Trade entity
        trade = tradeMapper.insertTrade(trade);

        //Step 4 create response dto
        TradeResponse response = new TradeResponse();
        //set id for now to simulate it having a unique id
        response.setTransactionId = trade.getId();

        return response;
    }
}