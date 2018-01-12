package cn.hutool.core.bean;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.bean.copier.ValueProvider;
import cn.hutool.core.collection.CollectionUtil;

/**
 * Bean工具单元测试
 * @author Looly
 *
 */
public class BeanUtilTest {
	
	@Test
	public void isBeanTest(){
		
		//HashMap不包含setXXX方法，不是bean
		boolean isBean = BeanUtil.isBean(HashMap.class);
		Assert.assertFalse(isBean);
	}
	
	@Test
	public void fillBeanTest(){
		Person person = BeanUtil.fillBean(new Person(), new ValueProvider<String>(){

			@Override
			public Object value(String key, Type valueType) {
				switch (key) {
					case "name":
						return "张三";
					case "age":
						return 18;
				}
				return null;
			}

			@Override
			public boolean containsKey(String key) {
				//总是存在key
				return true;
			}
			
		}, CopyOptions.create());
		
		Assert.assertEquals(person.getName(), "张三");
		Assert.assertEquals(person.getAge(), 18);
	}
	
	@Test
	public void fillBeanWithMapIgnoreCaseTest(){
		HashMap<String,Object> map = CollectionUtil.newHashMap();
		map.put("Name", "Joe");
		map.put("aGe", 12);
		map.put("openId", "DFDFSDFWERWER");
		SubPerson person = BeanUtil.fillBeanWithMapIgnoreCase(map, new SubPerson(), false);
		Assert.assertEquals(person.getName(), "Joe");
		Assert.assertEquals(person.getAge(), 12);
		Assert.assertEquals(person.getOpenid(), "DFDFSDFWERWER");
	}
	
	@Test
	public void mapToBeanIgnoreCaseTest(){
		HashMap<String,Object> map = CollectionUtil.newHashMap();
		map.put("Name", "Joe");
		map.put("aGe", 12);
		
		Person person = BeanUtil.mapToBeanIgnoreCase(map, Person.class, false);
		Assert.assertEquals(person.getName(), "Joe");
		Assert.assertEquals(person.getAge(), 12);
	}
	
	@Test
	public void beanToMapTest() {
		SubPerson person = new SubPerson();
		person.setAge(14);
		person.setOpenid("11213232");
		person.setName("测试A11");
		person.setSubName("sub名字");
		
		Map<String, Object> map = BeanUtil.beanToMap(person);
		Assert.assertEquals(map.get("name"), "测试A11");
		Assert.assertEquals(map.get("age"), 14);
		Assert.assertEquals("11213232", map.get("openid"));
	}
	
	@Test
	public void getPropertyTest() {
		SubPerson person = new SubPerson();
		person.setAge(14);
		person.setOpenid("11213232");
		person.setName("测试A11");
		person.setSubName("sub名字");
		
		Object name = BeanUtil.getProperty(person, "name");
		Assert.assertEquals("测试A11", name);
		Object subName = BeanUtil.getProperty(person, "subName");
		Assert.assertEquals("sub名字", subName);
	}
	
	@Test
	public void copyPropertiesTest() {
		SubPerson p1 = new SubPerson();
		p1.setSlow(true);
		
		SubPerson p2 = new SubPerson();
		BeanUtil.copyProperties(p1, p2);
		Assert.assertTrue(p2.isSlow());
	}
	
	@Test
	public void copyPropertiesTest2() {
		SubPerson p1 = new SubPerson();
		p1.setSlow(true);
		
		SubPerson2 p2 = new SubPerson2();
		BeanUtil.copyProperties(p1, p2);
		Assert.assertTrue(p2.isSlow());
	}
	
	//-----------------------------------------------------------------------------------------------------------------
	public static class SubPerson extends Person{
		private String subName;
		private Boolean isSlow;

		public String getSubName() {
			return subName;
		}
		public void setSubName(String subName) {
			this.subName = subName;
		}
		public Boolean isSlow() {
			return isSlow;
		}
		public void setSlow(Boolean isSlow) {
			this.isSlow = isSlow;
		}
	}
	
	public static class SubPerson2 extends Person{
		private String subName;
		//boolean参数值非isXXX形式
		private Boolean slow;
		
		public String getSubName() {
			return subName;
		}
		public void setSubName(String subName) {
			this.subName = subName;
		}
		public Boolean isSlow() {
			return slow;
		}
		public void setSlow(Boolean isSlow) {
			this.slow = isSlow;
		}
	}
	
	public static class Person{
		private String name;
		private int age;
		private String openid;
		
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public int getAge() {
			return age;
		}
		public void setAge(int age) {
			this.age = age;
		}
		public String getOpenid() {
			return openid;
		}
		public void setOpenid(String openid) {
			this.openid = openid;
		}
	}
}
