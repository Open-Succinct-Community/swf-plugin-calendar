package com.venky.swf.plugins.calendar.db.model;

import java.sql.Date;
import java.util.List;

import com.venky.swf.db.annotations.column.IS_NULLABLE;
import com.venky.swf.db.annotations.column.IS_VIRTUAL;
import com.venky.swf.db.annotations.column.UNIQUE_KEY;
import com.venky.swf.db.annotations.column.relationship.CONNECTED_VIA;
import com.venky.swf.db.annotations.model.MENU;
import com.venky.swf.db.model.Model;

@MENU("Inventory")
public interface WorkCalendar extends Model{
	@UNIQUE_KEY
	public String getName();
	public void setName(String name);
	
	public Long getParentWorkCalendarId();
	public void setParentWorkCalendarId(Long id);
	public WorkCalendar getParentWorkCalendar();
	
	@IS_NULLABLE(false)
	public Date getStartDate();
	public void setStartDate(Date date);
	
	@IS_NULLABLE(false)
	public Date getEndDate();
	public void setEndDate(Date date);
	
	public List<WorkSlot> getWorkSlots(); //These are global list of slots for the calendar on any day.


	public List<WorkDay> getWorkDays();
	public List<SpecialWorkDay> getSpecialWorkDays();
	public List<SpecialNonWorkDay> getSpecialNonWorkDays();
	
	@CONNECTED_VIA("PARENT_WORK_CALENDAR_ID")
	public List<WorkCalendar> getChildCalendars();

	public enum DayType {
		NON_WORKING,
		WORKING
	}
	
	@IS_VIRTUAL
	public boolean isWorking(java.util.Date date);

	@IS_VIRTUAL
	public WorkSlot getWorkSlot(java.util.Date date);

	@IS_VIRTUAL
	WorkSlot getSpecialWorkSlot(java.util.Date date);

	@IS_VIRTUAL
	WorkSlot getDefaultWorkSlot(java.util.Date date);
	
	@IS_VIRTUAL
	public java.util.Date nextWorkingDay(java.util.Date after);

	@IS_VIRTUAL
	/**
	 * Both included
	 */
	public int numWorkingDays(java.util.Date from, java.util.Date to);

}
