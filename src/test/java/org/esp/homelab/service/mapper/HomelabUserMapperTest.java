package org.esp.homelab.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class HomelabUserMapperTest {
    private HomelabUserMapper homelabUserMapper;

    @BeforeEach
    public void setUp() {
        homelabUserMapper = new HomelabUserMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(homelabUserMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(homelabUserMapper.fromId(null)).isNull();
    }
}
