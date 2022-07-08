package com.screenmagic.notificationservice;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.screenmagic.notificationservice.service.Emailer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@SpringBootApplication
public class NotificationserviceApplication {

	public final static Logger logger = LoggerFactory.getLogger(NotificationserviceApplication.class);
	
	public static void main(String[] args) throws IOException {		
		SpringApplication.run(NotificationserviceApplication.class, args);
		Emailer.smtpProps = readProperties(args[0]);
	}

	public static Properties readProperties(String path) throws IOException {
		Properties properties = new Properties();
		String propertyPath = path;
		File propFile = new File(propertyPath);
		FileInputStream propInputStream = new FileInputStream(propFile);
		properties.load(propInputStream);
		return properties;
	}
	
}
