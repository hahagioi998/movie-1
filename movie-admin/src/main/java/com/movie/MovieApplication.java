package com.movie;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * 启动程序
 * 
 * @author swallow
 */
@SpringBootApplication(exclude = { DataSourceAutoConfiguration.class })
public class MovieApplication
{
    public static void main(String[] args)
    {
        // System.setProperty("spring.devtools.restart.enabled", "false");
        SpringApplication.run(MovieApplication.class, args);
        System.out.println("(♥◠‿◠)ﾉﾞ  Movie启动成功   ლ(´ڡ`ლ)ﾞ  \n" +
                "                      _      \n" +
                "                     (_)     \n" +
                " _ __ ___   _____   ___  ___ \n" +
                "| '_ ` _ \\ / _ \\ \\ / / |/ _ \\\n" +
                "| | | | | | (_) \\ V /| |  __/\n" +
                "|_| |_| |_|\\___/ \\_/ |_|\\___|");
    }
}
