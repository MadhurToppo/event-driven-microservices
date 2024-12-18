package com.madhurtoppo.customer.service;

import com.madhurtoppo.customer.command.event.CustomerUpdatedEvent;
import com.madhurtoppo.customer.dto.CustomerDto;
import com.madhurtoppo.customer.entity.Customer;


public interface ICustomerService {

    /**
     * @param customerDto - CustomerDto Object
     */
    void createCustomer(Customer customerDto);

    /**
     * @param mobileNumber - Input Mobile Number
     * @return Accounts Details based on a given mobileNumber
     */
    CustomerDto fetchCustomer(String mobileNumber);

    /**
     * @param customerDto - CustomerDto Object
     * @return boolean indicating if the update of Account details is successful or not
     */
    boolean updateCustomer(CustomerUpdatedEvent customerDto);

    /**
     * @param customerId - Input Customer ID
     * @return boolean indicating if the delete of Customer details is successful or not
     */
    boolean deleteCustomer(String customerId);
}
