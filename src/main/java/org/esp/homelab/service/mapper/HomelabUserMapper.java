package org.esp.homelab.service.mapper;

import org.esp.homelab.domain.*;
import org.esp.homelab.service.dto.HomelabUserDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link HomelabUser} and its DTO {@link HomelabUserDTO}.
 */
@Mapper(componentModel = "spring", uses = { UserMapper.class })
public interface HomelabUserMapper extends EntityMapper<HomelabUserDTO, HomelabUser> {
    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.login", target = "userLogin")
    HomelabUserDTO toDto(HomelabUser homelabUser);

    @Mapping(source = "userId", target = "user")
    HomelabUser toEntity(HomelabUserDTO homelabUserDTO);

    default HomelabUser fromId(Long id) {
        if (id == null) {
            return null;
        }
        HomelabUser homelabUser = new HomelabUser();
        homelabUser.setId(id);
        return homelabUser;
    }
}
