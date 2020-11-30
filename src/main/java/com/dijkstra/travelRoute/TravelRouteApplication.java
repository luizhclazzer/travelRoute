package com.dijkstra.travelRoute;

import com.dijkstra.travelRoute.service.RouteService;
import com.dijkstra.travelRoute.utils.CommandLineOperate;
import com.dijkstra.travelRoute.utils.UtilValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

import java.util.Scanner;

@SpringBootApplication
public class TravelRouteApplication extends SpringBootServletInitializer implements CommandLineRunner {

	@Autowired
	private CommandLineOperate commandLineOperate;

	@Autowired
	private RouteService routeService;

	private static String fileName;

	public static void main(String[] args) {

		SpringApplication.run(TravelRouteApplication.class, args);

	}

	@Override
	public void run(String... args) {

		try {
			fileName = System.getProperty("param");

			UtilValidation.validateFileNameInput(fileName, "Por favor, informe o caminho do arquivo CSV como parâmetro da aplicação.");
			routeService.setCsvFile(fileName);
//			System.out.println(fileName);

		} catch (final Exception e) {
			System.out.println(e.getMessage());
			System.exit(1);
		}

		/*Does not start the command line execution if you are running the tests, so as not to stop the application*/
		if (UtilValidation.isJUnitTest() == false) {
			commandLineOperate.Execute();
		}

	}

}
