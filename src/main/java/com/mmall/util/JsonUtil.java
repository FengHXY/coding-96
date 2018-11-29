package com.mmall.util;

import java.text.SimpleDateFormat;
import java.util.List;

import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;
import org.codehaus.jackson.type.JavaType;
import org.codehaus.jackson.type.TypeReference;

import com.alipay.api.internal.util.StringUtils;
import com.github.pagehelper.StringUtil;
import com.google.common.collect.Lists;
import com.mmall.pojo.User;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JsonUtil {

	private static ObjectMapper objectMapper = new ObjectMapper();
	
	//需要这个类在加载到JVM的时候就要初始化objectMapper
	static{
		//对象的所有字段全部列入 always：全部字段  non_null:非空字段 
		objectMapper.setSerializationInclusion(Inclusion.ALWAYS);
		
		//取消默认转换timestamps形式
		objectMapper.configure(SerializationConfig.Feature.WRITE_DATES_AS_TIMESTAMPS, false);
		
		//忽略空bean转json的错误
		objectMapper.configure(SerializationConfig.Feature.FAIL_ON_EMPTY_BEANS, false);
		
		//所有的日期格式都统一为以下样式，即yyyy-MM-dd HH:mm:ss
		objectMapper.setDateFormat(new SimpleDateFormat(DateTimeUtil.STANDARD_FORMAT));
		
		//忽略在json字符串中存在，但是在java对象中不存在对应属性的情况，防止错误
		objectMapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
	}
	
	public static <T> String obj2String(T obj){
		if(obj == null){
			return null;
		}
		try {
			return obj instanceof String ? (String)obj : objectMapper.writeValueAsString(obj);
		} catch (Exception e) {
			log.warn("Parse object to String error",e);
			return null;
		}			
	}
	
	public static <T> String obj2StringPretty(T obj){
		if(obj == null){
			return null;
		}
		try {
			return obj instanceof String ? (String)obj : objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
		} catch (Exception e) {
			log.warn("Parse Object to String error",e);
			return null;
		}			
	}
	
	public static <T> T string2Obj(String str, Class<T> clazz){
		if(StringUtil.isEmpty(str) || clazz == null){
			return null;
		}
		
		try {
			return clazz.equals(String.class) ? (T)str : objectMapper.readValue(str,  clazz);
		} catch (Exception e) {
			log.warn("Parse String to Object error",e);
			return null;
		}		
	}
	
	public static <T> T String2Obj(String str, TypeReference<T> typeReference){
		if(StringUtils.isEmpty(str) || typeReference == null){
			return null;
		}
		try {
			return (T) (typeReference.getType().equals(String.class) ? str : objectMapper.readValue(str, typeReference));
		} catch (Exception e) {
			log.warn("Parse String to Object error",e);
			return null;
		}
	}
	
	public static <T> T String2Obj(String str, Class<?> collectionClass, Class<?>... elementClasses){
		JavaType javaType = objectMapper.getTypeFactory().constructParametricType(collectionClass, elementClasses);
		try {
			return objectMapper.readValue(str, javaType);
		} catch (Exception e) {
			log.warn("Parse String to Object error",e);
			return null;
		}
	}
	
//	public static void main(String args[]){
//		User user1 = new User();
//		user1.setId(1);
//		user1.setEmail("wangguoqing@enfi.com.cn");
//		
//		User user2 = new User();
//		user2.setId(2);
//		user2.setEmail("test@qq.com.cn");
//		
//		String json = JsonUtil.obj2String(user1);
//		log.info("json:{}", json);
//		
//		log.info("test:{}","测试");
//		
//		String jsonPre = JsonUtil.obj2StringPretty(user1);
//		log.info("jsonPre:{}", jsonPre);
//		
//		User user = JsonUtil.string2Obj(json, User.class);
//		System.out.println(user);
//		
//		log.info("=========================");
//		
//		List<User> userList = Lists.newArrayList();
//		userList.add(user1);
//		userList.add(user2);
//		
//		String userListStr = JsonUtil.obj2StringPretty(userList);
//		log.info(userListStr);
//		
//		List<User> userListObj1 = JsonUtil.String2Obj(userListStr, new TypeReference<List<User>>() {		
//		});
//		
//		List<User> userListObj2 = JsonUtil.String2Obj(userListStr, List.class, User.class);
//		
//	}
}
