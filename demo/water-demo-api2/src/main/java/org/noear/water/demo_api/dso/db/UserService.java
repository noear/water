package org.noear.water.demo_api.dso.db;

import org.noear.solon.annotation.XBean;
import org.noear.solon.annotation.XInject;
import org.noear.water.demo_api.model.BcfUserModel;

@XBean
public class UserService {
    @XInject
    UserDao userDao;

    public BcfUserModel getUser(long puid){
        return userDao.selectById(puid);
    }
}
