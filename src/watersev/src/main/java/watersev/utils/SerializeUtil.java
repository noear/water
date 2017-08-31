package watersev.utils;

import watersev.dao.LogUtil;

import java.io.*;

public class SerializeUtil {
    public static String toString(Object obj) {
        String objBody = null;
        ByteArrayOutputStream baops = null;
        ObjectOutputStream oos = null;

        try {
            baops = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(baops);
            oos.writeObject(obj);
            byte[] bytes = baops.toByteArray();
            objBody = new String(bytes);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (oos != null)
                    oos.close();
                if (baops != null)
                    baops.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return objBody;
    }

    @SuppressWarnings("unchecked")
    public static <T extends Serializable> T byString(String objBody, Class<T> clazz)
    {
        byte[] bytes = objBody.getBytes();
        ObjectInputStream ois = null;
        T obj = null;
        try {
            ois = new ObjectInputStream(new ByteArrayInputStream(bytes));
            obj = (T) ois.readObject();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {

            try {
                if (ois != null)
                    ois.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return obj;
    }

    public static class SerializeBlock implements Serializable{
        public String data;
        public Class<?> type;
    }
}
