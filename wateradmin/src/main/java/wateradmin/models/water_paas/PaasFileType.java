package wateradmin.models.water_paas;

public enum PaasFileType {
    api(0),
    pln(1),
    tml(2),
    all(9);

    public final int code;
    PaasFileType(int code) {
        this.code = code;
    }
}
