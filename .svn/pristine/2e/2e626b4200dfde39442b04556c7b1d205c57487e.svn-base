package com.alpha.commons.util;

import java.util.function.Supplier;

import org.springframework.cglib.beans.BeanCopier;
import org.springframework.cglib.core.Converter;

/**
 * 
 * @author luohongyun
 *
 */
public class BeanCopierUtil {

	public static void copy(Object srcObj, Object destObj, Converter converter) {

		BeanCopier copier = BeanCopier.create(srcObj.getClass(), destObj.getClass(), converter != null);
		copier.copy(srcObj, destObj, converter != null ? converter : null);
	}

	public static void copy(Object srcObj, Object destObj) {
		copy(srcObj, destObj, null);
	}

	public static <T, R> R uniformCopy(T source, Supplier<R> supplier) {
		R r = supplier.get();
		copy(source, r);
		return r;
	}

}
