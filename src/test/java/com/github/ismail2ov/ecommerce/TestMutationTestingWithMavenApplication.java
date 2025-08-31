package com.github.ismail2ov.ecommerce;

import org.springframework.boot.SpringApplication;

public class TestMutationTestingWithMavenApplication {

	public static void main(String[] args) {
		SpringApplication.from(MutationTestingWithMavenApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
