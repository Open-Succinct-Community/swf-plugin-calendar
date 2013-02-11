package com.venky.swf.plugins.calendar.db.model;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.venky.core.date.DateUtils;
import com.venky.swf.db.table.ModelImpl;
import com.venky.swf.plugins.calendar.db.model.WorkCalendar.DayType;

public class WorkCalendarImpl extends ModelImpl<WorkCalendar>{

	public WorkCalendarImpl(WorkCalendar proxy) {
		super(proxy);
	}
	public DayType getSpecialDayType(Date date){
		validateInput(date);
		WorkCalendar wc = getProxy();
		
		DayType dayType = null ;
		for (SpecialWorkDay sd: wc.getSpecialWorkDays()){
			Date st = DateUtils.getTimeOfDay(sd.getDate(),sd.getWorkSlot().getStartTime());
			Date et = DateUtils.getTimeOfDay(sd.getDate(),sd.getWorkSlot().getEndTime()); 
			
			if (st.compareTo(date) <= 0 && et.compareTo(date) >= 0){
				dayType = DayType.WORKING; 
				break;
			}
		}
		
		if (dayType == null){
			Date startOfInDate = DateUtils.getStartOfDay(date);
			for (SpecialNonWorkDay sd:wc.getSpecialNonWorkDays()){
				if (DateUtils.getStartOfDay(sd.getDate()).equals(startOfInDate)){
					dayType = DayType.NON_WORKING; 
					break;
				}
			}
		}
		
		if (dayType != null || wc.getParentWorkCalendarId() == null){ 
			return dayType ;
		}else {
			return wc.getParentWorkCalendar().getSpecialDayType(date);
		}
	}
	public DayType getDefaultDayType(Date date){
		validateInput(date);
		WorkCalendar wc = getProxy();
		
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		int iDow = cal.get(Calendar.DAY_OF_WEEK);
		List<WorkDay> workdays = wc.getWorkDays(); 
		DayType dayType = null ; 
		
		for (WorkDay day : workdays){
			if (DOW.getDOWNumber(day.getDayOfWeek()) == iDow){
				Date shiftStart = DateUtils.getTimeOfDay(date,day.getWorkSlot().getStartTime());
				Date shiftEnd = DateUtils.getTimeOfDay(date,day.getWorkSlot().getEndTime());
				if (shiftStart.compareTo(date) <= 0 && shiftEnd.compareTo(date)>=0){
					dayType = DayType.WORKING; 
					break;
				}
			}
		}
		if (dayType == null){
			if (!workdays.isEmpty() || wc.getParentWorkCalendarId() == null){
				dayType = DayType.NON_WORKING;
			}else {
				dayType = wc.getParentWorkCalendar().getDefaultDayType(date);
			}
		}
		return dayType;
	}
	private void validateInput(java.util.Date date){
		WorkCalendar wc = getProxy();
		if (wc.getStartDate().after(date) || wc.getEndDate().before(date)){
			throw new IllegalArgumentException("Outside calendar Range");
		}
	}
	public DayType getDayType(java.util.Date date){
		validateInput(date);
		DayType type = getSpecialDayType(date);
		if (type == null){
			type = getDefaultDayType(date);
		}
		return type;
	}
	public boolean isWorking(Date date){
		return getDayType(date) == DayType.WORKING;
	}
	
	public Date nextWorkingDay(Date after){
		Date nextDay = DateUtils.addHours(after, 24);
		while (!isWorking(nextDay)){
			nextDay = DateUtils.addHours(nextDay, 24);
		}
		return nextDay;
	}
}
