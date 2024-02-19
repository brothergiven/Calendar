package Manager;

import java.time.*;

public class DailySchedule {
	public int start, end; // 0 ~ 12
	public String name, content;
	public LocalDate date;

	public DailySchedule(LocalDate date) {
		this.date = date;
	}
	
	
	public String getMonthlyLine(){
		return String.format("%02d:00-%02d:00", start * 2, end * 2) + name;
		
	}
	
	public String toString() {
		return name + "%" + content + "%" + start + "%" + end;
	}
	
}
