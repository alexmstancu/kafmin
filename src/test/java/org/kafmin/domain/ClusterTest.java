package org.kafmin.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import org.kafmin.web.rest.TestUtil;

public class ClusterTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Cluster.class);
        Cluster cluster1 = new Cluster();
        cluster1.setId(1L);
        Cluster cluster2 = new Cluster();
        cluster2.setId(cluster1.getId());
        assertThat(cluster1).isEqualTo(cluster2);
        cluster2.setId(2L);
        assertThat(cluster1).isNotEqualTo(cluster2);
        cluster1.setId(null);
        assertThat(cluster1).isNotEqualTo(cluster2);
    }
}
