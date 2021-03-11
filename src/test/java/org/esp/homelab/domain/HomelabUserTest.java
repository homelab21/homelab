package org.esp.homelab.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.esp.homelab.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

public class HomelabUserTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(HomelabUser.class);
        HomelabUser homelabUser1 = new HomelabUser();
        homelabUser1.setId(1L);
        HomelabUser homelabUser2 = new HomelabUser();
        homelabUser2.setId(homelabUser1.getId());
        assertThat(homelabUser1).isEqualTo(homelabUser2);
        homelabUser2.setId(2L);
        assertThat(homelabUser1).isNotEqualTo(homelabUser2);
        homelabUser1.setId(null);
        assertThat(homelabUser1).isNotEqualTo(homelabUser2);
    }
}
