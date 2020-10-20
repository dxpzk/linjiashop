package cn.enilu.flash.api.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.sql.SQLException;

/**
 * @author zk
 * @since 2020-10-20
 */
@Configuration
@ConfigurationProperties(prefix = "spring.datasource")//读取 application.properties里的内容
public class DruidConfig {
    //数据库 url
    @Value("${spring.datasource.url}")
    private String url;

    @Value("${spring.datasource.driverClassName}")
    private String driver;

    //数据库用户名
    @Value("${spring.datasource.username}")
    private String username;

    //数据库登陆密码
    @Value("${spring.datasource.password}")
    private String password;

    //最大的连接数量
    @Value("${spring.datasource.druid.max-active}")
    private int maxActive;

    //初始化大小
    @Value("${spring.datasource.druid.initial-size}")
    private int initialSize;

    // 设置 超时的等待时间
    @Value("${spring.datasource.druid.max-wait}")
    private int maxWait;

    //最小的连接数量
    @Value("${spring.datasource.druid.min-idle}")
    private int minIdle;

    //监控统计拦截的filters，如果去掉后监控界面sql将无法统计
    @Value("${spring.datasource.druid.filters}")
    private String filters;

    @Value("${spring.datasource.druid.stat-view-servlet.login-username}")
    private String loginName;
    @Value("${spring.datasource.druid.stat-view-servlet.login-password}")
    private String loginPassword;
    @Value("${spring.datasource.druid.web-stat-filter.exclusions}")
    private String exclusions;


    //注册 Servlet 组件
    @Bean
    public ServletRegistrationBean statViewServlet(){
        ServletRegistrationBean servletRegistrationBean = new ServletRegistrationBean(new StatViewServlet(), "/druid/*");
        /*servletRegistrationBean.addInitParameter("allow", "192.168.1.3"); //白名单IP*/
        servletRegistrationBean.addInitParameter("loginUsername", loginName);
        servletRegistrationBean.addInitParameter("loginPassword", loginPassword);
        return servletRegistrationBean;
    }

    // 注册 Filter 组件
    @Bean
    public FilterRegistrationBean statFilter(){
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean(new WebStatFilter());
        //验证所有请求
        filterRegistrationBean.addUrlPatterns("/*");
        //对 *.js,*.gif,*.jpg,*.png,*.css,*.ico,/druid/* 不进行验证
        filterRegistrationBean.addInitParameter("exclusions", exclusions);
        return filterRegistrationBean;
    }

    //配置数据源
    @Bean
    public DruidDataSource dataSource() {
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setDriverClassName(driver);
        dataSource.setUrl(url);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        dataSource.setMaxActive(maxActive);
        dataSource.setInitialSize(initialSize);
        try {
            dataSource.setFilters(filters);
        } catch (SQLException e) {
            //do nothing
        }
        dataSource.setMaxWait(maxWait);
        dataSource.setMinIdle(minIdle);
        return dataSource;
    }
}
