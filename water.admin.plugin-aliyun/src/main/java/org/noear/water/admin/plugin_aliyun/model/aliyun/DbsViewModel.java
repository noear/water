package org.noear.water.admin.plugin_aliyun.model.aliyun;

public class DbsViewModel {

    public String version;

    public String productType;

    public String zoneId;

    public String dBInstanceClass;

    public String maxCon;

    public String iops;

    public String id;

    public String name;

    public String bandWith;

    public String qps;

    public long capacity;

    public void setCapacity(long capacity) {
        this.capacity = capacity;
    }

    public long getCapacity() {
        return capacity;
    }

    public void setQps(String qps) {
        this.qps = qps;
    }

    public String getQps() {
        return qps;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setBandWith(String bandWith) {
        this.bandWith = bandWith;
    }

    public String getName() {
        return name;
    }

    public String getBandWith() {
        return bandWith;
    }

    public String getId() {
        return id;
    }

    public String getNamel() {
        return name;
    }

    public int type;

    public void setType(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public void setZoneId(String zoneId) {
        this.zoneId = zoneId;
    }

    //https://help.aliyun.com/document_detail/26312.html?spm=a2c4g.11186623.2.3.ab8qsg
    public void setdBInstanceClass(String dBInstanceClass) {
        switch (dBInstanceClass){
            case "mysql.n1.micro.1":
                maxCon="2000";
                productType="1核/1G";
                iops="1200";
                break;
            case "rds.mysql.t1.small":
                maxCon="300";
                productType="1核/1G";
                iops="600";
                break;
            case "rds.mysql.s1.small":
                maxCon="600";
                productType="1核/2G";
                iops="1000";
                break;
            case "rds.mysql.s2.large":
                maxCon="1200";
                productType="2核/4G";
                iops="2000";
                break;
            case "rds.mysql.s2.xlarge":
                maxCon="2000";
                productType="2核/8GB";
                iops="4000";
                break;
            case "rds.mysql.s3.large":
                maxCon="2000";
                productType="4核/8G";
                iops="5000";
                break;
            case "rds.mysql.m1.medium":
                maxCon="4000";
                productType="4核/16G";
                iops="7000";
                break;
            case "rds.mysql.c1.large":
                maxCon="4000";
                productType="8核/16G";
                iops="8000";
                break;
        }
        this.dBInstanceClass = dBInstanceClass;
    }

    public void setMaxCon(String maxCon) {
        this.maxCon = maxCon;
    }

    public void setIops(String iops) {
        this.iops = iops;
    }

    public String getVersion() {
        return version;
    }

    public String getProductType() {
        return productType;
    }

    public String getZoneId() {
        return zoneId;
    }

    public String getdBInstanceClass() {
        return dBInstanceClass;
    }

    public String getMaxCon() {
        return maxCon;
    }

    public String getIops() {
        return iops;
    }
}
