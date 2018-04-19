package com.sep6th.base.config;

import static com.google.common.collect.Sets.newHashSet;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.Sets;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
/** 
 * The Apache License 2.0
 * Copyright (c) 2018 sep6th
 * @see http://springfox.github.io/springfox/docs/current/#configuring-springfox
 */
@EnableSwagger2
@Configuration
public class SwaggerConfig{
	
	@Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .produces(newHashSet(MediaType.APPLICATION_JSON_VALUE))
                .protocols(Sets.newHashSet("http"/* , "https" */))
                .forCodeGeneration(true)
                .select()
                .apis(RequestHandlerSelectors.any()) // 扫描所有路径下的api文档
                .paths(paths())		// 筛选路径，生成API文档
                .build();
    }
	/**
	 * api基本信息
	 */
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("API-Swagger项目 API文档 By sep6th")	// 标题
                .description("API-Swagger项目有两个业务模块，学校管理和学生管理！")	// 描述
                .license("Apache License 2.0")
                .version("1.1.0")	// api 版本号
                .build();
    }
	
    /**
     * 筛选路径
     */
	private Predicate<String> paths() {
        return Predicates.or(
        		PathSelectors.regex("/student.*"),
        		PathSelectors.regex("/school.*"));
      }
	
}
