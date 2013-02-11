package com.venky.swf.plugins.calendar.db.model;

import java.sql.Date;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.venky.core.date.DateUtils;
import com.venky.core.util.ExceptionUtil;
import com.venky.swf.db.Database;
import com.venky.swf.plugins.calendar.db.model.WorkCalendar.DayType;
import com.venky.swf.routing.Router;

public class WorkCalendarTest {

	@BeforeClass
	public static void setUp(){
		Router.instance().setLoader(WorkCalendarTest.class.getClassLoader());
		Database.getInstance().open(null);
	}
	@AfterClass
	public static void close(){
		Database.getInstance().close();
	}
	@After
	public void rollback(){
		Database.getInstance().getCurrentTransaction().rollback(null);
	}
	
	@Test
	public void test() {
		WorkCalendar cal = Database.getTable(WorkCalendar.class).newRecord();
		cal.setStartDate(new Date(DateUtils.getDate("01/01/2012").getTime()));
		cal.setEndDate(new Date(DateUtils.getDate("31/12/2011").getTime()));
		cal.setName("2012");
		try {
			cal.save();
			Assert.fail("Did not fail with it was supposed to fail");
		}catch (Exception e){
			Assert.assertEquals(ExceptionUtil.getRootCause(e).getMessage(), "Start date cannot be after End date!");
			cal.setEndDate(new Date(DateUtils.getDate("31/12/2012").getTime()));
			cal.save();
		}
		try {
			cal.getDayType(new Date(System.currentTimeMillis()));
			Assert.fail("Did not fail with it was supposed to fail");
		}catch (Exception ex){
			Assert.assertEquals(ExceptionUtil.getRootCause(ex).getMessage(), "Outside calendar Range");
			java.util.Date date = DateUtils.getDate("01/02/2012");
			
			Assert.assertNull(cal.getSpecialDayType(date)) ;
			Assert.assertEquals(DayType.NON_WORKING ,cal.getDefaultDayType(date)) ;
			Assert.assertEquals(DayType.NON_WORKING ,cal.getDayType(date)) ;
			Assert.assertEquals(false, cal.isWorking(date));
		}
		WorkSlot slot = Database.getTable(WorkSlot.class).newRecord();
		slot.setWorkCalendarId(cal.getId());
		slot.setStartTime("09:00");
		slot.setEndTime("05:30");
		slot.setName("Default");
		try {
			slot.save();
			Assert.fail("Slot save should have failed but passed.");
		}catch(Exception ex){
			Assert.assertEquals(ExceptionUtil.getRootCause(ex).getMessage(), "Start time cannot be after End time!");
			slot.setEndTime("17:30");
			slot.save();
		}
		
		for (String dow: DOW.iDows.keySet()){
			if (DOW.getDOWNumber(dow) != 1){
				WorkDay day = Database.getTable(WorkDay.class).newRecord();
				day.setWorkCalendarId(cal.getId());
				day.setWorkSlotId(slot.getId());
				day.setDayOfWeek(dow);
				day.save();
			}
		}
		java.util.Date dec25 = DateUtils.getDate("25/12/2012");
		Assert.assertFalse(cal.isWorking(dec25));
		
		java.util.Date Dec26 = DateUtils.getDate("26/12/2012");
		java.util.Date dec24 = DateUtils.getDate("24/12/2012");
		java.util.Date dec23 = DateUtils.getDate("23/12/2012");
		Assert.assertFalse(cal.isWorking(Dec26));
		
		Assert.assertFalse(cal.isWorking(DateUtils.getTimeOfDay(Dec26,"08:00")));
		Assert.assertTrue(cal.isWorking(DateUtils.getTimeOfDay(Dec26,"09:00")));
		Assert.assertTrue(cal.isWorking(DateUtils.getTimeOfDay(Dec26,"12:30")));
		Assert.assertTrue(cal.isWorking(DateUtils.getTimeOfDay(Dec26,"17:30")));
		Assert.assertFalse(cal.isWorking(DateUtils.getTimeOfDay(Dec26,"17:31")));
		Assert.assertTrue(cal.isWorking(DateUtils.getTimeOfDay(dec24,"17:00")));
		
		Assert.assertFalse(cal.isWorking(DateUtils.getTimeOfDay(dec25,"08:00")));
		Assert.assertTrue(cal.isWorking(DateUtils.getTimeOfDay(dec25,"09:00")));
		Assert.assertTrue(cal.isWorking(DateUtils.getTimeOfDay(dec25,"12:30")));
		Assert.assertTrue(cal.isWorking(DateUtils.getTimeOfDay(dec25,"17:30")));
		Assert.assertFalse(cal.isWorking(DateUtils.getTimeOfDay(dec25,"17:31")));

		
		SpecialNonWorkDay christmas = Database.getTable(SpecialNonWorkDay.class).newRecord();
		christmas.setDate(new Date(dec25.getTime()));
		christmas.setPurpose("Christmas");
		christmas.setWorkCalendarId(cal.getId());
		christmas.save();
		
		Assert.assertFalse(cal.isWorking(DateUtils.getTimeOfDay(dec25,"08:00")));
		Assert.assertFalse(cal.isWorking(DateUtils.getTimeOfDay(dec25,"09:00")));
		Assert.assertFalse(cal.isWorking(DateUtils.getTimeOfDay(dec25,"12:30")));
		Assert.assertFalse(cal.isWorking(DateUtils.getTimeOfDay(dec25,"17:30")));
		Assert.assertFalse(cal.isWorking(DateUtils.getTimeOfDay(dec25,"17:31")));
		

		
		Assert.assertFalse(cal.isWorking(DateUtils.getTimeOfDay(dec23,"17:00")));
		SpecialWorkDay componChristmas = Database.getTable(SpecialWorkDay.class).newRecord();
		componChristmas.setDate(new Date(dec23.getTime()));
		componChristmas.setWorkCalendarId(cal.getId());
		componChristmas.setWorkSlotId(slot.getId());
		componChristmas.setPurpose("Compensate for Christmas");
		componChristmas.save();
		
		Assert.assertFalse(cal.isWorking(DateUtils.getTimeOfDay(dec23,"08:00")));
		Assert.assertTrue(cal.isWorking(DateUtils.getTimeOfDay(dec23,"09:00")));
		Assert.assertTrue(cal.isWorking(DateUtils.getTimeOfDay(dec23,"12:30")));
		Assert.assertTrue(cal.isWorking(DateUtils.getTimeOfDay(dec23,"17:30")));
		Assert.assertFalse(cal.isWorking(DateUtils.getTimeOfDay(dec23,"17:31")));
		
		
	}

}
