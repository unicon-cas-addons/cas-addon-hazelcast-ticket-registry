package net.unicon.cas.addon.ticket.registry;

import org.jasig.cas.authentication.Authentication;
import org.jasig.cas.authentication.principal.Service;
import org.jasig.cas.ticket.ExpirationPolicy;
import org.jasig.cas.ticket.ServiceTicket;
import org.jasig.cas.ticket.TicketGrantingTicket;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class HazelcastTicketRegistryTests {

    @Autowired
    HazelcastTicketRegistry hzTicketRegistry1;

    @Autowired
    HazelcastTicketRegistry hzTicketRegistry2;

    @Test
    public void basicOperationsAndClustering() throws Exception {
        this.hzTicketRegistry1.addTicket(newTestTgt());

        assertNotNull(this.hzTicketRegistry1.getTicket("TGT-TEST"));
        assertNotNull(this.hzTicketRegistry2.getTicket("TGT-TEST"));
        assertTrue(this.hzTicketRegistry2.deleteTicket("TGT-TEST"));
        assertFalse(this.hzTicketRegistry1.deleteTicket("TGT-TEST"));
        assertNull(this.hzTicketRegistry1.getTicket("TGT-TEST"));
        assertNull(this.hzTicketRegistry2.getTicket("TGT-TEST"));

        ServiceTicket st = newTestSt();
        this.hzTicketRegistry2.addTicket(st);

        assertNotNull(this.hzTicketRegistry1.getTicket("ST-TEST"));
        assertNotNull(this.hzTicketRegistry2.getTicket("ST-TEST"));
        this.hzTicketRegistry1.deleteTicket("ST-TEST");
        assertNull(this.hzTicketRegistry1.getTicket("ST-TEST"));
        assertNull(this.hzTicketRegistry2.getTicket("ST-TEST"));
    }

    private TicketGrantingTicket newTestTgt() {
        return new MockTgt();
    }

    private ServiceTicket newTestSt() {
        return new MockSt();
    }

    private static class MockTgt implements TicketGrantingTicket {
        @Override
        public Authentication getAuthentication() {
            return null;
        }

        @Override
        public List<Authentication> getSupplementalAuthentications() {
            return null;
        }

        @Override
        public ServiceTicket grantServiceTicket(String id, Service service, ExpirationPolicy expirationPolicy, boolean credentialsProvided) {
            return null;
        }

        @Override
        public Map<String, Service> getServices() {
            return null;
        }

        @Override
        public void removeAllServices() {

        }

        @Override
        public void markTicketExpired() {

        }

        @Override
        public boolean isRoot() {
            return false;
        }

        @Override
        public TicketGrantingTicket getRoot() {
            return null;
        }

        @Override
        public List<Authentication> getChainedAuthentications() {
            return null;
        }

        @Override
        public String getId() {
            return "TGT-TEST";
        }

        @Override
        public boolean isExpired() {
            return false;
        }

        @Override
        public TicketGrantingTicket getGrantingTicket() {
            return this;
        }

        @Override
        public long getCreationTime() {
            return 0;
        }

        @Override
        public int getCountOfUses() {
            return 0;
        }
    }

    private static class MockSt implements ServiceTicket {
        @Override
        public Service getService() {
            return null;
        }

        @Override
        public boolean isFromNewLogin() {
            return false;
        }

        @Override
        public boolean isValidFor(Service service) {
            return false;
        }

        @Override
        public TicketGrantingTicket grantTicketGrantingTicket(String id, Authentication authentication, ExpirationPolicy expirationPolicy) {
            return null;
        }

        @Override
        public String getId() {
            return "ST-TEST";
        }

        @Override
        public boolean isExpired() {
            return false;
        }

        @Override
        public TicketGrantingTicket getGrantingTicket() {
            return null;
        }

        @Override
        public long getCreationTime() {
            return 0;
        }

        @Override
        public int getCountOfUses() {
            return 0;
        }
    }
}
