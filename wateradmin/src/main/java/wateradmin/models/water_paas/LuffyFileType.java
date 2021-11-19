package wateradmin.models.water_paas;

public enum LuffyFileType {
    api(0),
    pln(1),
    tml(2),
    msg(3),
    all(9);

    public final int code;
    LuffyFileType(int code) {
        this.code = code;
    }
}
