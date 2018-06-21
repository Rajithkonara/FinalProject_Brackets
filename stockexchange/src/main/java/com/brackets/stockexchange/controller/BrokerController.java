package com.brackets.stockexchange.controller;

import com.brackets.stockexchange.model.Broker;
import com.brackets.stockexchange.model.Broker_customer;
import com.brackets.stockexchange.model.Broker_stocks;
import com.brackets.stockexchange.model.User;
import com.brackets.stockexchange.repository.BrokerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/broker")
public class BrokerController {

    @Autowired
    private BrokerRepository brokerRepository;

    @RequestMapping(path="/all", method = RequestMethod.GET)
    public @ResponseBody Iterable<Broker> getAllBrokers() {
        return brokerRepository.findAll();
    }

    @RequestMapping(path = "/save", method = RequestMethod.POST)
    public @ResponseBody String createNewUser(@RequestBody Broker broker) {
            brokerRepository.save(broker);
            Broker broker1 = new Broker();
            broker1.setUsername(broker.getUsername());
            return "Saved";
    }

    @RequestMapping(path = "/customer", method = RequestMethod.POST)
    public @ResponseBody String createNewBrokerCustomer(@RequestBody Broker_customer b_customer) {

        Broker_customer broker_customer = new Broker_customer();
        User user = new User();
        broker_customer.setBroker_name(broker_customer.getBroker_name());
        user.setUsername(b_customer.getCustomer_name());
        brokerRepository.createNewCustomerForBroker(b_customer);
        return "saved";
    }
        
    @RequestMapping(path = "/qty", method = RequestMethod.POST)
    public @ResponseBody String balance(@RequestBody Broker_stocks user) {
            boolean cus = brokerRepository.checkQty(user);
            return String.valueOf(cus);
    }

    @RequestMapping(path = "/sell", method = RequestMethod.POST)
    public @ResponseBody String sellstocks(@RequestBody Broker_customer b_customer) {
         Broker_customer broker_customer = new Broker_customer();
         broker_customer.setPrice_sell(b_customer.getPrice_sell());
         broker_customer.setCustomer_name(b_customer.getCustomer_name());
         broker_customer.setBroker_name(b_customer.getBroker_name());
         broker_customer.setStocks(b_customer.getStocks());

         if (brokerRepository.sellStocks(broker_customer)) {
             return "Sold Successfully";
         }else {
             return "No Matching record found";
         }
    }

}
