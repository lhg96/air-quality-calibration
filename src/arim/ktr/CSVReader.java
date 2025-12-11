package arim.ktr;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.OptionalDouble;
import java.util.Properties;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * csv  edit
 * 
 * @author hyun
 *
 */
public class CSVReader {
	private static boolean debug = true;
	private static String loadCsvFileName1;
	private static String saveCsvFileName1;
	private static String sumCsvFileName1;
	
	private static String loadCsvFileName2;
	private static String saveCsvFileName2;
	private static String sumCsvFileName2;
	
	private static String loadCsvFileName3;
	private static String saveCsvFileName3;
	private static String sumCsvFileName3;
	
	private static String COMMA_DELIMITER = ",";
	
	/**
	 * Load configuration from config.properties file
	 */
	private static void loadConfiguration() {
		Properties prop = new Properties();
		InputStream input = null;
		
		try {
			// Try to load from file system first
			input = new FileInputStream("config.properties");
			prop.load(input);
			
			// Load sensor 1 configuration
			loadCsvFileName1 = prop.getProperty("sensor1.input", "COM12_log.csv");
			saveCsvFileName1 = prop.getProperty("sensor1.output", "COM12_log_new.csv");
			sumCsvFileName1 = prop.getProperty("sensor1.summary", "COM12_log_summery.csv");
			
			// Load sensor 2 configuration
			loadCsvFileName2 = prop.getProperty("sensor2.input", "COM13_log.csv");
			saveCsvFileName2 = prop.getProperty("sensor2.output", "COM13_log_new.csv");
			sumCsvFileName2 = prop.getProperty("sensor2.summary", "COM13_log_summery.csv");
			
			// Load sensor 3 configuration
			loadCsvFileName3 = prop.getProperty("sensor3.input", "COM14_log.csv");
			saveCsvFileName3 = prop.getProperty("sensor3.output", "COM14_log_new.csv");
			sumCsvFileName3 = prop.getProperty("sensor3.summary", "COM14_log_summery.csv");
			
			// Load other settings
			COMMA_DELIMITER = prop.getProperty("csv.delimiter", ",");
			debug = Boolean.parseBoolean(prop.getProperty("debug", "true"));
			
			System.out.println("Configuration loaded successfully from config.properties");
		} catch (IOException ex) {
			System.out.println("config.properties not found, using default values");
			// Use default values (already set above)
			loadCsvFileName1 = "COM12_log.csv";
			saveCsvFileName1 = "COM12_log_new.csv";
			sumCsvFileName1 = "COM12_log_summery.csv";
			loadCsvFileName2 = "COM13_log.csv";
			saveCsvFileName2 = "COM13_log_new.csv";
			sumCsvFileName2 = "COM13_log_summery.csv";
			loadCsvFileName3 = "COM14_log.csv";
			saveCsvFileName3 = "COM14_log_new.csv";
			sumCsvFileName3 = "COM14_log_summery.csv";
			COMMA_DELIMITER = ",";
			debug = true;
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	//
	public static void main(String[] args) throws ParseException {
		// Load configuration first
		loadConfiguration();
		
		CSVReader app =  new CSVReader();
		try {
			
			List<AirData> records1 = app.readActionLog(loadCsvFileName1, COMMA_DELIMITER);
			List<AirData> records2 = app.readActionLog(loadCsvFileName2, COMMA_DELIMITER);
			List<AirData> records3 = app.readActionLog(loadCsvFileName3, COMMA_DELIMITER);
			System.out.println(records1.size());
			System.out.println(records2.size());
			System.out.println(records3.size());
			
			//두개 평균 교체
			List<AirData> records1_1 = app.avePM25(100,0, records1,records2,records3);			
			System.out.println("1-1. PM2.5 Amount:"+records1_1.size());			
			//평균값 계산
			List<AirData> records1_2 = app.calAVE(records1_1);
			app.csvWrite(saveCsvFileName1, records1_2);
			System.out.println("1-2. save ave Amount:"+records1_2.size());
			//써머리 저장
			List<AirData> records1_3 = app.makeSummery(records1_2);
			app.csvWrite(sumCsvFileName1, records1_3);
			System.out.println("1-3. save summery Amount:"+records1_3.size());
			
			
			//-------------------------------------------------------------------
			//두개 평균 교체
			List<AirData> records2_1 = app.avePM25(100,0, records2,records1,records3);			
			System.out.println("2-1. PM2.5 Amount:"+records2_1.size());
			//평균값 계산
			List<AirData> records2_2 = app.calAVE(records2_1);
			app.csvWrite(saveCsvFileName2, records2_2);
			System.out.println("2-2. save ave Amount:"+records2_2.size());
			
			//써머리 저장
			List<AirData> records2_3 = app.makeSummery(records2_2);
			app.csvWrite(sumCsvFileName2, records2_3);
			System.out.println("2-3. save summery Amount:"+records2_3.size());
			
			//-------------------------------------------------------------------
			//두개 평균 교체
			List<AirData> records3_1 = app.avePM25(100,0, records3,records1,records2);			
			System.out.println("3-1. PM2.5 Amount:"+records3_1.size());
			//평균값 계산
			List<AirData> records3_2 = app.calAVE(records3_1);
			app.csvWrite(saveCsvFileName3, records3_2);
			System.out.println("3-2. save ave Amount:"+records3_2.size());
			//써머리 저장
			List<AirData> records3_3 = app.makeSummery(records3_2);
			app.csvWrite(sumCsvFileName3, records3_3);
			System.out.println("3-3. save summery Amount:"+records3_3.size());
			/*
			int cnt = checkPM25Over(300, records2_1);
			System.out.println("300 over:"+cnt);
			
			cnt = checkPM25Under(0, records2_1);
			System.out.println("0 under:"+cnt);
			*/
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {

			e.printStackTrace();
		}
	}
	//----------summery--------------------------
	private List<AirData> makeSummery(List<AirData> records) {
		long preTime = 0;//= records.get(0).getDate().getTime();
		List<AirData> newAirDatas = new ArrayList<>();
		
		for(int i=0;i<records.size();i++) {
			AirData record = records.get(i);
			long currentTime = record.getDate().getTime();
			if(currentTime-preTime>=60*60*1000) {
				preTime = currentTime;
				newAirDatas.add(record);
			}			
		}
		return newAirDatas;
		
	}
	//----------end summery----------------------	
	
	
	
	private List<AirData> calAVE(List<AirData> records) {
		AirDatas airDatas = new AirDatas();
		
		List<AirData> newAirDatas = new ArrayList<>();		
		records.forEach(record->{
			long timeStamp = record.getDate().getTime();
			double pm25= record.getPm25();
			airDatas.add(timeStamp, pm25);
			
			long 	oneHour  = 60*60*1000;			
			double oneHourAVE =airDatas.getAVE(timeStamp, oneHour);
			record.setOnehour(oneHourAVE);
			
			long 	oneDay  = 24*60*60*1000;			
			double oneDayAVE =airDatas.getAVE(timeStamp, oneDay);
			record.setOneday(oneDayAVE);
			
			newAirDatas.add(record);
		});
		
		return newAirDatas;
		
		/*
		int hour = 1;
		List<AirData> newAirDatas = new ArrayList<>();
		int count  = 0;
		for(int i=0;i<records.size();i++) {
			AirData record = records.get(i);
			Date date1 = record.getDate();			
			
			boolean isOneHour = true;
			int j = i;
			List<Double> pm25List = new ArrayList<>();
			while(isOneHour) {
				AirData record2 = records.get(j);
				Date date2 = record.getDate();
				
				if(j<i) {
					long timeGap = date1.getTime()-date2.getTime();
					//long referGap = 60*60*1000;  
					if(timeGap<referGap){						
						pm25List.add(record2.getPm25());
					}else {
						isOneHour =false;
					}					
				}else {
					pm25List.add(record2.getPm25());
				}
				j--;
				if(j<0) isOneHour =false;				
			}
			
			
			
			OptionalDouble average = pm25List
		            .stream()
		            .mapToDouble(a -> a)
		            .average();
			
			double avePM25 = average.getAsDouble();
			if(type==1) {
				record.setOnehour(avePM25);
			}else {
				record.setOneday(avePM25);
			}
			
			newAirDatas.add(record);
		}
		System.out.println("replaced count:"+count);
		
		return newAirDatas;*/
	}



	private List<AirData> avePM25(int rOver, int rUnder, List<AirData> records1, List<AirData> records2, List<AirData> records3) {
		List<AirData> newAirDatas = new ArrayList<>();
		int count  = 0;
		for(int i=0;i<records1.size();i++) {
			AirData record1 = records1.get(i);
			if(record1.getPm25()>rOver||record1.getPm25()<rUnder) {
				double pm25_2 = records2.get(i).getPm25();
				double pm25_3 = records3.get(i).getPm25();
				
				double pm25_ave= Math.abs((pm25_2+pm25_3)/2);				
				record1.setPm25(Math.round(pm25_ave));
				count++;
				//System.out.println(record1);
			}
			newAirDatas.add(record1);
		}
		System.out.println("replaced count:"+count);
		
		return newAirDatas;
	}

	private static int checkPM25Over(int reference, List<AirData> records) {
		AtomicInteger atInt = new AtomicInteger(0);
		records.stream().forEach(record->{
			if(record.getPm25()>reference) {
				int num = atInt.incrementAndGet();				
			}
		});
		return atInt.get();
	}
	
	private static int checkPM25Under(int reference, List<AirData> records) {
		AtomicInteger atInt = new AtomicInteger(0);
		records.stream().forEach(record->{
			if(record.getPm25()<reference) {
				int num = atInt.incrementAndGet();
				
			}
		});
		return atInt.get();
	}

	private Date toDate(String input) throws ParseException {
		//2020-02-25  10:00
		SimpleDateFormat transFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		Date to = transFormat.parse(input);
		return to;
	}
	private String dateToString(Date date) throws ParseException {
		//2020-02-25  10:00
		SimpleDateFormat transFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		String str = transFormat.format(date);		
		return str;
	}
	
	
	private double toDouble(String input) {
		double value = 0;
		try {
			value = Double.parseDouble(input);
		} catch (NumberFormatException ie) {
			System.out.println("double error:" + ie);
		}
		return value;
	}

	private AirData toADCData(String[] input) {
		AirData oneData = null;
		
		try {
			oneData = new AirData(toDate(input[0]), 
					toDouble(input[1]), toDouble(input[2]), toDouble(input[3]),
					toDouble(input[4]), toDouble(input[5]),	toDouble(input[6]), 
					toDouble(input[7]), toDouble(input[8]), toDouble(input[9]), 
					toDouble(input[10]));
		} catch (ArrayIndexOutOfBoundsException ie) {
			System.out.println("ArrayIndexOutOfBoundsException:" + ie);
		} catch (ParseException e) {			
			e.printStackTrace();
		}
		return oneData;
	}

	public List<AirData> readActionLog(String fileName, String delim) throws FileNotFoundException, IOException {	
		List<AirData> records = new ArrayList<AirData>();
		try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
			String line;
			int index = 0;
			while ((line = br.readLine()) != null) {
				if (index > 0) {
					String[] values = line.split(delim);					
					AirData data = toADCData(values);
					//System.out.println(data);
					records.add(data);
					//if(debug) System.out.println(data);
				}
				index++;

			}
		}
		return records;
	}
	//write-------------------------------------------------------------
	public void csvWrite(String fileName, List<AirData> records) throws IOException {
		FileWriter csvWriter = new FileWriter(fileName);
		/*
		csvWriter.append("Name");
		csvWriter.append(",");
		csvWriter.append("Role");
		csvWriter.append(",");
		csvWriter.append("Topic");
		csvWriter.append("\n");
		*/
		csvWriter.append(getHeader());
		csvWriter.append("\n");
		
		for (AirData airData : records) {			
		    try {
				csvWriter.append(convertToCSV(airData));
			} catch (ParseException e) {			
				e.printStackTrace();
			}
		    csvWriter.append("\n");
		}

		csvWriter.flush();
		csvWriter.close();
	}
	public String getHeader() {
		return "Date,pm1.0,pm2.5,pm10,temp,humi,voc,co2,radon,24hour,1hour";
	}
	public String convertToCSV(AirData airData) throws ParseException {
		String str = null;
		Date date = airData.getDate();
		str = dateToString(date)+COMMA_DELIMITER;
		str += airData.getPm10()+COMMA_DELIMITER;
		str += airData.getPm25()+COMMA_DELIMITER;
		str += airData.getPm100()+COMMA_DELIMITER;
		str += airData.getTemp()+COMMA_DELIMITER;
		str += airData.getHumi()+COMMA_DELIMITER;
		str += airData.getVoc()+COMMA_DELIMITER;
		str += airData.getCo2()+COMMA_DELIMITER;
		str += airData.getRadon()+COMMA_DELIMITER;
		str += airData.getOneday()+COMMA_DELIMITER;
		str += airData.getOnehour();
		
		return str;
		//return datas.stream().map(op->op.toString()).collect(Collectors.joining(","));		
	}
}


class AirDatas{	
	private List<PM25> pm25List = new ArrayList<>();
	
	public void add(long timeStamp, double pm25) {
		PM25 pmEntity = new PM25(timeStamp, pm25);
		pm25List.add(pmEntity);		
		//remove(timeStamp, timeGap);		
	}
	
	public double getAVE(long timeStamp, long timeGap) {
		
		List<Double> pmAVE = new ArrayList<>();
		pm25List.stream().forEach(entity->{
			double pm25= entity.getPm25();
			long entityTime = entity.getTime();
			if(timeStamp<=(entityTime+timeGap)) { //1시간 이내
				pmAVE.add(pm25);
			}
				
		});
		OptionalDouble average = pmAVE
	            .stream()
	            .mapToDouble(a -> a)
	            .average();
		
		return average.getAsDouble();
		
	}	
	/*
	public void remove(long timeStamp,long timeGap){
		pm25List.stream().forEach(entity->{
			long entityTime = entity.getTime();
			if(timeStamp>(entityTime+timeGap)) { //1시간 이내
				pm25List.remove(entity);
			}
		});
	}*/
}
class PM25{
	long time;
	double pm25;
	
	
	public PM25(long time, double pm25) {
		super();
		this.time = time;
		this.pm25 = pm25;
	}
	public long getTime() {
		return time;
	}
	public void setTime(long time) {
		this.time = time;
	}
	public double getPm25() {
		return pm25;
	}
	public void setPm25(double pm25) {
		this.pm25 = pm25;
	}
	
}
