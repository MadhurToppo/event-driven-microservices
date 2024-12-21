package com.madhurtoppo.customer.service.impl;

import com.madhurtoppo.common.event.CustomerDataChangedEvent;
import com.madhurtoppo.customer.command.event.CustomerUpdatedEvent;
import com.madhurtoppo.customer.constants.CustomerConstants;
import com.madhurtoppo.customer.dto.CustomerDto;
import com.madhurtoppo.customer.entity.Customer;
import com.madhurtoppo.customer.exception.CustomerAlreadyExistsException;
import com.madhurtoppo.customer.exception.ResourceNotFoundException;
import com.madhurtoppo.customer.mapper.CustomerMapper;
import com.madhurtoppo.customer.repository.CustomerRepository;
import com.madhurtoppo.customer.service.ICustomerService;
import lombok.AllArgsConstructor;
import org.axonframework.eventhandling.gateway.EventGateway;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
@AllArgsConstructor
public class CustomerServiceImpl implements ICustomerService {

    private CustomerRepository customerRepository;
    private EventGateway eventGateway;


    @Override
    public void createCustomer(Customer customer) {
        Optional<Customer> optionalCustomer = customerRepository.findByMobileNumberAndActiveSw(
                customer.getMobileNumber(), true);
        if (optionalCustomer.isPresent()) {
            throw new CustomerAlreadyExistsException("Customer already registered with given mobileNumber "
                    + customer.getMobileNumber());
        }
        Customer savedCustomer = customerRepository.save(customer);
    }


    @Override
    public CustomerDto fetchCustomer(String mobileNumber) {
        Customer customer = customerRepository.findByMobileNumberAndActiveSw(mobileNumber, true).orElseThrow(
                () -> new ResourceNotFoundException("Customer", "mobileNumber", mobileNumber)
        );
        return CustomerMapper.mapToCustomerDto(customer, new CustomerDto());
    }


    @Override
    public boolean updateCustomer(CustomerUpdatedEvent customerUpdatedEvent) {
        Customer customer = customerRepository.findByMobileNumberAndActiveSw(customerUpdatedEvent.getMobileNumber(), true)
                .orElseThrow(() -> new ResourceNotFoundException("Customer", "mobileNumber", customerUpdatedEvent.getMobileNumber()));
        CustomerMapper.mapEventToCustomer(customerUpdatedEvent, customer);
        customerRepository.save(customer);
        return true;
    }


    @Override
    public boolean deleteCustomer(String customerId) {
        Customer customer = customerRepository.findById(customerId).orElseThrow(
                () -> new ResourceNotFoundException("Customer", "customerId", customerId)
        );
        customer.setActiveSw(CustomerConstants.IN_ACTIVE_SW);
        customerRepository.save(customer);
        CustomerDataChangedEvent customerDataChangedEvent = new CustomerDataChangedEvent();
        customerDataChangedEvent.setMobileNumber(customer.getMobileNumber());
        customerDataChangedEvent.setActiveSw(CustomerConstants.IN_ACTIVE_SW);
        eventGateway.publish(customerDataChangedEvent);
        return true;
    }

}
