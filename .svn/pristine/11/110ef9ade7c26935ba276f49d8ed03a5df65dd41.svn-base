package com.alpha.commons.core.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Created by xc.xiong on 2017/6/19.
 */
public class StringUtil {
    /**
     * list 对象克隆
     *
     * @param obj
     * @return
     */
    public static Object cloneObject(Object obj) {
        try {
            ByteArrayOutputStream bo = new ByteArrayOutputStream();
            ObjectOutputStream os = new ObjectOutputStream(bo);
            os.writeObject(obj);
            ByteArrayInputStream bo1 = new ByteArrayInputStream(bo.toByteArray());
            ObjectInputStream os1 = new ObjectInputStream(bo1);
            return os1.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
