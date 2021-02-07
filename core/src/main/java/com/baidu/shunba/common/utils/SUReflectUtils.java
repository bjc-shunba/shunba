package com.baidu.shunba.common.utils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.PropertyUtils;

import com.google.gson.Gson;

public class SUReflectUtils {

	private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(SUReflectUtils.class);
	
	public static Field getField(Object entity, String name) throws Exception {
		try {
			return entity.getClass().getDeclaredField(name);
		} catch (Exception e) {
			return entity.getClass().getField(name);
		}
	}
	
	public static Method getMethod(Object entity, String name, Object value) throws Exception {
		try {
			if (value != null) {
				Class clazz = null;
				if (value instanceof Class) {
					clazz = (Class) value;
				} else {
					clazz = value.getClass();
				}
				return entity.getClass().getMethod(name,clazz);
			} else {
				return entity.getClass().getMethod(name);
			}
		} catch (Exception e) {
			if (value != null) {
				Class clazz = null;
				if (value instanceof Class) {
					clazz = (Class) value;
				} else {
					clazz = value.getClass();
				}
				return entity.getClass().getDeclaredMethod(name, clazz);
			} else {
				return entity.getClass().getDeclaredMethod(name);
			}
		}
	}

	public static <T> T getFieldValue(Object entity, String name) throws Exception {
		T idValue = null;
		Field field = null;
		try {
			field = getField(entity, name);
		} catch (Exception e) {
		}
		if (field == null) {
			Method getMethod = getMethod(entity, "get" + name.substring(0, 1).toUpperCase() + name.substring(1), null);
			idValue = (T) getMethod.invoke(entity);
		} else {
			boolean ogAccessible = field.isAccessible();
			field.setAccessible(true);
			idValue = (T) field.get(entity);
			field.setAccessible(ogAccessible);
		}
		

		return idValue;
	}
	
	public static void setFieldValue(Object entity, String name, Object value) throws Exception {
		try {
			Field field = getField(entity, name);
			boolean ogAccessible = field.isAccessible();
			field.setAccessible(true);
			try {
				field.set(entity, value);
			} finally {
				field.setAccessible(ogAccessible);
			}
		} catch (Exception e) {
			Method setMethod = getMethod(entity, "set" + name.substring(0, 1).toUpperCase() + name.substring(1), value);
			setMethod.invoke(entity, value);
		}
	}
	
	public static boolean isPrimitiveOrWrapClass(Class clz) {
		try {
			return CharSequence.class.isAssignableFrom(clz) || Date.class.isAssignableFrom(clz)
					|| clz.isPrimitive()
					|| ((Class) clz.getField("TYPE").get(null)).isPrimitive()
					;
		} catch (Exception e) {
			return false;
		}
	}
	
	
	
	public static void copyValues(Object fromObj, Object toObj) {
		try {
			Field[] fields = fromObj.getClass().getDeclaredFields();
			for (Field field : fields) {
				try {
					Object value = getFieldValue(fromObj, field.getName());
					setFieldValue(toObj, field.getName(), value);
				} catch (Exception e) {
				}
				
			}
			
		} catch (Exception e) {
		}
	}

	public static <T> List<Map<String, Object>> createListMapFromEntityList(List<T> entityList) {
		if (entityList == null) {
			return new ArrayList<>();
		} else {
			List<Map<String, Object>> dataList = new ArrayList<>(entityList.size());

			for (T entity : entityList) {
				Map<String, Object> map = new HashMap<>();
				MyBeanUtils.copyBean2Map(map, entity);
				dataList.add(map);
			}

			return dataList;
		}
	}

	public static <K, V> Map<K, V> createMapFromListByProperty(List<V> entityList, String propertyName) {
		if (entityList == null) {
			return new HashMap<>();
		} else {
			return createMapFromListByProperty(entityList, 0, entityList.size(), propertyName);
		}
	}
	public static <K, V> Map<K, V> createMapFromListByProperty(List<V> entityList, int start, int end, String propertyName) {
		if (entityList == null || entityList.size() == 0) {
			return new HashMap<>();
		} else {
			Map<K, V> resultMap = new HashMap<>();

			try {
				PropertyDescriptor propertyDescriptor = PropertyUtils.getPropertyDescriptor(entityList.get(0), propertyName);
				Method method = propertyDescriptor.getReadMethod();

				for (int i = start, len = entityList.size(); i < len && i < end; i++) {
					K key = (K)method.invoke(entityList.get(i));
					resultMap.put(key, entityList.get(i));
				}
			} catch (IllegalAccessException e) {
				logger.error("", e);
			} catch (InvocationTargetException e) {
				logger.error("", e);
			} catch (NoSuchMethodException e) {
				logger.error("", e);
			}

			return resultMap;
		}
	}

