package xyz.lsl.vue;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import lombok.extern.log4j.Log4j2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Log4j2
public class GeneratorCode {
    public static void main(String[] args) {
        String host = "localhost:3306";
        String database = "vue";
        String username = "root";
        String password = "12345678";
        String parentPacket = "xyz.lsl";
        String moduleName = "vue";

        List<String> tableName = new ArrayList<>();//表名
        /*要生产的表名*/
        tableName.add("goods");

        String logicDelete = "";

        FastAutoGenerator.create("jdbc:mysql://" + host + "/" + database + "?serverTimezone=UTC&useUnicode=true&characterEncoding=utf8&useSSL=false",
                        username, password)
                .globalConfig(builder -> {
                    builder//全局配置
                            .author("YIQU")//作者
                            .enableSwagger()//开启swagger
                            .fileOverride()//覆盖原有文件
                            .disableOpenDir()
                        .outputDir(System.getProperty("user.dir")+"/src/main/java")//输出目录
                        .commentDate("yyyy-MM-dd HH:mm:ss");//时间格式
                })
                .packageConfig(builder -> {builder//包配置
                        .parent(parentPacket)//父模块名
                        .moduleName(moduleName)//包名
                        .xml("Mapper")//xml配置输出
                        .pathInfo(Collections.singletonMap(OutputFile.mapperXml,System.getProperty("user.dir")+"/src/main/resources/mapper"));
                })
                .strategyConfig(builder -> {builder
                        .addInclude(tableName)
                        .serviceBuilder()
                        .formatServiceFileName("%sService")
                        .formatServiceImplFileName("%sServiceImpl")
                        .superServiceClass(IService.class)
                        .superServiceImplClass(ServiceImpl.class)
                        .entityBuilder()
                        .enableLombok()
                        //.logicDeleteColumnName(logicDelete)//逻辑删除字段
                        .enableTableFieldAnnotation()//属性上加上说明注解
                        .controllerBuilder()
                        .formatFileName("%sController")
                        .enableRestStyle()//开启RestController
                        .mapperBuilder()
                        .formatMapperFileName("%sMapper")
                        .superClass(BaseMapper.class)
                        .formatXmlFileName("%sMapper")
                        .enableBaseResultMap();
                })
                .templateEngine(new FreemarkerTemplateEngine())
                .execute();
    }
}
