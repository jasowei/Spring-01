package com.lanou.test;

import com.lanou.domain.HelloWord;
import com.lanou.domain.HelloWorldFactory2;
import com.lanou.domain.Person;
import com.lanou.domain.Student;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by dllo on 17/10/23.
 */
public class MainTest {

    private ApplicationContext context;

    @Before
    public void init(){
        // 获得Spring容器对象
        context = new ClassPathXmlApplicationContext("applicationContext.xml");
    }

    /*测试最基础的 ioc
    * 1. 在spring配置文件中定义的bean会在默认情况下会在new ApplicationContext容器对象时
    * 调用bean默认无参构造方法创建对象, 并将对象加入spring容器中, 以供后续getBean方法调用
    * 2. 加入Spring容器的对象默认情况下是单例模式, 即整个应用只创建一次该对象
    * */
    @Test
    public void testHello(){
        //从容器中获得id为helloword的对象
        HelloWord helloWord = (HelloWord) context.getBean("helloWord");

        //调用sayHello方法
        helloWord.sayHello();
        System.out.println("1."+helloWord.hashCode());

        //再次获取
        HelloWord helloWord1 = (HelloWord) context.getBean("helloWord");
        System.out.println("2."+helloWord1.hashCode());
    }

    /**一.
     * 静态工厂设计模式的测试
     */
    @Test
    public void testFactory(){
        // 通过工厂类id获得bean对象
        HelloWord helloWord = (HelloWord) context.getBean("helloWordFactory");
        //调用bean中方法
        helloWord.sayHello();
    }

    /**二.
     * 通过示例工厂的方式获得helloworld对象
     */
    @Test
    public void testFactory2(){
        //1. 先获得实例工厂类对象
        HelloWorldFactory2 helloWordFactory = (HelloWorldFactory2) context.getBean("helloWorldFactory2");
        //2. 通过实例工厂类对象获得HelloWorld对象
        HelloWord helloWord = helloWordFactory.getInstance();
        // 调用方法
        helloWord.sayHello();

    }


    /**三.
     * 实例工厂模式获得对象的完整写法
     * 1. 实例工厂对象的创建需要在spring配置文件中先配置实例工厂类对象
     * 2. 再配置具体的bean引用, 其中factory-bean指向实例工厂类的bean的id
     * 3. factory-method : 指向实例工厂类获得bean的方法, 如getInstance方法
     * 4. 在代码中就可以直接获得bean对象配置的id, 不用再获得示例工厂类对象
     * 5. 实例工厂模式会先进入工厂类的无参构造方法, 然后才能进入某个bean对象
     *      的构造方法, 即此模式创建了两个对象, 而静态工厂模式的方式只会创建一个对象
     */
    @Test
    public void testFactory3(){
        HelloWord helloWord = (HelloWord) context.getBean("helloWord2");
        helloWord.sayHello();
    }

    /**
     * 测试懒加载属性 lazy-init
     */
    @Test
    public void testLazy(){
        Person person = (Person) context.getBean("person");
        person.test();//调用普通方法

        //关闭容器, 触发bean中的销毁方法
        ((ClassPathXmlApplicationContext)context).close();

    }


    /**
     * 测试依赖注入
     */
    @Test
    public void testDI(){
        /*1. 测试构造器方法注入*/
        Student student = (Student) context.getBean("student");
        System.out.println(student);
        //输出引入其他对象的属性
        System.out.println(student.getPerson());
    }



}
