package queuesyspack;

import java.beans.ConstructorProperties;

import javax.management.openmbean.CompositeData;
import javax.management.openmbean.CompositeDataSupport;
import javax.management.openmbean.CompositeDataView;
import javax.management.openmbean.CompositeType;

public class TicketCategory implements CompositeDataView {
	public static char nextSymbol = 'A';
	public String name;
	public int priority;
	public char symbol;
	public int ticketsNum;
	
	public TicketCategory() {
		name = null;
		priority = 0;
		symbol = 0;
		ticketsNum = 0;
	}
	
	public TicketCategory(String name, int priority) {
		ticketsNum = 0;
		this.name = name;
		this.priority = priority;
		this.symbol = nextSymbol;
		++nextSymbol;
	}
	
	@ConstructorProperties({ "name", "priority", "symbol", "ticketsNum"})
	public TicketCategory(String name, int priority, char symbol, int ticketsNum) {
		this.ticketsNum = ticketsNum;
		this.name = name;
		this.priority = priority;
		this.symbol = symbol;
	}
	
	public static TicketCategory from(CompositeData cd) {
		return new TicketCategory((String) cd.get("name"), 
				(int)cd.get("priority"), (char) cd.get("symbol"), 
				(int) cd.get("ticketsNum"));
	}
	
	@Override
	public CompositeData toCompositeData(CompositeType ct) {
		try {
			CompositeData cd = new CompositeDataSupport(ct, new String[] { "name", "priority", "symbol", "ticketsNum" },
					new Object[] { name, priority, symbol, ticketsNum });
			assert ct.isValue(cd); 
			return cd;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public Ticket createTicket() {
		return new Ticket(this, ticketsNum++);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public char getSymbol() {
		return symbol;
	}

	public void setSymbol(char symbol) {
		this.symbol = symbol;
	}

	public int getTicketsNum() {
		return ticketsNum;
	}

	public void setTicketsNum(int ticketsNum) {
		this.ticketsNum = ticketsNum;
	}

	@Override
	public String toString() {
		return symbol + ": " + name + "; " + priority;
	}
}
