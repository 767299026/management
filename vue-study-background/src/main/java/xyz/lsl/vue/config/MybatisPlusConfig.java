package xyz.lsl.vue.config;

import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableTransactionManagement
@MapperScan("xyz.lsl.vue.mapper")
public class MybatisPlusConfig implements WebMvcConfigurer {

    //分页插件
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor(){
        return new MybatisPlusInterceptor();
    }
}
