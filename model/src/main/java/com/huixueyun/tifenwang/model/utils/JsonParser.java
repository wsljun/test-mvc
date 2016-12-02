package com.huixueyun.tifenwang.model.utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;

public class JsonParser {
    /**
     * 将object转成json字符串
     *
     * @param obj
     * @return
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws JSONException
     */
    public static String setObj2Json(Object obj)
            throws IllegalArgumentException, IllegalAccessException,
            JSONException {
        JSONObject json = setObj2JsonObject(obj);
        if (json == null)
            return null;
        return json.toString();
    }

    /**
     * 将object转成jsonobject
     *
     * @param obj
     * @return
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws JSONException
     */
    public static JSONObject setObj2JsonObject(Object obj)
            throws IllegalArgumentException, IllegalAccessException,
            JSONException {
        JSONObject json = new JSONObject();
        Field[] fs = obj.getClass().getFields();
        Field f;
        Object o;
        for (int i = 0, j = fs.length; i < j; i++) {
            f = fs[i];
            o = fs[i].get(obj);
            if (o == null)
                continue;
            if (o instanceof ArrayList) {
                json.put(f.getName(), setObj2JsonArray(o));
            } else if (o instanceof Double || o instanceof Integer
                    || o instanceof Float || o instanceof Long
                    || o instanceof String) {
                json.put(f.getName(), f.get(obj));
            } else {
                JSONObject j1 = setObj2JsonObject(f.get(obj));
                json.put(f.getName(), j1);
            }
        }
        return json;
    }

    /**
     * 将object转成jsonarray
     *
     * @param o
     * @return
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws JSONException
     */
    public static JSONArray setObj2JsonArray(Object o)
            throws IllegalArgumentException, IllegalAccessException,
            JSONException {
        ArrayList list = (ArrayList) o;
        if (list.size() < 1) {
            return null;
        }
        JSONArray array = new JSONArray();
        Object o1;
        for (int a = 0, b = list.size(); a < b; a++) {
            o1 = list.get(0);
            if (o1 == null)
                continue;
            if (o1 instanceof Double || o1 instanceof Integer
                    || o1 instanceof Float || o1 instanceof Long
                    || o1 instanceof String) {
                array.put(list.get(a));
            } else {
                JSONObject j1 = setObj2JsonObject(list.get(a));
                array.put(j1);
            }
        }
        return array;
    }

    /**
     * 将json转为object
     *
     * @param obj
     * @param content
     */
    public static void setJson2Obj(Object obj, String content)
            throws JSONException, IllegalArgumentException,
            IllegalAccessException, InvocationTargetException,
            InstantiationException, ClassNotFoundException {
        JSONObject json = new JSONObject(content);
        setJson2Obj(obj, json);
    }

    /**
     * 将json转为object
     *
     * @param obj
     * @param json
     * @throws JSONException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws InstantiationException
     * @throws ClassNotFoundException
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    public static void setJson2Obj(Object obj, JSONObject json)
            throws JSONException, IllegalArgumentException,
            IllegalAccessException, InvocationTargetException,
            InstantiationException, ClassNotFoundException {
        Method[] mtd = obj.getClass().getMethods();
        String name;
        Object value;
        for (int i = 0, j = mtd.length; i < j; i++) {
            name = mtd[i].getName();

            if (!name.startsWith("set"))
                continue;
            name = name.substring(3).toLowerCase();
            if (!json.has(name))
                continue;
            value = json.get(name);

            if (value instanceof JSONObject) {
                Class c = mtd[i].getParameterTypes()[0];
                Object o = c.newInstance();
                if (c.newInstance() instanceof String)
                    mtd[i].invoke(obj, value.toString());
                else {
                    setJson2Obj(o, (JSONObject) value);
                    mtd[i].invoke(obj, o);
                }
            } else if (value instanceof JSONArray) {
                Class c = mtd[i].getParameterTypes()[0];
                if (c.newInstance() instanceof String) {
                    mtd[i].invoke(obj, value.toString());
                } else {
                    if (!c.isAssignableFrom(ArrayList.class))
                        continue;
                    ParameterizedType p = (ParameterizedType) mtd[i]
                            .getGenericParameterTypes()[0];
                    Class c2 = Class.forName(p.getActualTypeArguments()[0]
                            .toString().substring(6));
                    ArrayList list = new ArrayList();
                    JSONArray array = (JSONArray) value;
                    Object o1 = array.get(0);
                    if (o1 instanceof JSONObject) {
                        Object o = c2.newInstance();
                        setJson2Obj(o, (JSONObject) o1);
                        list.add(o);
                        for (int a = 1, b = array.length(); a < b; a++) {
                            o = c2.newInstance();
                            setJson2Obj(o, array.getJSONObject(a));
                            list.add(o);
                        }
                    } else {
                        list.add(o1);
                        for (int a = 1, b = array.length(); a < b; a++) {
                            list.add(array.get(a));
                        }
                    }
                    mtd[i].invoke(obj, list);
                }
            } else {
                if (mtd[i].getParameterTypes()[0].equals(String.class)) {
                    mtd[i].invoke(obj, value.toString());
                } else if (mtd[i].getParameterTypes()[0].equals(Long.class)
                        || mtd[i].getParameterTypes()[0].equals(long.class)) {
                    mtd[i].invoke(obj, Long.valueOf(value.toString()));
                } else if (mtd[i].getParameterTypes()[0].equals(Integer.class)
                        || mtd[i].getParameterTypes()[0].equals(int.class)) {
                    mtd[i].invoke(obj, Integer.valueOf(value.toString()));
                } else {
                    mtd[i].invoke(obj, value);
                }
            }
        }
    }

    /**
     * 将json转为object
     *
     * @param objList
     * @param jsonArr
     * @param objClass
     */
    public static void setJson2Obj(List objList, JSONArray jsonArr,
                                   Class objClass) {
        if (jsonArr != null && jsonArr.length() > 0) {
            Object o = null;
            for (int i = 0; i < jsonArr.length(); i++) {
                try {
                    o = objClass.newInstance();
                    setJson2Obj(o, jsonArr.getJSONObject(i));
                } catch (IllegalArgumentException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (InstantiationException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                objList.add(o);
            }
        }
    }
}