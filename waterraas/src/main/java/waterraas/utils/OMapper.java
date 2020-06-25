package waterraas.utils;

public class OMapper {
//
//    public static ONode tran(ScriptObjectMirror jObj){
//        if(jObj == null){
//            return new ONode();
//        }
//
//        Object dObj = doTran(jObj);
//
//        return OMapper.map(dObj);
//    }
//
//    private static Object doTran(ScriptObjectMirror jObj){
//        if(jObj.isArray() || jObj.containsKey("toList")){
//            List<Object> list = new ArrayList<>();
//            for(String k : jObj.keySet()){
//                if(isDigit(k)) {
//                    Object v = jObj.getMember(k);
//                    if (v instanceof ScriptObjectMirror) {
//                        list.add(doTran((ScriptObjectMirror) v));
//                    } else {
//                        list.add(v);
//                    }
//                }
//            }
//            return list;
//        }else{
//            Map<String,Object> hash = new HashMap<>();
//
//            for(String k : jObj.keySet()){
//                Object v = jObj.getMember(k);
//
//                if(v instanceof ScriptObjectMirror){
//                    hash.put(k, doTran((ScriptObjectMirror)v));
//                }else {
//                    hash.put(k, v);
//                }
//            }
//
//            return hash;
//        }
//    }
//
//    private static boolean isDigit(String str) {
//        for (int i = 0; i < str.length(); i++) {
//            if (!Character.isDigit(str.charAt(i))) {
//                return false;
//            }
//        }
//        return true;
//    }
//
//    //---------------
//    //
//    //
//
//    public static ONode map(Object obj){
//        return createNode(obj);
//    }
//
//    public static Object map(ONode json, Class<?>c){
//        return parseObject(json,c);
//    }
//
//
//    //===============
//
//    private static ONode createNode(Object value){
//        if(value == null){
//            return new ONode();
//        }
//
//        if(value instanceof ONode){
//            return (ONode)value;
//        }else if(value instanceof String){
//            return new ONode((String) value);
//        }else if(value instanceof Date){
//            return new ONode((Date) value);
//        }else if(value instanceof Boolean){
//            return new ONode((Boolean) value);
//        }else if(value instanceof Double){
//            return new ONode((Double) value);
//        }else if(value instanceof Integer){
//            return new ONode((Integer) value);
//        }else if(value instanceof Long){
//            return new ONode((Long) value);
//        }else if(value instanceof Map){
//            return createNodeByMap((Map<?,?>) value);
//        }else if(value instanceof Collection<?>){
//            return createNodeByList((Collection<?>)value);
//        }else if(value instanceof Object []){
//            return createNodeByArray((Object [])value);
//        }else if(value instanceof Undefined){
//            return ONode.NULL;
//        }else{
//            return createNodeByObj(value);
//        }
//    }
//
//    private static ONode createNodeByObj(Object content){
//        if(content == null){
//            return ONode.NULL;
//        }
//        Field []fields = content.getClass().getDeclaredFields();
//        if(fields == null || fields.length == 0){
//            return ONode.NULL;
//        }
//        ONode writer = new ONode();
//        writer.asObject();
//        for (Field field : fields) {
//            field.setAccessible(true);
//            try {
//                ONode val = createNode(field.get(content));
//                writer.set(field.getName(), val);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//        return writer;
//    }
//
//    private static ONode createNodeByList(Collection<?> list){
//        if(list == null){
//            return null;
//        }
//        ONode writer = new ONode();
//        writer.asArray();
//        for (Object value : list) {
//            if (value != null) {
//                writer.add(createNode(value));
//            }
//        }
//        return writer;
//    }
//
//    private static ONode createNodeByArray(Object[] arrays){
//        if(arrays == null){
//            return null;
//        }
//        ONode writer = new ONode();
//        writer.asArray();
//        for (Object value : arrays) {
//            if (value != null) {
//                writer.add(createNode(value));
//            }
//        }
//
//        return writer;
//    }
//
//    private  static ONode createNodeByMap(Map<?,?> map){
//        if(map == null){
//            return null;
//        }
//        ONode writer = new ONode();
//        writer.asObject();
//        for (Map.Entry<?,?> entry: map.entrySet()) {
//            ONode val = createNode(entry.getValue());
//            writer.set((String)entry.getKey(), val);
//        }
//        return writer;
//    }
//
//    //---------------
//    //
//    //
//
////    @SuppressWarnings("unchecked")
//    private static <V> Object parseObject(ONode job, Class<?> c) {
//
//        if(job.isArray()){
//            return parseArray(job,c);
//        }
//
//        Object t = null;
//        try {
//                t = c.newInstance();
//        } catch (IllegalArgumentException e) {
//            e.printStackTrace();
//        } catch (Exception e) {
//                e.printStackTrace();
//        }
//
//        Field[] fields = c.getDeclaredFields();
//        for (Field field : fields) {
//            field.setAccessible(true);
//            Class<?> type = field.getType();
//            String name = field.getName();
//
//            // if the object don`t has a mapping for name, then continue
//            if(!job.contains(name)) continue;
//
//            String typeName = type.getName();
//            if(typeName.equals("java.lang.String")) {
//                try {
//                    String value = job.get(name).getString();
//                    if (value != null && value.equals("null")) {
//                        value = "";
//                    }
//                    field.set(t, value);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                    try {
//                        field.set(t, "");
//                    } catch (Exception e1) {
//                        e1.printStackTrace();
//                    }
//                }
//            } else if(typeName.equals("int") ||
//                    typeName.equals("java.lang.Integer")) {
//                try {
//                    field.set(t, job.get(name).getInt());
//                } catch (Exception e) {
//                        e.printStackTrace();
//                }
//            } else if(typeName.equals("boolean") ||
//                    typeName.equals("java.lang.Boolean")) {
//                try {
//                    field.set(t, job.get(name).getBoolean());
//                } catch (Exception e) {
//                        e.printStackTrace();
//                }
//            } else if(typeName.equals("float") ||
//                    typeName.equals("java.lang.Float")) {
//                try {
//                    field.set(t, Float.valueOf(job.get(name).getString()));
//                } catch (Exception e) {
//                        e.printStackTrace();
//                }
//            } else if(typeName.equals("double") ||
//                    typeName.equals("java.lang.Double")) {
//                try {
//                    field.set(t, job.get(name).getDouble());
//                } catch (Exception e) {
//                        e.printStackTrace();
//                }
//            } else if(typeName.equals("long") ||
//                    typeName.equals("java.lang.Long")) {
//                try {
//                    field.set(t, job.get(name).getLong());
//                } catch (Exception e) {
//                        e.printStackTrace();
//                }
//            } else if(typeName.equals("java.util.List") ||
//                    typeName.equals("java.util.ArrayList")){
//                try {
//                    ONode obj = job.get(name);
//                    Type genericType = field.getGenericType();
//                    String className = genericType.toString().replace("<", "")
//                            .replace(type.getName(), "").replace(">", "");
//                    Class<?> clazz = Class.forName(className);
//                    if(obj.isArray()) {
//                        ArrayList<?> objList = parseArray(obj, clazz);
//                        field.set(t, objList);
//                    }
//                } catch (Exception e) {
//                        e.printStackTrace();
//                }
//            } else {
//                try {
//                    ONode obj = job.get(name);
//                    Class<?> clazz = Class.forName(typeName);
//                    if(obj.isObject()) {
//                        Object parseJson = parseObject(obj, clazz);
//                        field.set(t, parseJson);
//                    }
//                } catch (Exception e) {
//                        e.printStackTrace();
//                }
//
//            }
//        }
//
//        return t;
//    }
//
////    @SuppressWarnings("unchecked")
//    private static <T, V> ArrayList<T> parseArray(ONode array, Class<T> c) {
//        ArrayList<T> list = new ArrayList<T>(array.count());
//        try {
//            for (int i = 0; i < array.count(); i++) {
//                ONode item = array.get(i);
//                if (item.isObject()) {
//                    T t = (T)parseObject(item, c);
//                    list.add(t);
//                } else {
//                    list.add((T) array.get(i));
//                }
//
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return list;
//    }

}
