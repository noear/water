package wateradmin.models;

import lombok.Getter;

import java.io.Serializable;

@Getter
public class TagCountsModel implements Serializable {
    public String tag;
    public long counts;
    public String note;
}
