package com.potevio.sdtv.biz;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.potevio.sdtv.domain.BedData;
import com.potevio.sdtv.util.CacheUtil;

/**
 * Data processing
 * 
 * @author daijun 1. 50-> 0,0 ,20 0, ->50 2.noise window 60 3.ratio:90%
 *
 */
@Service
public class BedDataProcessor {
	// Num of window 1 min =20 , 3 min =60

	// 61 , data index=30, pre:30(ind:0-29), after:30(ind:31-60)
	private final static int NOISE_WINDOW = 21;
	private final static int HALF_WINDOW = (NOISE_WINDOW - 1) / 2;

	private final static float RATIO = 0.9f;
	private static ConcurrentHashMap<String, List<BedData>> queueMap = new ConcurrentHashMap<String, List<BedData>>();
	private static ExecutorService pool = Executors.newFixedThreadPool(10);
	private static LinkedBlockingQueue<BedData> processedDataQueue = new LinkedBlockingQueue<BedData>();
	private static Logger logger = LoggerFactory.getLogger(BedDataProcessor.class);
	public static LinkedBlockingQueue<BedData> getProcessedDataQueue() {
		return processedDataQueue;
	}

	public static void setProcessedDataQueue(
			LinkedBlockingQueue<BedData> processedDataQueue) {
		BedDataProcessor.processedDataQueue = processedDataQueue;
	}

	private class DataProcessor implements Runnable {
		private final List<BedData> list;

		public DataProcessor(List<BedData> list) {
			this.list = list;
		}

		@Override
		public void run() {
			while (list.size() > NOISE_WINDOW) {
				BedData data = list.get(HALF_WINDOW);
				data = processData(list, data);
				//add data to be sent
				processedDataQueue.add(data);
				list.remove(0);
			}
		}

		private BedData processData(List<BedData> list, BedData data) {
			float onBed = 0f;
			float offBed = 0f;
			for (int i = 0; i < NOISE_WINDOW - 1; i++) {
				BedData temData = list.get(i);
				String status = temData.getStatus();
				if ("20".equals(status) || "41".equals(status)) {
					onBed++;
				} else if ("50".equals(status)) {
					offBed++;
				}
			}
			String dataStatus = data.getStatus();
			if ((onBed / NOISE_WINDOW) > RATIO) {
				BedData preData = list.get(HALF_WINDOW - 1);
				if ("50".equals(dataStatus)) {
					data = preData;
				} else {
					if ("".equals(data.getHeartrating())) {
						data.setHeartrating(preData.getHeartrating());
					}
					if ("".equals(data.getResping())) {
						data.setResping(preData.getResping());
					}
				}
			} else if((offBed/NOISE_WINDOW)>RATIO){
				if("20".equals(dataStatus)||"41".equals(dataStatus)){
					data.setStatus("50");
					data.setHeartrating("000");
					data.setResping("00");
				}
			}
			

			return data;
		}

	}

	@PostConstruct
	public void startTimer() {
		Executors.newSingleThreadExecutor().execute(new Runnable() {
			@Override
			public void run() {
				while (true) {
					try {
						BedData data = (BedData) CacheUtil.getYthtjrbedQueue()
								.take();
						data = process50(data);
						List q = getOrInitList(data);
						q.add(data);
						if (q.size() >= NOISE_WINDOW) {
							pool.submit(new DataProcessor(q));
						}

					} catch (Exception e) {
						e.printStackTrace();;
					}
				}
			}

			private List getOrInitList(BedData data) {
				String seriesId = data.getSeriesId();
				List list = queueMap.get(seriesId);
				if (list == null) {
					queueMap.put(seriesId, new CopyOnWriteArrayList<BedData>());
					return queueMap.get(seriesId);
				} else {
					return list;
				}
			}
		});

	}

	private BedData process50(BedData data) {
		String status = data.getStatus();
		if ("50".equals(status)) {
			data.setHeartrating("000");
			data.setResping("00");
		}
		return data;
	}

}
