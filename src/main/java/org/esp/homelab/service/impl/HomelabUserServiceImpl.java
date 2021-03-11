package org.esp.homelab.service.impl;

import java.util.Optional;
import org.esp.homelab.domain.HomelabUser;
import org.esp.homelab.repository.HomelabUserRepository;
import org.esp.homelab.repository.UserRepository;
import org.esp.homelab.service.HomelabUserService;
import org.esp.homelab.service.dto.HomelabUserDTO;
import org.esp.homelab.service.mapper.HomelabUserMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link HomelabUser}.
 */
@Service
@Transactional
public class HomelabUserServiceImpl implements HomelabUserService {
    private final Logger log = LoggerFactory.getLogger(HomelabUserServiceImpl.class);

    private final HomelabUserRepository homelabUserRepository;

    private final HomelabUserMapper homelabUserMapper;

    private final UserRepository userRepository;

    public HomelabUserServiceImpl(
        HomelabUserRepository homelabUserRepository,
        HomelabUserMapper homelabUserMapper,
        UserRepository userRepository
    ) {
        this.homelabUserRepository = homelabUserRepository;
        this.homelabUserMapper = homelabUserMapper;
        this.userRepository = userRepository;
    }

    @Override
    public HomelabUserDTO save(HomelabUserDTO homelabUserDTO) {
        log.debug("Request to save HomelabUser : {}", homelabUserDTO);
        HomelabUser homelabUser = homelabUserMapper.toEntity(homelabUserDTO);
        Long userId = homelabUserDTO.getUserId();
        userRepository.findById(userId).ifPresent(homelabUser::user);
        homelabUser = homelabUserRepository.save(homelabUser);
        return homelabUserMapper.toDto(homelabUser);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<HomelabUserDTO> findAll(Pageable pageable) {
        log.debug("Request to get all HomelabUsers");
        return homelabUserRepository.findAll(pageable).map(homelabUserMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<HomelabUserDTO> findOne(Long id) {
        log.debug("Request to get HomelabUser : {}", id);
        return homelabUserRepository.findById(id).map(homelabUserMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete HomelabUser : {}", id);
        homelabUserRepository.deleteById(id);
    }
}
