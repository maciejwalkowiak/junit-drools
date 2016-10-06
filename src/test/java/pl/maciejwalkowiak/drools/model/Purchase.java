package pl.maciejwalkowiak.drools.model;

public class Purchase {
    private final Customer customer;
    private final Ticket ticket;

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
