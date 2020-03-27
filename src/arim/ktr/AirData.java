package arim.ktr;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * arrayList 형태로 취한다.
 * 
 * @author user
 *
 */
public class AirData {
	Date date;
	double pm10;
	double pm25;
	double pm100;
	double temp;
	double humi;
	double voc;
	double co2;
	double radon;
	double oneday;
	double onehour;
	
	public AirData() {}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public double getPm10() {
		return pm10;
	}

	public void setPm10(double pm10) {
		this.pm10 = pm10;
	}

	public double getPm25() {
		return pm25;
	}

	public void setPm25(double pm25) {
		this.pm25 = pm25;
	}

	public double getPm100() {
		return pm100;
	}

	public void setPm100(double pm100) {
		this.pm100 = pm100;
	}

	public double getTemp() {
		return temp;
	}

	public void setTemp(double temp) {
		this.temp = temp;
	}

	public double getHumi() {
		return humi;
	}

	public void setHumi(double humi) {
		this.humi = humi;
	}

	public double getVoc() {
		return voc;
	}

	public void setVoc(double voc) {
		this.voc = voc;
	}

	public double getCo2() {
		return co2;
	}

	public void setCo2(double co2) {
		this.co2 = co2;
	}

	public double getRadon() {
		return radon;
	}

	public void setRadon(double radon) {
		this.radon = radon;
	}

	public double getOneday() {
		return oneday;
	}

	public void setOneday(double oneday) {
		this.oneday = oneday;
	}

	public double getOnehour() {
		return onehour;
	}

	public void setOnehour(double onehour) {
		this.onehour = onehour;
	}

	public AirData(Date date, double pm10, double pm25, double pm100, double temp, double humi, double voc, double co2,
			double radon, double oneday, double onehour) {
		super();
		this.date = date;
		this.pm10 = pm10;
		this.pm25 = pm25;
		this.pm100 = pm100;
		this.temp = temp;
		this.humi = humi;
		this.voc = voc;
		this.co2 = co2;
		this.radon = radon;
		this.oneday = oneday;
		this.onehour = onehour;
	}

	@Override
	public String toString() {
		return "AirData [date=" + date + ", pm10=" + pm10 + ", pm25=" + pm25 + ", pm100=" + pm100 + ", temp=" + temp
				+ ", humi=" + humi + ", voc=" + voc + ", co2=" + co2 + ", radon=" + radon + ", oneday=" + oneday
				+ ", onehour=" + onehour + "]";
	}	
	
	

    
}