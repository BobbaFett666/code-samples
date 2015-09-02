package com.aexp.gcs.performance;

import org.springframework.context.Lifecycle;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.core.env.CommandLinePropertySource;
import org.springframework.core.env.SimpleCommandLinePropertySource;

public class Send
{
	public static void main( String[] args )
	{
		long sleep = 0;
		Statistics stats = null;
		CommandLinePropertySource<?> clps = new SimpleCommandLinePropertySource(args);
		printHeader();
		try (FileSystemXmlApplicationContext ctx = new FileSystemXmlApplicationContext()) {
			String duration = clps.getProperty("duration");
			ctx.getEnvironment().getPropertySources().addFirst(clps);
			ctx.setConfigLocation("classpath:ctx.xml");
			ctx.refresh();
			stats = ctx.getBean(Statistics.class);
			if (duration == null) {
				sleep = 60000;
			} else {
				sleep = Long.parseLong(duration);
			}
			sleep(sleep);
			((Lifecycle) ctx.getBean("inboundChannel")).stop();
			sleep(sleep / 60000 < 60000 ? 60000 : sleep / 60000);
		}
		System.out.println(stats.toString());
	}

	private static void sleep(long sleep) {
		try {
			Thread.sleep(sleep);
		} catch (Throwable t) {

		}
	}

	private static void printHeader() {
		System.out.println("Parameters allowed and defaults:");
		System.out.println("--ws.url=WebServiceUniversalResourceLocation, REQUIRED");
		System.out.println("--dir=DirPath\t\tProvides the directory where we'll look for input.  DEFAULT: input");
		System.out
		.println("--filename=FileNamePattern\t\tProvides the name pattern for the file with wildcard support.  DEFAULT: input.xml");
		System.out
		.println("--rate=FixedRate\t\tThe rate at which input will be sent to the web service.  DEFAULT: 100");
		System.out
		.println("--msgpp=MessagesPerPoll\t\tThe number of files sent to the webservice each fixed rate interval.  DEFAULT: 1");
		System.out.println("--duration=TestLength\t\tThe number of milliseconds the test will run.  DEFAULT: 60000");
	}
}
