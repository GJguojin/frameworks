package net.qiyuesuo.framework.orm.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.junit.Test;

import com.gj.frameworks.data.orm.id.TwitterLongIdGenerator;

public class TwitterLongIdGeneratorTest {

	@Test
	public void testPerformance() {
		long avg = 0;
		for (int k = 0; k < 10; k++) {
			List<Callable<Long>> partitions = new ArrayList<>();
			final TwitterLongIdGenerator idGen = new TwitterLongIdGenerator();
			for (int i = 0; i < 1000000; i++) {
				partitions.add(new Callable<Long>() {
					@Override
					public Long call() throws Exception {
						return idGen.nextId();
					}
				});
			}
			ExecutorService executorPool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
			try {
				long s = System.currentTimeMillis();
				executorPool.invokeAll(partitions, 10000, TimeUnit.SECONDS);
				long s_avg = System.currentTimeMillis() - s;
				avg += s_avg;
				System.out.println("完成时间需要: " + s_avg / 1.0e3 + "秒");
				executorPool.shutdown();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		System.out.println("平均完成时间需要: " + avg / 10 / 1.0e3 + "秒");
	}

	@Test
	public void testNextId() throws Exception {
		final TwitterLongIdGenerator idGen = new TwitterLongIdGenerator();
		for (int i = 0; i < 1000; i++) {
			System.out.println(idGen.nextId());
		}
	}

	@Test
	public void testThreadNextId() throws Exception {
		final TwitterLongIdGenerator idGen = new TwitterLongIdGenerator();
		ExecutorService cachedThreadPool = Executors.newCachedThreadPool();
		try {
			for (int i = 0; i < 100; i++) {
				Runnable thread = new Runnable() {
					@Override
					public void run() {
						for (int i = 0; i < 100; i++) {
							System.out.println(idGen.nextId());
						}
					}
				};
				cachedThreadPool.execute(thread);
			}
		} catch (Exception e) {
		} finally {
			cachedThreadPool.shutdown();
		}
		Thread.sleep(2000);
	}

}
