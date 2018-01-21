package cn.et;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;



@SpringBootApplication  //启动注解          @ComponentScan("包的路径")：该注解可以指定不在默认扫描的范围的包
public class Main {

/**
 * springboot 扫描的范围是启动类所在包以及子包
 * 注解加在实现类上不加在接口上，映射路径不能重复（不同包中的类上的映射都不能重复）
 */
	public static void main(String[] args) {
		SpringApplication.run(Main.class, args);
	}
}
