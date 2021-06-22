package queuesyspack;

public class Ticket {
	public TicketCategory category;
	public int number;
	public Ticket(TicketCategory cat, int num) {
		category = cat;
		number = num;
	}
	@Override
	public String toString() {
		return category.symbol + Integer.toString(number);
	}
}