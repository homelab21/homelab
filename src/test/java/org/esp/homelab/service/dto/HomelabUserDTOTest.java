package org.esp.homelab.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.esp.homelab.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

public class HomelabUserDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(HomelabUserDTO.class);
        HomelabUserDTO homelabUserDTO1 = new HomelabUserDTO();
        homelabUserDTO1.setId(1L);
        HomelabUserDTO homelabUserDTO2 = new HomelabUserDTO();
        assertThat(homelabUserDTO1).isNotEqualTo(homelabUserDTO2);
        homelabUserDTO2.setId(homelabUserDTO1.getId());
        assertThat(homelabUserDTO1).isEqualTo(homelabUserDTO2);
        homelabUserDTO2.setId(2L);
        assertThat(homelabUserDTO1).isNotEqualTo(homelabUserDTO2);
        homelabUserDTO1.setId(null);
        assertThat(homelabUserDTO1).isNotEqualTo(homelabUserDTO2);
    }
}
