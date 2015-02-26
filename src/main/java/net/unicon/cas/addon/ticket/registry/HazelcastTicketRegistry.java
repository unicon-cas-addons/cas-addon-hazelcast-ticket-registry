package net.unicon.cas.addon.ticket.registry;

import com.hazelcast.config.MapConfig;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import org.jasig.cas.ticket.ServiceTicket;
import org.jasig.cas.ticket.Ticket;
import org.jasig.cas.ticket.TicketGrantingTicket;
import org.jasig.cas.ticket.registry.AbstractTicketRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.concurrent.TimeUnit;

/**
 * Hazelcast-based implementation of a {@link org.jasig.cas.ticket.registry.TicketRegistry}
 * <p/>
 * This implementation just wraps the Hazelcast's {@link com.hazelcast.core.IMap} which is an extension of the
 * standard Java's <code>ConcurrentMap</code>. The heavy lifting of distributed data partitioning,
 * network cluster discovery and join, data replication, etc. is done by Hazelcast's Map implementation.
 * <p/>
 *
 * @author Dmitriy Kopylenko
 * @author Jonathan Johnson
 * @since 1.0.0
 */
public class HazelcastTicketRegistry extends AbstractTicketRegistry {

    private static final Logger logger = LoggerFactory.getLogger(HazelcastTicketRegistry.class);

    private final IMap<String, Ticket> registry;

    private final long serviceTicketTimeoutInSeconds;

    private final long ticketGrantingTicketTimoutInSeconds;

    /**
     * @param hz An instance of <code>HazelcastInstance</code>
     * @param mapName Name of map to use
     * @param ticketGrantingTicketTimoutInSeconds TTL for TGT entries
     * @param serviceTicketTimeoutInSeconds TTL for ST entries
     */
    public HazelcastTicketRegistry(HazelcastInstance hz, String mapName, long ticketGrantingTicketTimoutInSeconds, long serviceTicketTimeoutInSeconds) {
        logInitialization(hz, mapName, ticketGrantingTicketTimoutInSeconds, serviceTicketTimeoutInSeconds);
        this.registry = hz.getMap(mapName);
        this.ticketGrantingTicketTimoutInSeconds = ticketGrantingTicketTimoutInSeconds;
        this.serviceTicketTimeoutInSeconds = serviceTicketTimeoutInSeconds;
    }

    private void logInitialization(HazelcastInstance hz, String mapName, long ticketGrantingTicketTimoutInSeconds, long serviceTicketTimeoutInSeconds) {
        logger.info("Setting up Hazelcast Ticket Registry");
        logger.debug("Hazelcast instance: {}", hz);
        logger.debug("TGT timeout: [{}s]", ticketGrantingTicketTimoutInSeconds);
        logger.debug("ST timeout: [{}s]", serviceTicketTimeoutInSeconds);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addTicket(Ticket ticket) {
        long ttl = getTimeout(ticket);
        addTicket(ticket, ttl);
    }

    private void addTicket(Ticket ticket, long ttl) {
        logger.debug("Adding ticket [{}] with ttl [{}s]", ticket.getId(), ttl);
        this.registry.set(ticket.getId(), ticket, ttl, TimeUnit.SECONDS);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Ticket getTicket(String ticketId) {
        return this.registry.get(ticketId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean deleteTicket(String ticketId) {
        logger.debug("Removing ticket [{}]", ticketId);
        return this.registry.remove(ticketId) != null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<Ticket> getTickets() {
        return this.registry.values();
    }

    /**
     * A method to get the starting TTL for a ticket based upon type.
     *
     * @param t Ticket to get starting TTL for
     *
     * @return Initial TTL for ticket
     */
    private long getTimeout(Ticket t) {
        if (t instanceof TicketGrantingTicket) {
            return this.ticketGrantingTicketTimoutInSeconds;
        }
        else if (t instanceof ServiceTicket) {
            return this.serviceTicketTimeoutInSeconds;
        }
        throw new IllegalArgumentException("Invalid ticket type");
    }
}
