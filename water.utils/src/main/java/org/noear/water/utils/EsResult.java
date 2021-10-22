package org.noear.water.utils;

import java.io.Serializable;
import java.util.List;

/**
 * @author noear 2021/10/22 created
 */
public class EsResult<T> implements Serializable {
    final long total;
    final List<T> list;

    public long getTotal() {
        return total;
    }

    public int getSize() {
        if (list == null) {
            return 0;
        } else {
            return list.size();
        }
    }

    public List<T> getList() {
        return list;
    }

    public EsResult(long total, List<T> list) {
        this.total = total;
        this.list = list;
    }

    @Override
    public String toString() {
        return "EsResult{" +
                "total=" + total +
                ", list=" + list +
                '}';
    }
}
