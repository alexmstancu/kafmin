package org.kafmin.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import org.kafmin.web.rest.TestUtil;

public class BrokerTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Broker.class);
        Broker broker1 = new Broker();
        broker1.setId(1L);
        Broker broker2 = new Broker();
        broker2.setId(broker1.getId());
        assertThat(broker1).isEqualTo(broker2);
        broker2.setId(2L);
        assertThat(broker1).isNotEqualTo(broker2);
        broker1.setId(null);
        assertThat(broker1).isNotEqualTo(broker2);
    }
}
