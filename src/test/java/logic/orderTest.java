package logic;

import java.util.Calendar;
import java.util.Date;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.parse4j.ParseException;

import data.management.DBManager;

public class orderTest {

	private static String startDate;
	private String driverId;
	
	@Before
	public void setUpTest() throws ParseException, InterruptedException {
		DBManager.initialize();
		try{
			
			// create new order
			Date startTime =new Date();
			Calendar cal = Calendar.getInstance(); // creates calendar
		    cal.setTime(startTime); // sets calendar time/date
		    cal.add(Calendar.HOUR_OF_DAY, 2);
			Date endTime =cal.getTime();
			this.startDate = startTime.toString();
			this.driverId= "3333333";
			new Order("3333333", "123", startTime, endTime);
			Thread.sleep(6000);

		} catch (final Exception ¢) {
			¢.printStackTrace();
			Assert.fail();
		}
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void checkIfDriverNull() throws ParseException, InterruptedException{
		Date startTime =new Date();
		Calendar cal = Calendar.getInstance(); // creates calendar
	    cal.setTime(startTime); // sets calendar time/date
	    cal.add(Calendar.HOUR_OF_DAY, 2);
		Date endTime =cal.getTime();
		new Order(null, "123", startTime, endTime);
		Thread.sleep(6000);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void checkIfSlotNull() throws ParseException, InterruptedException{
		Date startTime =new Date();
		Calendar cal = Calendar.getInstance(); // creates calendar
	    cal.setTime(startTime); // sets calendar time/date
	    cal.add(Calendar.HOUR_OF_DAY, 2);
		Date endTime =cal.getTime();
		new Order("3333333", null, startTime, endTime);
		Thread.sleep(6000);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void checkIfStartDateNull() throws ParseException, InterruptedException{
		new Order("3333333", "123", null, new Date());
		Thread.sleep(6000);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void checkIfEndDateNull() throws ParseException, InterruptedException{
		new Order("3333333", "123", new Date(), null);
		Thread.sleep(6000);
	}
	
	@After
	public void finishTest() throws ParseException, InterruptedException {
		String id=this.driverId.toString()+startDate.toString()+"1";
		Order o = new Order(id);
		Thread.sleep(6000);
		o.removeDriverFromDB();
		Thread.sleep(6000);
	}
}