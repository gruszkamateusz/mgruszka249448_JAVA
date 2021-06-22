package queuesyspack;

public interface AgentMXBean {
	void hello();
	
	char getNextCategorySymbol();
	
	void setNextCategorySymbol(char symbol);
	
	void addTicketCategory(TicketCategory category);
	
	void removeTicketCategory(TicketCategory category);
	
	void editTicketCategory(TicketCategory category);
	
	TicketCategory[] getTicketCategoryList();	
}
