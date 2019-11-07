package webapp.models.water_dev;

import lombok.Getter;
import lombok.Setter;
import org.noear.weed.GetHandlerEx;
import org.noear.weed.IBinder;

/**
 * 2019.01.22
 *
 * @author cjl
 */
@Getter
@Setter
public class FieldModel implements IBinder {
    public String field;
    public String type;
    public String key;
    public String comment;
    public String def;

    @Override
    public void bind(GetHandlerEx source) {
        field = source.get("Field").value(null);
        type = source.get("Type").value(null);
        key = source.get("Key").value(null);
        comment = source.get("Comment").value(null);
    }

    @Override
    public IBinder clone() {
        return new FieldModel();
    }
}
