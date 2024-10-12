package com.bank.mobileBanking.dao.impl;

import com.bank.mobileBanking.dao.UserDAO;
import com.bank.mobileBanking.dao.helper.AccountDAOHelper;
import com.bank.mobileBanking.dto.AccountDTO;
import com.bank.mobileBanking.dto.UserDTO;
import com.bank.mobileBanking.entity.Account;
import com.bank.mobileBanking.entity.User;
import com.bank.mobileBanking.entity.enums.AccountType;
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
        LocalDate dob;
        try {
            dob = LocalDate.parse(userDTO.getDob().toString()); // Ensure dob is in "YYYY-MM-DD" format
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid date format for dob. Expected format: YYYY-MM-DD", e);
        }

        jdbcTemplate.update(UserConstant.INSERT_USER_DETAIL, new Object[]{
                userDTO.getFirstName(),
                userDTO.getLastName(),
                userDTO.getEmail(),
                dob,
                userDTO.getMobileNumber(),
                userDTO.getPan(),
                userDTO.getAddress(),
                userDTO.getPinCode(),
                userDTO.getState(),
                userDTO.getCountry()

        });
    }

    @Override
    public UserDTO getUser(String email) {
        return jdbcTemplate.query(UserConstant.GET_USER_DETAIL, new Object[]{email}, rs -> {
            UserDTO userDTO = new UserDTO();
            try {
                if (rs.next()) {

                    User user = new User();
                    user.setId(rs.getInt("id"));
                    user.setFirstName(rs.getString("first_name"));
                    user.setLastName(rs.getString("last_name"));
                    user.setEmail(rs.getString("email"));

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

                    userDTO = modelMapper.map(user, UserDTO.class);
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
}
