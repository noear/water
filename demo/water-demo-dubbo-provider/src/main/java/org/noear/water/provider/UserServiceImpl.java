package org.noear.water.provider;

import org.apache.dubbo.config.annotation.Service;
import org.noear.water.demo_dubbo.service.UserService;

@Service
public class UserServiceImpl implements UserService {
    @Override
    public String getUser(String name) {
        return name;
    }
}
