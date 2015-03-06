package demo;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.codahale.metrics.ConsoleReporter;
import com.codahale.metrics.MetricRegistry;

@Component
public class AppConfig implements InitializingBean {
	
	@Autowired
	MetricRegistry metricRegistry;

	@Override
	public void afterPropertiesSet() throws Exception {
		final ConsoleReporter reporter = ConsoleReporter
				.forRegistry(metricRegistry).convertRatesTo(TimeUnit.SECONDS)
				.convertDurationsTo(TimeUnit.MILLISECONDS).build();
		reporter.start(1, TimeUnit.MINUTES);

	}

}