package com.brackets.stockexchange.repository;

import com.brackets.stockexchange.model.Bank;
import com.brackets.stockexchange.model.Broker;
import com.brackets.stockexchange.model.Broker_customer;
import com.brackets.stockexchange.model.Broker_stocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

/**
 * Created by rajith on 6/18/18.
 */
@Repository
public class BrokerRepositoryImpl implements BrokerRepositoryCustom {

    @PersistenceContext
    EntityManager entityManager;


    @Autowired
    private BankRepository bankRepository;

    @Override
    @Transactional
    public void createNewCustomerForBroker(Broker_customer broker_customer) {
        Query query = entityManager.createNativeQuery("INSERT INTO broker_customer VALUES (?,?)", Broker_customer.class);
        query.setParameter(1, broker_customer.getBroker_name());
        query.setParameter(2, broker_customer.getCustomer_name());
        entityManager.persist(broker_customer);
        entityManager.flush();
    }

    @Override
    @Transactional
    public String checkQty(Broker_stocks broker_stocks) {
        int cqty = broker_stocks.getQuantity();
        String cname = broker_stocks.getCusName();
        String bname = broker_stocks.getBroker_name();
        Query query = entityManager.createNativeQuery("select * from broker_stocks where (stock = ? and broker_name = ?)", Broker_stocks.class);
        query.setParameter(1, broker_stocks.getStock());
        query.setParameter(2, bname);
        query.setMaxResults(1);
        if (query.getResultList().size() == 0) {
            return "No Account Found";
        }
        Broker_stocks cus = (Broker_stocks) query.getSingleResult();

        Broker_customer customer = new Broker_customer();
        customer.setBroker_name(cus.getBroker_name());
        customer.setCustomer_name(cname);
        customer.setPrice(cus.getPrice());
        customer.setPrice_bought(broker_stocks.getPrice());
        customer.setPrice_sell(0);
        customer.setQuantity(cqty);
        customer.setStocks(cus.getStock());
        int qty = cus.getQuantity();
        if (cqty <= qty) {
            updateBrokerStocks(cus, cqty);

            Bank bank = bankRepository.balance(cname);
            int balance = (int) bank.getBalance();
            if (balance < broker_stocks.getPrice()) {
                return "No Sufficient credits available";
            } else {
                if (checkUserStock(broker_stocks.getBroker_name(), cname, broker_stocks.getStock())) {
                    addToBrokerCustomer(customer, cqty, cname);
                    bankRepository.deduct(cname, broker_stocks.getPrice());
                } else {
                    updateSameStocksForAUser(customer);
                    bankRepository.deduct(cname, broker_stocks.getPrice());
                }
            }
            return "Buy successfully";
        } else {
            return "No enough Stocks";
        }
    }

    /**
     * Check for already added stocks
     * @return
     */
    @Override
    public boolean checkUserStock(String bname, String cusName, String stock) {
        Query query = entityManager.createNativeQuery("SELECT * FROM broker_customer WHERE broker_name = ? AND customer_name = ? AND  stocks = ?", Broker_customer.class);
        query.setParameter(1, bname);
        query.setParameter(2, cusName);
        query.setParameter(3, stock);
        query.setMaxResults(1);
        if (query.getResultList().size() == 0) {
            return true;
        }
            return false;
    }

    @Override
    @Transactional
    public void updateSameStocksForAUser(Broker_customer broker_customer) {
        Query query = entityManager.createNativeQuery("UPDATE  broker_customer set price_bought=(?), quantity=quantity+(?) WHERE customer_name=(?) AND broker_name=(?)  AND stocks=(?)", Broker_customer.class);
        query.setParameter(1, broker_customer.getPrice_bought());
        query.setParameter(2, broker_customer.getQuantity());
        query.setParameter(3, broker_customer.getCustomer_name());
        query.setParameter(4,broker_customer.getBroker_name());
        query.setParameter(5,broker_customer.getStocks());
        query.executeUpdate();
    }

    @Override
    public List<Broker> getcustomerinfo(Broker_customer b_name) {
        Query query = entityManager.createNativeQuery("SELECT * FROM broker_customer WHERE broker_name = ?", Broker_customer.class);
        query.setParameter(1, b_name.getBroker_name());
        return query.getResultList();
    }

    @Override
    @Transactional
    public void updateBrokerStocks(Broker_stocks broker_stocks, int qty) {
        Query query = entityManager.createNativeQuery("UPDATE broker_stocks set quantity=quantity-(?) WHERE stock=(?)", Broker_stocks.class);
        query.setParameter(1, qty);
        query.setParameter(2, broker_stocks.getStock());
        query.executeUpdate();
    }

    @Deprecated
    @Override
    @Transactional
    public void addTomBrokerCustomer(Broker_stocks broker_stocks, int qty, String cname) {
        Query query = entityManager.createNativeQuery("UPDATE broker_customer set price_bought=(?),quantity=(?),stocks=(?) WHERE customer_name=(?)", Broker_stocks.class);
        query.setParameter(1, broker_stocks.getPrice());
        query.setParameter(2, qty);
        query.setParameter(3, broker_stocks.getStock());
        query.setParameter(4, cname);
        query.executeUpdate();
    }

    @Override
    @Transactional
    public void addToBrokerCustomer(Broker_customer broker_customer, int qty, String cname) {
        Query query = entityManager.createNativeQuery("INSERT INTO broker_customer VALUES (?,?,?,?,?,?,?)", Broker_stocks.class);
        query.setParameter(1, broker_customer.getBroker_name());
        query.setParameter(2, broker_customer.getCustomer_name());
        query.setParameter(3, 0);
        query.setParameter(4, broker_customer.getPrice());
        query.setParameter(5,0);
        query.setParameter(6, qty);
        query.setParameter(7, broker_customer.getStocks());
        entityManager.persist(broker_customer);
        entityManager.flush();
    }


    @Override
    @Transactional
    public boolean sellStocks(Broker_customer broker_customer) {
        Query query = entityManager.createNativeQuery("UPDATE  broker_customer set price_sell=(?) WHERE customer_name=(?) AND broker_name=(?)  AND stocks=(?)", Broker_customer.class);
        query.setParameter(1, broker_customer.getPrice_sell());
        query.setParameter(2, broker_customer.getCustomer_name());
        query.setParameter(3, broker_customer.getBroker_name());
        query.setParameter(4,broker_customer.getStocks());

        if (query.executeUpdate() == 1) {
            bankRepository.deposit(broker_customer.getCustomer_name(), broker_customer.getPrice_sell());
            return true;
        }else {
            return false;
        }
    }

}
