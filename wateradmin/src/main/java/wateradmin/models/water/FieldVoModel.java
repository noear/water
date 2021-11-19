package wateradmin.models.water;

import lombok.Getter;
import org.noear.weed.GetHandlerEx;
import org.noear.weed.IBinder;

@Getter
public class FieldVoModel implements IBinder {
    public String field;
    public String type;
    public String type2;
    public String key;
    public String comment;
    public String def;

    @Override
    public void bind(GetHandlerEx source) {
        field = source.get("Field").value(null);
        type = source.get("Type").value(null);
        type2 = type;
        key = source.get("Key").value(null);
        comment = source.get("Comment").value(null);
    }

    @Override
    public IBinder clone() {
        return new FieldVoModel();
    }
}
