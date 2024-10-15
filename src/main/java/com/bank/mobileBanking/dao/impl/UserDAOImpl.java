package com.bank.mobileBanking.dao.impl;

import com.bank.mobileBanking.dao.UserDAO;
import com.bank.mobileBanking.dao.helper.AccountDAOHelper;
import com.bank.mobileBanking.dto.AccountDTO;
import com.bank.mobileBanking.dto.UserDTO;
import com.bank.mobileBanking.entity.Account;
import com.bank.mobileBanking.entity.User;
import com.bank.mobileBanking.entity.enums.AccountType;
import com.bank.mobileBanking.exception.ResourcesNotFoundException;
import com.bank.mobileBanking.exception.SomeThingWentWrongException;
import com.bank.mobileBanking.exception.TransactionDateTimeNotFoundException;
import com.bank.mobileBanking.util.AccountConstant;
import com.bank.mobileBanking.util.UserConstant;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

@Repository
@RequiredArgsConstructor
@Slf4j
public class UserDAOImpl implements UserDAO {

    private final JdbcTemplate jdbcTemplate;

    private final ModelMapper modelMapper;

    private final AccountDAOHelper accountDAOHelper;


    @Override
    public void register(UserDTO userDTO) {
        // Validate and parse the date of birth (dob)
        if (userDTO.getDob() == null) {
            throw new IllegalArgumentException("Date of birth cannot be null.");
        }

        LocalDate dob;
        try {
            dob = LocalDate.parse(userDTO.getDob().toString()); // Ensure dob is in "YYYY-MM-DD" format
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid date format for dob. Expected format: YYYY-MM-DD", e);
        }

        jdbcTemplate.update(UserConstant.INSERT_USER_DETAIL, new Object[]{
//                userDTO.getId(),
                userDTO.getFirstName(),
                userDTO.getLastName(),
                userDTO.getEmail(),
                userDTO.getPassword(),
                dob,
                userDTO.getMobileNumber(),
                userDTO.getPan(),
                userDTO.getAddress(),
                userDTO.getPinCode(),
                userDTO.getState(),
                userDTO.getCountry(),
                userDTO.getIsLogged()

        });
    }

    @Override
    public UserDTO getUser(String email) {
        log.info("inside getUser()::email");
        return jdbcTemplate.query(UserConstant.GET_USER_DETAIL, new Object[]{email}, rs -> {
            UserDTO userDTO = new UserDTO();
            try {
                if (rs.next()) {

                    User user = new User();
                    user.setId(rs.getInt("id"));
                    user.setFirstName(rs.getString("first_name"));
                    user.setLastName(rs.getString("last_name"));
                    user.setEmail(rs.getString("email"));
                    user.setPassword(rs.getString("password"));

                    LocalDate dob = accountDAOHelper.getUserDOB(user.getId());
                    if (dob == null) {
                        throw new TransactionDateTimeNotFoundException("User DateOfBirth not found for Id: " + user.getId());
                    }
                    user.setDob(dob);
                    user.setMobileNumber(rs.getString("mobile_number"));
                    user.setPan(rs.getString("pan_card_number"));
                    user.setAddress(rs.getString("address"));
                    user.setPinCode(rs.getString("pin_code"));
                    user.setState(rs.getString("state"));
                    user.setCountry(rs.getString("country"));
                    user.setIsLogged(rs.getBoolean("is_logged"));

                    userDTO = modelMapper.map(user, UserDTO.class);
                    log.info("userDTO {}",userDTO);
                    return userDTO;
                } else {
                    return null;
                }
            } catch (Exception e) {
                log.error("Error in getting Account Balance :: {}", e.getMessage());
            }
            return userDTO;
        });
    }

    @Override
    public boolean userExists(String email, String mobileNumber) {
        Integer count = jdbcTemplate.queryForObject(UserConstant.IS_USER_EXISTS, new Object[]{email, mobileNumber}, Integer.class);
        return count != null && count > 0;
    }

    @Override
    public boolean userExistsByEmail(String email) {
        Integer count = jdbcTemplate.queryForObject(UserConstant.IS_USER_EXISTS_BY_EMAIL, new Object[]{email}, Integer.class);
        return count != null && count > 0;
    }

    @Override
    public UserDTO getUserByMobileNumber(String mobileNumber) {
        log.info("inside getUserByMobileNumber()");
        return jdbcTemplate.query(UserConstant.GET_USER_BY_MOBILE, new Object[]{mobileNumber}, rs -> {
            UserDTO userDTO = new UserDTO();
            try {
                if (rs.next()) {

                    User user = new User();
                    user.setId(rs.getInt("id"));
                    user.setFirstName(rs.getString("first_name"));
                    user.setLastName(rs.getString("last_name"));
                    user.setEmail(rs.getString("email"));
                    user.setPassword(rs.getString("password"));

                    LocalDate dob = accountDAOHelper.getUserDOB(user.getId());
                    if (dob == null) {
                        throw new TransactionDateTimeNotFoundException("User DateOfBirth not found for Id: " + user.getId());
                    }
                    user.setDob(dob);
                    user.setMobileNumber(rs.getString("mobile_number"));
                    user.setPan(rs.getString("pan_card_number"));
                    user.setAddress(rs.getString("address"));
                    user.setPinCode(rs.getString("pin_code"));
                    user.setState(rs.getString("state"));
                    user.setCountry(rs.getString("country"));
                    user.setIsLogged(rs.getBoolean("is_logged"));

                    userDTO = modelMapper.map(user, UserDTO.class);
                    return userDTO;
                } else {
                    return null;
                }
            } catch (Exception e) {
                log.error("Error in getting user :: {}", e.getMessage());
            }
            return userDTO;
        });
    }

    @Override
    public void updateUserLogin(UserDTO user) {
        jdbcTemplate.update(UserConstant.UPDATE_USER_LOGGED, user.getIsLogged(), user.getEmail());
    }

}
