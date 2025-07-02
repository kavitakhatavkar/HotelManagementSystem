package org.example.repositories;

import org.example.models.CustomerSession;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class CustomerSessionRepositoryImpl implements CustomerSessionRepository {
    private final Map<Long,CustomerSession> sessionMap = new HashMap<>();
    @Override
    public CustomerSession save(CustomerSession customerSession) {
        sessionMap.put(customerSession.getUser().getId(), customerSession);
        return customerSession;
    }

    @Override
    public Optional<CustomerSession> findActiveCustomerSessionByUserId(long userId) {
        CustomerSession customerSession = sessionMap.get(userId);
        if (customerSession != null && customerSession.isActive()){
            return Optional.of(customerSession);
        }
        return Optional.empty();
    }
}
