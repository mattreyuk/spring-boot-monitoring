package demo;

import java.lang.management.ManagementFactory;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.codahale.metrics.*;
import com.codahale.metrics.jvm.BufferPoolMetricSet;
import com.codahale.metrics.jvm.GarbageCollectorMetricSet;
import com.codahale.metrics.jvm.MemoryUsageGaugeSet;
import com.codahale.metrics.jvm.ThreadStatesGaugeSet;

@Component
public class AppConfig implements InitializingBean {
	
	@Autowired
	MetricRegistry metricRegistry;

	//need separate registry for threads data as it does not mix well with regular spring metrics
	//probably down to name=threads.deadlocks, value=[]
	final MetricRegistry threadRegistry = new MetricRegistry();
	
	//jvm metrics setup from http://jansipke.nl/getting-metrics-from-the-java-virtual-machine-jvm/
	
	private void registerAll(String prefix, MetricSet metricSet, MetricRegistry registry) {
	    for (Entry<String, Metric> entry : metricSet.getMetrics().entrySet()) {
	        if (entry.getValue() instanceof MetricSet) {
	            registerAll(prefix + "." + entry.getKey(), (MetricSet) entry.getValue(), registry);
	        } else {
	            registry.register(prefix + "." + entry.getKey(), entry.getValue());
	        }
	    }
	}
	@Override
	public void afterPropertiesSet() throws Exception {
		registerAll("gc", new GarbageCollectorMetricSet(), metricRegistry);
		registerAll("buffers", new BufferPoolMetricSet(ManagementFactory.getPlatformMBeanServer()), metricRegistry);
		registerAll("memory", new MemoryUsageGaugeSet(), metricRegistry);
		registerAll("threads", new ThreadStatesGaugeSet(), threadRegistry);
		
		final Slf4jReporter lreporter = Slf4jReporter.forRegistry(metricRegistry)
				.outputTo(LoggerFactory.getLogger("demo"))
				.convertRatesTo(TimeUnit.SECONDS)
				.convertDurationsTo(TimeUnit.MILLISECONDS).build();
		lreporter.start(1, TimeUnit.MINUTES);
		
		final Slf4jReporter tlreporter = Slf4jReporter.forRegistry(threadRegistry)
				.outputTo(LoggerFactory.getLogger("demo"))
				.convertRatesTo(TimeUnit.SECONDS)
				.convertDurationsTo(TimeUnit.MILLISECONDS).build();
		tlreporter.start(1, TimeUnit.MINUTES);

		final ConsoleReporter creporter = ConsoleReporter
				.forRegistry(metricRegistry).convertRatesTo(TimeUnit.SECONDS)
				.convertDurationsTo(TimeUnit.MILLISECONDS).build();
		creporter.start(1, TimeUnit.MINUTES);
		
		final ConsoleReporter tcreporter = ConsoleReporter
				.forRegistry(threadRegistry).convertRatesTo(TimeUnit.SECONDS)
				.convertDurationsTo(TimeUnit.MILLISECONDS).build();
		tcreporter.start(1, TimeUnit.MINUTES);

	}

}