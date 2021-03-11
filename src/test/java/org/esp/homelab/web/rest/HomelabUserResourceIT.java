package org.esp.homelab.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import javax.persistence.EntityManager;
import org.esp.homelab.HomelabApp;
import org.esp.homelab.domain.HomelabUser;
import org.esp.homelab.domain.User;
import org.esp.homelab.repository.HomelabUserRepository;
import org.esp.homelab.service.HomelabUserQueryService;
import org.esp.homelab.service.HomelabUserService;
import org.esp.homelab.service.dto.HomelabUserCriteria;
import org.esp.homelab.service.dto.HomelabUserDTO;
import org.esp.homelab.service.mapper.HomelabUserMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link HomelabUserResource} REST controller.
 */
@SpringBootTest(classes = HomelabApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class HomelabUserResourceIT {
    private static final String DEFAULT_NUM_CNI = "5";
    private static final String UPDATED_NUM_CNI = "3";

    private static final LocalDate DEFAULT_DATE_DE_NAISSANCE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_DE_NAISSANCE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_DATE_DE_NAISSANCE = LocalDate.ofEpochDay(-1L);

    private static final String DEFAULT_ADDRESS_LINE_1 = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS_LINE_1 = "BBBBBBBBBB";

    private static final String DEFAULT_ADDRESS_LINE_2 = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS_LINE_2 = "BBBBBBBBBB";

    private static final String DEFAULT_CITY = "AAAAAAAAAA";
    private static final String UPDATED_CITY = "BBBBBBBBBB";

    private static final String DEFAULT_PAYS = "AAAAAAAAAA";
    private static final String UPDATED_PAYS = "BBBBBBBBBB";

    private static final String DEFAULT_PHONE = "AAAAAAAAAA";
    private static final String UPDATED_PHONE = "BBBBBBBBBB";

    @Autowired
    private HomelabUserRepository homelabUserRepository;

    @Autowired
    private HomelabUserMapper homelabUserMapper;

    @Autowired
    private HomelabUserService homelabUserService;

    @Autowired
    private HomelabUserQueryService homelabUserQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restHomelabUserMockMvc;

    private HomelabUser homelabUser;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static HomelabUser createEntity(EntityManager em) {
        HomelabUser homelabUser = new HomelabUser()
            .numCNI(DEFAULT_NUM_CNI)
            .dateDeNaissance(DEFAULT_DATE_DE_NAISSANCE)
            .addressLine1(DEFAULT_ADDRESS_LINE_1)
            .addressLine2(DEFAULT_ADDRESS_LINE_2)
            .city(DEFAULT_CITY)
            .pays(DEFAULT_PAYS)
            .phone(DEFAULT_PHONE);
        // Add required entity
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        homelabUser.setUser(user);
        return homelabUser;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static HomelabUser createUpdatedEntity(EntityManager em) {
        HomelabUser homelabUser = new HomelabUser()
            .numCNI(UPDATED_NUM_CNI)
            .dateDeNaissance(UPDATED_DATE_DE_NAISSANCE)
            .addressLine1(UPDATED_ADDRESS_LINE_1)
            .addressLine2(UPDATED_ADDRESS_LINE_2)
            .city(UPDATED_CITY)
            .pays(UPDATED_PAYS)
            .phone(UPDATED_PHONE);
        // Add required entity
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        homelabUser.setUser(user);
        return homelabUser;
    }

    @BeforeEach
    public void initTest() {
        homelabUser = createEntity(em);
    }

    @Test
    @Transactional
    public void createHomelabUser() throws Exception {
        int databaseSizeBeforeCreate = homelabUserRepository.findAll().size();
        // Create the HomelabUser
        HomelabUserDTO homelabUserDTO = homelabUserMapper.toDto(homelabUser);
        restHomelabUserMockMvc
            .perform(
                post("/api/homelab-users")
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(homelabUserDTO))
            )
            .andExpect(status().isCreated());

        // Validate the HomelabUser in the database
        List<HomelabUser> homelabUserList = homelabUserRepository.findAll();
        assertThat(homelabUserList).hasSize(databaseSizeBeforeCreate + 1);
        HomelabUser testHomelabUser = homelabUserList.get(homelabUserList.size() - 1);
        assertThat(testHomelabUser.getNumCNI()).isEqualTo(DEFAULT_NUM_CNI);
        assertThat(testHomelabUser.getDateDeNaissance()).isEqualTo(DEFAULT_DATE_DE_NAISSANCE);
        assertThat(testHomelabUser.getAddressLine1()).isEqualTo(DEFAULT_ADDRESS_LINE_1);
        assertThat(testHomelabUser.getAddressLine2()).isEqualTo(DEFAULT_ADDRESS_LINE_2);
        assertThat(testHomelabUser.getCity()).isEqualTo(DEFAULT_CITY);
        assertThat(testHomelabUser.getPays()).isEqualTo(DEFAULT_PAYS);
        assertThat(testHomelabUser.getPhone()).isEqualTo(DEFAULT_PHONE);

        // Validate the id for MapsId, the ids must be same
        assertThat(testHomelabUser.getId()).isEqualTo(testHomelabUser.getUser().getId());
    }

    @Test
    @Transactional
    public void createHomelabUserWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = homelabUserRepository.findAll().size();

        // Create the HomelabUser with an existing ID
        homelabUser.setId(1L);
        HomelabUserDTO homelabUserDTO = homelabUserMapper.toDto(homelabUser);

        // An entity with an existing ID cannot be created, so this API call must fail
        restHomelabUserMockMvc
            .perform(
                post("/api/homelab-users")
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(homelabUserDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the HomelabUser in the database
        List<HomelabUser> homelabUserList = homelabUserRepository.findAll();
        assertThat(homelabUserList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void updateHomelabUserMapsIdAssociationWithNewId() throws Exception {
        // Initialize the database
        homelabUserRepository.saveAndFlush(homelabUser);
        int databaseSizeBeforeCreate = homelabUserRepository.findAll().size();

        // Add a new parent entity
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();

        // Load the homelabUser
        HomelabUser updatedHomelabUser = homelabUserRepository.findById(homelabUser.getId()).get();
        // Disconnect from session so that the updates on updatedHomelabUser are not directly saved in db
        em.detach(updatedHomelabUser);

        // Update the User with new association value
        updatedHomelabUser.setUser(user);
        HomelabUserDTO updatedHomelabUserDTO = homelabUserMapper.toDto(updatedHomelabUser);

        // Update the entity
        restHomelabUserMockMvc
            .perform(
                put("/api/homelab-users")
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedHomelabUserDTO))
            )
            .andExpect(status().isOk());

        // Validate the HomelabUser in the database
        List<HomelabUser> homelabUserList = homelabUserRepository.findAll();
        assertThat(homelabUserList).hasSize(databaseSizeBeforeCreate);
        HomelabUser testHomelabUser = homelabUserList.get(homelabUserList.size() - 1);
        // Validate the id for MapsId, the ids must be same
        // Uncomment the following line for assertion. However, please note that there is a known issue and uncommenting will fail the test.
        // Please look at https://github.com/jhipster/generator-jhipster/issues/9100. You can modify this test as necessary.
        // assertThat(testHomelabUser.getId()).isEqualTo(testHomelabUser.getUser().getId());
    }

    @Test
    @Transactional
    public void checkNumCNIIsRequired() throws Exception {
        int databaseSizeBeforeTest = homelabUserRepository.findAll().size();
        // set the field null
        homelabUser.setNumCNI(null);

        // Create the HomelabUser, which fails.
        HomelabUserDTO homelabUserDTO = homelabUserMapper.toDto(homelabUser);

        restHomelabUserMockMvc
            .perform(
                post("/api/homelab-users")
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(homelabUserDTO))
            )
            .andExpect(status().isBadRequest());

        List<HomelabUser> homelabUserList = homelabUserRepository.findAll();
        assertThat(homelabUserList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDateDeNaissanceIsRequired() throws Exception {
        int databaseSizeBeforeTest = homelabUserRepository.findAll().size();
        // set the field null
        homelabUser.setDateDeNaissance(null);

        // Create the HomelabUser, which fails.
        HomelabUserDTO homelabUserDTO = homelabUserMapper.toDto(homelabUser);

        restHomelabUserMockMvc
            .perform(
                post("/api/homelab-users")
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(homelabUserDTO))
            )
            .andExpect(status().isBadRequest());

        List<HomelabUser> homelabUserList = homelabUserRepository.findAll();
        assertThat(homelabUserList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkAddressLine1IsRequired() throws Exception {
        int databaseSizeBeforeTest = homelabUserRepository.findAll().size();
        // set the field null
        homelabUser.setAddressLine1(null);

        // Create the HomelabUser, which fails.
        HomelabUserDTO homelabUserDTO = homelabUserMapper.toDto(homelabUser);

        restHomelabUserMockMvc
            .perform(
                post("/api/homelab-users")
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(homelabUserDTO))
            )
            .andExpect(status().isBadRequest());

        List<HomelabUser> homelabUserList = homelabUserRepository.findAll();
        assertThat(homelabUserList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCityIsRequired() throws Exception {
        int databaseSizeBeforeTest = homelabUserRepository.findAll().size();
        // set the field null
        homelabUser.setCity(null);

        // Create the HomelabUser, which fails.
        HomelabUserDTO homelabUserDTO = homelabUserMapper.toDto(homelabUser);

        restHomelabUserMockMvc
            .perform(
                post("/api/homelab-users")
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(homelabUserDTO))
            )
            .andExpect(status().isBadRequest());

        List<HomelabUser> homelabUserList = homelabUserRepository.findAll();
        assertThat(homelabUserList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPaysIsRequired() throws Exception {
        int databaseSizeBeforeTest = homelabUserRepository.findAll().size();
        // set the field null
        homelabUser.setPays(null);

        // Create the HomelabUser, which fails.
        HomelabUserDTO homelabUserDTO = homelabUserMapper.toDto(homelabUser);

        restHomelabUserMockMvc
            .perform(
                post("/api/homelab-users")
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(homelabUserDTO))
            )
            .andExpect(status().isBadRequest());

        List<HomelabUser> homelabUserList = homelabUserRepository.findAll();
        assertThat(homelabUserList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPhoneIsRequired() throws Exception {
        int databaseSizeBeforeTest = homelabUserRepository.findAll().size();
        // set the field null
        homelabUser.setPhone(null);

        // Create the HomelabUser, which fails.
        HomelabUserDTO homelabUserDTO = homelabUserMapper.toDto(homelabUser);

        restHomelabUserMockMvc
            .perform(
                post("/api/homelab-users")
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(homelabUserDTO))
            )
            .andExpect(status().isBadRequest());

        List<HomelabUser> homelabUserList = homelabUserRepository.findAll();
        assertThat(homelabUserList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllHomelabUsers() throws Exception {
        // Initialize the database
        homelabUserRepository.saveAndFlush(homelabUser);

        // Get all the homelabUserList
        restHomelabUserMockMvc
            .perform(get("/api/homelab-users?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(homelabUser.getId().intValue())))
            .andExpect(jsonPath("$.[*].numCNI").value(hasItem(DEFAULT_NUM_CNI)))
            .andExpect(jsonPath("$.[*].dateDeNaissance").value(hasItem(DEFAULT_DATE_DE_NAISSANCE.toString())))
            .andExpect(jsonPath("$.[*].addressLine1").value(hasItem(DEFAULT_ADDRESS_LINE_1)))
            .andExpect(jsonPath("$.[*].addressLine2").value(hasItem(DEFAULT_ADDRESS_LINE_2)))
            .andExpect(jsonPath("$.[*].city").value(hasItem(DEFAULT_CITY)))
            .andExpect(jsonPath("$.[*].pays").value(hasItem(DEFAULT_PAYS)))
            .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE)));
    }

    @Test
    @Transactional
    public void getHomelabUser() throws Exception {
        // Initialize the database
        homelabUserRepository.saveAndFlush(homelabUser);

        // Get the homelabUser
        restHomelabUserMockMvc
            .perform(get("/api/homelab-users/{id}", homelabUser.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(homelabUser.getId().intValue()))
            .andExpect(jsonPath("$.numCNI").value(DEFAULT_NUM_CNI))
            .andExpect(jsonPath("$.dateDeNaissance").value(DEFAULT_DATE_DE_NAISSANCE.toString()))
            .andExpect(jsonPath("$.addressLine1").value(DEFAULT_ADDRESS_LINE_1))
            .andExpect(jsonPath("$.addressLine2").value(DEFAULT_ADDRESS_LINE_2))
            .andExpect(jsonPath("$.city").value(DEFAULT_CITY))
            .andExpect(jsonPath("$.pays").value(DEFAULT_PAYS))
            .andExpect(jsonPath("$.phone").value(DEFAULT_PHONE));
    }

    @Test
    @Transactional
    public void getHomelabUsersByIdFiltering() throws Exception {
        // Initialize the database
        homelabUserRepository.saveAndFlush(homelabUser);

        Long id = homelabUser.getId();

        defaultHomelabUserShouldBeFound("id.equals=" + id);
        defaultHomelabUserShouldNotBeFound("id.notEquals=" + id);

        defaultHomelabUserShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultHomelabUserShouldNotBeFound("id.greaterThan=" + id);

        defaultHomelabUserShouldBeFound("id.lessThanOrEqual=" + id);
        defaultHomelabUserShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    public void getAllHomelabUsersByNumCNIIsEqualToSomething() throws Exception {
        // Initialize the database
        homelabUserRepository.saveAndFlush(homelabUser);

        // Get all the homelabUserList where numCNI equals to DEFAULT_NUM_CNI
        defaultHomelabUserShouldBeFound("numCNI.equals=" + DEFAULT_NUM_CNI);

        // Get all the homelabUserList where numCNI equals to UPDATED_NUM_CNI
        defaultHomelabUserShouldNotBeFound("numCNI.equals=" + UPDATED_NUM_CNI);
    }

    @Test
    @Transactional
    public void getAllHomelabUsersByNumCNIIsNotEqualToSomething() throws Exception {
        // Initialize the database
        homelabUserRepository.saveAndFlush(homelabUser);

        // Get all the homelabUserList where numCNI not equals to DEFAULT_NUM_CNI
        defaultHomelabUserShouldNotBeFound("numCNI.notEquals=" + DEFAULT_NUM_CNI);

        // Get all the homelabUserList where numCNI not equals to UPDATED_NUM_CNI
        defaultHomelabUserShouldBeFound("numCNI.notEquals=" + UPDATED_NUM_CNI);
    }

    @Test
    @Transactional
    public void getAllHomelabUsersByNumCNIIsInShouldWork() throws Exception {
        // Initialize the database
        homelabUserRepository.saveAndFlush(homelabUser);

        // Get all the homelabUserList where numCNI in DEFAULT_NUM_CNI or UPDATED_NUM_CNI
        defaultHomelabUserShouldBeFound("numCNI.in=" + DEFAULT_NUM_CNI + "," + UPDATED_NUM_CNI);

        // Get all the homelabUserList where numCNI equals to UPDATED_NUM_CNI
        defaultHomelabUserShouldNotBeFound("numCNI.in=" + UPDATED_NUM_CNI);
    }

    @Test
    @Transactional
    public void getAllHomelabUsersByNumCNIIsNullOrNotNull() throws Exception {
        // Initialize the database
        homelabUserRepository.saveAndFlush(homelabUser);

        // Get all the homelabUserList where numCNI is not null
        defaultHomelabUserShouldBeFound("numCNI.specified=true");

        // Get all the homelabUserList where numCNI is null
        defaultHomelabUserShouldNotBeFound("numCNI.specified=false");
    }

    @Test
    @Transactional
    public void getAllHomelabUsersByNumCNIContainsSomething() throws Exception {
        // Initialize the database
        homelabUserRepository.saveAndFlush(homelabUser);

        // Get all the homelabUserList where numCNI contains DEFAULT_NUM_CNI
        defaultHomelabUserShouldBeFound("numCNI.contains=" + DEFAULT_NUM_CNI);

        // Get all the homelabUserList where numCNI contains UPDATED_NUM_CNI
        defaultHomelabUserShouldNotBeFound("numCNI.contains=" + UPDATED_NUM_CNI);
    }

    @Test
    @Transactional
    public void getAllHomelabUsersByNumCNINotContainsSomething() throws Exception {
        // Initialize the database
        homelabUserRepository.saveAndFlush(homelabUser);

        // Get all the homelabUserList where numCNI does not contain DEFAULT_NUM_CNI
        defaultHomelabUserShouldNotBeFound("numCNI.doesNotContain=" + DEFAULT_NUM_CNI);

        // Get all the homelabUserList where numCNI does not contain UPDATED_NUM_CNI
        defaultHomelabUserShouldBeFound("numCNI.doesNotContain=" + UPDATED_NUM_CNI);
    }

    @Test
    @Transactional
    public void getAllHomelabUsersByDateDeNaissanceIsEqualToSomething() throws Exception {
        // Initialize the database
        homelabUserRepository.saveAndFlush(homelabUser);

        // Get all the homelabUserList where dateDeNaissance equals to DEFAULT_DATE_DE_NAISSANCE
        defaultHomelabUserShouldBeFound("dateDeNaissance.equals=" + DEFAULT_DATE_DE_NAISSANCE);

        // Get all the homelabUserList where dateDeNaissance equals to UPDATED_DATE_DE_NAISSANCE
        defaultHomelabUserShouldNotBeFound("dateDeNaissance.equals=" + UPDATED_DATE_DE_NAISSANCE);
    }

    @Test
    @Transactional
    public void getAllHomelabUsersByDateDeNaissanceIsNotEqualToSomething() throws Exception {
        // Initialize the database
        homelabUserRepository.saveAndFlush(homelabUser);

        // Get all the homelabUserList where dateDeNaissance not equals to DEFAULT_DATE_DE_NAISSANCE
        defaultHomelabUserShouldNotBeFound("dateDeNaissance.notEquals=" + DEFAULT_DATE_DE_NAISSANCE);

        // Get all the homelabUserList where dateDeNaissance not equals to UPDATED_DATE_DE_NAISSANCE
        defaultHomelabUserShouldBeFound("dateDeNaissance.notEquals=" + UPDATED_DATE_DE_NAISSANCE);
    }

    @Test
    @Transactional
    public void getAllHomelabUsersByDateDeNaissanceIsInShouldWork() throws Exception {
        // Initialize the database
        homelabUserRepository.saveAndFlush(homelabUser);

        // Get all the homelabUserList where dateDeNaissance in DEFAULT_DATE_DE_NAISSANCE or UPDATED_DATE_DE_NAISSANCE
        defaultHomelabUserShouldBeFound("dateDeNaissance.in=" + DEFAULT_DATE_DE_NAISSANCE + "," + UPDATED_DATE_DE_NAISSANCE);

        // Get all the homelabUserList where dateDeNaissance equals to UPDATED_DATE_DE_NAISSANCE
        defaultHomelabUserShouldNotBeFound("dateDeNaissance.in=" + UPDATED_DATE_DE_NAISSANCE);
    }

    @Test
    @Transactional
    public void getAllHomelabUsersByDateDeNaissanceIsNullOrNotNull() throws Exception {
        // Initialize the database
        homelabUserRepository.saveAndFlush(homelabUser);

        // Get all the homelabUserList where dateDeNaissance is not null
        defaultHomelabUserShouldBeFound("dateDeNaissance.specified=true");

        // Get all the homelabUserList where dateDeNaissance is null
        defaultHomelabUserShouldNotBeFound("dateDeNaissance.specified=false");
    }

    @Test
    @Transactional
    public void getAllHomelabUsersByDateDeNaissanceIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        homelabUserRepository.saveAndFlush(homelabUser);

        // Get all the homelabUserList where dateDeNaissance is greater than or equal to DEFAULT_DATE_DE_NAISSANCE
        defaultHomelabUserShouldBeFound("dateDeNaissance.greaterThanOrEqual=" + DEFAULT_DATE_DE_NAISSANCE);

        // Get all the homelabUserList where dateDeNaissance is greater than or equal to UPDATED_DATE_DE_NAISSANCE
        defaultHomelabUserShouldNotBeFound("dateDeNaissance.greaterThanOrEqual=" + UPDATED_DATE_DE_NAISSANCE);
    }

    @Test
    @Transactional
    public void getAllHomelabUsersByDateDeNaissanceIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        homelabUserRepository.saveAndFlush(homelabUser);

        // Get all the homelabUserList where dateDeNaissance is less than or equal to DEFAULT_DATE_DE_NAISSANCE
        defaultHomelabUserShouldBeFound("dateDeNaissance.lessThanOrEqual=" + DEFAULT_DATE_DE_NAISSANCE);

        // Get all the homelabUserList where dateDeNaissance is less than or equal to SMALLER_DATE_DE_NAISSANCE
        defaultHomelabUserShouldNotBeFound("dateDeNaissance.lessThanOrEqual=" + SMALLER_DATE_DE_NAISSANCE);
    }

    @Test
    @Transactional
    public void getAllHomelabUsersByDateDeNaissanceIsLessThanSomething() throws Exception {
        // Initialize the database
        homelabUserRepository.saveAndFlush(homelabUser);

        // Get all the homelabUserList where dateDeNaissance is less than DEFAULT_DATE_DE_NAISSANCE
        defaultHomelabUserShouldNotBeFound("dateDeNaissance.lessThan=" + DEFAULT_DATE_DE_NAISSANCE);

        // Get all the homelabUserList where dateDeNaissance is less than UPDATED_DATE_DE_NAISSANCE
        defaultHomelabUserShouldBeFound("dateDeNaissance.lessThan=" + UPDATED_DATE_DE_NAISSANCE);
    }

    @Test
    @Transactional
    public void getAllHomelabUsersByDateDeNaissanceIsGreaterThanSomething() throws Exception {
        // Initialize the database
        homelabUserRepository.saveAndFlush(homelabUser);

        // Get all the homelabUserList where dateDeNaissance is greater than DEFAULT_DATE_DE_NAISSANCE
        defaultHomelabUserShouldNotBeFound("dateDeNaissance.greaterThan=" + DEFAULT_DATE_DE_NAISSANCE);

        // Get all the homelabUserList where dateDeNaissance is greater than SMALLER_DATE_DE_NAISSANCE
        defaultHomelabUserShouldBeFound("dateDeNaissance.greaterThan=" + SMALLER_DATE_DE_NAISSANCE);
    }

    @Test
    @Transactional
    public void getAllHomelabUsersByAddressLine1IsEqualToSomething() throws Exception {
        // Initialize the database
        homelabUserRepository.saveAndFlush(homelabUser);

        // Get all the homelabUserList where addressLine1 equals to DEFAULT_ADDRESS_LINE_1
        defaultHomelabUserShouldBeFound("addressLine1.equals=" + DEFAULT_ADDRESS_LINE_1);

        // Get all the homelabUserList where addressLine1 equals to UPDATED_ADDRESS_LINE_1
        defaultHomelabUserShouldNotBeFound("addressLine1.equals=" + UPDATED_ADDRESS_LINE_1);
    }

    @Test
    @Transactional
    public void getAllHomelabUsersByAddressLine1IsNotEqualToSomething() throws Exception {
        // Initialize the database
        homelabUserRepository.saveAndFlush(homelabUser);

        // Get all the homelabUserList where addressLine1 not equals to DEFAULT_ADDRESS_LINE_1
        defaultHomelabUserShouldNotBeFound("addressLine1.notEquals=" + DEFAULT_ADDRESS_LINE_1);

        // Get all the homelabUserList where addressLine1 not equals to UPDATED_ADDRESS_LINE_1
        defaultHomelabUserShouldBeFound("addressLine1.notEquals=" + UPDATED_ADDRESS_LINE_1);
    }

    @Test
    @Transactional
    public void getAllHomelabUsersByAddressLine1IsInShouldWork() throws Exception {
        // Initialize the database
        homelabUserRepository.saveAndFlush(homelabUser);

        // Get all the homelabUserList where addressLine1 in DEFAULT_ADDRESS_LINE_1 or UPDATED_ADDRESS_LINE_1
        defaultHomelabUserShouldBeFound("addressLine1.in=" + DEFAULT_ADDRESS_LINE_1 + "," + UPDATED_ADDRESS_LINE_1);

        // Get all the homelabUserList where addressLine1 equals to UPDATED_ADDRESS_LINE_1
        defaultHomelabUserShouldNotBeFound("addressLine1.in=" + UPDATED_ADDRESS_LINE_1);
    }

    @Test
    @Transactional
    public void getAllHomelabUsersByAddressLine1IsNullOrNotNull() throws Exception {
        // Initialize the database
        homelabUserRepository.saveAndFlush(homelabUser);

        // Get all the homelabUserList where addressLine1 is not null
        defaultHomelabUserShouldBeFound("addressLine1.specified=true");

        // Get all the homelabUserList where addressLine1 is null
        defaultHomelabUserShouldNotBeFound("addressLine1.specified=false");
    }

    @Test
    @Transactional
    public void getAllHomelabUsersByAddressLine1ContainsSomething() throws Exception {
        // Initialize the database
        homelabUserRepository.saveAndFlush(homelabUser);

        // Get all the homelabUserList where addressLine1 contains DEFAULT_ADDRESS_LINE_1
        defaultHomelabUserShouldBeFound("addressLine1.contains=" + DEFAULT_ADDRESS_LINE_1);

        // Get all the homelabUserList where addressLine1 contains UPDATED_ADDRESS_LINE_1
        defaultHomelabUserShouldNotBeFound("addressLine1.contains=" + UPDATED_ADDRESS_LINE_1);
    }

    @Test
    @Transactional
    public void getAllHomelabUsersByAddressLine1NotContainsSomething() throws Exception {
        // Initialize the database
        homelabUserRepository.saveAndFlush(homelabUser);

        // Get all the homelabUserList where addressLine1 does not contain DEFAULT_ADDRESS_LINE_1
        defaultHomelabUserShouldNotBeFound("addressLine1.doesNotContain=" + DEFAULT_ADDRESS_LINE_1);

        // Get all the homelabUserList where addressLine1 does not contain UPDATED_ADDRESS_LINE_1
        defaultHomelabUserShouldBeFound("addressLine1.doesNotContain=" + UPDATED_ADDRESS_LINE_1);
    }

    @Test
    @Transactional
    public void getAllHomelabUsersByAddressLine2IsEqualToSomething() throws Exception {
        // Initialize the database
        homelabUserRepository.saveAndFlush(homelabUser);

        // Get all the homelabUserList where addressLine2 equals to DEFAULT_ADDRESS_LINE_2
        defaultHomelabUserShouldBeFound("addressLine2.equals=" + DEFAULT_ADDRESS_LINE_2);

        // Get all the homelabUserList where addressLine2 equals to UPDATED_ADDRESS_LINE_2
        defaultHomelabUserShouldNotBeFound("addressLine2.equals=" + UPDATED_ADDRESS_LINE_2);
    }

    @Test
    @Transactional
    public void getAllHomelabUsersByAddressLine2IsNotEqualToSomething() throws Exception {
        // Initialize the database
        homelabUserRepository.saveAndFlush(homelabUser);

        // Get all the homelabUserList where addressLine2 not equals to DEFAULT_ADDRESS_LINE_2
        defaultHomelabUserShouldNotBeFound("addressLine2.notEquals=" + DEFAULT_ADDRESS_LINE_2);

        // Get all the homelabUserList where addressLine2 not equals to UPDATED_ADDRESS_LINE_2
        defaultHomelabUserShouldBeFound("addressLine2.notEquals=" + UPDATED_ADDRESS_LINE_2);
    }

    @Test
    @Transactional
    public void getAllHomelabUsersByAddressLine2IsInShouldWork() throws Exception {
        // Initialize the database
        homelabUserRepository.saveAndFlush(homelabUser);

        // Get all the homelabUserList where addressLine2 in DEFAULT_ADDRESS_LINE_2 or UPDATED_ADDRESS_LINE_2
        defaultHomelabUserShouldBeFound("addressLine2.in=" + DEFAULT_ADDRESS_LINE_2 + "," + UPDATED_ADDRESS_LINE_2);

        // Get all the homelabUserList where addressLine2 equals to UPDATED_ADDRESS_LINE_2
        defaultHomelabUserShouldNotBeFound("addressLine2.in=" + UPDATED_ADDRESS_LINE_2);
    }

    @Test
    @Transactional
    public void getAllHomelabUsersByAddressLine2IsNullOrNotNull() throws Exception {
        // Initialize the database
        homelabUserRepository.saveAndFlush(homelabUser);

        // Get all the homelabUserList where addressLine2 is not null
        defaultHomelabUserShouldBeFound("addressLine2.specified=true");

        // Get all the homelabUserList where addressLine2 is null
        defaultHomelabUserShouldNotBeFound("addressLine2.specified=false");
    }

    @Test
    @Transactional
    public void getAllHomelabUsersByAddressLine2ContainsSomething() throws Exception {
        // Initialize the database
        homelabUserRepository.saveAndFlush(homelabUser);

        // Get all the homelabUserList where addressLine2 contains DEFAULT_ADDRESS_LINE_2
        defaultHomelabUserShouldBeFound("addressLine2.contains=" + DEFAULT_ADDRESS_LINE_2);

        // Get all the homelabUserList where addressLine2 contains UPDATED_ADDRESS_LINE_2
        defaultHomelabUserShouldNotBeFound("addressLine2.contains=" + UPDATED_ADDRESS_LINE_2);
    }

    @Test
    @Transactional
    public void getAllHomelabUsersByAddressLine2NotContainsSomething() throws Exception {
        // Initialize the database
        homelabUserRepository.saveAndFlush(homelabUser);

        // Get all the homelabUserList where addressLine2 does not contain DEFAULT_ADDRESS_LINE_2
        defaultHomelabUserShouldNotBeFound("addressLine2.doesNotContain=" + DEFAULT_ADDRESS_LINE_2);

        // Get all the homelabUserList where addressLine2 does not contain UPDATED_ADDRESS_LINE_2
        defaultHomelabUserShouldBeFound("addressLine2.doesNotContain=" + UPDATED_ADDRESS_LINE_2);
    }

    @Test
    @Transactional
    public void getAllHomelabUsersByCityIsEqualToSomething() throws Exception {
        // Initialize the database
        homelabUserRepository.saveAndFlush(homelabUser);

        // Get all the homelabUserList where city equals to DEFAULT_CITY
        defaultHomelabUserShouldBeFound("city.equals=" + DEFAULT_CITY);

        // Get all the homelabUserList where city equals to UPDATED_CITY
        defaultHomelabUserShouldNotBeFound("city.equals=" + UPDATED_CITY);
    }

    @Test
    @Transactional
    public void getAllHomelabUsersByCityIsNotEqualToSomething() throws Exception {
        // Initialize the database
        homelabUserRepository.saveAndFlush(homelabUser);

        // Get all the homelabUserList where city not equals to DEFAULT_CITY
        defaultHomelabUserShouldNotBeFound("city.notEquals=" + DEFAULT_CITY);

        // Get all the homelabUserList where city not equals to UPDATED_CITY
        defaultHomelabUserShouldBeFound("city.notEquals=" + UPDATED_CITY);
    }

    @Test
    @Transactional
    public void getAllHomelabUsersByCityIsInShouldWork() throws Exception {
        // Initialize the database
        homelabUserRepository.saveAndFlush(homelabUser);

        // Get all the homelabUserList where city in DEFAULT_CITY or UPDATED_CITY
        defaultHomelabUserShouldBeFound("city.in=" + DEFAULT_CITY + "," + UPDATED_CITY);

        // Get all the homelabUserList where city equals to UPDATED_CITY
        defaultHomelabUserShouldNotBeFound("city.in=" + UPDATED_CITY);
    }

    @Test
    @Transactional
    public void getAllHomelabUsersByCityIsNullOrNotNull() throws Exception {
        // Initialize the database
        homelabUserRepository.saveAndFlush(homelabUser);

        // Get all the homelabUserList where city is not null
        defaultHomelabUserShouldBeFound("city.specified=true");

        // Get all the homelabUserList where city is null
        defaultHomelabUserShouldNotBeFound("city.specified=false");
    }

    @Test
    @Transactional
    public void getAllHomelabUsersByCityContainsSomething() throws Exception {
        // Initialize the database
        homelabUserRepository.saveAndFlush(homelabUser);

        // Get all the homelabUserList where city contains DEFAULT_CITY
        defaultHomelabUserShouldBeFound("city.contains=" + DEFAULT_CITY);

        // Get all the homelabUserList where city contains UPDATED_CITY
        defaultHomelabUserShouldNotBeFound("city.contains=" + UPDATED_CITY);
    }

    @Test
    @Transactional
    public void getAllHomelabUsersByCityNotContainsSomething() throws Exception {
        // Initialize the database
        homelabUserRepository.saveAndFlush(homelabUser);

        // Get all the homelabUserList where city does not contain DEFAULT_CITY
        defaultHomelabUserShouldNotBeFound("city.doesNotContain=" + DEFAULT_CITY);

        // Get all the homelabUserList where city does not contain UPDATED_CITY
        defaultHomelabUserShouldBeFound("city.doesNotContain=" + UPDATED_CITY);
    }

    @Test
    @Transactional
    public void getAllHomelabUsersByPaysIsEqualToSomething() throws Exception {
        // Initialize the database
        homelabUserRepository.saveAndFlush(homelabUser);

        // Get all the homelabUserList where pays equals to DEFAULT_PAYS
        defaultHomelabUserShouldBeFound("pays.equals=" + DEFAULT_PAYS);

        // Get all the homelabUserList where pays equals to UPDATED_PAYS
        defaultHomelabUserShouldNotBeFound("pays.equals=" + UPDATED_PAYS);
    }

    @Test
    @Transactional
    public void getAllHomelabUsersByPaysIsNotEqualToSomething() throws Exception {
        // Initialize the database
        homelabUserRepository.saveAndFlush(homelabUser);

        // Get all the homelabUserList where pays not equals to DEFAULT_PAYS
        defaultHomelabUserShouldNotBeFound("pays.notEquals=" + DEFAULT_PAYS);

        // Get all the homelabUserList where pays not equals to UPDATED_PAYS
        defaultHomelabUserShouldBeFound("pays.notEquals=" + UPDATED_PAYS);
    }

    @Test
    @Transactional
    public void getAllHomelabUsersByPaysIsInShouldWork() throws Exception {
        // Initialize the database
        homelabUserRepository.saveAndFlush(homelabUser);

        // Get all the homelabUserList where pays in DEFAULT_PAYS or UPDATED_PAYS
        defaultHomelabUserShouldBeFound("pays.in=" + DEFAULT_PAYS + "," + UPDATED_PAYS);

        // Get all the homelabUserList where pays equals to UPDATED_PAYS
        defaultHomelabUserShouldNotBeFound("pays.in=" + UPDATED_PAYS);
    }

    @Test
    @Transactional
    public void getAllHomelabUsersByPaysIsNullOrNotNull() throws Exception {
        // Initialize the database
        homelabUserRepository.saveAndFlush(homelabUser);

        // Get all the homelabUserList where pays is not null
        defaultHomelabUserShouldBeFound("pays.specified=true");

        // Get all the homelabUserList where pays is null
        defaultHomelabUserShouldNotBeFound("pays.specified=false");
    }

    @Test
    @Transactional
    public void getAllHomelabUsersByPaysContainsSomething() throws Exception {
        // Initialize the database
        homelabUserRepository.saveAndFlush(homelabUser);

        // Get all the homelabUserList where pays contains DEFAULT_PAYS
        defaultHomelabUserShouldBeFound("pays.contains=" + DEFAULT_PAYS);

        // Get all the homelabUserList where pays contains UPDATED_PAYS
        defaultHomelabUserShouldNotBeFound("pays.contains=" + UPDATED_PAYS);
    }

    @Test
    @Transactional
    public void getAllHomelabUsersByPaysNotContainsSomething() throws Exception {
        // Initialize the database
        homelabUserRepository.saveAndFlush(homelabUser);

        // Get all the homelabUserList where pays does not contain DEFAULT_PAYS
        defaultHomelabUserShouldNotBeFound("pays.doesNotContain=" + DEFAULT_PAYS);

        // Get all the homelabUserList where pays does not contain UPDATED_PAYS
        defaultHomelabUserShouldBeFound("pays.doesNotContain=" + UPDATED_PAYS);
    }

    @Test
    @Transactional
    public void getAllHomelabUsersByPhoneIsEqualToSomething() throws Exception {
        // Initialize the database
        homelabUserRepository.saveAndFlush(homelabUser);

        // Get all the homelabUserList where phone equals to DEFAULT_PHONE
        defaultHomelabUserShouldBeFound("phone.equals=" + DEFAULT_PHONE);

        // Get all the homelabUserList where phone equals to UPDATED_PHONE
        defaultHomelabUserShouldNotBeFound("phone.equals=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    public void getAllHomelabUsersByPhoneIsNotEqualToSomething() throws Exception {
        // Initialize the database
        homelabUserRepository.saveAndFlush(homelabUser);

        // Get all the homelabUserList where phone not equals to DEFAULT_PHONE
        defaultHomelabUserShouldNotBeFound("phone.notEquals=" + DEFAULT_PHONE);

        // Get all the homelabUserList where phone not equals to UPDATED_PHONE
        defaultHomelabUserShouldBeFound("phone.notEquals=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    public void getAllHomelabUsersByPhoneIsInShouldWork() throws Exception {
        // Initialize the database
        homelabUserRepository.saveAndFlush(homelabUser);

        // Get all the homelabUserList where phone in DEFAULT_PHONE or UPDATED_PHONE
        defaultHomelabUserShouldBeFound("phone.in=" + DEFAULT_PHONE + "," + UPDATED_PHONE);

        // Get all the homelabUserList where phone equals to UPDATED_PHONE
        defaultHomelabUserShouldNotBeFound("phone.in=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    public void getAllHomelabUsersByPhoneIsNullOrNotNull() throws Exception {
        // Initialize the database
        homelabUserRepository.saveAndFlush(homelabUser);

        // Get all the homelabUserList where phone is not null
        defaultHomelabUserShouldBeFound("phone.specified=true");

        // Get all the homelabUserList where phone is null
        defaultHomelabUserShouldNotBeFound("phone.specified=false");
    }

    @Test
    @Transactional
    public void getAllHomelabUsersByPhoneContainsSomething() throws Exception {
        // Initialize the database
        homelabUserRepository.saveAndFlush(homelabUser);

        // Get all the homelabUserList where phone contains DEFAULT_PHONE
        defaultHomelabUserShouldBeFound("phone.contains=" + DEFAULT_PHONE);

        // Get all the homelabUserList where phone contains UPDATED_PHONE
        defaultHomelabUserShouldNotBeFound("phone.contains=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    public void getAllHomelabUsersByPhoneNotContainsSomething() throws Exception {
        // Initialize the database
        homelabUserRepository.saveAndFlush(homelabUser);

        // Get all the homelabUserList where phone does not contain DEFAULT_PHONE
        defaultHomelabUserShouldNotBeFound("phone.doesNotContain=" + DEFAULT_PHONE);

        // Get all the homelabUserList where phone does not contain UPDATED_PHONE
        defaultHomelabUserShouldBeFound("phone.doesNotContain=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    public void getAllHomelabUsersByUserIsEqualToSomething() throws Exception {
        // Get already existing entity
        User user = homelabUser.getUser();
        homelabUserRepository.saveAndFlush(homelabUser);
        Long userId = user.getId();

        // Get all the homelabUserList where user equals to userId
        defaultHomelabUserShouldBeFound("userId.equals=" + userId);

        // Get all the homelabUserList where user equals to userId + 1
        defaultHomelabUserShouldNotBeFound("userId.equals=" + (userId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultHomelabUserShouldBeFound(String filter) throws Exception {
        restHomelabUserMockMvc
            .perform(get("/api/homelab-users?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(homelabUser.getId().intValue())))
            .andExpect(jsonPath("$.[*].numCNI").value(hasItem(DEFAULT_NUM_CNI)))
            .andExpect(jsonPath("$.[*].dateDeNaissance").value(hasItem(DEFAULT_DATE_DE_NAISSANCE.toString())))
            .andExpect(jsonPath("$.[*].addressLine1").value(hasItem(DEFAULT_ADDRESS_LINE_1)))
            .andExpect(jsonPath("$.[*].addressLine2").value(hasItem(DEFAULT_ADDRESS_LINE_2)))
            .andExpect(jsonPath("$.[*].city").value(hasItem(DEFAULT_CITY)))
            .andExpect(jsonPath("$.[*].pays").value(hasItem(DEFAULT_PAYS)))
            .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE)));

        // Check, that the count call also returns 1
        restHomelabUserMockMvc
            .perform(get("/api/homelab-users/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultHomelabUserShouldNotBeFound(String filter) throws Exception {
        restHomelabUserMockMvc
            .perform(get("/api/homelab-users?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restHomelabUserMockMvc
            .perform(get("/api/homelab-users/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingHomelabUser() throws Exception {
        // Get the homelabUser
        restHomelabUserMockMvc.perform(get("/api/homelab-users/{id}", Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateHomelabUser() throws Exception {
        // Initialize the database
        homelabUserRepository.saveAndFlush(homelabUser);

        int databaseSizeBeforeUpdate = homelabUserRepository.findAll().size();

        // Update the homelabUser
        HomelabUser updatedHomelabUser = homelabUserRepository.findById(homelabUser.getId()).get();
        // Disconnect from session so that the updates on updatedHomelabUser are not directly saved in db
        em.detach(updatedHomelabUser);
        updatedHomelabUser
            .numCNI(UPDATED_NUM_CNI)
            .dateDeNaissance(UPDATED_DATE_DE_NAISSANCE)
            .addressLine1(UPDATED_ADDRESS_LINE_1)
            .addressLine2(UPDATED_ADDRESS_LINE_2)
            .city(UPDATED_CITY)
            .pays(UPDATED_PAYS)
            .phone(UPDATED_PHONE);
        HomelabUserDTO homelabUserDTO = homelabUserMapper.toDto(updatedHomelabUser);

        restHomelabUserMockMvc
            .perform(
                put("/api/homelab-users")
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(homelabUserDTO))
            )
            .andExpect(status().isOk());

        // Validate the HomelabUser in the database
        List<HomelabUser> homelabUserList = homelabUserRepository.findAll();
        assertThat(homelabUserList).hasSize(databaseSizeBeforeUpdate);
        HomelabUser testHomelabUser = homelabUserList.get(homelabUserList.size() - 1);
        assertThat(testHomelabUser.getNumCNI()).isEqualTo(UPDATED_NUM_CNI);
        assertThat(testHomelabUser.getDateDeNaissance()).isEqualTo(UPDATED_DATE_DE_NAISSANCE);
        assertThat(testHomelabUser.getAddressLine1()).isEqualTo(UPDATED_ADDRESS_LINE_1);
        assertThat(testHomelabUser.getAddressLine2()).isEqualTo(UPDATED_ADDRESS_LINE_2);
        assertThat(testHomelabUser.getCity()).isEqualTo(UPDATED_CITY);
        assertThat(testHomelabUser.getPays()).isEqualTo(UPDATED_PAYS);
        assertThat(testHomelabUser.getPhone()).isEqualTo(UPDATED_PHONE);
    }

    @Test
    @Transactional
    public void updateNonExistingHomelabUser() throws Exception {
        int databaseSizeBeforeUpdate = homelabUserRepository.findAll().size();

        // Create the HomelabUser
        HomelabUserDTO homelabUserDTO = homelabUserMapper.toDto(homelabUser);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restHomelabUserMockMvc
            .perform(
                put("/api/homelab-users")
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(homelabUserDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the HomelabUser in the database
        List<HomelabUser> homelabUserList = homelabUserRepository.findAll();
        assertThat(homelabUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteHomelabUser() throws Exception {
        // Initialize the database
        homelabUserRepository.saveAndFlush(homelabUser);

        int databaseSizeBeforeDelete = homelabUserRepository.findAll().size();

        // Delete the homelabUser
        restHomelabUserMockMvc
            .perform(delete("/api/homelab-users/{id}", homelabUser.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<HomelabUser> homelabUserList = homelabUserRepository.findAll();
        assertThat(homelabUserList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
