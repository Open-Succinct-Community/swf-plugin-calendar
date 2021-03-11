package com.venky.swf.plugins.calendar.db.model;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.venky.core.date.DateUtils;
import com.venky.swf.db.Database;
import com.venky.swf.db.table.ModelImpl;
import com.venky.swf.plugins.calendar.db.model.WorkCalendar.DayType;

public class WorkCalendarImpl extends ModelImpl<WorkCalendar>{

	public WorkCalendarImpl(WorkCalendar proxy) {
		super(proxy);
	}
	private void validateInput(java.util.Date date){
		WorkCalendar wc = getProxy();
		if (wc.getStartDate().after(date) || wc.getEndDate().before(date)){
			throw new IllegalArgumentException("Outside calendar Range");
		}
	}
	public DayType getDayType(java.util.Date date){
		return isWorking(date) ? DayType.WORKING : DayType.NON_WORKING;
	}
	public boolean isWorking(Date date){
		return getWorkSlot(date) != null;
	}
	
	public Date nextWorkingDay(Date after){
		Date nextDay = DateUtils.addHours(after, 24);
		while (!isWorking(nextDay)){
			nextDay = DateUtils.addHours(nextDay, 24);
		}
		return nextDay;
	}

	public WorkSlot getSpecialWorkSlot(java.util.Date date){
		WorkCalendar wc = getProxy();
		validateInput(date);

		for (SpecialWorkDay workDay : wc.getSpecialWorkDays()){
			Date st = DateUtils.getTimeOfDay(workDay.getDate(),workDay.getWorkSlot().getStartTime());
			Date et = DateUtils.getTimeOfDay(workDay.getDate(),workDay.getWorkSlot().getEndTime());
			if ((workDay.getDate().equals(date) || st.compareTo(date) <= 0 ) && et.compareTo(date) >= 0){
				return workDay.getWorkSlot();
			}
		}
		for (SpecialNonWorkDay nonWorkDay : wc.getSpecialNonWorkDays()){
			if (DateUtils.getStartOfDay(date).equals(DateUtils.getStartOfDay(nonWorkDay.getDate()))){
				return Database.getTable(WorkSlot.class).newRecord();
			}
		}

		if (wc.getParentWorkCalendarId() != null){
			return wc.getParentWorkCalendar().getSpecialWorkSlot(date);
		}else {
			return null;
		}
	}
	public WorkSlot getDefaultWorkSlot(java.util.Date date){
		WorkCalendar wc = getProxy();
		validateInput(date);

		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		int iDow = cal.get(Calendar.DAY_OF_WEEK);
		List<WorkDay> workdays = wc.getWorkDays();

		for (WorkDay day : workdays){
			if (DOW.getDOWNumber(day.getDayOfWeek()) == iDow){
				Date shiftStart = DateUtils.getTimeOfDay(date,day.getWorkSlot().getStartTime());
				Date shiftEnd = DateUtils.getTimeOfDay(date,day.getWorkSlot().getEndTime());
				if ((shiftStart.compareTo(date) <= 0  || date.getTime() == DateUtils.getStartOfDay(date.getTime())) && shiftEnd.compareTo(date) >=0){
					return day.getWorkSlot();
				}
			}
		}
		if (!workdays.isEmpty() || wc.getParentWorkCalendarId() == null){
			return null;
		}else {
			return wc.getParentWorkCalendar().getDefaultWorkSlot(date);
		}
	}

	public WorkSlot getWorkSlot(java.util.Date date) {
		WorkCalendar wc = getProxy();
		validateInput(date);

		WorkSlot specialWorkSlot = getSpecialWorkSlot(date);
		if (specialWorkSlot != null){
			if (specialWorkSlot.getRawRecord().isNewRecord()){
				return null; // Special non working day.
			}else {
				return specialWorkSlot;
			}
		}

		return  getDefaultWorkSlot(date);
	}
}