	public static <K, V> Map<K, List<V>> createMapListFromListByProperty(List<V> entityList, String propertyName) {
		if (entityList == null) {
			return new LinkedHashMap<>();
		} else {
			return createMapListFromListByProperty(entityList, 0, entityList.size(), propertyName);
		}
	}
	public static <K, V> Map<K, List<V>> createMapListFromListByProperty(List<V> entityList, int start, int end, String propertyName) {
		if (entityList == null || entityList.size() == 0) {
			return new LinkedHashMap<>();
		} else {
			Map<K, List<V>> resultMap = new LinkedHashMap<>();

			try {
				PropertyDescriptor propertyDescriptor = PropertyUtils.getPropertyDescriptor(entityList.get(0), propertyName);
				Method method = propertyDescriptor.getReadMethod();

				for (int i = start, len = entityList.size(); i < len && i < end; i++) {
					V value = entityList.get(i);
					K key = (K)method.invoke(value);
					List<V> list = resultMap.get(key);

					if (list != null) {
						list.add(value);
					} else {
						list = new ArrayList<>();
						list.add(value);
						resultMap.put(key, list);
					}
				}
			} catch (IllegalAccessException e) {
				logger.error("", e);
			} catch (InvocationTargetException e) {
				logger.error("", e);
			} catch (NoSuchMethodException e) {
				logger.error("", e);
			}

			return resultMap;
		}
	}

	/**
	 * 把 enity.methodName ---> 收集起来
	 * @param entityList
	 * @param propertyName
	 * @param <T>
	 * @return
	 */
	public static <T> List<T> getFieldValueListByProperty(List entityList, String propertyName) {
		if (entityList == null) {
			return new ArrayList<>();
		} else {
			return getFieldValueListByProperty(entityList, 0, entityList.size(), propertyName);
		}
	}
	public static <T> List<T> getFieldValueListByProperty(List entityList, int start, int end, String propertyName) {
		if (entityList == null || entityList.size() == 0) {
			return new ArrayList<>();
		} else {
			List<T> resultList = new ArrayList<>(entityList.size());
			PropertyDescriptor propertyDescriptor = null;

			try {
				propertyDescriptor = PropertyUtils.getPropertyDescriptor(entityList.get(0), propertyName);
				Method method = propertyDescriptor.getReadMethod();

				for (int i = start, len = entityList.size(); i < len && i < end; i++) {
					try {
						T t = (T)method.invoke(entityList.get(i));
						resultList.add(t);
					} catch (IllegalAccessException e) {
						logger.error("", e);
					} catch (InvocationTargetException e) {
						logger.error("", e);
					}
				}
			} catch (IllegalAccessException e) {
				logger.error("", e);
			} catch (InvocationTargetException e) {
				logger.error("", e);
			} catch (NoSuchMethodException e) {
				logger.error("", e);
			}

			return resultList;
		}
	}

	
	public static void mainTest(String[] args) {
//		testGet();
		if (true) {
			return;
		}
		
		TestA a = new TestA();
		
		System.out.println(a);
		
		Field[] fs = TestA.class.getDeclaredFields();
		for (Field f : fs) {
			try {
				if (Modifier.isFinal(f.getModifiers())) {
					continue;
				}
				if (f.getType() == int.class || f.getType() == Integer.class) {
					continue;
				}
				f.setAccessible(true);
				f.set(a, ObjectUtils.getPriceValue(ObjectUtils.getDoubleValue(f.get(a))));
			} catch (Exception e) {
			}
		}
		System.out.println(a);
		
		
		if (true) {
			return;
		}
		
		
		
		System.out.println(isPrimitiveOrWrapClass(int.class));
		System.out.println(isPrimitiveOrWrapClass(Integer.class));
		System.out.println(isPrimitiveOrWrapClass(String.class));
		System.out.println(isPrimitiveOrWrapClass(Timestamp.class));
		
		
		System.out.println("xxxxxxxxxxxxxxxxxxx");
		
		System.out.println(int.class.isPrimitive());
		System.out.println(Integer.class.isPrimitive());
		System.out.println(String.class.isPrimitive());
		System.out.println(String.class.isSynthetic());
		System.out.println(String.class.isAssignableFrom(CharSequence.class));
		
		System.out.println(Timestamp.class.isPrimitive());
		System.out.println(Timestamp.class.isSynthetic());
		System.out.println(Timestamp.class.isAssignableFrom(Date.class));
		System.out.println(java.sql.Date.class.isAssignableFrom(Date.class));
		
		System.out.println("xxxxxxxxxxxxxxxxxxx");
		
		System.out.println(Date.class.isAssignableFrom(Timestamp.class));
		System.out.println(Date.class.isAssignableFrom(TestClass.class));
		System.out.println(Date.class.isAssignableFrom(java.sql.Date.class));
		System.out.println(Date.class.isAssignableFrom(Date.class));
	}
	
	private static void testGet() {
		try {
			TestB b = new TestB();
			Object obj = getFieldValue(b, "ib");
			
			System.err.println("aaa1:" + obj.getClass());
			System.err.println("aaa1:" + obj);
			obj = getFieldValue(b, "i1");
			System.err.println("aaa2:" + obj.getClass());
			System.err.println("aaa2:" + obj);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static class TestA {
		public final double da = 1.5;
		public final int ia = 2;
		
		public double db = 3.5;
		private int ib = 2;
		
		public int getIb() {
			return ib;
		}



		public void setIb(int ib) {
			this.ib = ib;
		}



		@Override
		public String toString() {
			return new Gson().toJson(this);
		}
	}
	
	public static class TestB extends TestA {
		private double d1 = 3.5;
		private int i1 = 3;
		
		
		public double getD1() {
			return d1;
		}



		public void setD1(double d1) {
			this.d1 = d1;
		}



		public int getI1() {
			return i1;
		}



		public void setI1(int i1) {
			this.i1 = i1;
		}



		@Override
		public String toString() {
			return new Gson().toJson(this);
		}
	}
	
	
	public static class TestClass extends Date {
		
	}
}
