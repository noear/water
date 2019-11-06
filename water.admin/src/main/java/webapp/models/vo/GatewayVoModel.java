package webapp.models.vo;

import lombok.Getter;
import lombok.Setter;
import webapp.models.water.ServiceModel;
import webapp.models.water.ServiceSpeedModel;

/**
 * 2019.01.22
 *
 * @author cjl
 */
@Getter
@Setter
public class GatewayVoModel {
    public ServiceModel service;
    public ServiceSpeedModel speed;
}
