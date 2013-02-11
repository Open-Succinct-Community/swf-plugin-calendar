package com.venky.swf.plugins.calendar.db.model;

import java.sql.Date;

import com.venky.swf.db.annotations.column.IS_NULLABLE;
import com.venky.swf.db.annotations.column.UNIQUE_KEY;
import com.venky.swf.db.annotations.column.ui.HIDDEN;
import com.venky.swf.db.model.Model;

public interface SpecialWorkDay extends Model{
	@UNIQUE_KEY
	@IS_NULLABLE(false)
	@HIDDEN
	public Integer getWorkCalendarId();
	public void setWorkCalendarId(Integer id);
	public WorkCalendar getWorkCalendar();
	
	@UNIQUE_KEY
	public Date getDate();
	public void setDate(Date date);
	
	public String getPurpose();
	public void setPurpose(String Purpose);

	@UNIQUE_KEY
	public Integer getWorkSlotId();
	public void setWorkSlotId(Integer id);
	public WorkSlot getWorkSlot();
}
