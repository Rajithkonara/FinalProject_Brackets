package com.brackets.stockexchange.repository;

import com.brackets.stockexchange.model.Broker_customer;

/**
 * Created by rajith on 6/18/18.
 */
public interface BrokerRepositoryCustom {
    void createNewCustomerForBroker(Broker_customer broker_customer);
}