package pl.maciejwalkowiak.drools;

public class Purchase {
    private Customer customer;
    private Ticket ticket;

    public Purchase(Customer customer) {
        this.customer = customer;
        this.ticket = new Ticket();
    }

    public Customer getCustomer() {
        return customer;
    }

    public Ticket getTicket() {
        return ticket;
    }
}
