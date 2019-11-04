package org.noear.water.client.utils.ext;

public interface Fun2Ex<T1,T2,R> {
    R run(T1 t1, T2 t2) throws Exception;
}
