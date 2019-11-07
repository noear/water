package webapp.models.water;

import lombok.Getter;
import org.noear.weed.GetHandlerEx;
import org.noear.weed.IBinder;

@Getter
public class EnumModel //implements IBinder
{
    public int id;
    public String tag;
    public String type;
    public int value;
    public String title;
}