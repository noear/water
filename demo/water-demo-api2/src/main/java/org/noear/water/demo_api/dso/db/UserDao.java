package org.noear.water.demo_api.dso.db;

import org.noear.water.demo_api.model.BcfUserModel;
import org.noear.weed.BaseMapper;
import org.noear.weed.annotation.Db;

@Db("water")
public interface UserDao extends BaseMapper<BcfUserModel> {
}
